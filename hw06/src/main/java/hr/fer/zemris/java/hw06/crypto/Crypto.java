package hr.fer.zemris.java.hw06.crypto;

import static hr.fer.zemris.java.hw06.crypto.Util.hextobyte;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class {@code Crypto} is a program which allows the user to encrypt or decrypt 
 * a file given through command line using the AES crypto-algorithm and the 128-bit 
 * encryption key or calculate and check the SHA-256 file digest.
 * 
 * @author stipe
 *
 */
public class Crypto {

	public static void main(String[] args) {

		try {
			switch (args[0]) {
				case "checksha":
					checkArgumentsLength(args, 2);
					checkSha(args[1]);
					break;
	
				case "encrypt":
					checkArgumentsLength(args, 3);
					encrypt(args[1], args[2]);
					break;
	
				case "decrypt":
					checkArgumentsLength(args, 3);
					decrypt(args[1], args[2]);
					break;
	
				default:
					System.out.println(args[0] + " is an unknown command. Command should be one of: "
							+ "\n\t - checksha \n\t - encrypt \n\t - decrypt");
					return;
			}

		} catch (CryptingException e) {
			System.out.println(e.getMessage());
			return;
		}
	}

	/**
	 * Checks the the number of arguments of the given input.
	 * 
	 * @param args {@code String} array of user command line input
	 * @param properLength
	 */
	private static void checkArgumentsLength(String[] args, int properLength) {
		if (args.length != properLength) {
			throw new CryptingException(
					"Command " + args[0] + " must be followed by " + (properLength - 1) + "arguments!");
		}
	}

	/**
	 * Sets the cryptography process to decryption.
	 * 
	 * @param crypted name of the file to be decrypted
	 * @param original name of the file to be created by decryption
	 * @throws CryptingException delegated from {@link #setUpCryptography(boolean, String, String)}
	 */
	private static void decrypt(String crypted, String original) {
		setUpCryptography(false, crypted, original);
	}

	/**
	 * 
	 * @param original name of the original file to be encrypted
	 * @param crypted name of the file to be created by encryption
	 * @throws CryptingException delegated from {@link #setUpCryptography(boolean, String, String)}
	 */
	private static void encrypt(String original, String crypted) {
		setUpCryptography(true, original, crypted);

	}

	/**
	 * Interacts with the user and accepts the password and initialization vector used for crypting.
	 * Calls for the creation and initialization of the cipher used for cryptography and starts the 
	 * process.
	 * 
	 * @param encrypt parameter deciding if encryption or decryption is to be done
	 * @param source name of the source file
	 * @param destination name of the destination file
	 * @throws CryptingException delegated to this method through {@link #getCipher(String, String, boolean)}
	 * 		   or {@link #doCryptography(Cipher, Path, Path)}
	 */
	private static void setUpCryptography(boolean encrypt, String source, String destination) {
		try (Scanner sc = new Scanner(System.in)) {
			
			System.out.print("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n> ");
			String keyText = sc.next();

			System.out.print("Please provide initialization vector as hex-encoded text (32 hex-digits):\n> ");
			String ivText = sc.next();

			Cipher cipher = getCipher(keyText, ivText, encrypt);
			doCryptography(cipher, Paths.get(source), Paths.get(destination));
			
			System.out.printf("%s completed. Generated file %s based on file %s.%n", 
					encrypt ? "Encryption" : "Decryption", destination, source);
		}
	}

	/**
	 * Creates and initializes the cipher using the key and vector given by the user.
	 * 
	 * @param keyText hex value with which a password key is created
	 * @param ivText hex value of the initialization vector
	 * @param encrypt parameter deciding whether encryption or decryption is to be done
	 * @return a newly created and initialized cipher
	 * @throws CryptingException if anything went wrong during the craeteion of the key, vector
	 * 		   or cipher
	 */
	private static Cipher getCipher(String keyText, String ivText, boolean encrypt) {
		SecretKeySpec keySpec = new SecretKeySpec(hextobyte(keyText), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(hextobyte(ivText));
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
			return cipher;
		} catch (GeneralSecurityException e) {
			throw new CryptingException(e.getMessage());
		}
	}

	/**
	 * Decrypts or encrypts a given file, creating a new one.
	 * 
	 * @param cipher cipher used for decryption
	 * @param source path to the source file
	 * @param destination path to the to be created destination file
	 * @throws CryptingException if something went wrong during reading or writing from file, or
	 * 		   during cryptography
	 */
	private static void doCryptography(Cipher cipher, Path source, Path destination) {
		try (InputStream is = new BufferedInputStream(Files.newInputStream(source,
					StandardOpenOption.READ));
			 OutputStream os = new BufferedOutputStream(Files.newOutputStream(destination, 
					StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
			
			byte[] inputBuffer = new byte[4096];
			byte[] outputBuffer = new byte[4096];
			int i = is.read(inputBuffer);
			while (i > 0) {
				outputBuffer = cipher.update(inputBuffer, 0, i);
				os.write(outputBuffer);
				i = is.read(inputBuffer);
			}
			outputBuffer = cipher.doFinal();
			os.write(outputBuffer);
			
		} catch (IOException | GeneralSecurityException e) {
			throw new CryptingException(e.getMessage());
		}
	}
	
	/**
	 * Verifies that the data received has arrived unchanged by calculating the digest of the file
	 * and comparing it with the published digest.
	 * 
	 * @param fileName name of the file whose digest is to be calculated and checked
	 */
	private static void checkSha(String fileName) {
		Path file = Paths.get(fileName);
		try (InputStream is = new BufferedInputStream(Files.newInputStream(file, StandardOpenOption.READ));
				Scanner sc = new Scanner(System.in)) {

			MessageDigest sha = MessageDigest.getInstance("SHA-256");

			byte[] buffer = new byte[4096];
			int i = is.read(buffer);
			while (i > 0) {
				sha.update(buffer, 0, i);
				i = is.read(buffer);
			}

			byte[] hash = sha.digest();
			String actualDigest = Util.bytetohex(hash);

			System.out.print("Please provide expected sha-256 digest for hw06test.bin:\n> ");
			String givenDigest = sc.next();

			if (givenDigest.equals(actualDigest)) {
				System.out.printf("Digesting completed. Digest of %s matches expected digest.", fileName);
			} else {
				System.out.printf(
						"Digesting completed. Digest of %s does not match the expected digest. Digest was: %s.%n",
						fileName, actualDigest);
			}

		} catch (IOException | NoSuchAlgorithmException ex) {
			throw new CryptingException(ex.getMessage());
		}
	}

}

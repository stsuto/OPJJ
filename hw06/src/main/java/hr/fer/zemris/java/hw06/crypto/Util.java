package hr.fer.zemris.java.hw06.crypto;

/**
 * Class {@code Util} represents a utility class used for hex-to-byte and byte-to-hex
 * conversions, offering methods for those tasks.
 * 
 * @author stipe
 *
 */
public class Util {

	/**
	 * Creates and returns a {@code String} of hexadecimal digits from the given byte array.
	 * Each byte creates two digits.
	 * 
	 * @param bytearray bytes to be transformed to hex digits
	 * @return {@code String} of hexadecimal digits with
	 */
	public static String bytetohex(byte[] bytearray) {
		StringBuilder hexDigits = new StringBuilder();
		for (byte num : bytearray) {
			hexDigits.append(Character.forDigit((num >> 4) & 0xF, 16));
			hexDigits.append(Character.forDigit((num & 0xF), 16));	    	
		}
		return hexDigits.toString();
	}

	/**
	 * Creates and returns a {@code byte} array from the given string.
	 * 
	 * @param keyText string from which the bytes are created
	 * @return a byte array
	 */
	public static byte[] hextobyte(String keyText) {
		checkKeyLength(keyText);
		return getBytesFromHexString(keyText);
	}
	
	/**
	 * Checks the length of the given {@code String} representing a key
	 * 
	 * @param keyText hex values used as key
	 */
	private static void checkKeyLength(String keyText) {
		int keyLength = keyText.length();
		if (keyLength % 2 != 0) {
			throw new IllegalArgumentException("Hexadecimal string is odd-sized!");
		}
	}

	/**
	 * Uses a string to create bytes of value equal to that of characters from the string.
	 * Each byte is created from the characters.
	 * 
	 * @param keyText string to be turned into bytes
	 * @return a byte array
	 */
	private static byte[] getBytesFromHexString(String keyText) {
		int keyLength = keyText.length();
		char[] chars = keyText.toCharArray();
		byte[] byteArray = new byte[keyLength / 2];
		
		for (int i = 0; i < keyLength; i += 2) {
			int firstDigit = Character.digit(chars[i], 16);
			int secondDigit = Character.digit(chars[i + 1], 16);
			if (firstDigit < 0 || secondDigit < 0) {
				throw new IllegalArgumentException("Hexadecimal string has invalid characters!");
			}
			byteArray[i / 2] = (byte) ((firstDigit << 4) + secondDigit);
		}
		return byteArray;
	}

}



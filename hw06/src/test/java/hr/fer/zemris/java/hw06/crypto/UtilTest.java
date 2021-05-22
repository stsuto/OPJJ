package hr.fer.zemris.java.hw06.crypto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UtilTest {

	@Test
	void testHexToByte() {
		byte[] bytearray = new byte[] {1, -82, 34};
		byte[] result = Util.hextobyte("01aE22");
		
		for (int i = 0; i < result.length; i++) {
			assertEquals(bytearray[i], result[i]);
		}
		
		byte[] resultUpper = Util.hextobyte("01AE22");
		
		for (int i = 0; i < result.length; i++) {
			assertEquals(result[i], resultUpper[i]);
		}
		
		byte[] resultLower = Util.hextobyte("01ae22");
		
		for (int i = 0; i < result.length; i++) {
			assertEquals(result[i], resultLower[i]);
		}
	}

	@Test
	void testByteToHex() {
		byte[] bytearray = new byte[] {1, -82, 34};
		String hex = "01ae22";
		
		assertEquals(hex, Util.bytetohex(bytearray));
	}
}

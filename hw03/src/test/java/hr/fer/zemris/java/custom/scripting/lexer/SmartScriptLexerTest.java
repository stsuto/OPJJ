package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SmartScriptLexerTest {

	@Test
	public void testRadAfterEOFInExtended() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		lexer.setState(SmartScriptLexerState.TAG);

		// will obtain EOF
		lexer.nextToken();
		// will throw!
		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}
	
	@Test
	void testWithEscapedQuote() {
		String document = "A tag follows {$= \"Joe \\\\ \\\"Long\\\" Smith\"$}.s";
		SmartScriptLexer lexer = new SmartScriptLexer(document);
		SmartScriptToken token = lexer.nextToken();
		assertEquals("A", token.getValue());
		token = lexer.nextToken();
		assertEquals(" ", token.getValue());
		token = lexer.nextToken();
		assertEquals("tag", token.getValue());
		token = lexer.nextToken();
		assertEquals(" ", token.getValue());
		token = lexer.nextToken();
		assertEquals("follows", token.getValue());
		token = lexer.nextToken();
		assertEquals(" ", token.getValue());
		token = lexer.nextToken();
		assertEquals(SmartScriptTokenType.OPEN_PARENTHESES, token.getType());
		token = lexer.nextToken();
		assertEquals(SmartScriptTokenType.TAG, token.getType());
		token = lexer.nextToken();
		assertEquals('=', token.getValue());
		token = lexer.nextToken();
		assertEquals("\"Joe \\ \"Long\" Smith\"", token.getValue());
		token = lexer.nextToken();
		assertEquals(SmartScriptTokenType.TAG, token.getType());
		token = lexer.nextToken();
		assertEquals(SmartScriptTokenType.CLOSE_PARENTHESES, token.getType());
		token = lexer.nextToken();
		assertEquals('.', token.getValue());
	}
	
	@Test
	void testWithIllegalEscape() {
		String document = "A tag follows {$= \"Joe \\\\ \\7 \\\"Long\\\" Smith\"$}.s"; // Escaping 7 is illegal!
		SmartScriptLexer lexer = new SmartScriptLexer(document);
		SmartScriptToken token = lexer.nextToken();
		assertEquals("A", token.getValue());
		token = lexer.nextToken();
		assertEquals(" ", token.getValue());
		token = lexer.nextToken();
		assertEquals("tag", token.getValue());
		token = lexer.nextToken();
		assertEquals(" ", token.getValue());
		token = lexer.nextToken();
		assertEquals("follows", token.getValue());
		token = lexer.nextToken();
		assertEquals(" ", token.getValue());
		token = lexer.nextToken();
		assertEquals(SmartScriptTokenType.OPEN_PARENTHESES, token.getType());
		token = lexer.nextToken();
		assertEquals(SmartScriptTokenType.TAG, token.getType());
		token = lexer.nextToken();
		assertEquals('=', token.getValue());
		assertThrows(SmartScriptLexerException.class, () ->  lexer.nextToken());	
	}
	
	@Test
	void testForTag() {
		String document = "{$ FOR i - 1 -10 1.6 $}";
		SmartScriptLexer lexer = new SmartScriptLexer(document);
		
		SmartScriptToken correctData[] = {
				new SmartScriptToken(SmartScriptTokenType.OPEN_PARENTHESES, Character.valueOf('{')),
				new SmartScriptToken(SmartScriptTokenType.TAG, Character.valueOf('$')),
				new SmartScriptToken(SmartScriptTokenType.KEY, "FOR"),
				new SmartScriptToken(SmartScriptTokenType.VARIABLE, "i"),
				new SmartScriptToken(SmartScriptTokenType.OPERATOR, Character.valueOf('-')),
				new SmartScriptToken(SmartScriptTokenType.INT, 1),
				new SmartScriptToken(SmartScriptTokenType.INT, -10),
				new SmartScriptToken(SmartScriptTokenType.DOUBLE, 1.6),
				new SmartScriptToken(SmartScriptTokenType.TAG, Character.valueOf('$')),
				new SmartScriptToken(SmartScriptTokenType.CLOSE_PARENTHESES, Character.valueOf('}')),
				new SmartScriptToken(SmartScriptTokenType.EOF, null)
			};

			checkTokenStream(lexer, correctData);
	}
	
	@Test
	void testForTagWithString() {
		String document = "{$ FOR sco_re 		\"-1\"10 \"1\" $}";
		SmartScriptLexer lexer = new SmartScriptLexer(document);
		
		SmartScriptToken correctData[] = {
				new SmartScriptToken(SmartScriptTokenType.OPEN_PARENTHESES, Character.valueOf('{')),
				new SmartScriptToken(SmartScriptTokenType.TAG, Character.valueOf('$')),
				new SmartScriptToken(SmartScriptTokenType.KEY, "FOR"),
				new SmartScriptToken(SmartScriptTokenType.VARIABLE, "sco_re"),
				new SmartScriptToken(SmartScriptTokenType.STRING, "\"-1\""),
				new SmartScriptToken(SmartScriptTokenType.INT, 10),
				new SmartScriptToken(SmartScriptTokenType.STRING, "\"1\""),
				new SmartScriptToken(SmartScriptTokenType.TAG, Character.valueOf('$')),
				new SmartScriptToken(SmartScriptTokenType.CLOSE_PARENTHESES, Character.valueOf('}')),
				new SmartScriptToken(SmartScriptTokenType.EOF, null)
			};

			checkTokenStream(lexer, correctData);
	}

	@Test
	void testBigDocument() {
		String document = "	This is sample text.\r\n" + 
				"	{$ FOR i 1 10 1 $}\r\n" + 
				"	This is {$= i $}-th time this message is generated.\r\n" + 
				"	{$END$}\r\n" + 
				"	{$FOR i 0 10 2 $}\r\n" + 
				"	sin({$=i$}^2) = {$= i i * @sin \"0.000\" @decfmt $}\r\n" + 
				"	{$END$}";
		SmartScriptLexer lexer = new SmartScriptLexer(document);
		
		SmartScriptToken correctData[] = {
				new SmartScriptToken(SmartScriptTokenType.WHITESPACE, "	"),
				new SmartScriptToken(SmartScriptTokenType.STRING, "This"),
				new SmartScriptToken(SmartScriptTokenType.WHITESPACE, " "),
				new SmartScriptToken(SmartScriptTokenType.STRING, "is"),
				new SmartScriptToken(SmartScriptTokenType.WHITESPACE, " "),
				new SmartScriptToken(SmartScriptTokenType.STRING, "sample"),
				new SmartScriptToken(SmartScriptTokenType.WHITESPACE, " "),
				new SmartScriptToken(SmartScriptTokenType.STRING, "text"),
				new SmartScriptToken(SmartScriptTokenType.SYMBOL, Character.valueOf('.')),
				new SmartScriptToken(SmartScriptTokenType.WHITESPACE, "\r\n	"),
				
					
				new SmartScriptToken(SmartScriptTokenType.OPEN_PARENTHESES, Character.valueOf('{')),
				new SmartScriptToken(SmartScriptTokenType.TAG, Character.valueOf('$')),
				new SmartScriptToken(SmartScriptTokenType.KEY, "FOR"),
				new SmartScriptToken(SmartScriptTokenType.VARIABLE, "i"),
				new SmartScriptToken(SmartScriptTokenType.INT, 1),
				new SmartScriptToken(SmartScriptTokenType.INT, 10),
				new SmartScriptToken(SmartScriptTokenType.INT, 1),
				new SmartScriptToken(SmartScriptTokenType.TAG, Character.valueOf('$')),
				new SmartScriptToken(SmartScriptTokenType.CLOSE_PARENTHESES, Character.valueOf('}')),
				new SmartScriptToken(SmartScriptTokenType.WHITESPACE, "\r\n	"),
				
				new SmartScriptToken(SmartScriptTokenType.STRING, "This"),
				new SmartScriptToken(SmartScriptTokenType.WHITESPACE, " "),
				new SmartScriptToken(SmartScriptTokenType.STRING, "is"),
				new SmartScriptToken(SmartScriptTokenType.WHITESPACE, " "),
				new SmartScriptToken(SmartScriptTokenType.OPEN_PARENTHESES, Character.valueOf('{')),
				new SmartScriptToken(SmartScriptTokenType.TAG, Character.valueOf('$')),
				new SmartScriptToken(SmartScriptTokenType.KEY, Character.valueOf('=')),
				new SmartScriptToken(SmartScriptTokenType.VARIABLE, "i"),
				new SmartScriptToken(SmartScriptTokenType.TAG, Character.valueOf('$')),
				new SmartScriptToken(SmartScriptTokenType.CLOSE_PARENTHESES, Character.valueOf('}')),
				new SmartScriptToken(SmartScriptTokenType.SYMBOL, Character.valueOf('-')),
				new SmartScriptToken(SmartScriptTokenType.STRING, "th"),
				new SmartScriptToken(SmartScriptTokenType.WHITESPACE, " "),
				new SmartScriptToken(SmartScriptTokenType.STRING, "time"),
				new SmartScriptToken(SmartScriptTokenType.WHITESPACE, " "),
				new SmartScriptToken(SmartScriptTokenType.STRING, "this"),
				new SmartScriptToken(SmartScriptTokenType.WHITESPACE, " "),
				new SmartScriptToken(SmartScriptTokenType.STRING, "message"),
				new SmartScriptToken(SmartScriptTokenType.WHITESPACE, " "),
				new SmartScriptToken(SmartScriptTokenType.STRING, "is"),
				new SmartScriptToken(SmartScriptTokenType.WHITESPACE, " "),
				new SmartScriptToken(SmartScriptTokenType.STRING, "generated"),
				new SmartScriptToken(SmartScriptTokenType.SYMBOL, Character.valueOf('.')),
				new SmartScriptToken(SmartScriptTokenType.WHITESPACE, "\r\n	"),
				
		};
		
		checkTokenStream(lexer, correctData);
	}
	
	@Test
	void testFunctionParse() {
		String document = "sin({$=i$}^2) = {$= i i * @sin \"0.000\" @decfmt $}\r\n";
		SmartScriptLexer lexer = new SmartScriptLexer(document);
		
		SmartScriptToken correctData[] = {
				new SmartScriptToken(SmartScriptTokenType.STRING, "sin"),
				new SmartScriptToken(SmartScriptTokenType.SYMBOL, Character.valueOf('(')),
				new SmartScriptToken(SmartScriptTokenType.OPEN_PARENTHESES, Character.valueOf('{')),
				new SmartScriptToken(SmartScriptTokenType.TAG, Character.valueOf('$')),
				new SmartScriptToken(SmartScriptTokenType.KEY, Character.valueOf('=')),
				new SmartScriptToken(SmartScriptTokenType.VARIABLE, "i"),
				new SmartScriptToken(SmartScriptTokenType.TAG, Character.valueOf('$')),
				new SmartScriptToken(SmartScriptTokenType.CLOSE_PARENTHESES, Character.valueOf('}')),
				new SmartScriptToken(SmartScriptTokenType.SYMBOL, Character.valueOf('^')),
				new SmartScriptToken(SmartScriptTokenType.INT, 2),
				new SmartScriptToken(SmartScriptTokenType.SYMBOL, Character.valueOf(')')),
				new SmartScriptToken(SmartScriptTokenType.WHITESPACE, " "),
				new SmartScriptToken(SmartScriptTokenType.SYMBOL, Character.valueOf('=')),
				new SmartScriptToken(SmartScriptTokenType.WHITESPACE, " "),
				new SmartScriptToken(SmartScriptTokenType.OPEN_PARENTHESES, Character.valueOf('{')),
				new SmartScriptToken(SmartScriptTokenType.TAG, Character.valueOf('$')),
				new SmartScriptToken(SmartScriptTokenType.KEY, Character.valueOf('=')),
				new SmartScriptToken(SmartScriptTokenType.VARIABLE, "i"),
				new SmartScriptToken(SmartScriptTokenType.VARIABLE, "i"),
				new SmartScriptToken(SmartScriptTokenType.OPERATOR, Character.valueOf('*')),
				new SmartScriptToken(SmartScriptTokenType.FUNCTION, "@sin"),
				new SmartScriptToken(SmartScriptTokenType.STRING, "\"0.000\""),
				new SmartScriptToken(SmartScriptTokenType.FUNCTION, "@decfmt"),
				new SmartScriptToken(SmartScriptTokenType.TAG, Character.valueOf('$')),
				new SmartScriptToken(SmartScriptTokenType.CLOSE_PARENTHESES, Character.valueOf('}')),
				new SmartScriptToken(SmartScriptTokenType.WHITESPACE, "\r\n"),
				new SmartScriptToken(SmartScriptTokenType.EOF, null)
			};

			checkTokenStream(lexer, correctData);
	}
	

	
	private void checkTokenStream(SmartScriptLexer lexer, SmartScriptToken[] correctData) {
		int counter = 0;
		for(SmartScriptToken expected : correctData) {
			SmartScriptToken actual = lexer.nextToken();
			String msg = "Checking token "+counter + ":";
			assertEquals(expected.getType(), actual.getType(), msg);
			assertEquals(expected.getValue(), actual.getValue(), msg);
			counter++;
		}
	}
}

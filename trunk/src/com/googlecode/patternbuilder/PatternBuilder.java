package com.googlecode.patternbuilder;

import java.util.regex.Pattern;

/**
 * 
 * A helper class to construct regular expression patterns. The idea is to make
 * patterns explicit and self-documenting. The inspiration of this class is
 * Python's verbose regular expressions.
 * 
 * The following example shows a complex regular expression pattern to describe
 * valid roman numerals:
 * 
 * <pre>
 *   // Roman numerals pattern - From Dive into Python 2.4
 *   //
 *   // '^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$'
 *   
 *   Pattern romanNumeralsPattern =
 *     beginningOfLine()
 *     .followedByCharacter("M").atLeastButNoMore(0, 4)
 *     .followedByCapturingGroupOf(
 *       either(
 *         characterSequence("CM"),
 *         characterSequence("CD"),
 *         character("D").onceOrNotAtAll()
 *         .followedByCharacter("C").atLeastButNoMore(0, 3)
 *       )
 *     )
 *     .followedByCapturingGroupOf(
 *       either(
 *         characterSequence("XC"),
 *         characterSequence("XL"),
 *         character("L").onceOrNotAtAll()
 *         .followedByCharacter("X").atLeastButNoMore(0, 3)								
 *       )
 *     )
 *     .followedByCapturingGroupOf(
 *       either(
 *         characterSequence("IX"),
 *         characterSequence("IV"),
 *         character("V").onceOrNotAtAll()
 *         .followedByCharacter("I").atLeastButNoMore(0, 3)								
 *       )
 *     )
 *     .followedByEndOfLine()
 *     .compile();
 * </pre> 
 * 
 * Not all of the expresiveness of the {@link Pattern} language is supported.
 * Also notice that the same pattern can be constructed in different ways. For
 * instance,
 * 
 * <pre>
 *   Pattern pattern =
 *     character("2")
 *     .followedByCharacter("0")
 *     .followedByCharacter("1")
 *     .followedByCharacter("0")
 *     .followedByWhitespace
 *     .followedByCharacterClass(
 *       anyInRange("A", "Z")
 *     ).compile();
 * </pre>
 * 
 * and
 * 
 * <pre>
 *   Pattern pattern =
 *     characterSequence("2010")
 *     .followedByCharacter(" ")
 *     .followedByCharacterClass(
 *       anyInRange("A", "Z")
 *     ).compile();
 * </pre>
 * 
 * Both are compile to the same "2010 [A-Z]" pattern.
 * 
 * @see Pattern
 * @author Agustin Barto <abarto@gmail.com>
 *
 */
public class PatternBuilder {
	/**
	 * A {@link StringBuilder} to hold the partial pattern as it is
	 * constructed.
	 */
	private StringBuilder partialPattern;
	
	/**
	 * Default no arguments constructor. An appropriate static builder method
	 * should be used instead to start building the pattern.
	 */
	public PatternBuilder() {
		partialPattern = new StringBuilder();
	}
	
	/**
	 * A copy constructor. Builds a new instance copying the contents of
	 * another.
	 * 
	 * @param patternBuilder The pattern builder to copy.
	 */
	public PatternBuilder(PatternBuilder patternBuilder) {
		partialPattern = new StringBuilder(patternBuilder.partialPattern);
	}
	
	/**
	 * Generates a {@link Pattern} from the current {@link PatternBuilder}. A
	 * call to this method marks the end of a construction sequence.
	 * 
	 * @return The pattern represented by the current status of the
	 * {@link PatternBuilder} instance.
	 * @see Pattern#compile(String)
	 */
	public Pattern compile() {
		return Pattern.compile(partialPattern.toString());
	}
	
	/**
	 * Same as {@link #compile()} but supplying specific match flags to the
	 * {@link Pattern#compile(String, int)} method.
	 * 
	 * @param flags The match flags to be passed to
	 * {@link Pattern#compile(String, int)}. 
	 * @return The pattern represented by the current status of the
	 * {@link PatternBuilder} instance.
	 * @see #compile()
	 * @see Pattern#compile(String, int)
	 */
	public Pattern compile(int flags) {
		return Pattern.compile(partialPattern.toString(), flags);
	}
	
	// Character sequences
	
	/**
	 * Continues the description of the pattern with a character sequence. For
	 * instance, to describe the pattern <code>"^\\d beers$"</code> we could use:
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     beginningOfLine()
	 *       .followedByDigit()
	 *       .followedByWhitespace()
	 *       .followedByCharacterSequence("beers")
	 *       .followedByEndOfLine()
	 *       .compile();
	 * </pre>
	 * 
	 * @param value The character sequence that continues the description of the
	 * pattern.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByCharacterSequence(CharSequence value) {
		// TODO Add automatic escaping
		partialPattern.append(value);
		
		return this;
	}
	
	/**
	 * Starts the description of the pattern with a character sequence. For
	 * instance, to describe the pattern <code>"Hello, .*"</code> we could use:
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     characterSequence("Hello,")
	 *     .followedByWhitespace()
	 *     .followedByAnyCharacter()
	 *     .zeroOrMoreTimes()
	 *     .compile();
	 * </pre>
	 * 
	 * @param value The character sequence that begins the description of the
	 * pattern.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder characterSequence(CharSequence value) {
		return new PatternBuilder().followedByCharacterSequence(value);
	}
			
	// Characters
	
	/**
	 * Continues the description of the pattern with a character. For instance,
	 * to describe the pattern <code>"\\d\\dDD"</code> we could use:
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     digit()
	 *       .followedByDigit()
	 *       .followedByCharacter("D")
	 *       .followedByCharacter("D")
	 *       .compile();
	 * </pre>
	 * 
	 * @param value The character that continues the description of the
	 * pattern.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByCharacter(String value) {
		// TODO Add automatic escaping
		partialPattern.append(value);
		
		return this;
	}
	
	/**
	 * Starts the description of the pattern with a character. For instance, to
	 * describe the pattern <code>"I spy"</code> we could use:
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     character("I")
	 *       .followedByWhitespace
	 *       .followedByCharacterSequence("spy")
	 *       .compile();
	 * </pre>
	 * 
	 * @param value The character that starts the description of the
	 * pattern.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder character(String value) {
		return new PatternBuilder().followedByCharacter(value);
	}
	
	/**
	 * Continues the description of the pattern with a backslash "\"
	 * character. For instance, to describe the pattern
	 * <code>"backslash hell: \\\\"</code> we could use:
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     characterSequence("backslash hell: ")
	 *       .followedByBackslashCharacter()
	 *       .compile();
	 * </pre>
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByBackslashCharacter() {
		partialPattern.append("\\\\");
		
		return this;
	}
	
	/**
	 * Starts the description of the pattern with a backslash "\" character.
	 * For instance, to describe the pattern <code>"\\\\ not escaped"</code> we
	 * could use:
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     backslashCharacter()
	 *       .followedByCharacterSequence(" not escaped")
	 *       .compile();
	 * </pre>
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder backslashCharater() {
		return new PatternBuilder().followedByBackslashCharacter();
	}
	
	/**
	 * Continues the description of the pattern with a tab "\t" character.
	 * For instance, to describe the pattern <code>"Population:\\t\\d"</code>
	 * we could use:
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     characterSequence("Population")
	 *       .followedByTabCharacter()
	 *       .followedByDigit()
	 *       .compile();
	 * </pre>
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByTabCharacter() {
		partialPattern.append("\\t");
		
		return this;
	}
	
	/**
	 * Starts the description of the pattern with a tab "\t" character. For
	 * instance, to describe the pattern <code>"\\t\\tTAB"</code> (notice that
	 * the pattern starts with two tab characters) we could use:
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     tabCharacter()
	 *       .followedByTabCharacter()
	 *       .followedByCharacterSequence("TAB")
	 *       .compile();
	 * </pre>
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder tabCharacter() {
		return new PatternBuilder().followedByTabCharacter();
	}
	
	/**
	 * Continues the description of the pattern with a new-line "\n"
	 * character. For instance, to describe the pattern
	 * <code>"this\\nn that"</code> we could use:
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     characterSequence("this")
	 *       .followedByNewlineCharacter()
	 *       .followedByCharacter("n")
	 *       .followedByCharacterSequence(" that")
	 *       .compile();
	 * </pre>
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByNewlineCharacter() {
		partialPattern.append("\\n");
		
		return this;
	}
	
	/**
	 * Starts the description of the pattern with a new-line "\n" character.
	 * For instance, to describe the pattern <code>"\\nempty line above"</code>
	 * we could use:
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     newLineCharacter()
	 *       .followedByCharacterSequence("empty")
	 *       .followedByWhitespace()
	 *       .followedByCharacterSequence("line")
	 *       .followedByWhitespace()
	 *       .followedByCharacterSequence("above")
	 *       .compile();
	 * </pre>
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder newLineCharacter() {
		return new PatternBuilder().followedByNewlineCharacter();
	}
	
	/**
	 * Continues the description of the pattern with a carriage return "\r"
	 * character.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByCarriageReturnCharacter() {
		partialPattern.append("\\r");
		
		return this;
	}
	
	/**
	 * Starts the description of the pattern with a carriage return "\r"
	 * character.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder carriageReturnCharacter() {
		return new PatternBuilder().followedByCarriageReturnCharacter();
	}
	
	/**
	 * Continues the description of the pattern with a form-feed "\f"
	 * character.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByFormFeedCharacter() {
		partialPattern.append("\\f");
		
		return this;
	}
	
	/**
	 * Starts the description of the pattern with a form-feed "\f" character.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder formFeedCharacter() {
		return new PatternBuilder().followedByFormFeedCharacter();
	}
	
	/**
	 * Continues the description of the pattern with an alert "\a"
	 * character.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByAlertCharacter() {
		partialPattern.append("\\a");
		
		return this;
	}
	
	/**
	 * Starts the description of the pattern with an alert "\a" character.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder alertCharacter() {
		return new PatternBuilder().followedByAlertCharacter();
	}
	
	/**
	 * Continues the description of the pattern with an escape "\e"
	 * character.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByEscapeCharacter() {
		partialPattern.append("\\e");
		
		return this;
	}
	
	/**
	 * Starts the description of the pattern with an escape "\e" character.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder escapeCharacter() {
		return new PatternBuilder().followedByEscapeCharacter();
	}
	
	/**
	 * Continues the description of the pattern with a control "\cX"
	 * character where X is a letter from "A" to "Z".
     *
	 * @param letter The letter of the control character.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByControlCharacter(String letter) {
		partialPattern.append("\\c" + letter);
		
		return this;
	}
	
	/**
	 * Starts the description of the pattern with a control "\cX"
	 * character where X is a letter from "A" to "Z".
     *
	 * @param letter The letter of the control character.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder controlCharacter(String letter) {
		return new PatternBuilder().followedByControlCharacter(letter);
	}

	// Character classes
		
	/**
	 * Continues the description of the pattern with the inclusion of a
	 * character class. Character classes can be specified literally or they
	 * can be built using {@link CharacterClassBuilder}. For instance, the
	 * pattern "\\d.*[A-C][D-F]" can be specified as follows:
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     digit()
	 *     .followedByAnyCharacter().zeroOrMoreTimes()
	 *     .followedByCharacterClass("[A-C]")
	 *     .followedByCharacterClass(
	 *       anyInRange("D", "F").compile()
	 *     ).compile();
	 * </pre>
	 * 
	 * @param characterClass The specification of a character class.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 * @see CharacterClassBuilder
	 */
	public PatternBuilder followedByCharacterClass(String characterClass) {
		partialPattern.append(characterClass);
		
		return this;
	}
	
	/**
	 * Continues the description of the pattern with the inclusion of a
	 * character class constructed using a {@link CharacterClassBuilder}. For
	 * instance pattern "\\d.*[A-C][D-F]" can be specified as follows:
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     digit()
	 *     .followedByAnyCharacter().zeroOrMoreTimes()
	 *     .followedByCharacterClass(
	 *       anyInRange("A", "C")
	 *     )
	 *     .followedByCharacterClass(
	 *       anyInRange("D", "F")
	 *     ).compile();
	 * </pre> 
	 * 
	 * @param characterClassBuilder The partially constructed character class builder.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 * @see #followedByCharacterClass(String)
	 */
	public PatternBuilder followedByCharacterClass(CharacterClassBuilder characterClassBuilder) {
		partialPattern.append(characterClassBuilder.compile());
		
		return this;
	}

	/**
	 * Starts the description of the pattern with the inclusion of a
	 * character class. Character classes can be specified literally or they
	 * can be built using {@link CharacterClassBuilder}. For instance, the
	 * pattern "[ABC].*[D-F]" can be specified as follows:
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     .characterClass("[ABC]")
	 *     .followedByAnyCharacter().zeroOrMoreTimes()
	 *     .followedByCharacterClass(
	 *       anyInRange("D", "F")
	 *     ).compile();
	 * </pre>
	 * 
	 * @param characterClass The specification of a character class.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 * @see CharacterClassBuilder
	 */
	public static PatternBuilder characterClass(String characterClass) {
		return new PatternBuilder().followedByCharacterClass(characterClass);
	}

	/**
	 * Starts the description of the pattern with the inclusion of a
	 * character class. Character classes can be specified literally or they
	 * can be built using {@link CharacterClassBuilder}. For instance, the
	 * pattern "[ABC].*[D-F]" can be specified as follows:
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     .characterClass(
	 *       anyOf("ABC")
	 *     )
	 *     .followedByAnyCharacter().zeroOrMoreTimes()
	 *     .followedByCharacterClass(
	 *       anyInRange("D", "F")
	 *     ).compile();
	 * </pre>
	 * 
	 * @param characterClassBuilder The partially constructed character class builder.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 * @see CharacterClassBuilder
	 */
	public static PatternBuilder characterClass(CharacterClassBuilder characterClassBuilder) {
		return new PatternBuilder().followedByCharacterClass(characterClassBuilder);
	}
	
	// Predefined character classes
	
	/**
	 * @return
	 */
	public PatternBuilder followedByAnyCharacter() {
		partialPattern.append(".");
		
		return this;
	}
	
	/**
	 * @return
	 */
	public static PatternBuilder anyCharacter() {
		return new PatternBuilder().followedByAnyCharacter();
	}
	
	/**
	 * @return
	 */
	public PatternBuilder followedByDigit() {
		partialPattern.append("\\d");
		
		return this;
	}
	
	/**
	 * @return
	 */
	public static PatternBuilder digit() {
		return new PatternBuilder().followedByDigit();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByNonDigit() {
		partialPattern.append("\\D");
		
		return this;
	}
	
	/**
	 * @return
	 */
	public static PatternBuilder nonDigit() {
		return new PatternBuilder().followedByNonDigit();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByWhitespace() {
		partialPattern.append("\\s");
		
		return this;
	}
	
	/**
	 * @return
	 */
	public static PatternBuilder whitespace() {
		return new PatternBuilder().followedByWhitespace();
	}
	
	/**
	 * @return
	 */
	public PatternBuilder followedByWordCharacter() {
		partialPattern.append("\\w");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder wordCharacter() {
		return new PatternBuilder().followedByWordCharacter();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByNonWordCharacter() {
		partialPattern.append("\\W");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder nonWordCharacter() {
		return new PatternBuilder().followedByNonWordCharacter();
	}
	
	// POSIX character classes
	
	/**
	 * @return
	 */
	public PatternBuilder followedByPosixLowerCaseAlphabeticCharacter() {
		partialPattern.append("\\p{Lower}");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder posixLowerCaseAlphabeticCharacter() {
		return new PatternBuilder().followedByPosixLowerCaseAlphabeticCharacter();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByPosixUpperCaseAlphabeticCharacter() {
		partialPattern.append("\\p{Upper}");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder posixUpperCaseAlphabeticCharacter() {
		return new PatternBuilder().followedByPosixUpperCaseAlphabeticCharacter();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByPosixAsciiCharacter() {
		partialPattern.append("\\p{ASCII}");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder posixAsciiCharacter() {
		return new PatternBuilder().followedByPosixAsciiCharacter();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByPosixAlphabeticCharacter() {
		partialPattern.append("\\p{Alpha}");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder posixAlphabeticCharacter() {
		return new PatternBuilder().followedByPosixAlphabeticCharacter();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByPosixAlphanumericCharacter() {
		partialPattern.append("\\p{Alnum}");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder posixAlphanumericCharacter() {
		return new PatternBuilder().followedByPosixAlphanumericCharacter();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByPosixPunctuationCharacter() {
		partialPattern.append("\\p{Punct}");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder posixPunctuactionCharacter() {
		return new PatternBuilder().followedByPosixAlphanumericCharacter();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByPosixVisibleCharacter() {
		partialPattern.append("\\p{Graph}");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder posixVisibleCharacter() {
		return new PatternBuilder().followedByPosixVisibleCharacter();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByPosixPrintableCharacter() {
		partialPattern.append("\\p{Print}");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder posixPrintableCharacter() {
		return new PatternBuilder().followedByPosixPrintableCharacter();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByPosixSpaceOrTabCharacter() {
		partialPattern.append("\\p{Blank}");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder posixSpaceOrTabCharacter() {
		return new PatternBuilder().followedByPosixSpaceOrTabCharacter();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByPosixControlCharacter() {
		partialPattern.append("\\p{Cntrl}");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder posixControlCharacter() {
		return new PatternBuilder().followedByPosixControlCharacter();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByPosixHexadecimalDigit() {
		partialPattern.append("\\p{XDigit}");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder posixHexadecimalDigit() {
		return new PatternBuilder().followedByPosixHexadecimalDigit();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByPosixWhitespace() {
		partialPattern.append("\\p{Space}");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder posixWhitespace() {
		return new PatternBuilder().followedByPosixWhitespace();
	}

	// java.lang.Character classes
	
	/**
	 * @return
	 */
	public PatternBuilder followedByJavaLowerCaseCharacter() {
		partialPattern.append("\\p{javaLowerCase}");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder javaLowerCaseCharacter() {
		return new PatternBuilder().followedByJavaLowerCaseCharacter();
	}
	
	/**
	 * @return
	 */
	public PatternBuilder followedByJavaUpperCaseCharacter() {
		partialPattern.append("\\p{javaUpperCase}");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder javaUpperCaseCharacter() {
		return new PatternBuilder().followedByJavaLowerCaseCharacter();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByJavaWhitespaceCharacter() {
		partialPattern.append("\\p{javaWhitespace}");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder javaWhitespaceCharacter() {
		return new PatternBuilder().followedByJavaWhitespaceCharacter();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByJavaMirroredCharacter() {
		partialPattern.append("\\p{javaMIrrored}");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder javaMirroredCharacter() {
		return new PatternBuilder().followedByJavaMirroredCharacter();
	}
	
	// Classes for Unicode blocks and categories

	/**
	 * @return
	 */
	public PatternBuilder followedByGreekBlockCharacter() {
		partialPattern.append("\\p{InGreek}");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder greekBlockCharacter() {
		return new PatternBuilder().followedByGreekBlockCharacter();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByUppercaseLetterCharacter() {
		partialPattern.append("\\p{Lu}");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder upperCaseLetterCharacter() {
		return new PatternBuilder().followedByUppercaseLetterCharacter();
	}
	
	/**
	 * @return
	 */
	public PatternBuilder followedByCurrencySymbolCharacter() {
		partialPattern.append("\\p{Sc}");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder currencySymbolCharacter() {
		return new PatternBuilder().followedByCurrencySymbolCharacter();
	}
	
	/**
	 * @return
	 */
	public PatternBuilder followedByNonGreekBlockCharacter() {
		partialPattern.append("\\P{InGreek}");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder nonGreekBlockCharacter() {
		return new PatternBuilder().followedByNonGreekBlockCharacter();
	}
	
	/**
	 * @return
	 */
	public PatternBuilder followedByNonUppercaseLetterCharacter() {
		partialPattern.append("[\\p{L}&&[^\\p{Lu}]");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder nonUpperCaseLetterCharacter() {
		return new PatternBuilder().followedByNonUppercaseLetterCharacter();
	}
	
	/**
	 * @param code
	 * @return
	 */
	public PatternBuilder followedByUnicodeCharacter(int code) {
		partialPattern.append("\\u" + code);
		
		return this;		
	}

	/**
	 * @param code
	 * @return
	 */
	public static PatternBuilder unicodeCharacter(int code) {
		return new PatternBuilder().followedByUnicodeCharacter(code);
	}
	
	/**
	 * @param property
	 * @return
	 */
	public PatternBuilder followedByUnicodeCharacterInBlock(String property) {
		partialPattern.append("\\p{" + property + "}");
		
		return this;		
	}

	/**
	 * @param property
	 * @return
	 */
	public static PatternBuilder unicodeCharacterInBlock(String property) {
		return new PatternBuilder().followedByUnicodeCharacterInBlock(property);
	}
	
	/**
	 * @param property
	 * @return
	 */
	public PatternBuilder followedByUnicodeCharacterOutsideBlock(String property) {
		partialPattern.append("\\p{" + property + "}");
		
		return this;		
	}

	/**
	 * @param property
	 * @return
	 */
	public static PatternBuilder unicodeCharacterOutsideBlock(String property) {
		return new PatternBuilder().followedByUnicodeCharacterOutsideBlock(property);
	}

	/**
	 * @param property
	 * @return
	 */
	public PatternBuilder followedByUnicodeCharacterInCategory(String property) {
		return this.followedByUnicodeCharacterInBlock(property);		
	}

	/**
	 * @param property
	 * @return
	 */
	public static PatternBuilder unicodeCharacterInCategory(String property) {
		return new PatternBuilder().followedByUnicodeCharacterInBlock(property);
	}
	
	/**
	 * @param property
	 * @return
	 */
	public PatternBuilder followedByUnicodeCharacterOutsideCategory(String property) {
		return this.followedByUnicodeCharacterOutsideBlock(property);
	}

	/**
	 * @param property
	 * @return
	 */
	public static PatternBuilder unicodeCharacterOutsideCategory(String property) {
		return new PatternBuilder().followedByUnicodeCharacterOutsideBlock(property);
	}
	
	// Boundary matchers
	
	/**
	 * @return
	 */
	public PatternBuilder followedByBeginningOfLine() {
		partialPattern.append("^");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder beginningOfLine() {
		return new PatternBuilder().followedByBeginningOfLine();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByEndOfLine() {
		partialPattern.append("$");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder endOfLine() {
		return new PatternBuilder().followedByEndOfLine();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByWordBoundary() {
		partialPattern.append("\\b");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder wordBoundary() {
		return new PatternBuilder().followedByWordBoundary();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByNonWordBoundary() {
		partialPattern.append("\\B");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder nonWordBoundary() {
		return new PatternBuilder().followedByNonWordBoundary();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByBeginningOfInput() {
		partialPattern.append("\\A");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder beginningOfInput() {
		return new PatternBuilder().followedByBeginningOfInput();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByEndOfPreviousMatch() {
		partialPattern.append("\\G");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder endOfPreviousMatch() {
		return new PatternBuilder().followedByEndOfPreviousMatch();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByNonFinalTerminatorEndOfInput() {
		partialPattern.append("\\Z");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder nonFinalTerminatorEndOfInput() {
		return new PatternBuilder().followedByNonFinalTerminatorEndOfInput();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByEndOfInput() {
		partialPattern.append("\\z");
		
		return this;		
	}

	/**
	 * @return
	 */
	public static PatternBuilder endOfInput() {
		return new PatternBuilder().followedByEndOfInput();
	}

	// Greedy quantifiers
	
	/**
	 * @return
	 */
	public PatternBuilder onceOrNotAtAll() {
		partialPattern.append("?");
		
		return this;		
	}
	
	/**
	 * @return
	 */
	public PatternBuilder zeroOrMoreTimes() {
		partialPattern.append("*");
		
		return this;		
	}

	/**
	 * @return
	 */
	public PatternBuilder oneOrMoreTimes() {
		partialPattern.append("+");
		
		return this;		
	}
	
	/**
	 * @param n
	 * @return
	 */
	public PatternBuilder exactly(int n) {
		partialPattern.append("{" + n + "}");
		
		return this;		
	}
	
	/**
	 * @param n
	 * @return
	 */
	public PatternBuilder atLeast(int n) {
		partialPattern.append("{" + n + ",}");
		
		return this;		
	}

	/**
	 * @param n
	 * @param m
	 * @return
	 */
	public PatternBuilder atLeastButNoMore(int n, int m) {
		partialPattern.append("{" + n + "," + m + "}");
		
		return this;		
	}
	
	// Reluctant quantifiers
	
	/**
	 * @return
	 */
	public PatternBuilder onceOrNotAtAllReluctant() {
		partialPattern.append("??");
		
		return this;		
	}
	
	/**
	 * @return
	 */
	public PatternBuilder zeroOrMoreTimesReluctant() {
		partialPattern.append("*?");
		
		return this;		
	}

	/**
	 * @return
	 */
	public PatternBuilder oneOrMoreTimesReluctant() {
		partialPattern.append("+?");
		
		return this;		
	}
	
	/**
	 * @param n
	 * @return
	 */
	public PatternBuilder exactlyReluctant(int n) {
		partialPattern.append("{" + n + "}?");
		
		return this;		
	}
	
	/**
	 * @param n
	 * @return
	 */
	public PatternBuilder atLeastReluctant(int n) {
		partialPattern.append("{" + n + ",}?");
		
		return this;		
	}

	/**
	 * @param n
	 * @param m
	 * @return
	 */
	public PatternBuilder atLeastButNoMoreReluctant(int n, int m) {
		partialPattern.append("{" + n + "," + m + "}?");
		
		return this;		
	}

	// Possessive quantifiers
	
	/**
	 * @return
	 */
	public PatternBuilder onceOrNotAtAllPossessive() {
		partialPattern.append("?+");
		
		return this;		
	}
	
	/**
	 * @return
	 */
	public PatternBuilder zeroOrMoreTimesPossessive() {
		partialPattern.append("*+");
		
		return this;		
	}

	/**
	 * @return
	 */
	public PatternBuilder oneOrMoreTimesPossessive() {
		partialPattern.append("++");
		
		return this;		
	}
	
	/**
	 * @param n
	 * @return
	 */
	public PatternBuilder exactlyPossessive(int n) {
		partialPattern.append("{" + n + "}+");
		
		return this;		
	}
	
	/**
	 * @param n
	 * @return
	 */
	public PatternBuilder atLeastPossessive(int n) {
		partialPattern.append("{" + n + ",}+");
		
		return this;		
	}

	/**
	 * @param n
	 * @param m
	 * @return
	 */
	public PatternBuilder atLeastButNoMorePossesive(int n, int m) {
		partialPattern.append("{" + n + "," + m + "}+");
		
		return this;		
	}
	
	// Logical operators

	/**
	 * @param patternBuilder
	 * @return
	 */
	public PatternBuilder followedBy(PatternBuilder patternBuilder) {
		partialPattern.append(patternBuilder.partialPattern);
		
		return this;
	}
	
	/**
	 * @param leftPatternBuilder
	 * @param rightPatternBuilder
	 * @return
	 */
	public static PatternBuilder followedBy(PatternBuilder leftPatternBuilder, PatternBuilder rightPatternBuilder) {
		return new PatternBuilder(leftPatternBuilder).followedBy(rightPatternBuilder);
	}
	
	/**
	 * @param patternBuilder
	 * @return
	 */
	public PatternBuilder either(PatternBuilder patternBuilder) {
		partialPattern.append("|" + patternBuilder.partialPattern);
		
		return this;
	}
	
	/**
	 * @param patternBuilder
	 * @return
	 */
	public PatternBuilder or(PatternBuilder patternBuilder) {
		partialPattern.append("|" + patternBuilder.partialPattern);
		
		return this;
	}
	
	/**
	 * @param leftPatternBuilder
	 * @param rightPatternBuilder
	 * @return
	 */
	public static PatternBuilder either(PatternBuilder ... patternBuilders) {
		PatternBuilder eitherPatternBuilder = null;
		
		if (patternBuilders.length > 0) {
			PatternBuilder leftPatternBuilder = new PatternBuilder(patternBuilders[0]);
			
			for (int i = 1 ; i < patternBuilders.length ; i++) {
				leftPatternBuilder.either(patternBuilders[i]);
			}
			
			eitherPatternBuilder = leftPatternBuilder;
		}
		
		return eitherPatternBuilder;
	}
	
	/**
	 * @param patternBuilder
	 * @return
	 */
	public PatternBuilder followedByCapturingGroupOf(PatternBuilder patternBuilder) {
		partialPattern.append("(" + patternBuilder.partialPattern + ")");
		
		return this;
	}
	
	/**
	 * @param patternBuilder
	 * @return
	 */
	public static PatternBuilder capturingGroupOf(PatternBuilder patternBuilder) {
		return new PatternBuilder().followedByCapturingGroupOf(patternBuilder);
	}
	
	// Back references
	
	/**
	 * @param group
	 * @return
	 */
	public PatternBuilder followedByCapturingGroupMatch(int group) {
		partialPattern.append("\\" + group);
		
		return this;		
	}

	/**
	 * @param group
	 * @return
	 */
	public static PatternBuilder capturingGroupMatch(int group) {
		return new PatternBuilder().followedByCapturingGroupMatch(group);
	}

	// Quotation
	
	/**
	 * @return
	 */
	public PatternBuilder followedByQuote() {
		partialPattern.append("\\");
		
		return this;		
	}
	
	/**
	 * @return
	 */
	public static PatternBuilder quote() {
		return new PatternBuilder().followedByQuote();
	}
	
	/**
	 * @return
	 */
	public PatternBuilder followedByQuoteStart() {
		partialPattern.append("\\Q");
		
		return this;		
	}
	
	/**
	 * @return
	 */
	public static PatternBuilder quoteStart() {
		return new PatternBuilder().followedByQuoteStart();
	}

	/**
	 * @return
	 */
	public PatternBuilder followedByQuoteEnd() {
		partialPattern.append("\\E");
		
		return this;		
	}
	
	/**
	 * @return
	 */
	public static PatternBuilder quoteEnd() {
		return new PatternBuilder().followedByQuoteEnd();
	}
	
	// Special constructs

	/**
	 * @param patternBuilder
	 * @return
	 */
	public PatternBuilder followedByNonCapturingGroupOf(PatternBuilder patternBuilder) {
		partialPattern.append("(?:" + patternBuilder.partialPattern + ")");
		
		return this;
	}
	
	/**
	 * @param patternBuilder
	 * @return
	 */
	public static PatternBuilder nonCapturingGroupOf(PatternBuilder patternBuilder) {
		return new PatternBuilder().followedByNonCapturingGroupOf(patternBuilder);
	}
	
	/**
	 * @param flags
	 * @return
	 */
	public PatternBuilder setFlags(String flags) {
		partialPattern.append("(?" + flags + ")");
		
		return this;
	}

	/**
	 * @param flags
	 * @return
	 */
	public PatternBuilder unsetFlags(String flags) {
		partialPattern.append("(?-" + flags + ")");
		
		return this;
	}
	
	/**
	 * @param onFlags
	 * @param offFlags
	 * @return
	 */
	public PatternBuilder setFlags(String onFlags, String offFlags) {
		partialPattern.append("(?" + onFlags + "-" + offFlags + ")");
		
		return this;
	}
	
	/**
	 * @param patternBuilder
	 * @param flags
	 * @return
	 */
	public PatternBuilder followedByNonCapturingGroupOf(PatternBuilder patternBuilder, String flags) {
		partialPattern.append("(?" + flags + ":" + patternBuilder.partialPattern + ")");
		
		return this;
	}
	
	/**
	 * @param patternBuilder
	 * @param flags
	 * @return
	 */
	public static PatternBuilder nonCapturingGroupOf(PatternBuilder patternBuilder, String flags) {
		return new PatternBuilder().followedByNonCapturingGroupOf(patternBuilder, flags);
	}

	/**
	 * @param patternBuilder
	 * @param onFlags
	 * @param offFlags
	 * @return
	 */
	public PatternBuilder followedByNonCapturingGroupOf(PatternBuilder patternBuilder, String onFlags, String offFlags) {
		partialPattern.append("(?" + onFlags + "-" + offFlags + ":" + patternBuilder.partialPattern + ")");
		
		return this;
	}
	
	/**
	 * @param patternBuilder
	 * @param onFlags
	 * @param offFlags
	 * @return
	 */
	public static PatternBuilder nonCapturingGroupOf(PatternBuilder patternBuilder, String onFlags, String offFlags) {
		return new PatternBuilder().followedByNonCapturingGroupOf(patternBuilder, onFlags, offFlags);
	}

	/**
	 * @param patternBuilder
	 * @return
	 */
	public PatternBuilder followedByZeroWidthPositiveLookAheadOf(PatternBuilder patternBuilder) {
		partialPattern.append("(?=" + patternBuilder.partialPattern + ")");
		
		return this;
	}
	
	/**
	 * @param patternBuilder
	 * @return
	 */
	public static PatternBuilder zeroWidthPositiveLookAheadOf(PatternBuilder patternBuilder) {
		return new PatternBuilder().followedByZeroWidthPositiveLookAheadOf(patternBuilder);
	}

	/**
	 * @param patternBuilder
	 * @return
	 */
	public PatternBuilder followedByZeroWidthNegativeLookAheadOf(PatternBuilder patternBuilder) {
		partialPattern.append("(?!" + patternBuilder.partialPattern + ")");
		
		return this;
	}
	
	/**
	 * @param patternBuilder
	 * @return
	 */
	public static PatternBuilder zeroWidthNegativeLookAheadOf(PatternBuilder patternBuilder) {
		return new PatternBuilder().followedByZeroWidthNegativeLookAheadOf(patternBuilder);
	}
	
	/**
	 * @param patternBuilder
	 * @return
	 */
	public PatternBuilder followedByZeroWidthPositiveLookBehindOf(PatternBuilder patternBuilder) {
		partialPattern.append("(?<=" + patternBuilder.partialPattern + ")");
		
		return this;
	}
	
	/**
	 * @param patternBuilder
	 * @return
	 */
	public static PatternBuilder zeroWidthPositiveLookBehindOf(PatternBuilder patternBuilder) {
		return new PatternBuilder().followedByZeroWidthPositiveLookBehindOf(patternBuilder);
	}

	/**
	 * @param patternBuilder
	 * @return
	 */
	public PatternBuilder followedByZeroWidthNegativeLookBehindOf(PatternBuilder patternBuilder) {
		partialPattern.append("(?<!" + patternBuilder.partialPattern + ")");
		
		return this;
	}
	
	/**
	 * @param patternBuilder
	 * @return
	 */
	public static PatternBuilder zeroWidthNegativeLookBehindOf(PatternBuilder patternBuilder) {
		return new PatternBuilder().followedByZeroWidthNegativeLookBehindOf(patternBuilder);
	}

	/**
	 * @param patternBuilder
	 * @return
	 */
	public PatternBuilder followedByIndependentNonCapturingGroupOf(PatternBuilder patternBuilder) {
		partialPattern.append("(?>" + patternBuilder.partialPattern + ")");
		
		return this;
	}
	
	/**
	 * @param patternBuilder
	 * @return
	 */
	public static PatternBuilder independentNonCapturingGroupOf(PatternBuilder patternBuilder) {
		return new PatternBuilder().followedByIndependentNonCapturingGroupOf(patternBuilder);
	}
}

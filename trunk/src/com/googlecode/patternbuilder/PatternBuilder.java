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
	 * Continues the description of the pattern with the predefined character
	 * class "." which indicates one occurence of character (which may or may
	 * not include line terminators). For instance, the pattern "SH.T" can be
	 * specified as follows: 
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     character("S")
	 *     .followedByCharacter("H")
	 *     .followedByAnyCharacter()
	 *     .followedByCharacter("T")
	 *     .compile();
	 * </pre> 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByAnyCharacter() {
		partialPattern.append(".");
		
		return this;
	}
	
	/**
	 * Starts the description of the pattern with the predefined character
	 * class "." which indicates one occurence of character (which may or may
	 * not include line terminators). For instance, the pattern ".UCK" can be
	 * specified as follows: 
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     anyCharacter()
	 *     .followedByCharacter("U")
	 *     .followedByCharacter("C")
	 *     .followedByCharacter("K")
	 *     .compile();
	 * </pre> 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder anyCharacter() {
		return new PatternBuilder().followedByAnyCharacter();
	}
	
	/**
	 * Continues the description of the pattern with the predefined character
	 * class "\d" which indicates one occurence of numeric character (digit).
	 * For instance, the pattern "Route \\d\\d" can be specified as follows: 
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     characterSequence("Route")
	 *     .followedByWhistespace()
	 *     .followedByDigit()
	 *     .followedByDigit()
	 *     .compile();
	 * </pre> 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByDigit() {
		partialPattern.append("\\d");
		
		return this;
	}
	
	/**
	 * Start the description of the pattern with the predefined character
	 * class "\d" which indicates one occurence of numeric character (digit).
	 * For instance, the pattern "\\d+" can be specified as follows: 
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     digit()
	 *     .oneOrMoreTimes();
	 * </pre> 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder digit() {
		return new PatternBuilder().followedByDigit();
	}

	/**
	 * Continues the description of the pattern with the predefined character
	 * class "\D" which indicates one occurence of any non-numeric character.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByNonDigit() {
		partialPattern.append("\\D");
		
		return this;
	}
	
	/**
	 * Starts the description of the pattern with the predefined character
	 * class "\D" which indicates one occurence of any non-numeric character.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder nonDigit() {
		return new PatternBuilder().followedByNonDigit();
	}

	/**
	 * Continues the description of the pattern with the predefined character
	 * class "\s" which indicates one occurence of a whitespace character
	 * (space, tab, new-line, etc.). For instance, the pattern
	 * "spaces\\sin\\sbetween" can be specified as follows: 
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     characterSequence("spaces")
	 *     .followedByWhistespace()
	 *     .followedByCharacterSequence("in")
	 *     .followedByWhistespace()
	 *     .followedByCharacterSequence("between")
	 *     .compile();
	 * </pre> 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByWhitespace() {
		partialPattern.append("\\s");
		
		return this;
	}
	
	/**
	 * Starts the description of the pattern with the predefined character
	 * class "\s" which indicates one occurence of a whitespace character
	 * (space, tab, new-line, etc.). For instance, the pattern
	 * "\\s\\strim\\s" can be specified as follows: 
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     whitespace()
	 *     .followedByWhistespace()
	 *     .followedByCharacterSequence("trim")
	 *     .followedByWhistespace()
	 *     .compile();
	 * </pre> 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder whitespace() {
		return new PatternBuilder().followedByWhitespace();
	}
	
	/**
	 * Continues the description of the pattern with the predefined character
	 * class "\S" which indicates one occurence of any character but a
	 * whitespace (space, tab, new-line, etc.)
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByNonWhitespace() {
		partialPattern.append("\\S");
		
		return this;
	}
	
	/**
	 * Starts the description of the pattern with the predefined character
	 * class "\S" which indicates one occurence of any character but a
	 * whitespace (space, tab, new-line, etc.)
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder nonWhitespace() {
		return new PatternBuilder().followedByWhitespace();
	}	
	
	/**
	 * Continues the description of the pattern with the predefined character
	 * class "\w" which indicates one occurence of a word character (a letter
	 * or a number). For instance, the pattern "Rocky \\w" can be
	 * specified as follows: 
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     characterSequence("Rocky")
	 *     .followedByWhistespace()
	 *     .followedByWordCharacter()
	 *     .compile();
	 * </pre> 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByWordCharacter() {
		partialPattern.append("\\w");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined character
	 * class "\w" which indicates one occurence of a word character (a letter
	 * or a number). For instance, the pattern "\\w{2,}" can be
	 * specified as follows: 
	 * 
	 * <pre>
	 *   Pattern pattern =
	 *     wordCharacter()
	 *     .atLeast(2)
	 *     .compile();
	 * </pre> 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder wordCharacter() {
		return new PatternBuilder().followedByWordCharacter();
	}

	/**
	 * Continues the description of the pattern with the predefined character
	 * class "\W" which indicates one occurence of a non-word character
	 * (anything but a letter or a number). 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByNonWordCharacter() {
		partialPattern.append("\\W");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined character
	 * class "\W" which indicates one occurence of a non-word character
	 * (anything but a letter or a number). 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder nonWordCharacter() {
		return new PatternBuilder().followedByNonWordCharacter();
	}
	
	// POSIX character classes
	
	/**
	 * Continues the description of the pattern with the predefined POSIX
	 * character class "\p{Lower}" for lower case alphabetic characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByPosixLowerCaseAlphabeticCharacter() {
		partialPattern.append("\\p{Lower}");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined POSIX
	 * character class "\p{Lower}" for lower case alphabetic characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder posixLowerCaseAlphabeticCharacter() {
		return new PatternBuilder().followedByPosixLowerCaseAlphabeticCharacter();
	}

	/**
	 * Continues the description of the pattern with the predefined POSIX
	 * character class "\p{Upper}" for upper case alphabetic characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByPosixUpperCaseAlphabeticCharacter() {
		partialPattern.append("\\p{Upper}");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined POSIX character
	 * class "\p{Upper}" for upper case alphabetic characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder posixUpperCaseAlphabeticCharacter() {
		return new PatternBuilder().followedByPosixUpperCaseAlphabeticCharacter();
	}

	/**
	 * Continues the description of the pattern with the predefined POSIX
	 * character class "\p{ASCII}" for ASCII characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByPosixAsciiCharacter() {
		partialPattern.append("\\p{ASCII}");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined POSIX
	 * character class "\p{ASCII}" for ASCII characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder posixAsciiCharacter() {
		return new PatternBuilder().followedByPosixAsciiCharacter();
	}

	/**
	 * Continues the description of the pattern with the predefined POSIX
	 * character class "\p{Alpha}" for alphabetic characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByPosixAlphabeticCharacter() {
		partialPattern.append("\\p{Alpha}");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined POSIX
	 * character class "\p{Alpha}" for alphabetic characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder posixAlphabeticCharacter() {
		return new PatternBuilder().followedByPosixAlphabeticCharacter();
	}

	/**
	 * Continues the description of the pattern with the predefined POSIX
	 * character class "\p{Alnum}" for alphanumeric characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByPosixAlphanumericCharacter() {
		partialPattern.append("\\p{Alnum}");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined POSIX
	 * character class "\p{Alnum}" for alphanumeric characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder posixAlphanumericCharacter() {
		return new PatternBuilder().followedByPosixAlphanumericCharacter();
	}

	/**
	 * Continues the description of the pattern with the predefined POSIX
	 * character class "\p{Punct}" for punctuation characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByPosixPunctuationCharacter() {
		partialPattern.append("\\p{Punct}");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined POSIX
	 * character class "\p{Punct}" for punctuation characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder posixPunctuactionCharacter() {
		return new PatternBuilder().followedByPosixAlphanumericCharacter();
	}

	/**
	 * Continues the description of the pattern with the predefined POSIX
	 * character class "\p{Graph}" for visible (alphanumeric o punctuation)
	 * characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByPosixVisibleCharacter() {
		partialPattern.append("\\p{Graph}");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined POSIX
	 * character class "\p{Graph}" for visible (alphanumeric o punctuation)
	 * characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder posixVisibleCharacter() {
		return new PatternBuilder().followedByPosixVisibleCharacter();
	}

	/**
	 * Continues the description of the pattern with the predefined POSIX
	 * character class "\p{Print}" for printable (visible o whitespace)
	 * characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByPosixPrintableCharacter() {
		partialPattern.append("\\p{Print}");
		
		return this;		
	}

	/**
	 * Continues the description of the pattern with the predefined POSIX
	 * character class "\p{Print}" for printable (visible o whitespace)
	 * characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder posixPrintableCharacter() {
		return new PatternBuilder().followedByPosixPrintableCharacter();
	}

	/**
	 * Continues the description of the pattern with the predefined POSIX
	 * character class "\p{Blank}" for blank (space or tab) characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByPosixSpaceOrTabCharacter() {
		partialPattern.append("\\p{Blank}");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined POSIX
	 * character class "\p{Blank}" for blank (space or tab) characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder posixSpaceOrTabCharacter() {
		return new PatternBuilder().followedByPosixSpaceOrTabCharacter();
	}

	/**
	 * Continues the description of the pattern with the predefined POSIX
	 * character class "\p{Cntrl}" for control characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByPosixControlCharacter() {
		partialPattern.append("\\p{Cntrl}");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined POSIX
	 * character class "\p{Cntrl}" for control characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder posixControlCharacter() {
		return new PatternBuilder().followedByPosixControlCharacter();
	}

	/**
	 * Continues the description of the pattern with the predefined POSIX
	 * character class "\p{XDigit}" for hexadecimal digit characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByPosixHexadecimalDigit() {
		partialPattern.append("\\p{XDigit}");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined POSIX
	 * character class "\p{XDigit}" for hexadecimal digit characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder posixHexadecimalDigit() {
		return new PatternBuilder().followedByPosixHexadecimalDigit();
	}

	/**
	 * Continues the description of the pattern with the predefined POSIX
	 * character class "\p{Space}" for whitespace characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByPosixWhitespace() {
		partialPattern.append("\\p{Space}");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined POSIX
	 * character class "\p{Space}" for whitespace characters. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder posixWhitespace() {
		return new PatternBuilder().followedByPosixWhitespace();
	}

	// java.lang.Character classes
	
	/**
	 * Continues the description of the pattern with the predefined Java
	 * character class "\p{javaLowerCase}" for characters for which
	 * {@link Character#isLowerCase(char)} holds. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByJavaLowerCaseCharacter() {
		partialPattern.append("\\p{javaLowerCase}");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined Java
	 * character class "\p{javaLowerCase}" for characters for which
	 * {@link Character#isLowerCase(char)} holds. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder javaLowerCaseCharacter() {
		return new PatternBuilder().followedByJavaLowerCaseCharacter();
	}
	
	/**
	 * Continues the description of the pattern with the predefined Java
	 * character class "\p{javaUpperCase}" for characters for which
	 * {@link Character#isUpperCase(char)} holds. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByJavaUpperCaseCharacter() {
		partialPattern.append("\\p{javaUpperCase}");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined Java
	 * character class "\p{javaUpperCase}" for characters for which
	 * {@link Character#isUpperCase(char)} holds. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder javaUpperCaseCharacter() {
		return new PatternBuilder().followedByJavaLowerCaseCharacter();
	}

	/**
	 * Continues the description of the pattern with the predefined Java
	 * character class "\p{javaWhitespace}" for characters for which
	 * {@link Character#isWhitespace(char)} holds. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByJavaWhitespaceCharacter() {
		partialPattern.append("\\p{javaWhitespace}");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined Java
	 * character class "\p{javaWhitespace}" for characters for which
	 * {@link Character#isWhitespace(char)} holds. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder javaWhitespaceCharacter() {
		return new PatternBuilder().followedByJavaWhitespaceCharacter();
	}

	/**
	 * Continues the description of the pattern with the predefined Java
	 * character class "\p{javaMirrored}" for characters for which
	 * {@link Character#isMirrored(char)} holds. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByJavaMirroredCharacter() {
		partialPattern.append("\\p{javaMIrrored}");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined Java
	 * character class "\p{javaMirrored}" for characters for which
	 * {@link Character#isMirrored(char)} holds. 
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder javaMirroredCharacter() {
		return new PatternBuilder().followedByJavaMirroredCharacter();
	}
	
	// Classes for Unicode blocks and categories

	/**
	 * Continues the description of the pattern with the predefined character
	 * class "\p{InGreek}" for characters in the greek unicode block.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByGreekBlockCharacter() {
		partialPattern.append("\\p{InGreek}");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined character
	 * class "\p{InGreek}" for characters in the greek unicode block.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder greekBlockCharacter() {
		return new PatternBuilder().followedByGreekBlockCharacter();
	}

	/**
	 * Continues the description of the pattern with the predefined character
	 * class "\p{Lu}" for characters in the upper-case letter unicode block.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByUppercaseLetterCharacter() {
		partialPattern.append("\\p{Lu}");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined character
	 * class "\p{Lu}" for characters in the upper-case letter unicode block.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder upperCaseLetterCharacter() {
		return new PatternBuilder().followedByUppercaseLetterCharacter();
	}
	
	/**
	 * Continues the description of the pattern with the predefined character
	 * class "\p{Sc}" for characters in the currency symbol unicode block.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByCurrencySymbolCharacter() {
		partialPattern.append("\\p{Sc}");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined character
	 * class "\p{Sc}" for characters in the currency symbol unicode block.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder currencySymbolCharacter() {
		return new PatternBuilder().followedByCurrencySymbolCharacter();
	}
	
	/**
	 * Continues the description of the pattern with the predefined character
	 * class "\P{InGreek}" for characters outside the greek unicode block.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByNonGreekBlockCharacter() {
		partialPattern.append("\\P{InGreek}");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined character
	 * class "\p{InGreek}" for characters outside the greek unicode block.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder nonGreekBlockCharacter() {
		return new PatternBuilder().followedByNonGreekBlockCharacter();
	}
	
	/**
	 * Continues the description of the pattern with the predefined character
	 * class "[\\p{L}&&[^\\p{Lu}]" for characters outside the upper-case letter unicode
	 * block.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByNonUppercaseLetterCharacter() {
		partialPattern.append("[\\p{L}&&[^\\p{Lu}]");
		
		return this;		
	}

	/**
	 * Start the description of the pattern with the predefined character
	 * class "[\\p{L}&&[^\\p{Lu}]" for characters outside the upper-case letter unicode
	 * block.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder nonUpperCaseLetterCharacter() {
		return new PatternBuilder().followedByNonUppercaseLetterCharacter();
	}
	
	/**
	 * Continues the description of the pattern with the predefined character
	 * class "\\uC" for unicode characters of code C.
	 * 
	 * @param code The unicode code of the character.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByUnicodeCharacter(int code) {
		partialPattern.append("\\u" + code);
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined character
	 * class "\\uC" for unicode characters of code C.
	 * 
	 * @param code The unicode code of the character.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder unicodeCharacter(int code) {
		return new PatternBuilder().followedByUnicodeCharacter(code);
	}

	/**
	 * Continues the description of the pattern with the predefined character
	 * class "\\p{X}" for unicode characters in the block associated with the
	 * property X.
	 * 
	 * @param property The property of the unicode character block.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByUnicodeCharacterInBlock(String property) {
		partialPattern.append("\\p{" + property + "}");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined character
	 * class "\\p{X}" for unicode characters in the block associated with the
	 * property X.
	 * 
	 * @param property The property of the unicode character block.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder unicodeCharacterInBlock(String property) {
		return new PatternBuilder().followedByUnicodeCharacterInBlock(property);
	}
	
	/**
	 * Continues the description of the pattern with the predefined character
	 * class "\\P{X}" for unicode characters outside the block associated with
	 * the property X.
	 * 
	 * @param property The property of the unicode character block.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByUnicodeCharacterOutsideBlock(String property) {
		partialPattern.append("\\P{" + property + "}");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern with the predefined character
	 * class "\\P{X}" for unicode characters outside the block associated with
	 * the property X.
	 * 
	 * @param property The property of the unicode character block.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder unicodeCharacterOutsideBlock(String property) {
		return new PatternBuilder().followedByUnicodeCharacterOutsideBlock(property);
	}

	/**
	 * Just a synonym for {@link #followedByUnicodeCharacterInBlock(String)}.
	 * 
	 * @param property The property of the unicode character block.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByUnicodeCharacterInCategory(String property) {
		return this.followedByUnicodeCharacterInBlock(property);		
	}

	/**
	 * Just a synonym for {@link #unicodeCharacterInBlock(String)}.
	 * 
	 * @param property The property of the unicode character block.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder unicodeCharacterInCategory(String property) {
		return new PatternBuilder().followedByUnicodeCharacterInBlock(property);
	}
	
	/**
	 * Just a synonym for
	 * {@link #followedByUnicodeCharacterOutsideBlock(String)}.
	 * 
	 * @param property The property of the unicode character block.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByUnicodeCharacterOutsideCategory(String property) {
		return this.followedByUnicodeCharacterOutsideBlock(property);
	}

	/**
	 * Just a synonym for
	 * {@link #unicodeCharacterOutsideBlock(String)}.
	 * 
	 * @param property The property of the unicode character block.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder unicodeCharacterOutsideCategory(String property) {
		return new PatternBuilder().followedByUnicodeCharacterOutsideBlock(property);
	}
	
	// Boundary matchers
	
	/**
	 * Continues the description of the pattern inserting the beginning of line
	 * matcher "^".
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByBeginningOfLine() {
		partialPattern.append("^");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern inserting the beginning of line
	 * matcher "^".
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder beginningOfLine() {
		return new PatternBuilder().followedByBeginningOfLine();
	}

	/**
	 * Continues the description of the pattern inserting the end of line
	 * matcher "$".
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByEndOfLine() {
		partialPattern.append("$");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern inserting the end of line
	 * matcher "$".
	 * 
	 * I don't know if it makes sense to start the pattern with an end-of-line
	 * boundary matcher.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder endOfLine() {
		return new PatternBuilder().followedByEndOfLine();
	}

	/**
	 * Continues the description of the pattern inserting the word boundary
	 * matcher "\\b".
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByWordBoundary() {
		partialPattern.append("\\b");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern inserting the word boundary
	 * matcher "\\b".
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder wordBoundary() {
		return new PatternBuilder().followedByWordBoundary();
	}

	/**
	 * Continues the description of the pattern inserting the non-word boundary
	 * matcher "\\B".
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByNonWordBoundary() {
		partialPattern.append("\\B");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern inserting the non-word boundary
	 * matcher "\\B".
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder nonWordBoundary() {
		return new PatternBuilder().followedByNonWordBoundary();
	}

	/**
	 * Continues the description of the pattern inserting the beginning of
	 * input matcher "\\A".
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByBeginningOfInput() {
		partialPattern.append("\\A");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern inserting the beginning of
	 * input matcher "\\A".
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder beginningOfInput() {
		return new PatternBuilder().followedByBeginningOfInput();
	}

	/**
	 * Continues the description of the pattern inserting the end of previous
	 * match matcher "\\G".
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByEndOfPreviousMatch() {
		partialPattern.append("\\G");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern inserting the end of previous
	 * match matcher "\\G".
     *
	 * I don't know if it makes sense to start the pattern with an end of
	 * previous match boundary matcher.
     *
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder endOfPreviousMatch() {
		return new PatternBuilder().followedByEndOfPreviousMatch();
	}

	/**
	 * Continues the description of the pattern inserting the end of input
	 * (but the final terminator) matcher "\\Z".
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByNonFinalTerminatorEndOfInput() {
		partialPattern.append("\\Z");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern inserting the end of input
	 * (but the final terminator) matcher "\\Z".
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder nonFinalTerminatorEndOfInput() {
		return new PatternBuilder().followedByNonFinalTerminatorEndOfInput();
	}

	/**
	 * Continues the description of the pattern inserting the end of input
	 * matcher "\\z".
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByEndOfInput() {
		partialPattern.append("\\z");
		
		return this;		
	}

	/**
	 * Starts the description of the pattern inserting the end of input
	 * matcher "\\z".
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder endOfInput() {
		return new PatternBuilder().followedByEndOfInput();
	}

	// Greedy quantifiers
	
	/**
	 * Inserts the ? greedy quantifier indicating that the rightmost character
	 * class or capturing group of the partial pattern might appear once or not
	 * at all.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder onceOrNotAtAll() {
		partialPattern.append("?");
		
		return this;		
	}
	
	/**
	 * Inserts the * greedy quantifier indicating that the rightmost character
	 * class or capturing group of the partial pattern might appear zero or
	 * more times.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder zeroOrMoreTimes() {
		partialPattern.append("*");
		
		return this;		
	}

	/**
	 * Inserts the + greedy quantifier indicating that the rightmost character
	 * class or capturing group of the partial pattern might appear one or
	 * more times.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder oneOrMoreTimes() {
		partialPattern.append("+");
		
		return this;		
	}
	
	/**
	 * Inserts the {n} greedy quantifier indicating that the rightmost
	 * character class or capturing group of the partial pattern must appear
	 * exactly n times.
	 * 
	 * @param n The amount of times that the pattern must be matched.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder exactly(int n) {
		partialPattern.append("{" + n + "}");
		
		return this;		
	}
	
	/**
	 * Inserts the {n,} greedy quantifier indicating that the rightmost
	 * character class or capturing group of the partial pattern must appear
	 * at least n times.
	 * 
	 * @param n The least amount of times that the pattern must be matched.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder atLeast(int n) {
		partialPattern.append("{" + n + ",}");
		
		return this;		
	}

	/**
	 * Inserts the {n,m} greedy quantifier indicating that the rightmost
	 * character class or capturing group of the partial pattern must appear
	 * at least n times, and at most m times.
     *
	 * @param n The minimum number of times that the pattern must be matched.
	 * @param m The maximum number of times that the pattern can be matched.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder atLeastButNoMore(int n, int m) {
		partialPattern.append("{" + n + "," + m + "}");
		
		return this;		
	}
	
	// Reluctant quantifiers
	
	/**
	 * Inserts the ?? reluctant quantifier indicating that the rightmost
	 * character class or capturing group of the partial pattern might appear
	 * once or not at all, and that the lookup must be done reluctantly.
	 * 
	 * @see <a href="http://java.sun.com/docs/books/tutorial/essential/regex/quant.html">The Java Tutorials</a>
	 * section on Quantifiers for an explanation on reluctant quantifiers.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder onceOrNotAtAllReluctant() {
		partialPattern.append("??");
		
		return this;		
	}
	
	/**
	 * Inserts the *? reluctant quantifier indicating that the rightmost
	 * character class or capturing group of the partial pattern might appear
	 * zero or more times, and that the lookup must be done reluctantly.
     *
	 * @see <a href="http://java.sun.com/docs/books/tutorial/essential/regex/quant.html">The Java Tutorials</a>
	 * section on Quantifiers for an explanation on reluctant quantifiers.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder zeroOrMoreTimesReluctant() {
		partialPattern.append("*?");
		
		return this;		
	}

	/**
	 * Inserts the +? reluctant quantifier indicating that the rightmost
	 * character class or capturing group of the partial pattern might appear
	 * one or more times, and that the lookup must be done reluctantly.
	 * 
	 * @see <a href="http://java.sun.com/docs/books/tutorial/essential/regex/quant.html">The Java Tutorials</a>
	 * section on Quantifiers for an explanation on reluctant quantifiers.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder oneOrMoreTimesReluctant() {
		partialPattern.append("+?");
		
		return this;		
	}
	
	/**
	 * Inserts the {n}? reluctant quantifier indicating that the rightmost
	 * character class or capturing group of the partial pattern must appear
	 * exactly n times, and that the lookup must be done reluctantly.
	 * 
	 * @see <a href="http://java.sun.com/docs/books/tutorial/essential/regex/quant.html">The Java Tutorials</a>
	 * section on Quantifiers for an explanation on reluctant quantifiers.
	 * @param n The amount of times that the pattern must be matched.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder exactlyReluctant(int n) {
		partialPattern.append("{" + n + "}?");
		
		return this;		
	}
	
	/**
	 * Inserts the {n,}? reluctant quantifier indicating that the rightmost
	 * character class or capturing group of the partial pattern must appear
	 * at least n times, and that the lookup must be done reluctantly.
	 * 
	 * @see <a href="http://java.sun.com/docs/books/tutorial/essential/regex/quant.html">The Java Tutorials</a>
	 * section on Quantifiers for an explanation on reluctant quantifiers.
	 * @param n The least amount of times that the pattern must be matched.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder atLeastReluctant(int n) {
		partialPattern.append("{" + n + ",}?");
		
		return this;		
	}

	/**
	 * Inserts the {n,m}? reluctant quantifier indicating that the rightmost
	 * character class or capturing group of the partial pattern must appear
	 * at least n times, and at most m times, and that the lookup must be done
	 * reluctantly.
     *
	 * @see <a href="http://java.sun.com/docs/books/tutorial/essential/regex/quant.html">The Java Tutorials</a>
	 * section on Quantifiers for an explanation on reluctant quantifiers.
	 * @param n The minimum number of times that the pattern must be matched.
	 * @param m The maximum number of times that the pattern can be matched.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder atLeastButNoMoreReluctant(int n, int m) {
		partialPattern.append("{" + n + "," + m + "}?");
		
		return this;		
	}

	// Possessive quantifiers
	
	/**
	 * Inserts the ?+ possesive quantifier indicating that the rightmost
	 * character class or capturing group of the partial pattern might appear
	 * once or not at all, and that the lookup must be done possessively.
	 * 
	 * @see <a href="http://java.sun.com/docs/books/tutorial/essential/regex/quant.html">The Java Tutorials</a>
	 * section on Quantifiers for an explanation on possessive quantifiers.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder onceOrNotAtAllPossessive() {
		partialPattern.append("?+");
		
		return this;		
	}
	
	/**
	 * Inserts the *+ reluctant quantifier indicating that the rightmost
	 * character class or capturing group of the partial pattern might appear
	 * zero or more times, and that the lookup must be done possessively.
     *
	 * @see <a href="http://java.sun.com/docs/books/tutorial/essential/regex/quant.html">The Java Tutorials</a>
	 * section on Quantifiers for an explanation on possessive quantifiers.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder zeroOrMoreTimesPossessive() {
		partialPattern.append("*+");
		
		return this;		
	}

	/**
	 * Inserts the ++ possessive quantifier indicating that the rightmost
	 * character class or capturing group of the partial pattern might appear
	 * one or more times, and that the lookup must be done possessively.
	 * 
	 * @see <a href="http://java.sun.com/docs/books/tutorial/essential/regex/quant.html">The Java Tutorials</a>
	 * section on Quantifiers for an explanation on possessive quantifiers.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder oneOrMoreTimesPossessive() {
		partialPattern.append("++");
		
		return this;		
	}
	
	/**
	 * Inserts the {n}+ possessive quantifier indicating that the rightmost
	 * character class or capturing group of the partial pattern must appear
	 * exactly n times, and that the lookup must be done possesively.
	 * 
	 * @see <a href="http://java.sun.com/docs/books/tutorial/essential/regex/quant.html">The Java Tutorials</a>
	 * section on Quantifiers for an explanation on possessive quantifiers.
	 * @param n The amount of times that the pattern must be matched.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder exactlyPossessive(int n) {
		partialPattern.append("{" + n + "}+");
		
		return this;		
	}
	
	/**
	 * Inserts the {n,}+ possesive quantifier indicating that the rightmost
	 * character class or capturing group of the partial pattern must appear
	 * at least n times, and that the lookup must be done possesively.
	 * 
	 * @see <a href="http://java.sun.com/docs/books/tutorial/essential/regex/quant.html">The Java Tutorials</a>
	 * section on Quantifiers for an explanation on possessive quantifiers.
	 * @param n The least amount of times that the pattern must be matched.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder atLeastPossessive(int n) {
		partialPattern.append("{" + n + ",}+");
		
		return this;		
	}

	/**
	 * Inserts the {n,m}? possessive quantifier indicating that the rightmost
	 * character class or capturing group of the partial pattern must appear
	 * at least n times, and at most m times, and that the lookup must be done
	 * possessively.
     *
	 * @see <a href="http://java.sun.com/docs/books/tutorial/essential/regex/quant.html">The Java Tutorials</a>
	 * section on Quantifiers for an explanation on possessive quantifiers.
	 * @param n The minimum number of times that the pattern must be matched.
	 * @param m The maximum number of times that the pattern can be matched.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder atLeastButNoMorePossesive(int n, int m) {
		partialPattern.append("{" + n + "," + m + "}+");
		
		return this;		
	}
	
	// Logical operators

	/**
	 * Appends the parameters's partial pattern of the parameter to the right
	 * of the current instant partial pattern, indicating that the former
	 * should follow the latter. 
	 * 
	 * @param patternBuilder The pattern that must follow the current partial
	 * pattern.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedBy(PatternBuilder patternBuilder) {
		partialPattern.append(patternBuilder.partialPattern);
		
		return this;
	}
	
	/**
	 * Builds a new pattern builder concatenating the two parameters two
	 * indicate that the right pattern should follow the left pattern.
	 * 
	 * @param leftPatternBuilder The left pattern that should match.
	 * @param rightPatternBuilder The pattern that should match after the left
	 * pattern.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder followedBy(PatternBuilder leftPatternBuilder, PatternBuilder rightPatternBuilder) {
		return new PatternBuilder(leftPatternBuilder).followedBy(rightPatternBuilder);
	}
	
	/**
	 * Appends the parameters's partial pattern of the parameter to the right
	 * of the current instant partial pattern with the or "|" logical operator,
	 * indicating that either pattern could match. 
	 * 
	 * @param patternBuilder The pattern that may match the in case the current
	 * partial pattern doesn't.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder either(PatternBuilder patternBuilder) {
		partialPattern.append("|" + patternBuilder.partialPattern);
		
		return this;
	}
	
	/**
	 * Just a synonym for {@link #either(PatternBuilder)}
	 * 
	 * @param patternBuilder patternBuilder The pattern that may match the in case the current
	 * partial pattern doesn't.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder or(PatternBuilder patternBuilder) {
		partialPattern.append("|" + patternBuilder.partialPattern);
		
		return this;
	}
	
	/**
	 * A generalized version of {@link #either(PatternBuilder)} that builds a
	 * disyunction pattern with all the partial patterns of the parameter
	 * following the order on which they appear.
	 * 
	 * @param patternBuilders A list of partial patterns.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
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
	 * Continues building the partial pattern inserting a capturing group of
	 * another partial pattern. For instance, the pattern "\\s*(\\w+)*" could
	 * be constructed as:
	 * 
	 * Pattern pattern =
	 *   whitespace().zeroOrMoreTimes()
	 *   .followedByCapturingGroupOf(
	 *     wordCharacter().oneOrMoreTimes()
	 *   ).zeroOrMoreTimes().
	 * 
	 * @param patternBuilder A partial pattern that will be inserted in a
	 * capturing group. 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByCapturingGroupOf(PatternBuilder patternBuilder) {
		partialPattern.append("(" + patternBuilder.partialPattern + ")");
		
		return this;
	}
	
	/**
	 * Starts building the partial pattern inserting a capturing group of
	 * another partial pattern. For instance, the pattern "([a-z]+){2}" could
	 * be constructed as:
	 * 
	 * Pattern pattern =
	 *   capturingGroupOf(
	 *     anyInRange("a", "z").oneOrMoreTimes()
	 *   ).exactly(2);
	 * 
	 * @param patternBuilder A partial pattern that will be inserted in a
	 * capturing group. 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder capturingGroupOf(PatternBuilder patternBuilder) {
		return new PatternBuilder().followedByCapturingGroupOf(patternBuilder);
	}
	
	// Back references
	
	/**
	 * Continues building the partial pattern inserting a back reference to a
	 * capturing group to indicate that whatever the group matched, should be
	 * matched again.
	 * 
	 * @param group The number of the capturing group to back-reference.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByCapturingGroupMatch(int group) {
		partialPattern.append("\\" + group);
		
		return this;		
	}

	/**
	 * Starts building the partial pattern inserting a back reference to a
	 * capturing group to indicate that whatever the group matched, should be
	 * matched again.
	 * 
	 * @param group The number of the capturing group to back-reference.
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder capturingGroupMatch(int group) {
		return new PatternBuilder().followedByCapturingGroupMatch(group);
	}

	// Quotation
	
	/**
	 * Continues building the partial pattern inserting quote symbol ("\") to
	 * indicate that the following character should be quoted. For example,
	 * the pattern "^\\.$" could be constructed as follows:
	 * 
	 * Pattern pattern =
	 *   beginningOfLine()
	 *   .followedByQuote()
	 *   .followedByCharacter(".")
	 *   .followedByEndOfLine()
	 *   .compile();
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByQuote() {
		partialPattern.append("\\");
		
		return this;		
	}
	
	/**
	 * Starts building the partial pattern inserting quote symbol ("\") to
	 * indicate that the following character should be quoted. For example,
	 * the pattern "\\^" could be constructed as follows:
	 * 
	 * Pattern pattern =
	 *   quote()
	 *   .followedByCharacter("^")
	 *   .compile();
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder quote() {
		return new PatternBuilder().followedByQuote();
	}
	
	/**
	 * Continues building the partial pattern inserting quote start symbol
	 * ("\Q") to indicate that the following string (up to the next appearance
	 * of a quote end symbol ("\E"), should be matched as-is. For instance, the
	 * pattern "^\\Qinside quotes\\E" could be constructed as follows:
	 * 
	 * Pattern pattern =
	 *   beginningOfLine()
	 *   .followedByQuoteStart()
	 *   .followedByCharacterSequence("inside quotes")
	 *   .followedByQuoteEnd()
	 *   .compile().
	 * 
	 * @see #followedByQuoteEnd()
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByQuoteStart() {
		partialPattern.append("\\Q");
		
		return this;		
	}
	
	/**
	 * Starts building the partial pattern inserting quote start symbol ("\Q")
	 * to indicate that the following string (up to the next appearance of a
	 * quote end symbol ("\E"), should be matched as-is.
	 * 
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder quoteStart() {
		return new PatternBuilder().followedByQuoteStart();
	}

	/**
	 * Continues building the partial pattern inserting quote end symbol
	 * ("\E") to indicate that the preceding string, which should have been
	 * preceded by a quote start symbol ("\Q"), should be matched as-is.
	 * 
	 * @see #followedByQuoteStart()
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByQuoteEnd() {
		partialPattern.append("\\E");
		
		return this;		
	}
	
	/**
	 * Starts building the partial pattern inserting quote end symbol
	 * ("\E") to indicate that the preceding string, which should have been
	 * preceded by a quote start symbol ("\Q"), should be matched as-is.
	 * 
	 * @see #quoteStart()
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder quoteEnd() {
		return new PatternBuilder().followedByQuoteEnd();
	}
	
	// Special constructs

	/**
	 * @param patternBuilder
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByNonCapturingGroupOf(PatternBuilder patternBuilder) {
		partialPattern.append("(?:" + patternBuilder.partialPattern + ")");
		
		return this;
	}
	
	/**
	 * @param patternBuilder
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder nonCapturingGroupOf(PatternBuilder patternBuilder) {
		return new PatternBuilder().followedByNonCapturingGroupOf(patternBuilder);
	}
	
	/**
	 * @param flags
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder setFlags(String flags) {
		partialPattern.append("(?" + flags + ")");
		
		return this;
	}

	/**
	 * @param flags
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder unsetFlags(String flags) {
		partialPattern.append("(?-" + flags + ")");
		
		return this;
	}
	
	/**
	 * @param onFlags
	 * @param offFlags
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder setFlags(String onFlags, String offFlags) {
		partialPattern.append("(?" + onFlags + "-" + offFlags + ")");
		
		return this;
	}
	
	/**
	 * @param patternBuilder
	 * @param flags
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByNonCapturingGroupOf(PatternBuilder patternBuilder, String flags) {
		partialPattern.append("(?" + flags + ":" + patternBuilder.partialPattern + ")");
		
		return this;
	}
	
	/**
	 * @param patternBuilder
	 * @param flags
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder nonCapturingGroupOf(PatternBuilder patternBuilder, String flags) {
		return new PatternBuilder().followedByNonCapturingGroupOf(patternBuilder, flags);
	}

	/**
	 * @param patternBuilder
	 * @param onFlags
	 * @param offFlags
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByNonCapturingGroupOf(PatternBuilder patternBuilder, String onFlags, String offFlags) {
		partialPattern.append("(?" + onFlags + "-" + offFlags + ":" + patternBuilder.partialPattern + ")");
		
		return this;
	}
	
	/**
	 * @param patternBuilder
	 * @param onFlags
	 * @param offFlags
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder nonCapturingGroupOf(PatternBuilder patternBuilder, String onFlags, String offFlags) {
		return new PatternBuilder().followedByNonCapturingGroupOf(patternBuilder, onFlags, offFlags);
	}

	/**
	 * @param patternBuilder
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByZeroWidthPositiveLookAheadOf(PatternBuilder patternBuilder) {
		partialPattern.append("(?=" + patternBuilder.partialPattern + ")");
		
		return this;
	}
	
	/**
	 * @param patternBuilder
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder zeroWidthPositiveLookAheadOf(PatternBuilder patternBuilder) {
		return new PatternBuilder().followedByZeroWidthPositiveLookAheadOf(patternBuilder);
	}

	/**
	 * @param patternBuilder
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByZeroWidthNegativeLookAheadOf(PatternBuilder patternBuilder) {
		partialPattern.append("(?!" + patternBuilder.partialPattern + ")");
		
		return this;
	}
	
	/**
	 * @param patternBuilder
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder zeroWidthNegativeLookAheadOf(PatternBuilder patternBuilder) {
		return new PatternBuilder().followedByZeroWidthNegativeLookAheadOf(patternBuilder);
	}
	
	/**
	 * @param patternBuilder
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByZeroWidthPositiveLookBehindOf(PatternBuilder patternBuilder) {
		partialPattern.append("(?<=" + patternBuilder.partialPattern + ")");
		
		return this;
	}
	
	/**
	 * @param patternBuilder
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder zeroWidthPositiveLookBehindOf(PatternBuilder patternBuilder) {
		return new PatternBuilder().followedByZeroWidthPositiveLookBehindOf(patternBuilder);
	}

	/**
	 * @param patternBuilder
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByZeroWidthNegativeLookBehindOf(PatternBuilder patternBuilder) {
		partialPattern.append("(?<!" + patternBuilder.partialPattern + ")");
		
		return this;
	}
	
	/**
	 * @param patternBuilder
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder zeroWidthNegativeLookBehindOf(PatternBuilder patternBuilder) {
		return new PatternBuilder().followedByZeroWidthNegativeLookBehindOf(patternBuilder);
	}

	/**
	 * @param patternBuilder
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public PatternBuilder followedByIndependentNonCapturingGroupOf(PatternBuilder patternBuilder) {
		partialPattern.append("(?>" + patternBuilder.partialPattern + ")");
		
		return this;
	}
	
	/**
	 * @param patternBuilder
	 * @return The current partially constructed {@link PatternBuilder}
	 * instance.
	 */
	public static PatternBuilder independentNonCapturingGroupOf(PatternBuilder patternBuilder) {
		return new PatternBuilder().followedByIndependentNonCapturingGroupOf(patternBuilder);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PatternBuilder [partialPattern=" + partialPattern + "]";
	}
}

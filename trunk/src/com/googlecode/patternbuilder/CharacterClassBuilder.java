package com.googlecode.patternbuilder;

import java.util.regex.Pattern;


/**
 * 
 * A helper class to construct character class descriptions to be used within
 * the context of a {@link PatternBuilder} call sequence. 
 * 
 * @see Pattern
 * @author Agustin Barto <abarto@gmail.com>
 * 
 */
public class CharacterClassBuilder {
	/**
	 * A {@link StringBuilder} to hold the partial pattern as it is
	 * constructed.
	 */
	private StringBuilder partialPattern;

	/**
	 * Default no-arguments constructor. 
	 */
	private CharacterClassBuilder() {
		partialPattern = new StringBuilder();
	}

	/**
	 * Starts building the character class pattern with a simple character
	 * class that lists the characters that should be included in the set. For
	 * instance, the pattern "[abc]" could be constructed as follows:
	 * 
	 * <pre>
	 * String characterClass =
	 *   CharacterClassBuilder.anyOf("abc");
	 * </pre>
	 * 
	 * @param characters The characters that should be included in the class.
	 * @return The current partially constructed {@link CharacterClassBuilder}
	 * instance.
	 */
	public static CharacterClassBuilder anyOf(String characters) {
		return new CharacterClassBuilder().orAnyOf(characters);
	}

	/**
	 * Continues building the character class pattern by adding a simple
	 * character class that lists the characters that should be included in
	 * the set. For instance, the pattern "[abc][def]" could be constructed as
	 * follows:
	 * 
	 * <pre>
	 * String characterClass =
	 *   CharacterClassBuilder.anyOf("abc")
	 *     .orAnyOf("def");
	 * </pre>
	 * 
	 * Notice that the actual result of the previous call would be
	 * "[[abc][def]]" but it is equivalent to the intended character class.
	 * 
	 * @param characters The characters that should be included in the class.
	 * @return The current partially constructed {@link CharacterClassBuilder}
	 * instance.
	 */
	public CharacterClassBuilder orAnyOf(String characters) {
		partialPattern.append("[" + characters + "]");

		return this;
	}
	
	/**
	 * Starts building the character class pattern with a range of characters
	 * that should be included in the set. For instance, the pattern "[a-z]"
	 * could be constructed as follows:
	 * 
	 * <pre>
	 * String characterClass =
	 *   CharacterClassBuilder.anyInRage("a", "z");
	 * </pre>
	 * 
	 * @param start The start of the character range.
	 * @param end The end of the character range.
	 * @return The current partially constructed {@link CharacterClassBuilder}
	 * instance.
	 */
	public static CharacterClassBuilder anyInRange(String start, String end) {
		return new CharacterClassBuilder().orAnyInRange(start, end);
	}

	/**
	 * Continues building the character class pattern with a range of characters
	 * that should be included in the set. For instance, the pattern "[abc][d-z]"
	 * could be constructed as follows:
	 * 
	 * <pre>
	 * String characterClass =
	 *   CharacterClassBuilder.anyOf("abc")
	 *     .orAnyInRage("d", "z");
	 * </pre>
	 * 
	 * @param start The start of the character range.
	 * @param end The end of the character range.
	 * @return The current partially constructed {@link CharacterClassBuilder}
	 * instance.
	 */
	public CharacterClassBuilder orAnyInRange(String start, String end) {
		partialPattern.append("[" + start + "-" + end + "]");

		return this;
	}

	/**
	 * Starts building the character class pattern with a negated simple
	 * character class that lists the characters that shouldn't be included in
	 * the set. For instance, the pattern "[^abc]" could be constructed as
	 * follows:
	 * 
	 * <pre>
	 * String characterClass =
	 *   CharacterClassBuilder.noneOf("abc");
	 * </pre>
	 * 
	 * @param characters The characters that shouldn't be included in the class.
	 * @return The current partially constructed {@link CharacterClassBuilder}
	 * instance.
	 */
	public static CharacterClassBuilder noneOf(String characters) {
		return new CharacterClassBuilder().orNoneOf(characters);
	}

	/**
	 * Continues building the character class pattern with a negated simple
	 * character class that lists the characters that shouldn't be included in
	 * the set. For instance, the pattern "[ABC][^DE]" could be constructed as
	 * follows:
	 * 
	 * <pre>
	 * String characterClass =
	 *   CharacterClassBuilder.anyOf("ABC")
	 *     .orNoneOf("DE");
	 * </pre>
	 * 
	 * @param characters The characters that shouldn't be included in the class.
	 * @return The current partially constructed {@link CharacterClassBuilder}
	 * instance.
	 */
	public CharacterClassBuilder orNoneOf(String characters) {
		partialPattern.append("[^" + characters + "]");

		return this;
	}

	/**
	 * Starts building the character class pattern with a negated range of
	 * characters that shouldn't be included in the set. For instance, the
	 * pattern "[^a-z]" could be constructed as follows:
	 * 
	 * <pre>
	 * String characterClass =
	 *   CharacterClassBuilder.noneInRage("a", "z");
	 * </pre>
	 * 
	 * @param start The start of the character range.
	 * @param end The end of the character range.
	 * @return The current partially constructed {@link CharacterClassBuilder}
	 * instance.
	 */
	public static CharacterClassBuilder noneInRage(String start, String end) {
		return new CharacterClassBuilder().orNoneInRage(start, end);
	}

	/**
	 * Continues building the character class pattern with a negated range of
	 * characters that shouldn't be included in the set. For instance, the
	 * pattern "[A-Z][^a-z]" could be constructed as follows:
	 * 
	 * <pre>
	 * String characterClass =
	 *   CharacterClassBuilder.anyInRange("A", "Z")
	 *     .orNoneInRage("a", "z");
	 * </pre>
	 * 
	 * @param start The start of the character range.
	 * @param end The end of the character range.
	 * @return The current partially constructed {@link CharacterClassBuilder}
	 * instance.
	 */
	public CharacterClassBuilder orNoneInRage(String start, String end) {
		partialPattern.append("[^" + start + "-" + end + "]");

		return this;
	}

	/**
	 * Intersects the partially constructed character class with a simple
	 * character class. For instance, the character class "[a-z]&&[aeiou]"
	 * could be constructed as follows:
	 * 
	 * <pre>
	 * String characterClass =
	 *   CharacterClassBuilder.anyInRange("a", "z")
	 *     .intersectedWith("aeiou");
	 * </pre>
	 * 
	 * @param characters The characters of the simple class to intersect.
	 * @return The current partially constructed {@link CharacterClassBuilder}
	 * instance.
	 */
	public CharacterClassBuilder intersectedWith(String characters) {
		partialPattern.append("&&[" + characters + "]");

		return this;
	}
	
	/**
	 * Intersects the partially constructed character class with a range
	 * character class. For instance, the character class "[a-z]&&[x-z]"
	 * could be constructed as follows:
	 * 
	 * <pre>
	 * String characterClass =
	 *   CharacterClassBuilder.anyInRange("a", "z")
	 *     .intersectedWithRage("x", "z");
	 * </pre>
	 * 
	 * @param start The start of the character range.
	 * @param end The end of the character range.
	 * @return The current partially constructed {@link CharacterClassBuilder}
	 * instance.
	 */
	public CharacterClassBuilder intersectedWithRange(String start, String end) {
		partialPattern.append("&&[" + start + "-" + end + "]");

		return this;
	}


	/**
	 * Intersects the partially constructed character class with a negated
	 * simple character class. For instance, the character class
	 * "[a-z]&&[^aeiou]" could be constructed as follows:
	 * 
	 * <pre>
	 * String characterClass =
	 *   CharacterClassBuilder.anyInRange("a", "z")
	 *     .butNoneOf("aeiou");
	 * </pre>
	 * 
	 * @param characters The characters of the simple class to intersect.
	 * @return The current partially constructed {@link CharacterClassBuilder}
	 * instance.
	 */
	public CharacterClassBuilder butNoneOf(String characters) {
		partialPattern.append("&&[^" + characters + "]");

		return this;
	}

	/**
	 * Intersects the partially constructed character class with a negated
	 * range character class. For instance, the character class "[a-z]&&[^x-z]"
	 * could be constructed as follows:
	 * 
	 * <pre>
	 * String characterClass =
	 *   CharacterClassBuilder.anyInRange("a", "z")
	 *     .butNoneInRage("x", "z");
	 * </pre>
	 * 
	 * @param start The start of the character range.
	 * @param end The end of the character range.
	 * @return The current partially constructed {@link CharacterClassBuilder}
	 * instance.
	 */
	public CharacterClassBuilder butNoneInRange(String start, String end) {
		partialPattern.append("&&[^" + start + "-" + end + "]");

		return this;
	}

	/**
	 * Renders the character class pattern to be used in a {@link PatternBuilder}
	 * call sequence.
	 * 
	 * @return A string representation of the constructed character class.
	 * @see PatternBuilder#characterClass(String)
	 * @see PatternBuilder#followedByCharacterClass(String)
	 */
	public String compile() {
		return "[" + partialPattern.toString() + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CharacterClassBuilder [partialPattern=" + partialPattern + "]";
	}
}
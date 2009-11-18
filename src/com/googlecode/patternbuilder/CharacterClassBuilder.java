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
	 * 
	 */
	private StringBuilder partialPattern;

	/**
	 * 
	 */
	public CharacterClassBuilder() {
		partialPattern = new StringBuilder();
	}

	/**
	 * @param characters
	 * @return
	 */
	public static CharacterClassBuilder anyOf(String characters) {
		return new CharacterClassBuilder().orAnyOf(characters);
	}

	/**
	 * @param characters
	 * @return
	 */
	public CharacterClassBuilder orAnyOf(String characters) {
		partialPattern.append("[" + characters + "]");

		return this;
	}

	/**
	 * @param start
	 * @param end
	 * @return
	 */
	public static CharacterClassBuilder anyInRange(String start, String end) {
		return new CharacterClassBuilder().orAnyInRange(start, end);
	}

	/**
	 * @param start
	 * @param end
	 * @return
	 */
	public CharacterClassBuilder orAnyInRange(String start, String end) {
		partialPattern.append("[" + start + "-" + end + "]");

		return this;
	}

	/**
	 * @param characters
	 * @return
	 */
	public static CharacterClassBuilder noneOf(String characters) {
		return new CharacterClassBuilder().orNoneOf(characters);
	}

	/**
	 * @param characters
	 * @return
	 */
	public CharacterClassBuilder orNoneOf(String characters) {
		partialPattern.append("[^" + characters + "]");

		return this;
	}

	/**
	 * @param start
	 * @param end
	 * @return
	 */
	public static CharacterClassBuilder noneInRage(String start, String end) {
		return new CharacterClassBuilder().orNoneInRage(start, end);
	}

	/**
	 * @param start
	 * @param end
	 * @return
	 */
	public CharacterClassBuilder orNoneInRage(String start, String end) {
		partialPattern.append("[^" + start + "-" + end + "]");

		return this;
	}

	/**
	 * @param characters
	 * @return
	 */
	public CharacterClassBuilder intersectedWith(String characters) {
		partialPattern.append("&&[" + characters + "]");

		return this;
	}

	/**
	 * @param characters
	 * @return
	 */
	public CharacterClassBuilder butNoneOf(String characters) {
		partialPattern.append("&&[^" + characters + "]");

		return this;
	}

	/**
	 * @param start
	 * @param end
	 * @return
	 */
	public CharacterClassBuilder butNoneInRange(String start, String end) {
		partialPattern.append("&&[^" + start + "-" + end + "]");

		return this;
	}

	/**
	 * @return
	 */
	public String compile() {
		return "[" + partialPattern.toString() + "]";
	}
}
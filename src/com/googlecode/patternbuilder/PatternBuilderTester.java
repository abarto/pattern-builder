package com.googlecode.patternbuilder;

import static com.googlecode.patternbuilder.CharacterClassBuilder.*;
import static com.googlecode.patternbuilder.PatternBuilder.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternBuilderTester {
	public static void main(String[] args) {
		// Roman numerals pattern - From Dive into Python 2.4
		//
		// '^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$'
		
		Pattern romanNumeralsPattern =
			beginningOfLine()
				.followedByCharacter("M").atLeastButNoMore(0, 4)
				.followedByCapturingGroupOf(
						either(
								characterSequence("CM"),
								characterSequence("CD"),
								character("D").onceOrNotAtAll()
								.followedByCharacter("C").atLeastButNoMore(0, 3)
								)
						)
				.followedByCapturingGroupOf(
						either(
								characterSequence("XC"),
								characterSequence("XL"),
								character("L").onceOrNotAtAll()
									.followedByCharacter("X").atLeastButNoMore(0, 3)								
								)
						)
				.followedByCapturingGroupOf(
						either(
								characterSequence("IX"),
								characterSequence("IV"),
								character("V").onceOrNotAtAll()
									.followedByCharacter("I").atLeastButNoMore(0, 3)								
								)
						)
				.followedByEndOfLine()
				.compile();
		
			System.out.println("romanNumeralsPattern = " + romanNumeralsPattern);
			
			Matcher matcher = romanNumeralsPattern.matcher("MMDCLXVI");
			System.out.println("MMDCLXVI matches romanNumeralsPattern: " + matcher.matches());
			
			matcher = romanNumeralsPattern.matcher("XCLXVI");
			System.out.println("XCLXVI matches romanNumeralsPattern: " + matcher.matches());
			
			Pattern onlyLettersPattern =
				characterClass(
						anyInRange("a", "z")
						.orAnyInRange("A", "Z")
						)
					.oneOrMoreTimes()
					.compile();
			
			System.out.println("onlyLettersPattern = " + onlyLettersPattern);

			matcher = onlyLettersPattern.matcher("XCLXVI");
			System.out.println("XCLXVI matches onlyLettersPattern: " + matcher.matches());

			matcher = onlyLettersPattern.matcher("abc123");
			System.out.println("abc123 matches onlyLettersPattern: " + matcher.matches());
	}
}
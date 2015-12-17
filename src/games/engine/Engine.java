/***********************************************************************//**
* @file			Engine.java
* @author		Kurt E. Clothier
* @date			December 4, 2015
* 
* @breif		Engine constants and utilities
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/
package games.engine;

import java.nio.charset.Charset;
import java.util.Locale;

import games.Strings;

public enum Engine {
	
	INSTANCE;
	
	/** Specifies the direction of play. */
	public static enum DirectionOfPlay {
		/** Clockwise = to the right */
		CW, 
		/** Counter Clockwise = to the left */
		CCW
	}
	
	/** Title for GUI Frames */
	public static final String GUI_TITLE = "Java Card Game Engine";
	/** Character set used in plugin files. */
	public static final Charset CHARSET = Charset.forName("UTF-8");
	/** Locale to be used. */
	public static final Locale LOCALE = Locale.ENGLISH;
	
	/**
	 * Used to convert an enum member name to a string found in a <tt>Plugin</tt> file.
	 * Returns a lowercase string with all '_' replaced with '-'.
	 * 
	 * @param enumMember a member of a generic Enum to convert
	 * @return a converted enum string
	 */
	protected static <T extends Enum<T>> String convertEnumMemberToPluginString(final T enumMember) {
		return enumMember.toString().toLowerCase(Engine.LOCALE).replace('_', '-');
	}
	
	/**
	 * Used to convert an enum member name to a more legible string.
	 * Returns a Camel Case string with all '_' replaced with ' '.
	 * 
	 * @param enumMember a member of a generic Enum to convert
	 * @return a legibly converted string
	 */
	protected static <T extends Enum<T>> String convertEnumMemberToLegibleString(final T enumMember) {
		return Strings.toCamelCase(enumMember.toString().toLowerCase(Engine.LOCALE).replace('_', ' '));
	}
	
	/**
	 * Returns the specified string as an enum member of the specified values type, if it exists.
	 * 
	 * @param values enum values of type T; pass enum.values() for desired enum type T
	 * @param string string to be converted to an enum member
	 * @return enum member of type T, or null
	 */
	public static <T extends Enum<T>> T stringToEnumMember(final String string, final T[] values) {
		// This way is done rather than the "valueOf(String)" method as that involves exception handling
		T enumMember = null;
		for (T e : values) {
			if (e.toString().equalsIgnoreCase(string)) {
				enumMember = e;
				break;
			}
		}
		return enumMember;
	}
	
	/**
	 * Returns the specified string as an enum member of the specified values type, if it exists.
	 * 
	 * @param enumType generic Enum class - pass EnumName.class
	 * @param string string to be converted to an enum member
	 * @return enum member of enumType, or null
	 */
	public static <T extends Enum<T>> T stringToEnumMember(final String string, final Class<T> enumType) {
		// This way is done rather than the "valueOf(String)" method as that involves exception handling
		T enumMember = null;
		for (T e : enumType.getEnumConstants()) {
			if (Engine.convertEnumMemberToPluginString(e).equalsIgnoreCase(string)) {
				enumMember = e;
				break;
			}
		}
		return enumMember;
	}
	
	/**
	 * Returns <tt>true</tt> if the specified string is a member of the specified enumType.
	 * 
	 * @param enumType generic Enum class - pass EnumName.class
	 * @param string string to be tested as an enum member
	 * @return  <tt>true</tt> if the specified string is a member of the specified enumType
	 */
	public static <T extends Enum<T>> boolean stringIsMemberOf(final String string, final Class<T> enumType) {
		return Engine.stringToEnumMember(string, enumType) != null;
	}
}

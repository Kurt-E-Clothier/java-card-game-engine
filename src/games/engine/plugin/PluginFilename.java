/***********************************************************************//**
* @file			PluginFilename.java
* @author		Kurt E. Clothier
* @date			November 18, 2015
* 
* @breif		Immutable name of a Plugin File
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/

package games.engine.plugin;

import java.io.Serializable;

public final class PluginFilename implements Serializable {
	
	private static final long serialVersionUID = 7791724932238208227L;
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/	
	private final String fullname;
	
/*------------------------------------------------
 	Constructors
 ------------------------------------------------*/
	/**
	 * Construct a <tt>PluginFilename</tt> from the given type and rawName.
	 * Plugins ignore comments, book-end whitespace, and capitalization.
	 * 
	 * @param type	the type of file to be created
	 * @param rawName	raw name of plugin component (no prefix or extension)
	 * @throws IllegalArgumentException if parameters are null
	 */
	public PluginFilename(final Plugin.Type type, final String rawName) throws IllegalArgumentException {
		if (type == null || rawName == null || rawName.equals("")) {
			throw new IllegalArgumentException("Parameters cannot be null!");
		}
		final StringBuilder str = new StringBuilder();
		str.append(type.toString().toLowerCase(Plugin.LOCALE))
		   .append('.').append(rawName).append('.').append(Plugin.EXTENSION);
		fullname = str.toString();
	}
	
	/**
	 * Construct a <tt>PluginFilename</tt> from the given String.
	 * Plugins ignore comments, book-end whitespace, and capitalization.
	 * 
	 * @param fullName full name of plugin component with no prefix and extension)
	 * @throws IllegalArgumentException if parameters are null or filename is invalid
	 */
	public PluginFilename(final String fullName) throws IllegalArgumentException {
		if (fullName == null || fullName.equals("")) {
			throw new IllegalArgumentException("Parameters cannot be null!");
		}
		
		// Make sure this is a valid name
		final String str[] = fullName.split(PluginPattern.PERIOD.toString());
		if (!(str.length == Plugin.LENGTH &&
			  str[2].equalsIgnoreCase(Plugin.EXTENSION))) {
			throw new IllegalArgumentException("Invalid plugin name; must be: type.name.txt");
		}
		// Make sure this is a valid type
		boolean isInvalidType = true;
		for (final Plugin.Type t : Plugin.Type.values()) {
			if (t.toString().equalsIgnoreCase(str[0])) {
				isInvalidType = false;
				break;
			}
		}
		if (isInvalidType) {
			throw new IllegalArgumentException("Invalid type of plugin: " + str[0]);
		}
		fullname = fullName;
	}
	
/*------------------------------------------------
 	Accessors
 ------------------------------------------------*/
	/**
	 * Returns this <tt>PluginFilename</tt> as a string.
	 * 
	 * @return this plugin filename as a String
	 */
	public String getName() {
		return fullname;
	}
	
	/**
	 * Returns this <tt>PluginFilename</tt> as a string, without the type prefix or extension.
	 * 
	 * @return this plugin filename as a String, without the type prefix or extension.
	 */
	public String getRawName() {
		return this.split()[1];
	}
	
	/**
	 * Returns the raw name of this <tt>PluginFilename</tt> as a string, converted to spaced camel-case words.
	 * 
	 * @return the raw name as a String, converted to spaced camel-case words
	 */
	public String getConvertedName() {
		final String rawName = this.getRawName().toLowerCase(Plugin.LOCALE)
								   .replaceAll("_", " ").replaceAll("-", " - ");
		final String[] words = rawName.split(PluginPattern.WHITESPACE.toString());
		final StringBuilder str = new StringBuilder();
		for (final String word : words) {
			str.append(Character.toUpperCase(word.charAt(0)))
			   .append(word.substring(1)).append(' ');
		}
		str.setLength(str.length() - 1);
		return str.toString();
	}
	
	/**
	/**
	 * Returns the type of this <tt>PluginFilename</tt> as a string.
	 * 
	 * @return the type of this plugin filename as a String
	 */
	public String getTypePrefix() {
		return this.split()[0];
	}
	
	/**
	 * Returns the type of this <tt>PluginFilename</tt>.
	 * 
	 * @return the type of this plugin filename
	 */
	public Plugin.Type getType() {
		Plugin.Type type = null;
		for (final Plugin.Type t : Plugin.Type.values()) {
			if (t.toString().equalsIgnoreCase(this.getTypePrefix())) {
				type = t;
				break;
			}
		}
		return type;
	}
	
	/** 
	 * Returns a <tt>PluginFile</tt> created with this <tt>PluginFilename</tt>.
	 * 
	 * @return a <tt>PluginFile</tt> created with this <tt>PluginFilename</tt>
	 * @throws PluginException if the plugin does not exist
	 */
	public PluginFile toFile() throws PluginException {
		return new PluginFile(this);
	}
	
	/** 
	 * Returns a <tt>PluginFile</tt> created with the raw name of this <tt>PluginFilename</tt>.
	 * 
	 * @param type the type of plugin file to create
	 * @return a <tt>PluginFile</tt> created with the raw name of this <tt>PluginFilename</tt>
	 * @throws PluginException if the plugin does not exist
	 */
	public PluginFile toFile(final Plugin.Type type) throws PluginException {
		return new PluginFile(new PluginFilename(type, this.getRawName()));
	}
	
/*------------------------------------------------
 	Utility Methods
 ------------------------------------------------*/
	/**
	 * Returns this filename split into String pieces.
	 * 
	 * @return array of String pieces of this filename
	 */
	public String[] split(){
		return fullname.split(PluginPattern.PERIOD.toString());
	}
	
/*------------------------------------------------
	Overridden Methods
 ------------------------------------------------*/
	/**
	 * Return this filename as a string
	 *
	 * @return this filename as a string
	 */
	@Override public String toString() {
		return fullname;
	}	


}

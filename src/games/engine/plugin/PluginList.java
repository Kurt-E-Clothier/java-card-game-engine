/***********************************************************************//**
* @file			PluginFilename.java
* @author		Kurt E. Clothier
* @date			November 18, 2015
* 
* @breif		Immutable file used to create a Plugin
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/
package games.engine.plugin;

import java.io.File;
import java.io.FilenameFilter;

public final class PluginList { 
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final String[] list;
	
/*------------------------------------------------
 	Constructors
 ------------------------------------------------*/
	/**
	 * Construct a <tt>PluginList</tt> from the given PluginFilename.
	 * 
	 * @param filename the name of the file to be created
	 * @throws PluginException if the plugin does not exist
	 */
	public PluginList(final Plugin.Type type) {
		final String prefix = type.toString().toLowerCase(Plugin.LOCALE);
		this.list = Plugin.DIRECTORY.list(new FilenameFilter() {
			@Override public boolean accept(final File dir, final String name) {
				final String nameLC = name.toLowerCase(Plugin.LOCALE);
				return 	nameLC.startsWith(prefix) &&
						nameLC.endsWith(Plugin.EXTENSION) &&
						!(	nameLC.contains("template") || 
							nameLC.contains("test"));
			}
		});
	}
	
/*------------------------------------------------
 	Accessors
 ------------------------------------------------*/
	
	/**
	 * Returns an array of PluginFilenames.
	 * 
	 * @return an array of PluginFilenames
	 */
	public PluginFilename[] get() {
		final PluginFilename[] pList = new PluginFilename[list.length];
		for (int i = 0; i < list.length; i++) {
			pList[i] = new PluginFilename(list[i]);
		}
		return pList;
	}
	
/*------------------------------------------------
	Overridden Methods
 ------------------------------------------------*/
	
/*------------------------------------------------
	Static Methods
 ------------------------------------------------*/


}


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

public final class PluginFile extends File {

	private static final long serialVersionUID = -9008915600033979478L;
	private final PluginFilename filename;
	
	/**
	 * Construct a <tt>PluginFile</tt> from the given PluginFilename.
	 * 
	 * @param filename the name of the file to be created
	 * @throws PluginException if the plugin does not exist
	 */
	public PluginFile(final PluginFilename filename) throws PluginException {
		super(Plugin.DIRECTORY, filename.toString());
		this.filename = filename;
	}
	
	/**
	 * Returns the <tt>PluginFilename</tt> of this <tt>PluginFile</tt>.
	 * 
	 * @return the <tt>PluginFilename</tt> of this <tt>PluginFile</tt>.
	 */
	public PluginFilename getFilename() {
		return filename;
	}
	
	/**
	 * Returns this <tt>PluginFilename</tt> as a string.
	 *
	 * @return this <tt>PluginFilename</tt> as a string
	 */
	@Override public String toString() {
		return filename.toString();
	}	
}

/***********************************************************************//**
* @file			PluginException.java
* @author		Kurt E. Clothier
* @date			November 18, 2015
*
* @breif		Special exception for plugin text files
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
* @see			PlayingCard
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/

package games.engine.plugin;


/******************************************************************//**
 * The PluginException Class
 * - Custom exception to consolidate issues involving plugin text files
 * - Composed exceptions should contain a message detailing the cause
 * Causes (not limited to): 
 * - IO issues (missing or corrupted files, etc)
 * - Missing mandatory keywords
 * - Invalid or missing keyword parameters
 * - Conflicts between keyword parameters 
 ********************************************************************/
public class PluginException extends Exception {
	
	private static final long serialVersionUID = -1067733586071491339L;
	private static final String IN_PLUGIN = "\" in plugin: ";
	private static final String UNKNOWN_VAR = "UNKNOWN";

	/**
	 * Types of Plugin Exceptions
	 * INVALID_PARAMETER - Keyword parameter is invalid
	 * MISSING_KEYWORD - Mandatory keyword not found
	 * DOES_NOT_EXIST - Plugin does not exist
	 * INVALID_NAME - Invalid plugin filename
	 * INVALID_TYPE - Invalid plugin type
	 * FILE_READ_ERROR - Error reading pugin file.
	 */
	public static enum Type { 	INVALID_CONDITIONAL_STATEMENT,
								INVALID_COMPONENT,
								INVALID_OPERATION,
								INVALID_OPERATION_PARAMS,
								INVALID_PARAMETER,
								MISSING_PARAMETER,
								INVALID_KEYWORD,
								MISSING_KEYWORD,
								DOES_NOT_EXIST,
								INVALID_NAME,
								INVALID_TYPE,
								FILE_READ_ERROR,
								DATA_REPRESENTATION,
								MISMATCH,
								UNKNOWN};

	private final PluginException.Type type;

	/**
	 * Constructs a new <tt>PluginException</tt>.
	 */
	public PluginException() {
		this("Plugin file, keyword, or parameter error!");
	}
	
	/**
	 * Constructs a new <tt>PluginException</tt>.
	 * 
	 * @param message	information about this exception
	 */
	public PluginException(final String message) {
		this(message, PluginException.Type.UNKNOWN);
	}
	
	/**
	 * Constructs a new <tt>PluginException</tt>.
	 *
	 * @param message information about this exception
	 * @param type specific type of plugin exception
	 */
	public PluginException(final String message, final PluginException.Type type) {
		super(message);
		this.type = type;
	}
	
	/**
	 * Constructs a new <tt>PluginException</tt>.
	 * 
	 * @param cause	the original exception which caused this exception to be created
	 */
	public PluginException(final Throwable cause) {
		super(cause);
		this.type = PluginException.Type.UNKNOWN;
	}
	
	/**
	 * Constructs a new <tt>PluginException</tt>.
	 * 
	 * @param message	information about this exception
	 * @param cause		the original exception which caused this exception to be created
	 */
	public PluginException(final String message, final Throwable cause) {
		this(message, cause, PluginException.Type.UNKNOWN);
	}
	
	/**
	 * Constructs a new <tt>PluginException</tt>.
	 *
	 * @param message information about this exception
	 * @param cause 	the original exception which caused this exception to be created
	 * @param type specific type of plugin exception
	 */
	public PluginException(final String message, final Throwable cause, final PluginException.Type type) {
		super(message, cause);
		this.type = type;
	}
	
	/**
	 * Constructs a new <tt>PluginException</tt>.
	 * 
	 * @param message	information about this exception
	 * @param cause		the original exception which caused this exception to be created
	 * @param enableSuppression		whether or not suppression is enabled
	 * @param writableStackTrace	whether or not the stack trace should be writable
	 */
	public PluginException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		type = PluginException.Type.UNKNOWN;
	}
	
	/**
	 * Returns the specific type of this exception.
	 * 
	 * @return specific type of this exception
	 */
	public final PluginException.Type getType() {
		return this.type;
	}
	
	/**
	 * Creates and returns a new <tt>PluginException</tt>.
	 * 
	 * @param type	specific type of plugin exception
	 * @param plugin plugin causing this exception
	 * @param vars optional identifiers (in this order): filename, keyword, parameter
	 * @return new PluginException
	 */
	public static final PluginException create(final PluginException.Type type, final Plugin plugin, final String... vars) { 
		return PluginException.create(type, null, plugin, vars);
	}
	
	/**
	 * Creates and returns a new <tt>PluginException</tt>.
	 * 
	 * @param type	specific type of plugin exception
	 * @param pluginFilename pluginFilename of the Plugin causing this exception
	 * @param vars optional identifiers (in this order): filename, keyword, parameter
	 * @return new PluginException
	 */
	public static final PluginException create(final PluginException.Type type, final PluginFilename pluginFilename, final String... vars) { 
		return PluginException.create(type, null, pluginFilename, vars);
	}
	
	/**
	 * Creates and returns a new <tt>PluginException</tt>.
	 * 
	 * @param type	specific type of plugin exception
	 * @param cause	the original exception which caused this exception to be created
	 * @param plugin plugin causing this exception
	 * @param vars optional identifiers (in this order): filename, keyword, parameter
	 * @return new PluginException
	 */
	public static final PluginException create(final PluginException.Type type, final Throwable cause, final Plugin plugin, final String... vars) {
		return PluginException.create(type, cause, plugin.getFilename(), vars);
	}
	
	/**
	 * Creates and returns a new <tt>PluginException</tt>.
	 * 
	 * @param type	specific type of plugin exception
	 * @param cause	the original exception which caused this exception to be created
	 * @param pluginFilename pluginFilename of the Plugin causing this exception
	 * @param vars optional identifiers (in this order): keyword/operation, parameter/second-keyword
	 * @return new PluginException
	 */
	public static final PluginException create(final PluginException.Type type, final Throwable cause, final PluginFilename pluginFilename, final String... vars) {
		String var0 = vars.length > 0 ? vars[0] : UNKNOWN_VAR;
		String var1 = vars.length > 1 ? vars[1] : UNKNOWN_VAR;
		final StringBuilder str = new StringBuilder();
		switch (type) {
		case INVALID_CONDITIONAL_STATEMENT:
			str.append(" Invalid conditional statement \"").append(var1)
			   .append("\" for condition \"").append(var0).append(IN_PLUGIN);
			break;
		case INVALID_COMPONENT:
			str.append(" Invalid component \"").append(var0).append(IN_PLUGIN);
			break;
		case INVALID_OPERATION:
			str.append(" Invalid operation \"").append(var0).append(IN_PLUGIN);
			break;
		case INVALID_OPERATION_PARAMS:
			str.append(" Invalid operation parameters for \"")
			   .append(var0).append(IN_PLUGIN);
			break;
		case INVALID_PARAMETER:
			str.append("Invalid parameter \"").append(var1)
			   .append("\" for keyword \"").append(var0)
			   .append(IN_PLUGIN);
			break;
		case MISSING_PARAMETER:
			str.append("Missing parameter for keyword \"").append(var0);
			if (vars.length > 1) {
				str.append("\" or \"").append(var1);
			}
			str.append(IN_PLUGIN);
			break;
		case INVALID_KEYWORD:
			str.append("Expected keyword \"").append(var0)
			   .append("\" not \"").append(var1).append(IN_PLUGIN);
			break;
		case MISSING_KEYWORD:
			str.append("Missing keyword \"").append(var0);
			if (vars.length > 1) {
				str.append("\" or \"").append(var1);
			}
			str.append(IN_PLUGIN);
			break;
		case DOES_NOT_EXIST:
			str.append("Plugin does not exist: ");
			break;
		case INVALID_NAME:
			str.append("Invalid plugin filename: ");
			break;
		case INVALID_TYPE:
			str.append("Invalid plugin type: ");
			break;
		case FILE_READ_ERROR:
			str.append("Error reading plugin file: ");
			break;
		case DATA_REPRESENTATION:
			str.append("Data representation error for \"")
			   .append(var0).append(IN_PLUGIN);
			break;
		case MISMATCH:
			str.append(var0).append(" data mismatch in file: ");
			break;
		default:
			return new PluginException();
		}
		str.append(pluginFilename.toString());
		return cause == null ?	new PluginException(str.toString(), type) :
								new PluginException(str.toString(), cause, type);
	}
}

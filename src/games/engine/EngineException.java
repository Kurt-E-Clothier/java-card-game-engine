/***********************************************************************//**
* @file			EngineException.java
* @author		Kurt E. Clothier
* @date			December 10, 2015
* 
* @breif		Special exception for game engine
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
* @see			PlayingCard
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/

package games.engine;

/******************************************************************//**
 * The EngineException Class
 * - Custom exception to consolidate issues involving the game engine
 ********************************************************************/
public class EngineException extends Exception {

	private static final long serialVersionUID = -4933041681362143891L;

	/** Types of Engine Exceptions */
	public static enum Type {
		/** Unknown or invalid type of operation 
		 * 	- String...vars = { component name } */
		INVALID_COMPONENT ("Unknown or invalid type of engine component."),
		/** Missing or invalid parameters for an operation
		 * 	- String...vars = { parameter(s) } */
		INVALID_OPERATION_PARAMETER ("Missing or invalid parameters for an operation."),
		/** Missing or invalid parameters for an operation
		 * 	- String...vars = { parameter, type, expected type  } */
		INVALID_PARAMETER_TYPE ("Operation expected a different type of parameter than was given."),
		/** Incorrect number of parameters 
		 * 	- String...vars = { action/condition name } */
		INVALID_NUMBER_OF_PARAMETERS("Incorrect number of parameters for the specified operation."),
		/** Value type mismatch
		 * 	- String...vars = { value object, casting class name } */
		VALUE_TYPE_MISMATCH("Cannot cast value to the specified type."),
		/** Condition includes statement which calls itself.
		 * 	- String...vars = {condition name, statement string} */
		INFINITE_CONDITIONAL_RECURSION("Condition calls statement, which calls condition."),
		/** Unknown type of engine exception 
		 * 	- String...vars = {} */
		UNKNOWN ("Unknown type of engine exception.");
		
		private String description;
		private Type(final String s) {
			description = s;
		}
		/** Returns a string description of this exception type. */
		public String getDescription() {
			return description;
		}
	};

	private final EngineException.Type type;
	private final Operation operation;
	
	/**
	 * Constructs a new <tt>EngineException</tt>.
	 *
	 * @param message information about this exception
	 * @param type specific type of engine exception
	 * @param operation the engine operation that caused this exception
	 */
	public EngineException(final String message, final EngineException.Type type, final Operation operation) {
		this(message, null, type, operation);
	}
	
	/**
	 * Constructs a new <tt>EngineException</tt>.
	 *
	 * @param message information about this exception
	 * @param cause the original exception which caused this exception to be created
	 * @param type specific type of engine exception
	 * @param operation the engine operation that caused this exception
	 */
	public EngineException(final String message, final Throwable cause, final EngineException.Type type, final Operation operation) {
		super(message);
		this.type = type;
		this.operation = operation;
	}
	
	/**
	 * Returns the specific type of this exception.
	 * 
	 * @return specific type of this exception
	 */
	public final EngineException.Type getType() {
		return this.type;
	}
	
	/**
	 * Returns the <tt>Operation</tt> involved in this exception.
	 * 
	 * @return the <tt>Operation</tt> involved in this exception
	 */
	public final Operation getOperation() {
		return this.operation;
	}
	
	/**
	 * Returns a description of this <tt>EngineException</tt> type.
	 * 
	 * @return a description of this <tt>EngineException</tt> type
	 */
	public final String getDescription() {
		return this.type.getDescription();
	}
	
	/**
	 * Creates and returns a new <tt>EngineException</tt>.
	 * 
	 * @return new <tt>EngineException</tt>
	 */
	public static EngineException create(final EngineException.Type type, final Operation operation, final String... vars) {
		return create(type, null, operation, vars);
	}
	
	/**
	 * Creates and returns a new <tt>EngineException</tt>.
	 * 
	 * @return new <tt>EngineException</tt>
	 */
	public static EngineException create(final EngineException.Type type, final Throwable cause, final Operation operation, final String... vars) {
		final StringBuilder str = new StringBuilder();
		switch (type) {
		case INVALID_COMPONENT:
			str.append("Unknown or invalid engine component ").append(getVar(0, vars));
			break;
		case INVALID_OPERATION_PARAMETER:
			str.append("Invalid or missing parameters ").append(getVar(0, vars));
			break;
		case INVALID_PARAMETER_TYPE:
			str.append("Invalid parameter ").append(getVar(0, vars))
			   .append(" of type ").append(getVar(1, "???", vars))
			   .append(" when expecting type ").append(getVar(2, "???", vars));
			break;
		case INVALID_NUMBER_OF_PARAMETERS:
			str.append("Incorrect number of parameters as part of ").append(getVar(0, "action or condition", vars));			
			break;
		case VALUE_TYPE_MISMATCH:
			str.append("Error trying to cast ").append(getVar(0, "Parameter Value", vars))
			   .append(" to ").append(getVar(1, "class", vars));
			break;
		case INFINITE_CONDITIONAL_RECURSION:
			str.append("Condition ").append(getVar(0, "", vars)).append(" contained in ")
			   .append(getVar(1, "", vars)).append(" which references itself");
			break;
		case UNKNOWN:
		default:
			str.append("Engine Exception of unknown type has occured...");
			break;
		}
		if (operation != null) {
			str.append(" for operation ").append(operation.toString());
		}
		return cause == null ?	new EngineException(str.toString(), type, operation) :
								new EngineException(str.toString(), cause, type, operation);
	}
	
	/* Return the string at the specified index, if it exists. */
	private static String getVar(final int index, final String...vars) {
		return getVar(index, "", vars);
	}
	
	/* Return the string at the specified index, if it exists, or the specified string */
	private static String getVar(final int index, final String string, final String...vars) {
		return vars.length > index ? "\"" + vars[index] + "\"" : string;
	}
	
}
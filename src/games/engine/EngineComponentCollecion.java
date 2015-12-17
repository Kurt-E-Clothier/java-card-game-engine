/***********************************************************************//**
* @file			EngineComponentCollection.java
* @author		Kurt E. Clothier
* @date			December 7, 2015
*
* @breif		Collection of all Engine Component sets
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/

package games.engine;

import games.engine.util.PlayingCardAlias;

public final class EngineComponentCollecion {
	
	private final EngineComponentSet<Action> actions;
	private final EngineComponentSet<Condition> conditions;
	private final EngineComponentSet<ControlledAction> cActions;
	private final EngineComponentSet<Phase> phases;
	private final EngineComponentSet<PlayingCardAlias> aliases;
	
	/**
	 * Constructs a new <tt>EngineComponentCollecion</tt>.
	 * 
	 * @param actions an <tt>EngineComponentSet</tt> of <tt>Actions</tt>
	 * @param conditionsan <tt>EngineComponentSet</tt> of <tt>Conditions</tt>
	 * @param cActionsan <tt>EngineComponentSet</tt> of <tt>ControlledActions</tt>
	 * @param phasesan <tt>EngineComponentSet</tt> of <tt>Phases</tt>
	 * @param aliasesan <tt>EngineComponentSet</tt> of <tt>PlayingCardAliases</tt>
	 */
	public EngineComponentCollecion(final EngineComponentSet<Condition> conditions,
									final EngineComponentSet<Action> actions,
									final EngineComponentSet<ControlledAction> cActions,
									final EngineComponentSet<Phase> phases,
									final EngineComponentSet<PlayingCardAlias> aliases) {
		this.actions = actions;
		this.conditions = conditions;
		this.cActions = cActions;
		this.phases = phases;
		this.aliases = aliases;
	}
	
	/**
	 * Returns the <tt>EngineComponentSet</tt> of <tt>Actions</tt> from this <tt>EngineComponentCollecion</tt>.
	 * 
	 * @return the <tt>EngineComponentSet</tt> of <tt>Actions</tt> from this <tt>EngineComponentCollecion</tt>
	 */
	public EngineComponentSet<Action> getActions() {
		return actions;
	}
	
	/**
	 * Returns the <tt>EngineComponentSet</tt> of <tt>Conditions</tt> from this <tt>EngineComponentCollecion</tt>.
	 * 
	 * @return the <tt>EngineComponentSet</tt> of <tt>Conditions</tt> from this <tt>EngineComponentCollecion</tt>
	 */
	public EngineComponentSet<Condition> getConditions() {
		return conditions;
	}
	
	/**
	 * Returns the <tt>EngineComponentSet</tt> of <tt>ControlledActions</tt> from this <tt>EngineComponentCollecion</tt>.
	 * 
	 * @return the <tt>EngineComponentSet</tt> of <tt>ControlledActions</tt> from this <tt>EngineComponentCollecion</tt>
	 */
	public EngineComponentSet<ControlledAction> getControlledActions() {
		return cActions;
	}
	
	/**
	 * Returns the <tt>EngineComponentSet</tt> of <tt>Phases</tt> from this <tt>EngineComponentCollecion</tt>.
	 * 
	 * @return the <tt>EngineComponentSet</tt> of <tt>Phases</tt> from this <tt>EngineComponentCollecion</tt>
	 */
	public EngineComponentSet<Phase> getPhases() {
		return phases;
	}
	
	/**
	 * Returns the <tt>EngineComponentSet</tt> of <tt>PlayingCardAliases</tt> from this <tt>EngineComponentCollecion</tt>.
	 * 
	 * @return the <tt>EngineComponentSet</tt> of <tt>PlayingCardAliases</tt> from this <tt>EngineComponentCollecion</tt>
	 */
	public EngineComponentSet<PlayingCardAlias> getAliases() {
		return aliases;
	}
}

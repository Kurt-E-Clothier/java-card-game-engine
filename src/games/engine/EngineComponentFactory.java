/***********************************************************************//**
* @file			EngineComponentFactory.java
* @author		Kurt E. Clothier
* @date			December 7, 2015
*
* @breif		Factory for creating game engine components
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/
package games.engine;

import games.engine.plugin.Plugin;
import games.engine.plugin.PluginException;
import games.engine.plugin.PluginKeyword;
import games.engine.plugin.PluginPattern;
import games.engine.util.PlayingCardAlias;
import games.engine.util.PlayingCardFactory;

/******************************************************************//**
 * The EngineComponentFactory Enum
 * - Access statically as EngineComponentFactory.INSTANCE.method()
 *	 or EngineComponentFactory factory = EngineComponentFactory.getInstance()
 *
 * TODO These methods are extremely UGLY, and will be cleaned up if time allows...
 *********************************************************************/
public enum EngineComponentFactory {
	
	/** An instance of this component factory */
	INSTANCE;
	
	/* Constructs this <tt>EngineComponentFactory</tt> when first used. */
	private EngineComponentFactory() {}
	
	/** Return an instance of this <tt>EngineComponentFactory</tt>. */
	public static EngineComponentFactory getInstance()
	{
		return INSTANCE;
	}
	
	/**
	 * Create and return an <tt>EngineComponentSet</tt> of <tt>PlayingCardAliases</tt>.
	 * 
	 * @param plugin rules plugin to be searched
	 * @return an <tt>EngineComponentSet</tt> of <tt>PlayingCardAliases</tt>
	 * @throws PluginException if the Alias keyword is found with invalid parameters
	 */
	public EngineComponentSet<PlayingCardAlias> createAliases (final Plugin plugin) throws PluginException {
		plugin.checkType(Plugin.Type.RULES);
		final int numAlias = plugin.getNumberOf(PluginKeyword.ALIAS);
		final PlayingCardAlias[] aliases = new PlayingCardAlias[numAlias];
		int ndx = 0;
		final PlayingCardFactory factory = PlayingCardFactory.getInstance();
		for (int i = 0; i < aliases.length; i++) {
			ndx = plugin.getIndexOf(PluginKeyword.ALIAS, ndx + 1);
			String[] parts = plugin.getLine(ndx).split(PluginPattern.WHITESPACE.toString());
			switch (parts.length) {
			case 3:
				aliases[i] = new PlayingCardAlias(parts[1], factory.createPlayingCard(factory.createFace(parts[2]), null));
				break;
			case 4:
				aliases[i] = new PlayingCardAlias(parts[1], factory.createPlayingCard(factory.createFace(parts[2]), factory.createGroup(parts[3])));
				break;
			default:
				throw PluginException.create(PluginException.Type.INVALID_PARAMETER, plugin, PluginKeyword.ALIAS.toString());
			}
		}
		return new EngineComponentSet<PlayingCardAlias>(aliases);
	}
	
	/**
	 * Create and return an <tt>EngineComponentSet</tt> of <tt>Actions</tt>.
	 * 
	 * @param plugin the plugin being searched
	 * @return set of actions
	 * @throws PluginException if the components are ill formatted, or missing keywords
	 */
	public EngineComponentSet<Action> createActions(final Plugin plugin) throws PluginException {
		plugin.checkType(Plugin.Type.RULES);
		final int numActions = plugin.getNumberOf(PluginKeyword.ACTION);
		final Action[] actions = new Action[numActions];
		int actionNdx = 0;
		// Loop through plugin file, look for acitons...
		final int endNdx = plugin.checkIndexOf(PluginKeyword.END_ACTION);
		int ndx = plugin.checkIndexOf(PluginKeyword.ACTION);
		StringBuilder name = new StringBuilder();
		while (ndx < endNdx) {
			
			name.setLength(0);
			name.append(plugin.checkParamsFor(PluginKeyword.ACTION, ndx++));

			// Split the second line into operation and parameters
			String[] line2 = plugin.getLine(ndx++).split(PluginPattern.WHITESPACE.toString(), 2);
			
			// Make sure this line starts with a valid operation
			final Operation operation = Engine.stringToEnumMember(line2[0], Operation.class); 
			if (operation == null) {
				throw PluginException.create(PluginException.Type.INVALID_OPERATION, plugin, line2[0]);
			}
			// Split parameters, if any, and make sure it is the correct amount
			String[] params = line2.length > 1 ? line2[1].split(PluginPattern.WHITESPACE.toString()) : new String[0];
			if (operation.getNumberOfParams() != params.length) {
				throw PluginException.create(PluginException.Type.INVALID_OPERATION_PARAMS, plugin, line2[0]);
			}
			// Check if there is an optional description
			if (ndx == plugin.getIndexOf(PluginKeyword.ACTION, ndx) || ndx == endNdx) {
				actions[actionNdx++] = new Action(name.toString(), operation, params);
			}
			else {
				actions[actionNdx++] = new Action(name.toString(), plugin.getLine(ndx++), operation, params);
			}
		}
		// Check that the keywords are used properly
		if (actionNdx != numActions) {
			throw PluginException.create(PluginException.Type.MISMATCH, plugin, "Invalid Action formatting or similar");
		}
		return new EngineComponentSet<Action>(actions);
	}
	
	/**
	 * Create and return an <tt>EngineComponentSet</tt> of <tt>Conditions</tt>.
	 * 
	 * @param plugin the plugin being searched
	 * @return set of conditions
	 * @throws PluginException if the components are ill formatted, or missing keywords
	 */
	public EngineComponentSet<Condition> createConditions(final Plugin plugin) throws PluginException {
		plugin.checkType(Plugin.Type.RULES);
		final int numConditions = plugin.getNumberOf(PluginKeyword.CONDITION);
		final Condition[] conditions = new Condition[numConditions];
		int conditionNdx = 0;
		// Loop through plugin file, look for conditions...
		final int endNdx = plugin.checkIndexOf(PluginKeyword.END_CONDITION);
		int ndx = plugin.checkIndexOf(PluginKeyword.CONDITION);
		StringBuilder name = new StringBuilder();
		int nextNdx = 0;
		int numStatements = 0;
		while (ndx < endNdx) {
			
			name.setLength(0);
			name.append(plugin.checkParamsFor(PluginKeyword.CONDITION, ndx++));
			nextNdx = plugin.getIndexOf(PluginKeyword.CONDITION, ndx);
			if (nextNdx < 0) {	// If no more Condition keywords are found...
				nextNdx = endNdx;
			}
			numStatements = nextNdx - ndx;
			
			// Read statemebts from the plugin
			ConditionalStatement[] statements = new ConditionalStatement[numStatements];
			for (int i = 0; i < numStatements; i++) {
				statements[i] = new ConditionalStatement(plugin.getLine(ndx++));
			}
			conditions[conditionNdx++] = new Condition(name.toString(), statements);
		}
		// Check that the keywords are used properly
		if (conditionNdx != numConditions) {
			throw PluginException.create(PluginException.Type.MISMATCH, plugin, "Invalid Condition formatting or similar");
		}
		// Validate Conditional Statements
		for (final Condition c : conditions) {
			for (final ConditionalStatement s : c.getStatements()) {
				// Check statement length requirements
				if 	(s.getNumberOfParts() == 0 ||
					((s.isInverted() || s.containsLogic()) && s.getNumberOfParts() < 2) ||
					(s.isInverted() && s.containsLogic() && s.getNumberOfParts() < 3)) {
					throw PluginException.create(PluginException.Type.INVALID_CONDITIONAL_STATEMENT, plugin, c.getName(), s.toString());
				}
				// Check for Operation or embedded Condition
				if (!s.containsOperation()) {
					boolean throwException = true;
					for (int i = 0; i < conditions.length; i++) {
						if (s.getOperationOrCondition().equals(conditions[i].getName())) {
							throwException = false;
							break;
						}
					}
					if (throwException) {
						throw PluginException.create(PluginException.Type.INVALID_CONDITIONAL_STATEMENT, plugin, c.getName(), s.toString());
					}
				}
			}
		}		
		return new EngineComponentSet<Condition>(conditions);
	}
	
	/**
	 * Create and return an <tt>EngineComponentSet</tt> of <tt>ControlledActions</tt>.
	 * 
	 * @param plugin the plugin being searched
	 * @param conditions set of known conditions
	 * @param actions set of known performable actions
	 * @return set of controlled actions (conditional)
	 * @throws PluginException if the components are ill formatted, or missing keywords
	 */
	public EngineComponentSet<ControlledAction> createControlledActions(final Plugin plugin, 
																		final EngineComponentSet<Condition> conditions, 
																		final EngineComponentSet<Action> actions) throws PluginException {
		plugin.checkType(Plugin.Type.RULES);
		final int numActions = plugin.getNumberOf(PluginKeyword.CONTROLLED_ACTION);
		final ControlledAction[] cActions = new ControlledAction[numActions];
		int actionNdx = 0;
		// Loop through plugin file, look for acitons...
		final int endNdx = plugin.checkIndexOf(PluginKeyword.END_CONTROLLED_ACTION);
		int ndx = plugin.checkIndexOf(PluginKeyword.CONTROLLED_ACTION);
		StringBuilder name = new StringBuilder();
		while (ndx < endNdx) {
			
			name.setLength(0);
			name.append(plugin.checkParamsFor(PluginKeyword.CONTROLLED_ACTION, ndx++));

			// Split the second line into parts
			String[] parts = plugin.getLine(ndx++).split(PluginPattern.WHITESPACE.toString());
			
			// Error check the line... this is confusing!
			if (	// Make sure correct number of parts
					parts.length < 2 || parts.length > 4 ||
					// One of the following two groups must be true...
					!(
					// No leading "not" - ensure proper conditions and actions
					(conditions.contains(parts[0]) && actions.contains(parts[1]) &&
					 (parts.length == 2 || actions.contains(parts[2]))) ||
					// Leading "not" - ensure proper conditions and actions
					(parts.length > 2 &&
					 Engine.stringToEnumMember(parts[0], Condition.Logic.values()) == Condition.Logic.NOT &&
					 conditions.contains(parts[1]) && actions.contains(parts[2]) &&
					 (parts.length == 3 || actions.contains(parts[3]))))) {
				throw PluginException.create(PluginException.Type.INVALID_COMPONENT, plugin, plugin.getLine(ndx - 2));
			}
			
			// Should this action be repeated?
			if (plugin.lineStartsWith(PluginKeyword.REPEAT, ndx)) {
				switch (parts.length) {
				case 2:
					// condition tAction
					cActions[actionNdx++] = new ControlledAction(name.toString(), plugin.checkNumericParams(PluginKeyword.REPEAT, ndx),
																 conditions.get(parts[0]), actions.get(parts[1]));
					break;
				case 3:
					if (Engine.stringToEnumMember(parts[0], Condition.Logic.values()) == Condition.Logic.NOT) {
						// not condition tAction
						cActions[actionNdx++] = new ControlledAction(name.toString(), plugin.checkNumericParams(PluginKeyword.REPEAT, ndx),
																	 new Condition(true, conditions.get(parts[1])), actions.get(parts[2]));
					}
					else {
						// condition tAction fAction
						cActions[actionNdx++] = new ControlledAction(name.toString(), plugin.checkNumericParams(PluginKeyword.REPEAT, ndx),
																	 conditions.get(parts[0]), actions.get(parts[1]), actions.get(parts[2]));
					}
					break;
				case 4:
				default:
					// not condition tAction fAction
					cActions[actionNdx++] = new ControlledAction(name.toString(), plugin.checkNumericParams(PluginKeyword.REPEAT, ndx),
																 new Condition(true, conditions.get(parts[1])), 
																 actions.get(parts[2]), actions.get(parts[3]));
					break;
				}
				++ndx;
				
			}
			else {
				switch (parts.length) {
				case 2:
					// condition tAction
					cActions[actionNdx++] = new ControlledAction(name.toString(), conditions.get(parts[0]), actions.get(parts[1]));
					break;
				case 3:
					if (Engine.stringToEnumMember(parts[0], Condition.Logic.values()) == Condition.Logic.NOT) {
						// not condition tAction
						cActions[actionNdx++] = new ControlledAction(name.toString(), new Condition(true, conditions.get(parts[1])), actions.get(parts[2]));
					}
					else {
						// condition tAction fAction
						cActions[actionNdx++] = new ControlledAction(name.toString(), conditions.get(parts[0]), actions.get(parts[1]), actions.get(parts[2]));
					}
					break;
				case 4:
				default:
					// not condition tAction fAction
					cActions[actionNdx++] = new ControlledAction(name.toString(), new Condition(true, conditions.get(parts[1])), 
																	actions.get(parts[2]), actions.get(parts[3]));
					break;
				}
			}
		}
		// Check that the keywords are used properly
		if (actionNdx != numActions) {
			throw PluginException.create(PluginException.Type.MISMATCH, plugin, "Invalid ControlledAction formatting or similar");
		}
		return new EngineComponentSet<ControlledAction>(cActions);
	}
	
	/**
	 * Create and return an <tt>EngineComponentSet</tt> of <tt>Phases</tt>.
	 * 
	 * @param plugin the plugin being searched
	 * @param actions set of known performable actions
	 * @param cons set of known conditions
	 * @param cActions set of controlled actions (conditional)
	 * @return set of game phases
	 * @throws PluginException if the components are ill formatted, or missing keywords
	 */
	public EngineComponentSet<Phase> createPhases(final Plugin plugin, final EngineComponentSet<Action> actions, final EngineComponentSet<Condition> cons,
																	   final EngineComponentSet<ControlledAction> cActions) throws PluginException {
		plugin.checkType(Plugin.Type.RULES);
		final int numPhases = plugin.getNumberOf(PluginKeyword.PHASE);
		if (numPhases == 0) {
			throw PluginException.create(PluginException.Type.MISSING_KEYWORD, plugin, PluginKeyword.PHASE.toString());
		}
		final Phase[] phases = new Phase[numPhases];
		int endNdx = 0;
		int ndx = 0;
		for (int i = 0; i < numPhases; i++) {
			ndx = plugin.getIndexOf(PluginKeyword.PHASE, ndx + 1);
			endNdx = plugin.getIndexOf(PluginKeyword.END_PHASE, ndx);
			phases[i] = EngineComponentFactory.createPhase(plugin.divide(ndx, endNdx), actions, cons, cActions);
		}
		return new EngineComponentSet<Phase>(phases);
	}
	
	/*
	 * Create and return a game phase using the subdivided plugin
	 * 
	 * @param plugin the subdivided plugin being searched
	 * @param parts split parts of the line being checked
	 * @param actions set of known performable actions
	 * @param cons set of known conditions
	 * @param cActions set of known performable controlled actions
	 * @return a game phase created from the plugin
	 * @throws PluginException it the component is improperly formatted
	 */
	private static Phase createPhase(final Plugin plugin, final EngineComponentSet<Action> actions, final EngineComponentSet<Condition> cons,
														  final EngineComponentSet<ControlledAction> cActions) throws PluginException {
		plugin.checkType(Plugin.Type.RULES);
		// Plugin must have a valid phase name and at least one action
		if (plugin.getSize() < 2) {
			throw PluginException.create(PluginException.Type.INVALID_COMPONENT, plugin, plugin.getLine(0));
		}
		int ndx = 1; // line 0 is the phase name
		
		// Get Start actions (if any)
		EngineComponentSet<Performable> startActions = EngineComponentFactory.getDoActions(plugin, ndx, actions, cActions);
		
		// Get Allowed Actions (if any)
		int numAllowedActions = 0;
		for (int line = ndx; line < plugin.getSize(); line++) {
			if (plugin.lineStartsWith(PluginKeyword.END_ALLOW, line)) {
				break;
			}
			else if (!plugin.lineStartsWith(PluginKeyword.DO, line)) {
				++numAllowedActions;
			}
		}
		AllowedAction[] aActions = new AllowedAction[numAllowedActions];
		int aNdx = 0;
		for (ndx = 1; ndx < plugin.getSize(); ndx++) {
			if (plugin.lineStartsWith(PluginKeyword.END_ALLOW, ndx)) {
				break;
			}
			else if (!plugin.lineStartsWith(PluginKeyword.DO, ndx)) {
				String[] parts = plugin.getLine(ndx).split(PluginPattern.WHITESPACE.toString());
				aActions[aNdx++] = createAllowedAction(plugin, ndx + 1, parts, actions, cons, cActions);
			}
		}
		// Check that the keywords are used properly
		if (aNdx != numAllowedActions) {
			throw PluginException.create(PluginException.Type.MISMATCH, plugin, plugin.getLine(0) + ": Invalid \"Allow\" formatting or similar");
		}
		EngineComponentSet<AllowedAction> allowedActions = numAllowedActions == 0 ? null : new EngineComponentSet<AllowedAction>(aActions);
		
		// Get end actions (if any)
		ndx = plugin.getIndexOf(PluginKeyword.END_ALLOW);
		EngineComponentSet<Performable> endActions = ndx < 0 ? null : EngineComponentFactory.getDoActions(plugin, ndx + 1, actions, cActions);

		return new Phase(plugin.checkParamsFor(PluginKeyword.PHASE), startActions, allowedActions, endActions);
	}
	
	/*
	 * Check an engine component part for formatting errors.
	 *  
	 * @param plugin the plugin being searched
	 * @param parts split parts of the line being checked
	 * @param actions set of known performable actions
	 * @param cons set of known conditions
	 * @param cActions set of known performable controlled actions
	 * @return the performable action found on this line
	 * @throws PluginException if the line has too many/not enough parts or the action is unknown
	 */
	private static AllowedAction createAllowedAction(final Plugin plugin, final int startNdx, final String[] parts,
												final EngineComponentSet<Action> actions, final EngineComponentSet<Condition> cons,
												final EngineComponentSet<ControlledAction> cActions) throws PluginException {
		Performable p = null;
		Condition c = null;
		if (PluginKeyword.ALLOW.compareToString(parts[0])) {
			p = EngineComponentFactory.checkActionParts(plugin, parts, actions, cActions);
		}
		else if (cons.contains(parts[0])) {
			c = cons.get(parts[0]);
			p = EngineComponentFactory.checkActionParts(plugin, copyParts(parts, 1), actions, cActions);
			
		}
		else if (cons.contains(parts[1]) && parts[0].equalsIgnoreCase(Condition.Logic.NOT.toString())) {
			c = new Condition(true, cons.get(parts[1]));
			p = EngineComponentFactory.checkActionParts(plugin, copyParts(parts, 2), actions, cActions);
		}
		else {
			throw PluginException.create(PluginException.Type.INVALID_COMPONENT, plugin, plugin.getLine(0));
		}
		return  new AllowedAction(p, c, EngineComponentFactory.getDoActions(plugin, startNdx, actions, cActions));
	}
	
	private static String[] copyParts(final String[] parts, final int startNdx) {
		final int newSize = parts.length - startNdx;
		final String[] copy = new String[newSize];
		System.arraycopy(parts, startNdx, copy, 0, newSize);
		return copy;
	}
	
	/*
	 * Check an engine component part for formatting errors.
	 *  
	 * @param plugin the plugin being searched
	 * @param parts split parts of the line being checked
	 * @param actions set of known performable actions
	 * @param cActions set of known performable controlled actions
	 * @return the performable action found on this line
	 * @throws PluginException if the line has too many/not enough parts or the action is unknown
	 */
	private static Performable checkActionParts(final Plugin plugin, final String[] parts,
												final EngineComponentSet<Action> actions,
												final EngineComponentSet<ControlledAction> cActions) throws PluginException {
		if (parts.length != 2 ) {
			throw PluginException.create(PluginException.Type.INVALID_COMPONENT, plugin, plugin.getLine(0));
		}
		else if (actions.contains(parts[1])) {
			return actions.get(parts[1]);
		}
		else if (cActions.contains(parts[1])) {
			return cActions.get(parts[1]);
		}
		else {
			throw PluginException.create(PluginException.Type.INVALID_COMPONENT, plugin, plugin.getLine(0));
		}
	}
	
	/*
	 * Return a set of actions found after a "do" keyword
	 *  
	 * @param plugin the plugin being searched
	 * @param startNdx the line index to start searching
	 * @param actions set of known performable actions
	 * @param cActions set of known performable controlled actions
	 * @return a set of performable actions to be done
	 * @throws PluginException it the component is improperly formatted
	 */
	private static EngineComponentSet<Performable> getDoActions(final Plugin plugin, final int startNdx,
																final EngineComponentSet<Action> actions,
			   													final EngineComponentSet<ControlledAction> cActions) throws PluginException {
		int ndx = startNdx;
		int numActions = 0;
		for (int line = ndx; line < plugin.getSize(); line++) {
			if (plugin.lineStartsWith(PluginKeyword.DO, line)) {
				++numActions;
			}
			else {
				break;
			}
		}
		Performable[] doActions = new Performable[numActions];
		for (int i = 0; i < numActions; i++) {
			String[] parts = plugin.getLine(ndx++).split(PluginPattern.WHITESPACE.toString());
			doActions[i] = EngineComponentFactory.checkActionParts(plugin, parts, actions, cActions);
		}
		return numActions == 0 ? null : new EngineComponentSet<Performable>(doActions);
	}
}
/***********************************************************************//**
* @file			CardGameEngine.java
* @author		Kurt E. Clothier
* @date			December 16, 2015
* 
* @breif		An engine used to play card games
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/

package games.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import games.engine.Operation.Parameter;
import games.engine.Operation.Parameter.Value;
import games.engine.plugin.PluginException;
import games.engine.plugin.PluginPattern;
import games.engine.util.CardDealer;
import games.engine.util.CardGameBoard;
import games.engine.util.CardPile;
import games.engine.util.CardPileCollection;
import games.engine.util.CardPileParameter;
import games.engine.util.CardPlayer;
import games.engine.util.PlayingCard;
import games.engine.util.PlayingCardRanking;
import games.engine.util.CardPileParameter.Visibility;
import games.engine.util.CardPileParameter.Visible;

/******************************************************************//**
 * The CardGameEngine Class
 * 	- Controls a card game by checking conditions and performing actions.
 * 	- Should really replace all of the "System.out.println()" calls with a Logger
 ********************************************************************/
public final class CardGameEngine {
	
/*------------------------------------------------
 	Constants and Attributes
 ------------------------------------------------*/
	private final List<String> conditionList;
	private final String name;
	private final EngineComponentCollecion components;
	private final PlayingCardRanking ranking;
	private final CardGameBoard board;
	private final CardDealer dealer;
	private final CardPlayer[] players;
	private final CardPileCollection commonPiles;
	private CardPlayer currentPlayer;
	private int turnNumber;
	
/*------------------------------------------------
 	Constructor(s)
 ------------------------------------------------*/
	
	public CardGameEngine(final String name,
						  final EngineComponentCollecion components,
						  final PlayingCardRanking ranking,
						  final CardGameBoard board,
						  final CardDealer dealer,
						  final CardPlayer...players) {
		this.name = name;
		this.components = components;
		this.ranking = ranking;
		this.board = board;
		this.dealer = dealer;
		this.players = Arrays.copyOf(players, players.length);
		this.commonPiles = board.getCommonPiles();
		this.conditionList = new ArrayList<String>();
		this.reset();
	}
	
/*------------------------------------------------
    Accessors
 ------------------------------------------------*/
	/**
	 * Returns the name of this card game.
	 * 
	 * @return the name of this card game
	 */
	public String getName() {
		return name;
	}
	/** 
	 * Returns an array of the card players in this card game.
	 * 
	 * @return an array of the card players in this card game
	 */
	public CardPlayer[] getPlayers() {
		return Arrays.copyOf(players, players.length);
	}
	
	/**
	 * Returns the card player whose turn is currently is.
	 * 
	 * @return the card player whose turn is currently is
	 */
	public CardPlayer getCurrentPlayer() {
		return currentPlayer;
	}
	
	/**
	 * Sets the card players whose turn is currently is.
	 * 
	 * @param player the card player whose turn it should be
	 */
	void setCurrentPlayer(final CardPlayer player) {
		currentPlayer = player;
		if (player.equals(players[0])) {
			++turnNumber;
		}
	}
	
	/**
	 * Returns the card ranking used in this engine.
	 * 
	 * @return the card ranking used in this engine
	 */
	PlayingCardRanking getRanking() {
		return ranking;
	}
	
	/**
	 * Returns the card players whose turn it will be next.
	 * 
	 * @return the card players whose turn it will be next
	 */
	public CardPlayer getNextPlayer() {
		CardPlayer nextPlayer = null;
		for (int i = 0; i < players.length; i++) {
			if (currentPlayer.equals(players[i])) {
				if (i >= players.length - 1) {
					nextPlayer = players[0];
				}
				else {
					nextPlayer = players[i + 1];
				}
				break;
			}
		}
		return nextPlayer;
	}
	
	/**
	 * Returns the current turn number.
	 * 
	 * @return the current turn number
	 */
	public int getTurnNumber() {
		return turnNumber;
	}
	
	/**
	 * Returns the <tt>CardGameBoard</tt> in use.
	 * 
	 * @return the <tt>CardGameBoard</tt> in use
	 */
	public CardGameBoard getGameBoard() {
		return board;
	}
	
	/**
	 * Returns the set of <tt>AllowedActions</tt> for the current player.
	 * 
	 * @return the set of <tt>AllowedActions</tt> for the current player
	 * @throws PluginException 
	 * @throws EngineException 
	 * @throws NoSuchFieldException 
	 */
	public  EngineComponentSet<AllowedAction> getAllowedActions() throws NoSuchFieldException, EngineException, PluginException {
		return this.getAllowedActions(currentPlayer);
	}
	
	/**
	 * Returns the set of <tt>AllowedActions</tt> for the specified player.
	 * Evaluates any preconditions on the allowed actions.
	 * 
	 * @param player the card player to get the allowed actions for
	 * @return the set of <tt>AllowedActions</tt> for the specified player
	 * @throws PluginException 
	 * @throws EngineException 
	 * @throws NoSuchFieldException 
	 */
	public EngineComponentSet<AllowedAction> getAllowedActions(final CardPlayer player) throws NoSuchFieldException, EngineException, PluginException {
		final EngineComponentSet<AllowedAction> actions = player.getPhase().getAllowedActions();
		List<AllowedAction> actionList = new ArrayList<AllowedAction>(actions.getSize());
		for (AllowedAction a : actions.toArray()) {
			if (a.hasPrecondition()) {
				//System.out.println(" -- " + a.getPrecondition().getName()); 	// XXX
				if (evaluate(a.getPrecondition(), null, null)) {
					actionList.add(a);
				}
			}
			else {
				actionList.add(a);
			}
		}
		conditionList.clear();
		return new EngineComponentSet<AllowedAction>(actionList.toArray(new AllowedAction[actionList.size()]));
	}
	
/*------------------------------------------------
	Utility Methods
 ------------------------------------------------*/
	/**
	 * Resets this <tt>CardGameEngine</tt> to the default state.
	 */
	public void reset() {
		for (final CardPlayer p : players) {
			p.reset();
			for (final CardPile pile : p.getPlayerPiles().toArray()) {
				pile.removeAll();
			}
		}
		for (final CardPile pile : commonPiles.toArray()) {
			pile.removeAll();
		}
		dealer.reset();
		currentPlayer = players[0];
		turnNumber = 0;
		conditionList.clear();
	}
	
	/**
	 * Start the engine...
	 * Deal cards and start phase actions for the first player.
	 * @throws PluginException 
	 * @throws EngineException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchFieldException 
	 */
	public void start() throws EngineException, PluginException {
		dealAllCards();
		try {
			startPhase();
		} catch (NoSuchFieldException | IllegalArgumentException e) {
			throw EngineException.create(EngineException.Type.UNKNOWN, e, null);
		}
	}
	
	/**
	 * Performs the start of phase actions for the current player.
	 * @throws PluginException 
	 * @throws EngineException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchFieldException 
	 */
	public void startPhase() throws NoSuchFieldException, IllegalArgumentException, EngineException, PluginException {
		boolean shouldRepeat = false;
		do {
			shouldRepeat = false;
			/*
			System.out.println("-------------------------------------------------------------------------------------------------");
			System.out.println(">>>>> Performing START action set for " + currentPlayer.getName() + ", phase " + currentPlayer.getPhase().getName());	// XXX
			System.out.println("-------------------------------------------------------------------------------------------------");
			*/
			CardPlayer player = currentPlayer;
			Phase phase = player.getPhase();
			performP(currentPlayer.getPhase().getStartActions());
			// Still the same player, but started a new phase...
			if (player.equals(currentPlayer) && !phase.equals(player.getPhase())) {
				shouldRepeat = true;
			}
		} while(shouldRepeat);
	}
	
	/**
	 * Deal all <tt>PlayingCards</tt> in <tt>Deck</tt> to the appropriate <tt>CardPiles</tt>.
	 */
	public void dealAllCards() {
		dealer.dealAll();
		if (turnNumber == 0) {
			turnNumber = 1;
		}
	}
	
	/**
	 * Deal next <tt>PlayingCard</tt> in <tt>Deck</tt> to the appropriate <tt>CardPiles</tt>.
	 * Returns a string containing information about the cards being dealt.
	 * Example: Dealt Playing Card: "face" - "group" to "card pile"
	 * 
	 * @return a string containing information about the cards being dealt
	 */
	public String dealNextCard() {
		final String str = dealer.dealNext();
		if (dealer.isDone() && turnNumber == 0) {
			turnNumber = 1;
		}
		return str;
	}
	
	/**
	 * Returns <tt>true</tt> when all playing cards have been dealt.
	 * 
	 * @return <tt>true</tt> when all playing cards have been dealt
	 */
	public boolean isDoneDealingCards() {
		return dealer.isDone();
	}
	
	/**
	 * Returns the number of cards visible for specified <tt>CardPile</tt>.
	 * This value will change as cards are moved around and the current 
	 * turn player changes.
	 * 
	 * If this pile is owned by a <tt>CardPlayer</tt>, that player must 
	 * be passed for accurate results; otherwise, pass <tt>null</tt>.
	 * 
	 * @param cardpile the card pile to be checked
	 * @param player the card player that owns this pile (if applicable)
	 * @return number of visible cards in this pile, at this time
	 */
	public int getNumberOfVisibleCards(final CardPile cardpile, final CardPlayer player) {
		if (cardpile == null) {
			return 0;
		}
		int num = 0;
		CardPileParameter params = cardpile.getParameters();
		CardPileParameter.Visibility visibility = params.getVisibility();
		if (params.getVisible() != Visible.NONE &&
			(visibility == Visibility.ALL ||
			(visibility == Visibility.OWNER && params.getOwner() == CardPileParameter.Owner.COMMON) ||
		    (visibility == Visibility.OWNER && currentPlayer.equals(player)) ||
		    (visibility == Visibility.OTHER && !currentPlayer.equals(player)))) {
			switch(params.getVisible()) {
			case NUMBER:
				num = params.getNumVisible();
				break;
			case ALL:
				num = cardpile.getSize();
				break;
			case TOP:
			default:
				num = 1;
				break;
			}
		}
		return num;
	}
	
	/**
	 * Returns a List of <tt>Operation.Parameter.OptionLists</tt> containing possible parameters for the specified action.
	 * 
	 * @param action the action which can be performed using chosen parameters from this list
	 * @return list of parameter options for use is performing an action
	 * @throws EngineException if the action was defined with an incompatible number of parameters
	 * @throws PluginException if a component (card pile, playing card, etc) specified in a plugin is unknown
	 */
	public List<Operation.Parameter.OptionList> getOptions(final AllowedAction action) throws EngineException, PluginException {
		
		final Operation operation = action.getOperation();
		final String[] params = action.getParams();
		final List<Operation.Parameter.OptionList> options = new ArrayList<Operation.Parameter.OptionList>();
		
		if (action.getParams().length != operation.getNumberOfParams()) {
			throw EngineException.create(EngineException.Type.INVALID_NUMBER_OF_PARAMETERS, operation, action.getName());
		}
		// Would be best to do this generically for any given operation,
		// It's possible, but just don't have time to work on doing that...
		// This can also be expanded (and would need to be) as new functionality is added to the engine
		
		// All cases should use a condition, if fails, go to default
		switch(operation) {
		
		// Currently, user cannot select a pile to add a card to...
		// That can be added, by modifying the CardPile Parameters, add an "allow adding cards" kind of keyword
		
		case PUT:
		case PUT_MULTIPLE:
			if (Engine.convertEnumMemberToPluginString(Operation.Parameter.PLAYING_CARD).equals(params[2])) {
				options.add(createPlayingCardOptions(false, getCardPile(params[0])));
			}
			else if (Engine.convertEnumMemberToPluginString(Operation.Parameter.PLAYING_CARDS).equals(params[2])) {
				options.add(createPlayingCardOptions(true, getCardPile(params[0])));
			}
			break;
		case EXCHANGE:
			if (Engine.convertEnumMemberToPluginString(Operation.Parameter.PLAYING_CARD).equals(params[2]) &&
				Engine.convertEnumMemberToPluginString(Operation.Parameter.PLAYING_CARD).equals(params[3])) {
				options.add(createPlayingCardOptions(false, getCardPile(params[0])));
				options.add(createPlayingCardOptions(false, getCardPile(params[1])));
			}
			break;
		default:
			break;
		}
		return options;
	}
	
	/* Returns an OptionList of PlayingCards from the specified cardpile. */
	private Operation.Parameter.OptionList createPlayingCardOptions(final boolean isMultiple, final CardPile pile) {
		final CardPileParameter p = pile.getParameters();
		final StringBuilder str = new StringBuilder();
		str.append(" from the ").append(p.getOwner()).append(" card pile - ").append(p.getName());
		final boolean isVisible = getNumberOfVisibleCards(pile, currentPlayer) > 0;
		return new Operation.Parameter.OptionList(isVisible, isMultiple, str.toString(), Operation.Parameter.Value.asValues(pile.get()));
	}
	
	/**
	 * Attempt to perform the specified <tt>AllowedAction</tt> with the given <tt>Operation.Parameter.Values</tt>.
	 * Perform any nested conditional checks and actions in the process.
	 * Response actions are also performed, regardless of conditional checks for this allowed action.
	 * If the action is performed, any end of phase actions are also performed. The start of phase actions
	 * are also performed, for whomever the current player currently is.
	 * Returns <tt>true</tt> if the action was performed.
	 * 
	 * @param action the action to attempt to perform
	 * @param params the values to use as parameters
	 * @return <tt>true</tt> if the action was performed
	 * @throws PluginException if a data representation error is found with the defined actions
	 * @throws EngineException if there is an operation related error
	 * @throws NoSuchFieldException if a Parameter type is not considered in this code
	 */
	public boolean perform(final AllowedAction action, final List<Operation.Parameter.Value> params)
														throws NoSuchFieldException, EngineException, PluginException {
		//System.out.println(">> Attempting action " + action.getName());	// XXX
		//System.out.println(">> # Params: " + params.size());	// XXX
		
		final CardPlayer player = currentPlayer;
		final Phase phase = currentPlayer.getPhase();
		
		final Operation.Parameter.Value[] values = getValues(action, listToArray(action.getOperation(), params));
		
		final boolean bool = performP(action.getPerformable(), values).getBool();
		if (action.hasResponseActions()) {
			//System.out.println(">>>>> Performing RESPONSE action set");	// XXX
			performP(action.getResponseActions());
		}
		// Only worry about this if the action was performed
		if (bool) {
			// Still the same player (turn hasn't ended yet), and still in the same phase
			if (player.equals(currentPlayer) && phase.equals(player.getPhase())) {
				//System.out.println(">>>>> Should be Performing END action set");	// XXX
				performP(phase.getEndActions());
			}
			// Still the same player, but went to a new phase (this can happen after the previous end-actions...
			// Or original player's turn is over, and need to start a new phase for the next player
			if (player.equals(currentPlayer) && !phase.equals(player.getPhase()) ||
				!player.equals(currentPlayer)) {
				//System.out.println(">>>>> should be Performing START action set");	// XXX
				startPhase();
			}
		}
		return bool;
	}
	
/*------------------------------------------------
	Performing ... Performables
 ------------------------------------------------*/
	/* Convert the param list to a Value array. Condenses same type of value into an array. */
	private Operation.Parameter.Value[] listToArray(final Operation operation, final List<Operation.Parameter.Value> params) throws EngineException {
		boolean replacePiles = false;
		boolean replaceCards = false;
		final Value[] values = params.toArray(new Operation.Parameter.Value[params.size()]);
		final List<Operation.Parameter.Value> newParams = new ArrayList<Operation.Parameter.Value>(params.size());
		PlayingCard[] cards = null;
		CardPile[] piles = null;
		for (Operation.Parameter p : operation.getParams()) {
			if (p == Operation.Parameter.CARDPILES) {
				replacePiles = true;
				piles = Operation.Parameter.Value.asPiles(values);
				break;
			}
		}
		for (Operation.Parameter p : operation.getParams()) {
			if (p == Operation.Parameter.PLAYING_CARDS) {
				replaceCards = true;
				cards = Operation.Parameter.Value.asCards(values);
			}
		}
		boolean haveNotReplacedPiles = true;
		boolean haveNotReplacedCards = true;
		for (final Operation.Parameter.Value v : params) {
			if ((v.getType() == Operation.Parameter.CARDPILE ||
				v.getType() == Operation.Parameter.CARDPILES) && replacePiles) {
				if (haveNotReplacedPiles) {
					newParams.add(new Operation.Parameter.Value(piles));
					haveNotReplacedPiles = false;
				}
			}
			else if ((v.getType() == Operation.Parameter.PLAYING_CARD ||
					v.getType() == Operation.Parameter.PLAYING_CARDS) && replaceCards) {
				if (haveNotReplacedCards) {
					newParams.add(new Operation.Parameter.Value(cards));
					haveNotReplacedCards = false;
				}
			}
			else {
				newParams.add(v);
			}
		}
		return newParams.toArray(new Operation.Parameter.Value[newParams.size()]);
	}
	
	/* Perform this Performable action using the given Parameter Values */
	private Operation.Return.Value performP(final Performable action, final Operation.Parameter.Value...params) 
											throws NoSuchFieldException, IllegalArgumentException, EngineException, PluginException {
		//System.out.println("\n> Trying to perform action " + action.getName());	// XXX
		Operation.Return.Value ret = null;
		int timesToRepeat = 1;
		if (action instanceof Repeatable ) {
			final Repeatable repeatable =  (Repeatable) action;
			if (repeatable.shouldBeRepeated()) {
				timesToRepeat = repeatable.getNumberOfRepetitions();
			}
		}
		do {
			//System.out.println("\n## Checking Conditionals...");	// XXX
			if (action instanceof Conditional ) {
				final Conditional conditional =  (Conditional) action;
				if (evaluate(((Conditional)action).getCondition(), params)) {
					//System.out.println(" ## Condition passed, performing true action");	// XXX
					ret = performP(conditional.getTrueAction().getOperation(), getValues(action, params));
				}
				else if (conditional.hasFalseAction() ){
					//System.out.println(" ## Condition not passed, performing false action");	// XXX
					ret = performP(conditional.getFalseAction().getOperation(), getValues(action, params));
				}
				else {	
				//	System.out.println(" ## Condition not passed!");	// XXX
					ret = Operation.Return.Value.FALSE;
				}
				conditionList.clear();
			}
			else {
				//System.out.println(">>> Performing action " + action.getName());	// XXX
				ret = performP(action.getOperation(), getValues(action, params));
			}	
		} while (--timesToRepeat != 0 && ret != Operation.Return.Value.FALSE);
		return ret;
	}
	
	/* Perform this set of actions (in a phase) */
	private void performP(EngineComponentSet<Performable> actions) throws NoSuchFieldException, IllegalArgumentException, EngineException, PluginException {
		//System.out.println(">>>>> Performing start/end/response action set");	// XXX
		if (actions != null && !actions.isEmpty()) {
			final CardPlayer player = currentPlayer;
			final Phase phase = player.getPhase();
			for (final Performable action : actions.toArray()) {
				performP(action);
				// If player turn has ended, or the phase is over, stop performing these actions.
				if (!player.equals(currentPlayer) || !phase.equals(player.getPhase())) {
					break;
				}
			}
		}
	}
	
	/* Perform this Operation using the given Parameter Values */
	private Operation.Return.Value performP(final Operation operation, final Operation.Parameter.Value...params) throws EngineException {
		//System.out.println("! Performing operation " + operation.toString());	// XXX
		return Operations.perform(operation, this /*engine*/, params);
	}
	
/*------------------------------------------------
	Breaking Down Conditions
 ------------------------------------------------*/
	/* Evaluate this condition, and all embedded statements (Value params) */
	private boolean evaluate(final Condition condition, final Operation.Parameter.Value...params)
									throws NoSuchFieldException, EngineException, PluginException {
		return evaluate(condition, params, null);
	}
	
	/* Evaluate this condition, and all embedded statements (String params) */
	private boolean evaluate(final Condition condition, final String...params) throws NoSuchFieldException, EngineException, PluginException {
		return evaluate(condition, null, params);
	}
	
	/* Evaluate this condition, and all embedded statements (Backbone) */
	private boolean evaluate(final Condition condition, Operation.Parameter.Value[] valueParams, final String[] stringParams) 
																throws NoSuchFieldException, EngineException, PluginException {
		//System.out.println("Evaluating " + condition.getName());	// XXX
		conditionList.add(condition.getName());
		boolean bool = true;
		for (final ConditionalStatement s : condition.getStatements()) {
			bool = 	stringParams == null || stringParams.length == 0 ?
					evaluate (s, valueParams) :
					evaluate (s, stringParams);
			if (isBreakCondition(s, bool)) {
				break;
			}
		}
	//	System.out.println("Inverting? " + condition.isInverted());
		return condition.isInverted() ? !bool : bool;
	}
	
	/* Evaluate this statement, and all embedded conditions (String params) */
	private boolean evaluate(final ConditionalStatement statement, final String...params) 
												throws NoSuchFieldException, EngineException, PluginException {
		return evaluate(statement, null, params);
	}
	
	/* Evaluate this statement, and all embedded conditions (Value params) */
	private boolean evaluate(final ConditionalStatement statement, final Operation.Parameter.Value...params) 
												throws NoSuchFieldException, EngineException, PluginException {
		return evaluate(statement, params, null);
	}
	
	/* Evaluate this statement, and all embedded conditions (backbone)
	 * @param statement a conditional statement to evaluate
	 * @param condition the parent condition of this statement, used to prevent endless recursive loops
	 * @param valueParams Parameter.Values to be used in evaluating this statement
	 * @param stringParams Strings (defined parameters) to be used in evaluating this statement */
	private boolean evaluate(final ConditionalStatement statement, final Operation.Parameter.Value[] valueParams, 
								final String[] stringParams) throws NoSuchFieldException, EngineException, PluginException {
		boolean bool = true;
		//System.out.println(" --> Evaluating " + statement.toString());	// XXX
		final String conditionString = statement.getOperationOrCondition();
		if (conditionList.contains(conditionString)) {
			throw EngineException.create(EngineException.Type.INFINITE_CONDITIONAL_RECURSION, null, conditionString, conditionList.get(conditionList.size()-1));
		}
		// Is this an operation ? try to perform it with the given params...
		if (statement.containsOperation()) {
			final Operation operation = Engine.stringToEnumMember(conditionString, Operation.class);
			final Operation.Parameter.Value[] values =	getValues(operation, statement.getParams(), conditionList.get(conditionList.size() - 1), valueParams, stringParams);
			bool = performP(Engine.stringToEnumMember(statement.getOperationOrCondition(), Operation.class), values).getBool();
		}
		// This should be an Embedded condition... pass along any newly defined params and carry on
		else if (components.getConditions().contains(conditionString)){
			
			// If a conditional statement contains param definitions, these are passed on INSTEAD of the passed Value[] params.
			if (statement.containsParams()) {
				bool = evaluate(components.getConditions().get(conditionString), statement.getParams());
			}
			else {
				bool = evaluate(components.getConditions().get(conditionString), valueParams, stringParams);
			}
			conditionList.remove(conditionList.size() - 1);
		}
		else {
			throw EngineException.create(EngineException.Type.INVALID_COMPONENT, null, conditionString);
		}
		return statement.isInverted() ? !bool : bool;
	}
	
	/* Returns true if the evaluation of this statement means other statements are unnecessary */
	private boolean isBreakCondition(final ConditionalStatement s, final boolean evaluation) {
		if (s.containsLogic()) {
			if (!evaluation && s.getLogic() == Condition.Logic.AND ||
				 evaluation && s.getLogic() == Condition.Logic.OR) {
			//	System.out.println("Broke at " + s.toString() + " with " + evaluation);	// XXX
				return true;
			}
		}
		return false;
	}
	
/*------------------------------------------------
	Breaking Down Performables
 ------------------------------------------------*/
	/* Get an array of Parameter Values to use when performing a Performable action (Value params) */
	private Operation.Parameter.Value[] getValues(final Performable action, final Operation.Parameter.Value...params) 
													throws EngineException, NoSuchFieldException, PluginException {
		return getValues(action.getOperation(), action.getParams(), action.getName(), params);
	}
	
	/* Get an array of Parameter Values to use when performing a Performable (Value params) */
	private Operation.Parameter.Value[] getValues(final Operation operation, final String[] definedParams, final String actionName, 
													final Operation.Parameter.Value...params) throws EngineException, NoSuchFieldException, PluginException {
		return getValues(operation, definedParams, actionName, params, null);
	}
	
	/* Get an array of Parameter Values to use when performing a Performable (Backbone) */
	private Operation.Parameter.Value[] getValues(final Operation operation, final String[] definedParams, final String actionName, 
													final Operation.Parameter.Value[] valueParams,										
													final String[] stringParams) throws EngineException, NoSuchFieldException, PluginException {
		if (operation.getNumberOfParams() > definedParams.length) {
			throw EngineException.create(EngineException.Type.INVALID_NUMBER_OF_PARAMETERS, operation, actionName);
		} 
		final int numberOfValues = getNumberOfParameterValues(operation, definedParams.length);
		final Operation.Parameter.Value[] values = new Operation.Parameter.Value[numberOfValues];
		final Operation.Parameter[] paramTypes = operation.getParams();
		int paramNdx = 0;
		for (int i = 0; i < definedParams.length; i++) {
			if (Engine.stringIsMemberOf(definedParams[i], Operation.Parameter.class)) {
				if (stringParams == null || stringParams.length == 0) {
					paramNdx = getParamIndex(paramNdx, operation, paramTypes[i], valueParams);
					values[i] = createValueFromParamValue(operation, paramTypes[i], definedParams[i], valueParams[paramNdx++]);
				}
				else {
					values[i] = createValueFromParamValue(operation, paramTypes[i], definedParams[i], 
															createValueFromObjectString(paramTypes[i], stringParams[paramNdx++]));	
				}
			}
			else if (paramTypes != null &&
					i >= paramTypes.length &&
					paramTypes[paramTypes.length - 1].isAnArray()) {
				values[i] = createValueFromObjectString(paramTypes[paramTypes.length - 1], definedParams[i]);
			}
			else {
				values[i] = createValueFromObjectString(paramTypes[i], definedParams[i]);
			}
		}
		return values;
	}
	
	/* Returns the number of necessary parameter values */
	private int getNumberOfParameterValues(final Operation operation, final int paramArraySize) {
		return operation.expectsArrayParameter() ? paramArraySize : operation.getNumberOfParams();
	}
	
	/* Return the params[] index of the next Value with the desired Parameter type. */
	private int getParamIndex(final int startingNdx, final Operation operation, final Parameter type,
								final Operation.Parameter.Value...params) throws EngineException {
		int j = 0;
		for (j = startingNdx; j < params.length; j++) {
			if (type == params[j].getType() || type.getArrayType() == params[j].getType()) {
				break;
			}
		}
		if (j >= params.length) {
			throw EngineException.create(EngineException.Type.INVALID_OPERATION_PARAMETER, operation, type.toString());
		}
		return j;
	}
	
	/* Return the specified Parameter Value as long as it is the correct and expected type. */
	private Operation.Parameter.Value createValueFromParamValue(final Operation operation, final Operation.Parameter type, final String definedParam, 
																final Operation.Parameter.Value param) throws EngineException {
		Operation.Parameter paramType = Engine.stringToEnumMember(definedParam, Operation.Parameter.class);
		if (type != param.getType() && type.getArrayType() != param.getType()) {
			throw EngineException.create(EngineException.Type.INVALID_PARAMETER_TYPE, operation, 
						param.get().toString(), param.getType().toString(), type.toString());
		}
		else if (type != paramType && type.getArrayType() != paramType) {
			throw EngineException.create(EngineException.Type.INVALID_PARAMETER_TYPE, operation, 
					"UNDEFINED", paramType.toString(), type.toString());
		}
		return param;
	}
	
	/* Return a Parameter Value created from a defined object string */
	private Operation.Parameter.Value createValueFromObjectString(final Operation.Parameter type, final String string) 
																	throws NoSuchFieldException, PluginException {
		// These could be refactored to be individual objects with polymorphism, but no time for that now!
		switch (type) {
		case CARD_PLAYER:
			return new Operation.Parameter.Value(getCardPlayer(string));
		case PLAYING_CARDS:
		case PLAYING_CARD:
			return new Operation.Parameter.Value(getPlayingCard(string));
		case CARDPILES:
		case CARDPILE:
			return new Operation.Parameter.Value(getCardPile(string));
		case PHASE:
			return new Operation.Parameter.Value(getPhase(string));
		case STRING:
			return new Operation.Parameter.Value(string);
		case INTEGER:
			return new Operation.Parameter.Value(getInt(string));
		default:
			throw new NoSuchFieldException("Switch statment does not cover Operation.Parameter field: " + type.toString());
		}
	}
	
	/* Get a known PlayingCard from the specified string */
	private PlayingCard getPlayingCard(final String string) throws PluginException {
	//	System.out.println(" <> Getting Card " + string);		// XXX
		if (components.getAliases().contains(string)) {
			return components.getAliases().get(string).getPlayingCard();
		}
		final String[] parts = string.split(PluginPattern.PERIOD.toString());
		if (parts.length != 3) {
			throw new PluginException("Invalid PlayingCard: " + string, PluginException.Type.DATA_REPRESENTATION);
		}
		final CardPile pile = getCardPile(parts[0], parts[1]);
		PlayingCard card = null;
		switch (parts[2]) {
		case "top":
			card = pile.getTop();
			break;
		case "bottom":
			card = pile.getBottom();
			break;
		case "random":
			card = pile.getRandom();
			break;
		default:
			if (parts[2].startsWith("top-")) {
				final int n = getTopBottomNumber(parts[2], 4, pile.getSize());
				card = pile.get(n + 1)[n];
			}
			else if (parts[2].startsWith("bottom+")) {
				card = pile.get()[pile.getSize() - 1 - getTopBottomNumber(parts[2], 7, pile.getSize())];
			}
			break;
		}
		return card;
	}
	
	/* Get the number of cards up/down from the pile bottom/top. */
	private int getTopBottomNumber(final String string, final int substringNdx, final int pileSize) throws PluginException {
		int n = 0;
		try {
			n = Integer.valueOf(string.substring(substringNdx));
		} catch (NumberFormatException e) {
			throw new PluginException("Invalid PlayingCard: " + string, e, PluginException.Type.DATA_REPRESENTATION);
		}
		if (n >= pileSize) {
			throw new PluginException("Invalid PlayingCard: " + string, PluginException.Type.DATA_REPRESENTATION);
		}
		return n;
	}
	
	
	/* Get a known card pile from the specified owner and name */
	private CardPile getCardPile(final String owner, final String name) throws PluginException {
		CardPileCollection piles = null;
		switch (owner) {
		case "common" :
			piles = commonPiles;
			break;
		default:
			piles = getCardPlayer(owner).getPlayerPiles();
			break;
		}
		if (!piles.contains(name)) {
			throw new PluginException("Unknown cardpile " + owner + '.' + name, PluginException.Type.DATA_REPRESENTATION);
		}
		return piles.get(name);
	}
	
	/* Get a known card pile from the specified string */
	private CardPile getCardPile(final String string) throws PluginException {
		final String[] parts = string.split(PluginPattern.PERIOD.toString());
		if (parts.length != 2) {
			throw new PluginException("Invalid cardpile " + string, PluginException.Type.DATA_REPRESENTATION);
		}
		return getCardPile(parts[0], parts[1]);
	}
	
	/* Get a known Card Player from the specified string */
	private CardPlayer getCardPlayer(final String string) throws PluginException {
		switch (string) {
		case "player":
		case "current-player":
			return currentPlayer;
		case "next-player":
			return getNextPlayer();
		default:
			throw new PluginException("Invalid card player " + string, PluginException.Type.DATA_REPRESENTATION);
		}
	}
	
	/* Get a known game phase from the specified string */
	private Phase getPhase(final String string) throws PluginException {
		if (this.components.getPhases().contains(string)) {
			return components.getPhases().get(string);
		}
		else {
			throw new PluginException("Unknown phase " + string, PluginException.Type.DATA_REPRESENTATION);
		}
	}
	
	/* Get an integer from the specified string */
	private Integer getInt(final String string) throws PluginException {
		try {
			return Integer.valueOf(string);
		} catch (NumberFormatException e) {
			throw new PluginException("Invalid Integer: " + string, e, PluginException.Type.DATA_REPRESENTATION);
		}
	}
}

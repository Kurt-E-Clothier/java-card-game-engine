/***********************************************************************//**
 * @file			Operation.java
 * @author		Kurt E. Clothier
 * @date			December 4, 2015
 * 
 * @breif		Valid engine operations for a card game
 * 
 * @pre			Compiler: Eclipse - Mars Release (4.5.0)
 * @pre			Java: JRE 7 or greater
 * 
 * @see			http://www.projectsbykec.com/
 * 
 * @copyright	The MIT License (MIT) - see LICENSE.txt
 ***************************************************************************/
package games.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import games.engine.util.CardPile;
import games.engine.util.CardPlayer;
import games.engine.util.PlayingCard;

/***********************************************************************//**
 * This file is big and ugly, and there are some smelly operations going 
 * on here. I would love to clean it up and make things work a bit better,
 * but for now, I am just shooting for something that works at all...
 ***************************************************************************/
public enum Operation {
	
	
	//TODO
	// These should realistically be split into support operations and actions a player can do
	
	
/*------------------------------------------------
 	Basic Engine Operations
 ------------------------------------------------*/
	/** Go to a different phase */
	GOTO(Return.BOOLEAN, Parameter.PHASE),
	
	/** Player wins */
	PLAYER_WIN(Return.BOOLEAN, Parameter.CARD_PLAYER),
	
	/** Player has lost */
	PLAYER_LOSE(Return.BOOLEAN, Parameter.CARD_PLAYER),
	
	/** End Current Player's turn ... */
	END_TURN(Return.BOOLEAN),
	
/*------------------------------------------------
 	Playing Card Operations
 ------------------------------------------------*/
	/** Match attributes of Playing Cards */
	MATCH(Return.BOOLEAN, Parameter.PLAYING_CARDS),
	
	/** Match rank of Playing Cards */
	MATCH_RANK(Return.BOOLEAN, Parameter.PLAYING_CARDS),
	
	/** Compare rank of Playing Card A to Playing Card B */
	COMPARE_RANK(Return.INTEGER, Parameter.PLAYING_CARD, Parameter.PLAYING_CARD),
	
	/** Check if card A rank is higher than card B rank */
	CHECK_RANK(Return.BOOLEAN, Parameter.PLAYING_CARD, Parameter.PLAYING_CARD),
	
/*------------------------------------------------
 	Card Pile Operations
 ------------------------------------------------*/
	/** Sort Playing Cards in Card Pile by rank */
	SORT_BY_RANK(Return.BOOLEAN, Parameter.CARDPILE),
	
	/** Sort Playing Cards in Card Pile by face */
	SORT_BY_FACE(Return.BOOLEAN, Parameter.CARDPILE),
	
	/** Compare size of Card Pile with a given value */
	COMPARE_SIZE_TO(Return.INTEGER, Parameter.CARDPILE, Parameter.INTEGER),
	
	/** Compare size of Card Pile A to Card Pile B */
	COMPARE_SIZES(Return.INTEGER, Parameter.CARDPILE, Parameter.CARDPILE),
	
	/** Check if card pile size is greater than value */
	CHECK_SIZE(Return.BOOLEAN, Parameter.CARDPILE, Parameter.INTEGER),
	
	/** Check if card pile is empty */
	CHECK_IF_EMPTY(Return.BOOLEAN, Parameter.CARDPILE),
	
	/** Get size of a card pile */
	GET_SIZE(Return.INTEGER, Parameter.CARDPILE),
	
/*------------------------------------------------
 	Hybrid Operations - Things a player might actually be able to do
 ------------------------------------------------*/	
	/** Put a Playing Card from first Card Pile into second Card Pile */
	PUT(Return.BOOLEAN, Parameter.CARDPILE, Parameter.CARDPILE, Parameter.PLAYING_CARD),
	
	/** Put multiple Playing Cards from first Card Pile into second Card Pile */
	PUT_MULTIPLE(Return.BOOLEAN, Parameter.CARDPILE, Parameter.CARDPILE, Parameter.PLAYING_CARDS),
	
	/** Put all Playing Cards from first Card Pile into second Card Pile */
	PUT_ALL(Return.BOOLEAN, Parameter.CARDPILE, Parameter.CARDPILE),
	
	/** Exchange first Playing Card  in first Card Pile with second Playing Card in second Card Pile */
	EXCHANGE(Return.BOOLEAN, Parameter.CARDPILE, Parameter.CARDPILE, Parameter.PLAYING_CARD, Parameter.PLAYING_CARD),
	
	/** Remove Playing Card in a Card Pile from play */
	REMOVE(Return.BOOLEAN, Parameter.PLAYING_CARD, Parameter.CARDPILE),
	
	/** Remove all Playing Cards in a Card Pile from play */
	REMOVE_ALL(Return.BOOLEAN, Parameter.CARDPILE);
	
	/*------------------------------------------------
 	Private Attributes and Constructor
 ------------------------------------------------*/
	private final Operation.Parameter[] params;
	private final Operation.Return ret;
	
	private Operation(final Operation.Return ret, final Operation.Parameter...params) {
		this.params = params == null || params.length == 0 ? new Operation.Parameter[0] : Arrays.copyOf(params, params.length);
		this.ret = ret;
	}
	
/*------------------------------------------------
 	Accessors and Utilities
 ------------------------------------------------*/
	/**
	 * Returns the type of return from this <tt>Operation</tt>.
	 * 
	 * @return the type of return from this <tt>Operation</tt>
	 */
	public Operation.Return getReturnType() {
		return ret;
	}
	
	/**
	 * Returns the set of parameters necessary for this <tt>Operation</tt>.
	 * 
	 * @return the set of parameters necessary for this <tt>Operation</tt>
	 */
	public Operation.Parameter[] getParams() {
		return Arrays.copyOf(params, params.length);
	}
	
	/**
	 * Returns the number of parameters required for this <tt>Operation</tt>.
	 * 
	 * @return the number of parameters required for this <tt>Operation</tt>
	 */
	public int getNumberOfParams() {
		return params.length;
	}
	
	/**
	 * Returns <tt>true</tt> if this operation has an array parameter.
	 * 
	 * @return <tt>true</tt> if this operation has an array parameter
	 */
	public boolean expectsArrayParameter() {
		for (Operation.Parameter p : params) {
			if (p.isAnArray()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Used to convert an enum name into a useable string.
	 * Returns a lowercase string with all '_' replaced with '-'.
	 * 
	 * @param string base string of enum (super.toString())
	 * @return a converted enum string
	 */
	protected static String convertEnumToString(final String string) {
		return string.toLowerCase(Engine.LOCALE).replace('_', '-');
	}

/*------------------------------------------------
	Overridden Methods
 ------------------------------------------------*/
	/**
	 * Returns the string represented by this <tt>Operation</tt>.
	 * 
	 * @return the string represented by this <tt>Operation</tt>
	 */
	@Override public String toString() {
		return Operation.convertEnumToString(super.toString());
	}
	

	/***********************************************************************//**
	 * Nested Enum Parameter - types of operation parameters.
	 ***************************************************************************/
	public static enum Parameter {
		/** A player in a game */
		CARD_PLAYER(CardPlayer.class),
		/** A single playing card */
		PLAYING_CARD(PlayingCard.class),
		/** A group of playing cards */
		PLAYING_CARDS(PlayingCard[].class, Operation.Parameter.PLAYING_CARD),
		/** A single card pile */
		CARDPILE(CardPile.class),
		/** A group of card piles */
		CARDPILES(CardPile[].class, Operation.Parameter.CARDPILE),
		/** A game phase */
		PHASE(Phase.class),
		/** A String value */
		STRING(String.class),
		/** A numeric integer value */
		INTEGER(Integer.class);
		
		private final Class<?> pClass;
		private Operation.Parameter aType;
		private Parameter(final Class<?> pClass) {
			this(pClass, null);
		}
		
		private Parameter(final Class<?> pClass, final Operation.Parameter aType) {
			this.pClass = pClass;
			this.aType = aType;
		}
		
		/** Returns the class for this <tt>Parameter</tt>. */
		public Class<?> getParamClass() {
			return pClass;
		}
		
		/** Returns <tt>true</tt> if this parameter represents an array. */
		public boolean isAnArray() {
			return aType != null;
		}
		
		/** Returns the class of the grouped parameter if this <tt>Parameter</tt> is an array. */
		public Operation.Parameter getArrayType() {
			return aType;
		}
		
		/***********************************************************************//**
		 * Operation.Parameter.Options - Parameters list to use for an Operation
		 ***************************************************************************/
		public static class OptionList {
			
			/** Type of options presented. */
			public static enum Type {
				/** Pick a card */
				PICK_CARD("playing card"),
				/** Pick a card pile */
				PICK_PILE("card pile"),
				/** Pick a game player */
				PICK_PLAYER("game player"),
				/** Pick a number */
				PICK_INT("number");
				
				private static final String PICK = "Pick a ";
				private final String message;
				private Type(final String message) {
					this.message = message;
				}
				
				/** Returns the user message associated with this options type. */
				public String getMessage() {
					return PICK + message;
				}
			}
			
			private final Operation.Parameter.OptionList.Type type;
			private final List<Operation.Parameter.Value> options;
			private final boolean isVisible;
			private final boolean isMultiple;
			private final String appendedString;
			
			/** 
			 * Construct this set of <tt>Parameter.Options</tt>.
			 * The generic <tt>Operation.Parameter.Value</tt> is used, but all values must
			 * be of the same base type.
			 * 
			 * @param options array of values to be used as options
			 * @throws IllegalArgumentException if Options is null or empty or contains different base types or an unsupported type.
			 * */
			public OptionList(final Value...options) throws IllegalArgumentException {
				this (true, "", options);
			}
			
			/** 
			 * Construct this set of <tt>Parameter.Options</tt>.
			 * The generic <tt>Operation.Parameter.Value</tt> is used, but all values must
			 * be of the same base type.
			 * 
			 * @param isVisible false if these options should be invisible to the player
			 * @param options array of values to be used as options
			 * @throws IllegalArgumentException if Options is null or empty or contains different base types or an unsupported type.
			 * */
			public OptionList(final boolean isVisible, final Value...options) throws IllegalArgumentException {
				this (isVisible , "", options);
			}
			
			/** 
			 * Construct this set of <tt>Parameter.Options</tt>.
			 * The generic <tt>Operation.Parameter.Value</tt> is used, but all values must
			 * be of the same base type.
			 * 
			 * @param appendedString any additional text to append to the user message of this OptionList
			 * @param options array of values to be used as options
			 * @throws IllegalArgumentException if Options is null or empty or contains different base types or an unsupported type.
			 * */
			public OptionList(final String appendedString, final Value...options) throws IllegalArgumentException {
				this(true, "", options);
			}
			
			/** 
			 * Construct this set of <tt>Parameter.Options</tt>.
			 * The generic <tt>Operation.Parameter.Value</tt> is used, but all values must
			 * be of the same base type.
			 * 
			 * @param isVisible false if these options should be invisible to the player
			 * @param appendedString any additional text to append to the user message of this OptionList
			 * @param options array of values to be used as options
			 * @throws IllegalArgumentException if Options is null or empty or contains different base types or an unsupported type.
			 * */
			public OptionList(final boolean isVisible, final String appendedString, final Value...options) throws IllegalArgumentException {
				this(isVisible, false, "", options);
			}
			
			/** 
			 * Construct this set of <tt>Parameter.Options</tt>.
			 * The generic <tt>Operation.Parameter.Value</tt> is used, but all values must
			 * be of the same base type.
			 * 
			 * @param isVisible false if these options should be invisible to the player
			 * @param appendedString any additional text to append to the user message of this OptionList
			 * @param options array of values to be used as options
			 * @throws IllegalArgumentException if Options is null or empty or contains different base types or an unsupported type.
			 * */
			public OptionList(final boolean isVisible, final boolean isMultiple, final String appendedString, final Value...options) throws IllegalArgumentException {
				if (options == null || options.length == 0) {
					throw new IllegalArgumentException("Options can not be null or empty.");
				}
				for (int i = 0; i < options.length; i++) {
					for (int j = i+1; j < options.length; j++) {
						if (!options[i].getParamClass().equals(options[j].getParamClass())) {
							throw new IllegalArgumentException("All options must be of same base type.");
						}
					} 
				}
				this.isVisible = isVisible;
				this.isMultiple = isMultiple;
				this.appendedString = appendedString;
				this.options = new ArrayList<Value>(options.length);
				for (Value v : options) {
					this.options.add(v);
				}
				Type temp = null;
				switch (options[0].getType()) {
				case PLAYING_CARD:
					temp = Type.PICK_CARD;
					break;
				case CARDPILE:
					temp = Type.PICK_PILE;
					break;
				case CARD_PLAYER:
					temp = Type.PICK_PLAYER;
					break;
				case INTEGER:
					temp = Type.PICK_INT;
					break;
				default:
					throw new IllegalArgumentException("Unsupported Options of type " + options[0].getType().toString());
				}
				this.type = temp;
			}
			
			/** Returns a message to be displayed to users about these Options. */
			public String getUserMessage() {
				return type.getMessage() + appendedString;
			}
			
			/** 
			 * Returns this set of <tt>Value</tt> options as a <tt>List</tt>. 
			 * A List is used to allow the removal of a value once it has been selected in order
			 * for the list to be displayed again without the previously chosen value(s). 
			 * */
			public List<Operation.Parameter.Value> getOptions() {
				return options;
			}
			
			/**
			 * Returns <tt>true</tt> if these options should be visible to the user.
			 * 
			 * @return <tt>true</tt> if these options should be visible to the user
			 */
			public boolean isVisible() {
				return isVisible;
			}
			
			/**
			 * Returns <tt>true</tt> if these options should be used multiple times.
			 * 
			 * @return <tt>true</tt> if these options should be used multiple times
			 */
			public boolean isMultiple() {
				return isMultiple;
			}
			
		}
		
		/***********************************************************************//**
		 * Operation.Parameter.Value - Generic Parameters for an Operation
		 ***************************************************************************/
		public static class Value {
			
			private final Operation.Parameter type;
			private final CardPlayer player;
			private final PlayingCard card;
			private final PlayingCard[] cards;
			private final CardPile pile;
			private final CardPile[] piles;
			private final Phase phase;
			private final String string;
			private final Integer value;
			
		/*------------------------------------------------
			Constructor(s)
		------------------------------------------------*/
			
			/** Construct a <tt>CardPlayer</tt> <tt>Value</tt>. */
			public Value(final CardPlayer player) {
				this(Parameter.CARD_PLAYER, player, null, null, null, null, null);
			}
			
			/** Construct a <tt>PlayingCardr</tt> <tt>Value</tt>. */
			public Value(final PlayingCard...cards) {
				this(cards.length == 1 ? Parameter.PLAYING_CARD : Parameter.PLAYING_CARDS, null, cards, null, null, null, null);
			}
			
			/** Construct a <tt>CardPile</tt> <tt>Value</tt>. */
			public Value(final CardPile...piles) {
				this(piles.length == 1 ? Parameter.CARDPILE : Parameter.CARDPILES, null, null, piles, null, null, null);
			}
			
			/** Construct a <tt>Phase</tt> <tt>Value</tt>. */
			public Value(final Phase phase) {
				this(Parameter.PHASE, null, null, null, phase, null, null);
			}
			
			/** Construct a <tt>String</tt> <tt>Value</tt>. */
			public Value(final String string) {
				this(Parameter.STRING, null, null, null, null, string, null);
			}
			
			/** Construct a <tt>Integer</tt> <tt>Value</tt>. */
			public Value(final Integer value) {
				this(Parameter.INTEGER, null, null, null, null, null, value);
			}
			
			/* Primary constructor, used by all others */
			private Value(	final Operation.Parameter type, final CardPlayer player,
							final PlayingCard[] cards, final CardPile[] piles,
							final Phase phase, final String string, final Integer value) {
				this.type = type;
				this.player = player;
				this.card = cards == null ? null : cards.length == 1 ? cards[0] : null;
				this.pile = piles == null ? null : piles.length == 1 ? piles[0] : null;
				this.phase = phase;
				this.string = string;
				this.value = value;
				this.cards = cards == null ? null : cards.length == 1 ? null : Arrays.copyOf(cards, cards.length);
				this.piles = piles == null ? null : piles.length == 1 ? null : Arrays.copyOf(piles, piles.length);
			}
			
		/*------------------------------------------------
			Accessor(s)
		------------------------------------------------*/
			
			/**
			 * Returns the type of <tt>Parameter</tt> for this <tt>Value</tt>.
			 * 
			 * @return the type of <tt>Parameter</tt> for this <tt>Value</tt>
			 */
			public Operation.Parameter getType() {
				return type;
			}
			
			/**
			 * Checks if this <tt>Operation.Parameter.Value</tt> contains an object of the specified class type.
			 * Returns the <tt>Operation.Parameter.Type</tt> if so.
			 * 
			 * @param clazz the class type of desired object
			 * @return the <tt>Operation.Parameter.Type</tt> of this <tt>Value</tt>
			 * @throws EngineException if the contained object is not of the specified type
			 */
			public Operation.Parameter checkType(final Class<?> clazz) throws EngineException {
				if (!this.getParamClass().equals(clazz)) {
					throw EngineException.create(EngineException.Type.VALUE_TYPE_MISMATCH, null, this.get().toString(), CardPile.class.toString());
				}
				return type;
			}
			
			/**
			 * Returns the class of the stored <tt>Parameter</tt> <tt>Value</tt>.
			 * 
			 * @return the class of the stored <tt>Parameter</tt> <tt>Value</tt>
			 */
			public Class<?> getParamClass() {
				return type.getParamClass();
			}
			
			/**
			 * Returns string information for the actual object in this <tt>Value</tt>.
			 * 
			 * @return string information for the actual object in this <tt>Value</tt>
			 */
			@Override public String toString() {
				return this.get().toString();
			}
			
		/*------------------------------------------------
			Returning the contained object
		------------------------------------------------*/
			
			/**
			 * Returns the value of this parameter as a raw object.
			 * 
			 * @return the value of this parameter as a raw object
			 */
			public Object get() {
				switch (type) {
				case CARD_PLAYER:
					return player;
				case PLAYING_CARD:
					return card;
				case PLAYING_CARDS:
					return cards;
				case CARDPILE:
					return pile;
				case CARDPILES:
					return piles;
				case PHASE:
					return phase;
				case STRING:
					return string;
				case INTEGER:
					return value;
				default:
					return null;
				}
			}
			
			/**
			 * Returns a <tt>CardPlayer</tt> from this <tt>Value</tt>.
			 * 
			 * @return  a <tt>CardPlayer</tt> from this <tt>Value</tt>
			 * @throws EngineException if this <tt>Value</tt> does not contain an object of the desired type
			 */
			public CardPlayer getCardPlayer() throws EngineException {
				this.checkType(CardPlayer.class);
				return CardPlayer.class.cast(this.get());
			}
			
			/**
			 * Returns a <tt>PlayingCard</tt> from this <tt>Value</tt>.
			 * 
			 * @return  a <tt>PlayingCard</tt> from this <tt>Value</tt>
			 * @throws EngineException if this <tt>Value</tt> does not contain an object of the desired type
			 */
			public PlayingCard getPlayingCard() throws EngineException {
				this.checkType(PlayingCard.class);
				return PlayingCard.class.cast(this.get());
			}
			
			/**
			 * Returns a <tt>PlayingCard[]</tt> from this <tt>Value</tt>.
			 * 
			 * @return  a <tt>PlayingCard[]</tt> from this <tt>Value</tt>
			 * @throws EngineException if this <tt>Value</tt> does not contain an object of the desired type
			 */
			public PlayingCard[] getPlayingCards() throws EngineException {
				this.checkType(PlayingCard[].class);
				return PlayingCard[].class.cast(this.get());
			}
			
			/**
			 * Returns a <tt>CardPile</tt> from this <tt>Value</tt>.
			 * 
			 * @return  a <tt>CardPile</tt> from this <tt>Value</tt>
			 * @throws EngineException if this <tt>Value</tt> does not contain an object of the desired type
			 */
			public CardPile getCardPile() throws EngineException {
				this.checkType(CardPile.class);
				return CardPile.class.cast(this.get());
			}
			
			/**
			 * Returns a <tt>CardPile[]</tt> from this <tt>Value</tt>.
			 * 
			 * @return  a <tt>CardPile[]</tt> from this <tt>Value</tt>
			 * @throws EngineException if this <tt>Value</tt> does not contain an object of the desired type
			 */
			public CardPile[] getCardPiles() throws EngineException {
				this.checkType(CardPile[].class);
				return CardPile[].class.cast(this.get());
			}
			
			/**
			 * Returns a <tt>Phase</tt> from this <tt>Value</tt>.
			 * 
			 * @return  a <tt>Phase</tt> from this <tt>Value</tt>
			 * @throws EngineException if this <tt>Value</tt> does not contain an object of the desired type
			 */
			public Phase getPhase() throws EngineException {
				this.checkType(Phase.class);
				return Phase.class.cast(this.get());
			}
			
			/**
			 * Returns a <tt>String</tt> from this <tt>Value</tt>.
			 * 
			 * @return  a <tt>String</tt> from this <tt>Value</tt>
			 * @throws EngineException if this <tt>Value</tt> does not contain an object of the desired type
			 */
			public String getString() throws EngineException {
				this.checkType(String.class);
				return String.class.cast(this.get());
			}
			
			/**
			 * Returns a <tt>Integer</tt> from this <tt>Value</tt>.
			 * 
			 * @return  a <tt>Integer</tt> from this <tt>Value</tt>
			 * @throws EngineException if this <tt>Value</tt> does not contain an object of the desired type
			 */
			public Integer getInteger() throws EngineException {
				this.checkType(Integer.class);
				return Integer.class.cast(this.get());
			}
			
		/*------------------------------------------------
			Converting Value arrays to Object arrays
		------------------------------------------------*/
			/**
			 * Returns an array of the <tt>PlayincCards</tt> found in these Values.
			 * 
			 * @param values the values to search for Playing Cards
			 * @return an array of PlayingCards
			 * @throws EngineException if a Value contains an invalid type
			 */
			public static PlayingCard[] asCards(final Value...values) throws EngineException {
				final List<PlayingCard> cardList = new ArrayList<PlayingCard>();
				for (final Value v : values) {
					if (v != null) {
						if (v.getType() == Parameter.PLAYING_CARD) {
							cardList.add(v.getPlayingCard());
						}
						else if (v.getType() == Parameter.PLAYING_CARDS) {
							for (final PlayingCard c : v.getPlayingCards()) {
								cardList.add(c);
							}
						}
					}
				}
				return cardList.toArray(new PlayingCard[cardList.size()]);
			}
			
			/**
			 * Returns an array of the <tt>CardPiles</tt> found in these Values.
			 * 
			 * @param values the values to search for CardPiles
			 * @return an array of CardPiles
			 * @throws EngineException if a Value contains an invalid type
			 */
			public static CardPile[] asPiles(final Value...values) throws EngineException {
				final List<CardPile> pileList = new ArrayList<CardPile>();
				for (final Value v : values) {
					if (v != null) {
						if (v.getType() == Parameter.CARDPILE) {
							pileList.add(v.getCardPile());
						}
						else if (v.getType() == Parameter.CARDPILES) {
							for (final CardPile c : v.getCardPiles()) {
								pileList.add(c);
							}
						}
					}
				}
				return pileList.toArray(new CardPile[pileList.size()]);
			}
			
		/*------------------------------------------------
			Converting Object arrays to Value arrays
		------------------------------------------------*/
			
			/**
			 * Converts an array of PlayingCards into generic Parameter Values.
			 * 
			 * @param cards PlayingCards to convert
			 * @return an array of values
			 */
			public static Value[] asValues(final PlayingCard...cards) {
				Value[] values = new Value[cards.length];
				for (int i = 0; i < cards.length; i++) {
					values[i] = new Value(cards[i]);
				}
				return values;
			}
			
			/**
			 * Converts an array of CardPiles into generic Parameter Values.
			 * 
			 * @param piles CardPiles to convert
			 * @return an array of values
			 */
			public static Value[] asValues(final CardPile...piles) {
				Value[] values = new Value[piles.length];
				for (int i = 0; i < piles.length; i++) {
					values[i] = new Value(piles[i]);
				}
				return values;
			}
			
			/**
			 * Converts an array of CardPlayers into generic Parameter Values.
			 * 
			 * @param players CardPlayers to convert
			 * @return an array of values
			 */
			public static Value[] asValues(final CardPlayer...players) {
				Value[] values = new Value[players.length];
				for (int i = 0; i < players.length; i++) {
					values[i] = new Value(players[i]);
				}
				return values;
			}
			
			/**
			 * Converts an array of Integers into generic Parameter Values.
			 * 
			 * @param ints Integers to convert
			 * @return an array of values
			 */
			public static Value[] asValues(final int...ints) {
				Value[] values = new Value[ints.length];
				for (int i = 0; i < ints.length; i++) {
					values[i] = new Value(ints[i]);
				}
				return values;
			}
			
			/**
			 * Converts an array of Strings into generic Parameter Values.
			 * 
			 * @param ints Integers to convert
			 * @return an array of values
			 */
			public static Value[] asValues(final String...strings) {
				Value[] values = new Value[strings.length];
				for (int i = 0; i < strings.length; i++) {
					values[i] = new Value(strings[i]);
				}
				return values;
			}
		}	// End of Operation.Parameter.Value
		
	}	// End of Operation.Parameter
	
	/***********************************************************************//**
	 * Nested Enum Return - types of return from this operation.
	 ***************************************************************************/
	public static enum Return {
		/** A boolean value */
		BOOLEAN,
		/** An integer value */
		INTEGER;

		/***********************************************************************//**
		 * Operation.Return.Value - Generic Parameters for an Operation Return
		 ***************************************************************************/
		public static class Value {
			
			public static final Value TRUE = new Value(true);
			public static final Value FALSE = new Value(false);
			private final boolean b;
			private final int i;
			private Operation.Return type;
			
			/** Construct a <tt>ReturnValue</tt> using an integer. */
			public Value(final int i) {
				this.i = i;
				this.b = false;
				this.type = Operation.Return.INTEGER;
			}
			/** Construct a <tt>ReturnValue</tt> using a boolean. */
			private Value(final boolean b) {
				this.b = b;
				this.i = 0;
				this.type = Operation.Return.BOOLEAN;
			}
			
			/** 
			 * Returns the integer return value of an operation.
			 * 
			 * @return the integer return value of an operation
			 * @throws UnsupportedOperationException if the operation does not return an integer
			 * */ 
			public int getInt() throws UnsupportedOperationException {
				if (type != Operation.Return.INTEGER) {
					throw new UnsupportedOperationException("Operation does not return an integer!");
				}
				return i;
			}
			
			/** 
			 * Returns the boolean return value of an operation.
			 * 
			 * @return the boolean return value of an operation
			 * @throws UnsupportedOperationException if the operation does not return a boolean
			 * */ 
			public boolean getBool() throws UnsupportedOperationException {
				if (type != Operation.Return.BOOLEAN) {
					throw new UnsupportedOperationException("Operation does not return a boolean!");
				}
				return b;
			}
		}	// End of Operation.Return.Value
	
	}	// End of Operation.Return

}	// End of Operation
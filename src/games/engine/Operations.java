/***********************************************************************//**
* @file			Operations.java
* @author		Kurt E. Clothier
* @date			December 14, 2015
* 
* @breif		Individual operation definitions
*
* @pre			Compiler: Eclipse - Mars Release (4.5.0)
* @pre			Java: JRE 7 or greater
*
* @see			http://www.projectsbykec.com/
*
* @copyright	The MIT License (MIT) - see LICENSE.txt
****************************************************************************/

package games.engine;

import games.engine.util.CardPile;
import games.engine.util.PlayingCard;
import games.engine.util.PlayingCardRanking;

/******************************************************************//**
 * The Operation Class
 * 	- this is super big and ugly...
 * 	- This would be much better as individual operation classes using polymorphism
 * 		... that will have to be a project for another day!
 ********************************************************************/
public final class Operations {

	private Operations() {}
	
	/** Check that the specified parameter array has at least 'number' parameters */
	static void checkNumberOfParams(final int number, final Operation operation, final Operation.Parameter.Value...params) throws EngineException {
		checkNumberOfParams(number, operation, params.length);
	}
	
	/** Check that the specified array length is at least the size of 'number' */
	static void checkNumberOfParams(final int number, final Operation operation, final int paramsLength) throws EngineException {
		if (paramsLength < number) {
			throw EngineException.create(EngineException.Type.INVALID_NUMBER_OF_PARAMETERS, operation);
		}
	}
	
	/** Perform engine operations... this is gross and needs refactor! */
	static Operation.Return.Value perform(final Operation operation, final CardGameEngine engine, final Operation.Parameter.Value...params) throws EngineException {
		Operation.Return.Value ret = Operation.Return.Value.TRUE;;
		switch(operation) {
		case END_TURN:
			engine.setCurrentPlayer(engine.getNextPlayer());
			break;
		case GOTO:
			checkNumberOfParams(1, operation, params);
			engine.getCurrentPlayer().setPhase(params[0].getPhase());
			break;
		case PLAYER_WIN:
			checkNumberOfParams(1, operation, params);
			params[0].getCardPlayer().wins();
			break;
		case PLAYER_LOSE:
			checkNumberOfParams(1, operation, params);
			params[0].getCardPlayer().loses();
			break;
		case MATCH:
		case MATCH_RANK:
		case COMPARE_RANK:
		case CHECK_RANK:
			PlayingCard[] cards = Operation.Parameter.Value.asCards(params);
			PlayingCard card = cards[0];
			switch (operation) {
			case MATCH:
				for (final PlayingCard c : cards) {
					if (!card.equals(c)) {
						ret = Operation.Return.Value.FALSE;
						break;
					}
				}
				break;
			default:
				PlayingCardRanking ranking = engine.getRanking();
				switch (operation) {
				case MATCH_RANK:
					for (final PlayingCard c : cards) {
						System.out.println(c);
						if (ranking.compare(card, c) != 0) {
							System.out.println("NO MATCH!");
							ret = Operation.Return.Value.FALSE;
							break;
						}
					}
					break;
				case COMPARE_RANK:
					ret = new Operation.Return.Value(ranking.compare(card, cards[1]));
					break;
				case CHECK_RANK:
				default:
					if (ranking.compare(card, cards[1]) <= 0) {
						ret = Operation.Return.Value.FALSE;
					}
					break;
				}
				break;
			}
			break;
		case SORT_BY_RANK:
			checkNumberOfParams(1, operation, params);
			CardPile pile = params[0].getCardPile(); 
			cards = pile.removeAll();
			PlayingCardRanking ranking = engine.getRanking();
			int i = 0;
			for (int j = 1; j < cards.length; j++) {
				card = cards[j];
				for (i = j-1; i >= 0 && ranking.compare(cards[i], card) < 0; i--) {
					cards[i + 1] = cards[i];
				}
				cards[i + 1] = card;
			}
			pile.add(cards);
			break;
		case SORT_BY_FACE:
			checkNumberOfParams(1, operation, params);
			// TODO
			break;	
		case COMPARE_SIZES:
			checkNumberOfParams(2, operation, params);
			ret = new Operation.Return.Value(Integer.compare(params[0].getCardPile().getSize(), 
															 params[1].getCardPile().getSize()));
			break;
		case CHECK_IF_EMPTY:
			checkNumberOfParams(1, operation, params);
			if (!params[0].getCardPile().isEmpty()) {
				ret = Operation.Return.Value.FALSE;
			}
			break;
		case GET_SIZE:
			checkNumberOfParams(1, operation, params);
			ret = new Operation.Return.Value(params[0].getCardPile().getSize());
			break;
		case REMOVE_ALL:
			checkNumberOfParams(1, operation, params);
			params[0].getCardPile().removeAll();
			break;
		case PUT_ALL:
			checkNumberOfParams(2, operation, params);
			params[1].getCardPile().add(params[0].getCardPile().removeAll());
			break;
		case COMPARE_SIZE_TO:
			checkNumberOfParams(2, operation, params);
			ret = new Operation.Return.Value(Integer.compare(params[0].getCardPile().getSize(), 
															 params[1].getInteger()));
			break;
		case CHECK_SIZE:
			checkNumberOfParams(2, operation, params);
			if (Integer.compare(params[0].getCardPile().getSize(), params[1].getInteger()) <= 0) {
				ret = Operation.Return.Value.FALSE;
			}
			break;
		case REMOVE:
			checkNumberOfParams(2, operation, params);
			if (!params[1].getCardPile().remove(params[0].getPlayingCard())) {
				ret = Operation.Return.Value.FALSE;
			}
			break;
		case PUT:
		case PUT_MULTIPLE:
			checkNumberOfParams(3, operation, params);
			cards = Operation.Parameter.Value.asCards(params);
			for (final PlayingCard c : cards) {
				if (!params[0].getCardPile().contains(c)) {
					ret = Operation.Return.Value.FALSE;
					break;
				}
			}
			if (ret == Operation.Return.Value.TRUE) {
				for (final PlayingCard c : cards) {
					params[0].getCardPile().remove(c);
					params[1].getCardPile().add(c);
				}
			}
			break;
		case EXCHANGE:
			checkNumberOfParams(4, operation, params);
			if (	params[0].getCardPile().contains(params[2].getPlayingCard()) &&
					params[1].getCardPile().contains(params[3].getPlayingCard())) {
				params[0].getCardPile().remove(params[2].getPlayingCard());
				params[1].getCardPile().remove(params[3].getPlayingCard());
				params[1].getCardPile().add(params[2].getPlayingCard());
				params[0].getCardPile().add(params[3].getPlayingCard());
			}
			else {
				ret = Operation.Return.Value.FALSE;
			}
			break;
		default:
			throw EngineException.create(EngineException.Type.INVALID_COMPONENT, operation, operation.toString());
		}
		return ret;
	}
}

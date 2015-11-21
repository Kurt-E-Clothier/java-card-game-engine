package games.engine.tests;

import games.engine.util.*;
import games.engine.plugin.*;

public final class DealerTB {
	
	private DealerTB() {}
	
	public static void main(String[] args) throws PluginException {
		
		final Plugin rulesPlugin = new Plugin(Plugin.Type.RULES, "test");
		final Deck deck = DeckFactory.INSTANCE.createDeck(rulesPlugin);
		
		final CardPileFactory factory = CardPileFactory.getInstance();
		final Plugin[] pilePlugins = factory.createPlugins(rulesPlugin);
		final CardPileCollection commonPileCollecion = factory.createCardPileCollection(CardPileParameter.Owner.COMMON, pilePlugins);
		
		final CardPlayer[] players = new CardPlayer[3];
		players[0] = new CardPlayer("Kurt", factory.createCardPileCollection(CardPileParameter.Owner.PLAYER, pilePlugins));
		players[1] = new CardPlayer("Kyle", factory.createCardPileCollection(CardPileParameter.Owner.PLAYER, pilePlugins));
		players[2] = new CardPlayer("Lovedeep", factory.createCardPileCollection(CardPileParameter.Owner.PLAYER, pilePlugins));
		
		final CardDealer dealer = CardDealerFactory.INSTANCE.createCardDealer(rulesPlugin, commonPileCollecion, deck, players);
		
		while(!dealer.isDone()){
			System.out.println(dealer.dealNext());
		}
		
		// SUCCESS!!
	}
}

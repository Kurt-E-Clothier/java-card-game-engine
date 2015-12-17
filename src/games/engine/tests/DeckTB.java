package games.engine.tests;

import games.engine.plugin.Plugin;
import games.engine.plugin.PluginException;
import games.engine.util.*;

public final class DeckTB {
	
	private DeckTB() {}

	public static void main(String[] args) throws PluginException {
		
		final Plugin plugin = new Plugin(Plugin.Type.RULES, "test");
		
		// Create Deck Plugin in from text file, create Deck from plugin
		DeckFactory factory = DeckFactory.getInstance();
		Deck deck = null;
		Deck deck2 = null;
		try {
			deck = factory.createDeck(plugin);
			deck2 = factory.createDeck(plugin);
		} catch (PluginException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
		
		System.out.println("Equal before shuffle? " + deck.equals(deck2));
		deck.shuffle();
		System.out.println("Equal after shuffle? " + deck.equals(deck2));
		System.out.println();
		
		PlayingCard[] cards;
		
		cards = deck.deal(-1);
		System.out.println(cards.length);
		
		cards = deck.deal(0);
		System.out.println(cards.length);
		
		cards = deck.deal(1);
		System.out.println(cards.length);
		
		cards = deck.deal(30);
		System.out.println(cards.length);
		
		cards = deck.deal(30);
		System.out.println(cards.length);
		
		
		//PlayingCardRanking ranking = PlayingCardFactory.INSTANCE.createCardRanking(plugin);
		//System.out.println(ranking);
	}
}

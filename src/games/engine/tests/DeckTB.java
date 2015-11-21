package games.engine.tests;

import games.engine.plugin.Plugin;
import games.engine.plugin.PluginException;
import games.engine.util.*;

public final class DeckTB {
	
	private final static String DECK_NAME = "test";
	
	private DeckTB() {}

	public static void main(String[] args) {
		
		// Create Deck Plugin in from text file, create Deck from plugin
		DeckFactory factory = DeckFactory.getInstance();
		Deck deck = null;
		Deck deck2 = null;
		try {
			deck = factory.createDeck(new Plugin(Plugin.Type.DECK, DECK_NAME));
			deck2 = factory.createDeck(new Plugin(Plugin.Type.DECK, DECK_NAME));
		} catch (PluginException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
		
		System.out.println("Equal before shuffle? " + deck.equals(deck2));
		deck.shuffle();
		System.out.println("Equal after shuffle? " + deck.equals(deck2));
		System.out.println();
		
		PlayingCard[] cards = deck.deal(5);
		for (final PlayingCard c : cards) {
			System.out.println(c.toString());
		}
		System.out.println();
		System.out.println(deck.deal());
		System.out.println();
		cards = deck.deal(40);
		for (final PlayingCard c : cards) {
			System.out.println(c.toString());
		}
		
		deck2.deal(45);
		cards = deck2.dealAll();
		System.out.println();
		for (final PlayingCard c : cards) {
			System.out.println(c.toString());
		}
	}
}

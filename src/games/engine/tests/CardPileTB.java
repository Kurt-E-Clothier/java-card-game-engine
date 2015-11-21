package games.engine.tests;

import games.engine.plugin.*;
import games.engine.util.*;

public final class CardPileTB {
	
	private final static String BOARD_NAME = "test";
	
	private CardPileTB() {}

	public static void main(String[] args) {

		Plugin[] pilePlugins = null;
		CardPile[] piles = null;
		try {
			pilePlugins = CardPileFactory.INSTANCE.createPlugins(new Plugin(Plugin.Type.BOARD, BOARD_NAME));
			piles = new CardPile[pilePlugins.length];
			for (int i = 0; i < piles.length; i++) {
				piles[i] = CardPileFactory.INSTANCE.createCardPile(pilePlugins[i]);
			}
		} catch (PluginException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}
		
		for (final CardPile p : piles) {
			System.out.println(p);
			System.out.println();
		}
	}

}

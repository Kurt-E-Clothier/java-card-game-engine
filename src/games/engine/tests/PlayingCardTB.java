package games.engine.tests;

import games.engine.util.PlayingCard;
import games.engine.util.PlayingCardFace;
import games.engine.util.PlayingCardFactory;
import games.engine.util.PlayingCardGroup;

public final class PlayingCardTB {
	
	private PlayingCardTB() {}

	public static void main(String[] args) {
		
		System.out.println(" --- Playing Card Test Bench ---\n");
		
		//TODO
		// Card rankings!!
		// Comparators for sorting
		// New Deck
		
		PlayingCardFactory factory = PlayingCardFactory.getInstance();
		
		//factory.createFace(null);
		//factory.createFace(2);
		factory.createFace("2");
		
		//factory.createPlayingCard(null, null);
		factory.createPlayingCard(factory.createFace("King"), null);
		factory.createPlayingCard(null, factory.createGroup("Red"));
		factory.createPlayingCard(factory.createFace("King"), factory.createGroup("Red"));
		
		PlayingCardFace king = factory.createFace("King");
		PlayingCardFace queen = factory.createFace("Queen");
		PlayingCardGroup clubs = factory.createGroup("Clubs");
		PlayingCardGroup hearts = factory.createGroup("Hearts");
		
		PlayingCard nullCardA = factory.createPlayingCard(null, clubs);
		PlayingCard nullCardB = factory.createPlayingCard(king, null);
		PlayingCard nullCardC = factory.createPlayingCard(queen, null);
		PlayingCard nullCardD = factory.createPlayingCard(null, hearts);
		
		
		PlayingCard cardA = factory.createPlayingCard(king, clubs);
		PlayingCard cardB = factory.createPlayingCard(queen, clubs);
		PlayingCard cardC = factory.createPlayingCard(king, hearts);
		PlayingCard cardD = factory.createPlayingCard(queen, hearts);
		
		System.out.println(cardA.equals(cardA));
		System.out.println(cardA.equals(cardB));
		System.out.println(cardA.equals(cardC));
		System.out.println(cardA.equals(cardD));
		System.out.println(cardA.equals(null));
		System.out.println(cardA.equals(nullCardA));
		System.out.println(cardA.equals(nullCardB));
		System.out.println(cardA.equals(nullCardC));
		System.out.println(cardA.equals(nullCardD));
		System.out.println(cardA.equals(king));

		System.out.println();
		
		System.out.println(nullCardA.equals(nullCardA));
		System.out.println(nullCardA.equals(cardA));
		System.out.println(nullCardA.equals(null));
		System.out.println(nullCardA.equals(nullCardB));
		System.out.println(nullCardA.equals(nullCardC));
		System.out.println(nullCardA.equals(nullCardD));
		
		System.out.println();
		
		System.out.println(nullCardB.equals(nullCardB));
		System.out.println(nullCardB.equals(cardA));
		System.out.println(nullCardB.equals(null));
		System.out.println(nullCardB.equals(nullCardA));
		System.out.println(nullCardB.equals(nullCardC));
		System.out.println(nullCardB.equals(nullCardD));
		
		System.out.println();
		
		System.out.println(cardA.compareTo(cardA));
		System.out.println(cardA.compareTo(cardB));
		System.out.println(cardA.compareTo(cardC));
		System.out.println(cardA.compareTo(cardD));
		System.out.println(cardA.compareTo(null));
		System.out.println(cardA.compareTo(nullCardA));
		System.out.println(cardA.compareTo(nullCardB));
		System.out.println(cardA.compareTo(nullCardC));
		System.out.println(cardA.compareTo(nullCardD));

		System.out.println();
		
		System.out.println(nullCardA.compareTo(nullCardA));
		System.out.println(nullCardA.compareTo(cardA));
		System.out.println(nullCardA.compareTo(null));
		System.out.println(nullCardA.compareTo(nullCardB));
		System.out.println(nullCardA.compareTo(nullCardC));
		System.out.println(nullCardA.compareTo(nullCardD));
		
		System.out.println();
		
		System.out.println(nullCardB.compareTo(nullCardB));
		System.out.println(nullCardB.compareTo(cardA));
		System.out.println(nullCardB.compareTo(null));
		System.out.println(nullCardB.compareTo(nullCardA));
		System.out.println(nullCardB.compareTo(nullCardC));
		System.out.println(nullCardB.compareTo(nullCardD));
	}

}

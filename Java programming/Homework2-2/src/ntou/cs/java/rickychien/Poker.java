package ntou.cs.java.rickychien;

import java.util.Random;

import ntou.cs.java.rickychien.Card.CardType;

public class Poker {
	private final int NUMBER_OF_CARDS = 52;
	private final int NUMBER_OF_DEALS = 5;
	private int currentDealCard = 0;
	private Card[] deck;
	private Random random = new Random();

	/* Constructor fills deck of cards */
	public Poker() {
		deck = new Card[NUMBER_OF_CARDS];

		// Populate deck with Card objects
		for (int i = 0; i < deck.length; i++) {
			deck[i] = new Card(i % 13, i / 13);
		}
	}

	/* Suffling cards */
	public void suffleCard() {
		// For each Card, pick another random Card and swap them
		for (int first = 0; first < deck.length; ++first) {
			// Select a random number between 0 and 51
			int second = random.nextInt(NUMBER_OF_CARDS);

			// Swap current Card with a randomly select Card
			Card temp = deck[first];
			deck[first] = deck[second];
			deck[second] = temp;
		}
	}

	/* Deal a fix number cards to a player */
	public Card[] dealCard() {
		Card[] card = new Card[NUMBER_OF_DEALS];
		int end = currentDealCard;
		
		for (int i = 0; currentDealCard < end + NUMBER_OF_DEALS; ++currentDealCard, ++i) {
			card[i] = deck[currentDealCard];
		}
		
		sortSuits(card);
		sortFaces(card);
		
		return card;
	}

	/* Return cards type */
	public CardType getCardType(Card[] card) {
		// init
		int[] faceCounter = new int[13];
		int[] suitCounter = new int[4];
		int sameFace2 = 0;
		int sameFace3 = 0;
		int sameFace4 = 0;
		boolean flush = false;
		boolean straight = false;
		int i, j;

		for (i = 0; i < card.length; ++i) {
			++faceCounter[card[i].getFace()];
			++suitCounter[card[i].getSuit()];
		}

		// Check straight in deck
		for (i = 0; i < 10; ++i) {
			for (j = i; j < i + 5; ++j) {
				if (faceCounter[j] == 0) {
					break;
				}
			}
			if (j == i + 5) {
				straight = true;
				break;
			}
		}

		// Check same face in deck
		for (i = 0; i < faceCounter.length; ++i) {
			if (faceCounter[i] == 2) {
				++sameFace2;
			}
			if (faceCounter[i] == 3) {
				++sameFace3;
			}
			if (faceCounter[i] == 4) {
				++sameFace4;
			}
		}

		// Check flush in deck
		for (i = 0; i < suitCounter.length; ++i) {
			if (suitCounter[i] == 5) {
				flush = true;
				break;
			}
		}

		// Through the counters to determine card type
		if (straight && flush) {
			return Card.CardType.STRAIGHT_FLUSH;
		} else if (sameFace4 == 1) {
			return Card.CardType.FOUR_OF_KIND;
		} else if (sameFace2 == 1 && sameFace3 == 1) {
			return Card.CardType.FULL_HOUSE;
		} else if (flush) {
			return Card.CardType.FLUSH;
		} else if (straight) {
			return Card.CardType.STRAIGHT;
		} else if (sameFace3 == 1) {
			return Card.CardType.THREE_OF_KIND;
		} else if (sameFace2 == 2) {
			return Card.CardType.TWO_PAIRS;
		} else if (sameFace2 == 1) {
			return Card.CardType.PAIRS;
		} else {
			return Card.CardType.NONE;
		}
	}

	/* Compare two decks, deck1 > deck2 return true, deck1 < deck2 return false */
	public boolean compareDecks(Card[] deck_1, Card[] deck_2) {
		int[] faceCounter1 = new int[13];
		int[] faceCounter2 = new int[13];
		int deck_1_face = 0;
		int deck_2_face = 0;
		Card.CardType deck_1_Type = this.getCardType(deck_1);
		Card.CardType deck_2_Type = this.getCardType(deck_2);
		
		for (int i = 0; i < deck_1.length; ++i) {
			++faceCounter1[deck_1[i].getFace()];
			++faceCounter2[deck_2[i].getFace()];
		}
		
		// Compare precedence (precedence 1 > precedence 2)
		if (deck_1_Type.ordinal() < deck_2_Type.ordinal()) {
			return true;
		} else if (deck_1_Type.ordinal() > deck_2_Type.ordinal()) {
			return false;
		}

		// Else deck_1_Type == deck_2_Type, compare faces

		if (deck_1_Type == Card.CardType.STRAIGHT_FLUSH
				|| deck_1_Type == Card.CardType.STRAIGHT
				|| deck_1_Type == Card.CardType.FLUSH
				|| deck_1_Type == Card.CardType.NONE) {

			if (deck_1[deck_1.length - 1].getFace() > deck_2[deck_2.length - 1]
					.getFace()) {
				return true;
			} else if (deck_1[deck_1.length - 1].getFace() < deck_2[deck_2.length - 1]
					.getFace()) {
				return false;
			} else {
				if (deck_1[deck_1.length - 1].getSuit() > deck_2[deck_2.length - 1]
						.getSuit()) {
					return true;
				} else {
					return false;
				}
			}
		} else if (deck_1_Type == Card.CardType.FOUR_OF_KIND
				|| deck_1_Type == Card.CardType.FULL_HOUSE
				|| deck_1_Type == Card.CardType.THREE_OF_KIND) {

			for (int i = 0; i < faceCounter1.length; ++i) {
				if (faceCounter1[i] >= 3) {
					deck_1_face = i;
				}
				if (faceCounter2[i] >= 3) {
					deck_2_face = i;
				}
			}

			if (deck_1_face > deck_2_face) {
				return true;
			} else {
				return false;
			}

		} else if (deck_1_Type == Card.CardType.TWO_PAIRS) {
			Card tempCard1 = new Card();
			Card tempCard2 = new Card();
			for (int i = 0; i < deck_1.length - 1; ++i) {
				if (deck_1[i].getFace() == deck_1[i + 1].getFace()) {
					tempCard1 = deck_1[i];
				}
				
				if (deck_2[i].getFace() == deck_2[i + 1].getFace()) {
					tempCard2 = deck_2[i];
				}
			}
			
			if (tempCard1.getFace() > tempCard2.getFace()) {
				return true;
			} else if (tempCard1.getFace() < tempCard2.getFace()) {
				return false;
			} else {
				if (tempCard1.getSuit() > tempCard2.getSuit()) {
					return true;
				} else {
					return false;
				}
			}
			
		} else {
			
		}

		return false;
	}

	public void sortFaces(Card[] deck) {
		for (int i = 0; i < deck.length - 1; i++) {
			for (int j = 0; j < deck.length - 1 - i; j++) {
				if (deck[j].getFace() > deck[j + 1].getFace()) {
					swapCard(deck[j], deck[j + 1]);
				}
			}
		}
	}

	public void sortSuits(Card[] deck) {
		for (int i = 0; i < deck.length - 1; i++) {
			for (int j = 0; j < deck.length - 1 - i; j++) {
				if (deck[j].getSuit() > deck[j + 1].getSuit()) {
					swapCard(deck[j], deck[j + 1]);
				}
			}
		}
	}

	public void swapCard(Card card1, Card card2) {
		Card temp = new Card();
		temp.setFace(card1.getFace());
		temp.setSuit(card1.getSuit());
		card1.setFace(card2.getFace());
		card1.setSuit(card2.getSuit());
		card2.setFace(temp.getFace());
		card2.setSuit(temp.getSuit());
	}
}

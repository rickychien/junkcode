package ntou.cs.java.rickychien;

public class Card {
	enum CardType {
		STRAIGHT_FLUSH, FOUR_OF_KIND, FULL_HOUSE, FLUSH, STRAIGHT, THREE_OF_KIND, TWO_PAIRS, PAIRS, NONE;

	};

	enum FaceType {
		ACE("Ace"), DUECE("Duece"), THREE("Three"), FOUR("Four"), FIVE("Ace"), SIX(
				"Six"), SEVEN("Seven"), EIGHT("Eight"), NIGHT("Night"), TEN(
				"Ten"), JACK("Jack"), QUEEN("Queen"), KING("King");

		private String faceName;

		FaceType(String face) {
			faceName = face;
		}

		public String toString() {
			return faceName;
		}
	};

	enum SuitType {
		CLUBS("Clubs"), DIAMONDS("Diamonds"), HEARTS("Hearts"), SPADES("Spades");
		
		private String suitName;

		SuitType(String suit) {
			suitName = suit;
		}

		public String toString() {
			return suitName;
		}
	};

	private int face;
	private int suit;

	public Card() {
		face = 0;
		suit = 0;
	}

	public Card(int cardFace, int cardSuit) {
		face = cardFace;
		suit = cardSuit;
	}

	public int getFace() {
		return face;
	}
	
	public void setFace(int face_) {
		face = face_;
	}

	public int getSuit() {
		return suit;
	}
	
	public void setSuit(int suit_) {
		suit = suit_;
	}

	public String toStirng() {
		String[] faceStr = { "Duece", "Three", "Four", "Five", "Six",
				"Seven", "Eight", "Night", "Ten", "Jack", "Queen", "King", "Ace" };
		String[] suitStr = { "Clubs", "Diamonds", "Hearts", "Spades" };

		return faceStr[face] + " of " + suitStr[suit];
	}
}

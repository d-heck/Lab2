package pokerBase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Locale;

import exceptions.DeckException;
import exceptions.HandException;
import pokerEnums.*;

import static java.lang.System.out;
import static java.lang.System.err;

public class Hand {

	private ArrayList<Card> CardsInHand;
	private ArrayList<Card> BestCardsInHand;
	private HandScore HandScore;
	private boolean bScored = false;

	public Hand() {
		CardsInHand = new ArrayList<Card>();
		BestCardsInHand = new ArrayList<Card>();
	}

	public ArrayList<Card> getCardsInHand() {
		return CardsInHand;
	}

	private void setCardsInHand(ArrayList<Card> cardsInHand) {
		CardsInHand = cardsInHand;
	}

	public ArrayList<Card> getBestCardsInHand() {
		return BestCardsInHand;
	}

	public void setBestCardsInHand(ArrayList<Card> bestCardsInHand) {
		BestCardsInHand = bestCardsInHand;
	}

	public HandScore getHandScore() {
		return HandScore;
	}

	public void setHandScore(HandScore handScore) {
		HandScore = handScore;
	}

	public boolean isbScored() {
		return bScored;
	}

	public void setbScored(boolean bScored) {
		this.bScored = bScored;
	}

	public Hand AddCardToHand(Card c) {
		CardsInHand.add(c);
		return this;
	}

	public Hand Draw(Deck d) throws DeckException {
		CardsInHand.add(d.Draw());
		return this;
	}

	/**
	 * EvaluateHand is a static method that will score a given Hand of cards
	 * 
	 * @param h
	 * @return
	 * @throws HandException 
	 */
	public static Hand EvaluateHand(Hand h) throws HandException {

		Collections.sort(h.getCardsInHand());

		//Collections.sort(h.getCardsInHand(), Card.CardRank);

		if (h.getCardsInHand().size() != 5) {
			throw new HandException(h);
		}

		HandScore hs = new HandScore();
		try {
			Class<?> c = Class.forName("pokerBase.Hand");

			for (eHandStrength hstr : eHandStrength.values()) {
				Class[] cArg = new Class[2];
				cArg[0] = pokerBase.Hand.class;
				cArg[1] = pokerBase.HandScore.class;

				Method meth = c.getMethod(hstr.getEvalMethod(), cArg);
				Object o = meth.invoke(null, new Object[] { h, hs });

				// If o = true, that means the hand evaluated- skip the rest of
				// the evaluations
				if ((Boolean) o) {
					break;
				}
			}

			h.bScored = true;
			h.HandScore = hs;

		} catch (ClassNotFoundException x) {
			x.printStackTrace();
		} catch (IllegalAccessException x) {
			x.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return h;
	}
	
	private static boolean isHandFlush(ArrayList<Card> cards) {
		int count = 0;
		boolean Flush = false;
		for (eSuit Suit : eSuit.values()) {
			count = 0;
			for (Card C : cards) {
				if (C.geteSuit() == Suit) {
					count++;
				}
			}
			if (count == 5)
				Flush = true;
		}
		return Flush;
	}
	
	private static boolean isStraight(ArrayList<Card> Cards, Card High) {
		
			boolean straight = false;
			boolean ace = false;

			int initial = 0;
			High.seteRank(Cards.get(eCardNo.FirstCard.getCardNo()).geteRank());
			High.seteSuit(Cards.get(eCardNo.FirstCard.getCardNo()).geteSuit());

			if (Cards.get(eCardNo.FirstCard.getCardNo()).geteRank() == eRank.ACE) {
				ace = true;
				initial++;
			}
			
			for (int i = initial; i < Cards.size() - 1; i++) {
				if ((Cards.get(i).geteRank().getiRankNbr() - Cards.get(i + 1).geteRank().getiRankNbr()) == 1) {
					straight = true;
				} 
				else {
					straight = false;
					break;
				}
			}
			if ((ace == true) && (straight == true)) {
				if (Cards.get(eCardNo.SecondCard.getCardNo()).geteRank() == eRank.KING) {
					High.seteRank(Cards.get(eCardNo.FirstCard.getCardNo()).geteRank());
					High.seteSuit(Cards.get(eCardNo.FirstCard.getCardNo()).geteSuit());
				}
				if (Cards.get(eCardNo.SecondCard.getCardNo()).geteRank() == eRank.FIVE) {
					High.seteRank(Cards.get(eCardNo.SecondCard.getCardNo()).geteRank());
					High.seteSuit(Cards.get(eCardNo.SecondCard.getCardNo()).geteSuit());
				} 
				else {
					straight = false;
				}
			}
			return straight;
		}

		public static boolean isHandFiveOfAKind(Hand hand, HandScore handScore) {

			int count = 0;
			boolean five = false;

			for (int i = 0; i < eRank.values().length; i++) {
				for (int k = 0; k < hand.getCardsInHand().size(); k++) {
					if (hand.getCardsInHand().get(k).geteRank() == eRank.values()[i]) {
						count++;
					}
				}
				if (count == 5) {
					five = true;
					break;
				}
			}
			if (five == true) {
				handScore.setHandStrength(eHandStrength.FiveOfAKind.getHandStrength());
				handScore.setHiHand(hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
				handScore.setLoHand(0);
			}
			return five;
		}

		public static boolean isHandRoyalFlush(Hand hand, HandScore handScore) {

			Card card = new Card();
			boolean royalflush = false;
			if ((isHandFlush(hand.getCardsInHand())) && (isStraight(hand.getCardsInHand(), card))) {
				if (card.geteRank() == eRank.ACE) {
					royalflush = true;
					handScore.setHandStrength(eHandStrength.RoyalFlush.getHandStrength());
					handScore.setHiHand(hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
					handScore.setLoHand(0);
				}
			}
			return royalflush;
		}

		public static boolean isHandStraightFlush(Hand hand, HandScore handScore) {
			Card card = new Card();
			boolean royalflush = false;
			if ((isHandFlush(hand.getCardsInHand())) && (isStraight(hand.getCardsInHand(), card))) {
				royalflush = true;
				handScore.setHandStrength(eHandStrength.StraightFlush.getHandStrength());
				handScore.setHiHand(hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
				handScore.setLoHand(0);
			}
			return royalflush;
		}

		public static boolean isHandFourOfAKind(Hand hand, HandScore handScore) {

			boolean check = false;

			if (hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == hand.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank()) {
				check = true;
				handScore.setHandStrength(eHandStrength.FourOfAKind.getHandStrength());
				handScore.setHiHand(hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
				handScore.setLoHand(0);
				ArrayList<Card> kickers = new ArrayList<Card>();
				kickers.add(hand.getCardsInHand().get(eCardNo.FifthCard.getCardNo()));
				handScore.setKickers(kickers);
			} 
			else if (hand.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == hand.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank()) {
				check = true;
				handScore.setHandStrength(eHandStrength.FourOfAKind.getHandStrength());
				handScore.setHiHand(hand.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank().getiRankNbr());
				handScore.setLoHand(0);
				ArrayList<Card> kickers = new ArrayList<Card>();
				kickers.add(hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()));
				handScore.setKickers(kickers);
			}
			return check;
		}

		public static boolean isHandFullHouse(Hand hand, HandScore handScore) {

			boolean check = false;
			if ((hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == hand.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank())&& (hand.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == hand.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank())) {
				check = true;
				handScore.setHandStrength(eHandStrength.FullHouse.getHandStrength());
				handScore.setHiHand(hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
				handScore.setLoHand(hand.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank().getiRankNbr());
			} 
			else if ((hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == hand.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank())&& (hand.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == hand.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank())) {
				check = true;
				handScore.setHandStrength(eHandStrength.FullHouse.getHandStrength());
				handScore.setHiHand(hand.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank().getiRankNbr());
				handScore.setLoHand(hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			}
			return check;
		}

		public static boolean isHandFlush(Hand hand, HandScore handScore) {

			boolean check = false;
			if (isHandFlush(hand.getCardsInHand())) {
				handScore.setHandStrength(eHandStrength.Flush.getHandStrength());
				handScore.setHiHand(hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
				handScore.setLoHand(0);
				ArrayList<Card> kicker = new ArrayList<Card>();
				kicker.add(hand.getCardsInHand().get(eCardNo.SecondCard.getCardNo()));
				kicker.add(hand.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()));
				kicker.add(hand.getCardsInHand().get(eCardNo.FourthCard.getCardNo()));
				kicker.add(hand.getCardsInHand().get(eCardNo.FifthCard.getCardNo()));
				handScore.setKickers(kicker);
				check = true;
			}
			return check;
		}

		public static boolean isHandStraight(Hand h, HandScore hs) {

			boolean bIsStraight = false;
			Card highCard = new Card();
			if (isStraight(h.getCardsInHand(), highCard)) {
				hs.setHandStrength(eHandStrength.Straight.getHandStrength());
				hs.setHiHand(highCard.geteRank().getiRankNbr());
				hs.setLoHand(0);
				bIsStraight = true;
			}
			return bIsStraight;
		}

		public static boolean isHandThreeOfAKind(Hand hand, HandScore handScore) {

			boolean check = false;
			ArrayList<Card> kicker = new ArrayList<Card>();
			if (hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == hand.getCardsInHand()
					.get(eCardNo.ThirdCard.getCardNo()).geteRank()) {
				check = true;
				handScore.setHiHand(hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
				kicker.add(hand.getCardsInHand().get(eCardNo.FourthCard.getCardNo()));
				kicker.add(hand.getCardsInHand().get(eCardNo.FifthCard.getCardNo()));
			} 
			else if (hand.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == hand.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank()) {
				check = true;
				handScore.setHiHand(hand.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank().getiRankNbr());
				kicker.add(hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()));
				kicker.add(hand.getCardsInHand().get(eCardNo.FifthCard.getCardNo()));
			} 
			else if (hand.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == hand.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank()) {
				check = true;
				handScore.setHiHand(hand.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank().getiRankNbr());
				kicker.add(hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()));
				kicker.add(hand.getCardsInHand().get(eCardNo.SecondCard.getCardNo()));
			}
			if (check) {
				handScore.setHandStrength(eHandStrength.ThreeOfAKind.getHandStrength());
				handScore.setLoHand(0);
				handScore.setKickers(kicker);
			}
			return check;
		}

		public static boolean isHandTwoPair(Hand hand, HandScore handScore) {

			boolean check = false;
			ArrayList<Card> kicker = new ArrayList<Card>();
			if ((hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == hand.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank())&& (hand.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == hand.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank())) {
				check = true;
				handScore.setHandStrength(eHandStrength.TwoPair.getHandStrength());
				handScore.setHiHand(hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
				handScore.setLoHand(hand.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank().getiRankNbr());
				kicker.add(hand.getCardsInHand().get((eCardNo.FifthCard.getCardNo())));
				handScore.setKickers(kicker);
			} 
			else if ((hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == hand.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank())&& (hand.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == hand.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank())) {
				check = true;
				handScore.setHandStrength(eHandStrength.TwoPair.getHandStrength());
				handScore.setHiHand(hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
				handScore.setLoHand(hand.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank().getiRankNbr());
				kicker.add(hand.getCardsInHand().get((eCardNo.ThirdCard.getCardNo())));
				handScore.setKickers(kicker);
			} 
			else if ((hand.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == hand.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank())&& (hand.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == hand.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank())) {
				check = true;
				handScore.setHandStrength(eHandStrength.TwoPair.getHandStrength());
				handScore.setHiHand(hand.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank().getiRankNbr());
				handScore.setLoHand(hand.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank().getiRankNbr());
				kicker.add(hand.getCardsInHand().get((eCardNo.FirstCard.getCardNo())));
				handScore.setKickers(kicker);
			}
			return check;
		}

		public static boolean isHandPair(Hand hand, HandScore handScore) {
			boolean check = false;
			ArrayList<Card> kicker = new ArrayList<Card>();
			if (hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank() == hand.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank()) {
				check = true;
				handScore.setHandStrength(eHandStrength.Pair.getHandStrength());
				handScore.setHiHand(hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
				handScore.setLoHand(0);
				kicker.add(hand.getCardsInHand().get((eCardNo.ThirdCard.getCardNo())));
				kicker.add(hand.getCardsInHand().get((eCardNo.FourthCard.getCardNo())));
				kicker.add(hand.getCardsInHand().get((eCardNo.FifthCard.getCardNo())));
				handScore.setKickers(kicker);
			} 
			else if (hand.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank() == hand.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank()) {
				check = true;
				handScore.setHandStrength(eHandStrength.Pair.getHandStrength());
				handScore.setHiHand(hand.getCardsInHand().get(eCardNo.SecondCard.getCardNo()).geteRank().getiRankNbr());
				handScore.setLoHand(0);
				kicker.add(hand.getCardsInHand().get((eCardNo.FirstCard.getCardNo())));
				kicker.add(hand.getCardsInHand().get((eCardNo.FourthCard.getCardNo())));
				kicker.add(hand.getCardsInHand().get((eCardNo.FifthCard.getCardNo())));
				handScore.setKickers(kicker);
			} 
			else if (hand.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank() == hand.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank()) {
				check = true;
				handScore.setHandStrength(eHandStrength.Pair.getHandStrength());
				handScore.setHiHand(hand.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()).geteRank().getiRankNbr());
				handScore.setLoHand(0);
				kicker.add(hand.getCardsInHand().get((eCardNo.FirstCard.getCardNo())));
				kicker.add(hand.getCardsInHand().get((eCardNo.SecondCard.getCardNo())));
				kicker.add(hand.getCardsInHand().get((eCardNo.FifthCard.getCardNo())));
				handScore.setKickers(kicker);
			} 
			else if (hand.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank() == hand.getCardsInHand().get(eCardNo.FifthCard.getCardNo()).geteRank()) {
				check = true;
				handScore.setHandStrength(eHandStrength.Pair.getHandStrength());
				handScore.setHiHand(hand.getCardsInHand().get(eCardNo.FourthCard.getCardNo()).geteRank().getiRankNbr());
				handScore.setLoHand(0);
				kicker.add(hand.getCardsInHand().get((eCardNo.FirstCard.getCardNo())));
				kicker.add(hand.getCardsInHand().get((eCardNo.SecondCard.getCardNo())));
				kicker.add(hand.getCardsInHand().get((eCardNo.ThirdCard.getCardNo())));
				handScore.setKickers(kicker);
			}
			return check;
		}

		public static boolean isHandHighCard(Hand hand, HandScore handScore) {
			handScore.setHandStrength(eHandStrength.HighCard.getHandStrength());
			handScore.setHiHand(hand.getCardsInHand().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr());
			handScore.setLoHand(0);
			ArrayList<Card> kicker = new ArrayList<Card>();
			kicker.add(hand.getCardsInHand().get(eCardNo.SecondCard.getCardNo()));
			kicker.add(hand.getCardsInHand().get(eCardNo.ThirdCard.getCardNo()));
			kicker.add(hand.getCardsInHand().get(eCardNo.FourthCard.getCardNo()));
			kicker.add(hand.getCardsInHand().get(eCardNo.FifthCard.getCardNo()));
			handScore.setKickers(kicker);
			return true;
		}

		public static Comparator<Hand> HandRank = new Comparator<Hand>() {

			public int compare(Hand h1, Hand h2) {

				int result = 0;
				result = h2.getHandScore().getHandStrength() - h1.getHandScore().getHandStrength();
				if (result != 0) {
					return result;
				}
				result = h2.getHandScore().getHiHand() - h1.getHandScore().getHiHand();
				if (result != 0) {
					return result;
				}
				result = h2.getHandScore().getLoHand() - h1.getHandScore().getLoHand();
				if (result != 0) {
					return result;
				}
				if (h2.getHandScore().getKickers().size() > 0) {
					if (h1.getHandScore().getKickers().size() > 0) {
						result = h2.getHandScore().getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr()- h1.getHandScore().getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank().getiRankNbr();
					}
					if (result != 0) {
						return result;
					}
				}
				if (h2.getHandScore().getKickers().size() > 1) {
					if (h1.getHandScore().getKickers().size() > 1) {
						result = h2.getHandScore().getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank().getiRankNbr()- h1.getHandScore().getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank().getiRankNbr();
					}
					if (result != 0) {
						return result;
					}
				}
				if (h2.getHandScore().getKickers().size() > 2) {
					if (h1.getHandScore().getKickers().size() > 2) {
						result = h2.getHandScore().getKickers().get(eCardNo.ThirdCard.getCardNo()).geteRank().getiRankNbr()- h1.getHandScore().getKickers().get(eCardNo.ThirdCard.getCardNo()).geteRank().getiRankNbr();
					}
					if (result != 0) {
						return result;
					}
				}
				if (h2.getHandScore().getKickers().size() > 3) {
					if (h1.getHandScore().getKickers().size() > 3) {
						result = h2.getHandScore().getKickers().get(eCardNo.FourthCard.getCardNo()).geteRank().getiRankNbr()- h1.getHandScore().getKickers().get(eCardNo.FourthCard.getCardNo()).geteRank().getiRankNbr();
					}
					if (result != 0) {
						return result;
					}
				}
				return 0;
			}
		};
}
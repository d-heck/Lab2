package pokerBase;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import exceptions.DeckException;
import exceptions.HandException;
import pokerEnums.eCardNo;
import pokerEnums.eHandStrength;
import pokerEnums.eRank;
import pokerEnums.eSuit;

public class HandTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	private Hand SetHand(ArrayList<Card> setCards, Hand h)
	{
		Object t = null;
		
		try {
			//	Load the Class into 'c'
			Class<?> c = Class.forName("pokerBase.Hand");
			//	Create a new instance 't' from the no-arg Deck constructor
			t = c.newInstance();
			//	Load 'msetCardsInHand' with the 'Draw' method (no args);
			Method msetCardsInHand = c.getDeclaredMethod("setCardsInHand", new Class[]{ArrayList.class});
			//	Change the visibility of 'setCardsInHand' to true *Good Grief!*
			msetCardsInHand.setAccessible(true);
			//	invoke 'msetCardsInHand'
			Object oDraw = msetCardsInHand.invoke(t, setCards);
			
			
		} catch (ClassNotFoundException x) {
			x.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		h = (Hand)t;
		return h;
		
	}
	/**
	 * This test checks to see if a HandException is throw if the hand only has two cards.
	 * @throws Exception
	 */
	@Test(expected = HandException.class)
	public void TestEvalShortHand() throws Exception {
		
		Hand h = new Hand();
		
		ArrayList<Card> ShortHand = new ArrayList<Card>();
		ShortHand.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		ShortHand.add(new Card(eSuit.CLUBS,eRank.ACE,0));

		h = SetHand(ShortHand,h);
		
		//	This statement should throw a HandException
		h = Hand.EvaluateHand(h);	
	}	
			
	@Test
	public void TestFourOfAKind1() {
		
		HandScore hs = new HandScore();
		ArrayList<Card> FourOfAKind = new ArrayList<Card>();
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));		
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.KING,0));
		Collections.sort(FourOfAKind);
		Hand h = new Hand();
		h = SetHand(FourOfAKind,h);
		
		boolean bActualIsHandFourOfAKind = Hand.isHandFourOfAKind(h, hs);
		boolean bExpectedIsHandFourOfAKind = true;
		
		//	Did this evaluate to Four of a Kind?
		assertEquals(bActualIsHandFourOfAKind,bExpectedIsHandFourOfAKind);		
		//	Was the four of a kind an Ace?
		assertEquals(hs.getHiHand(),eRank.ACE.getiRankNbr());		
		//	FOAK has one kicker.  Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		//	FOAK has one kicker.  Was it a King?		
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.KING);
	}
	
	@Test
	public void TestFourOfAKindEval() {
		
		HandScore hs = new HandScore();
		ArrayList<Card> FourOfAKind = new ArrayList<Card>();
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));		
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.KING,0));
		Collections.sort(FourOfAKind);
		Hand h = new Hand();
		h = SetHand(FourOfAKind,h);
		
		try {
			h = Hand.EvaluateHand(h);
		} catch (HandException e) {			
			e.printStackTrace();
			fail("TestFourOfAKindEval failed");
		}
		
		boolean bActualIsHandFourOfAKind = Hand.isHandFourOfAKind(h, hs);
		boolean bExpectedIsHandFourOfAKind = true;
		
		//	Did this evaluate to Four of a Kind?
		assertEquals(bActualIsHandFourOfAKind,bExpectedIsHandFourOfAKind);		
		//	Was the four of a kind an Ace?
		assertEquals(hs.getHiHand(),eRank.ACE.getiRankNbr());		
		//	FOAK has one kicker.  Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		//	FOAK has one kicker.  Was it a King?		
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.KING);
	}

	@Test
	public void TestFourOfAKind2() {
		
		HandScore hs = new HandScore();
		ArrayList<Card> FourOfAKind = new ArrayList<Card>();
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.KING,0));
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.KING,0));
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.KING,0));		
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.KING,0));
		FourOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		Collections.sort(FourOfAKind);
		Hand h = new Hand();
		h = SetHand(FourOfAKind,h);
		
		boolean bActualIsHandFourOfAKind = Hand.isHandFourOfAKind(h, hs);
		boolean bExpectedIsHandFourOfAKind = true;
		
		//	Did this evaluate to Four of a Kind?
		assertEquals(bActualIsHandFourOfAKind,bExpectedIsHandFourOfAKind);		
		//	Was the four of a kind an Ace?
		assertEquals(hs.getHiHand(),eRank.KING.getiRankNbr());		
		//	FOAK has one kicker.  Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		//	FOAK has one kicker.  Was it a King?		
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.ACE);
	}
	
	@Test
	public void TestThreeOfAKind1(){
		ArrayList<Card> ThreeOfAKind = new ArrayList<Card>();
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.JACK, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		Collections.sort(ThreeOfAKind);
		Hand h = new Hand();
		h = SetHand(ThreeOfAKind, h);
		HandScore hs = new HandScore();
		
		boolean bActualIsHandThreeOfAKind = Hand.isHandThreeOfAKind(h, hs);
		boolean bExpectedIsHandThreeOfAKind = true;


		assertEquals(bActualIsHandThreeOfAKind, bExpectedIsHandThreeOfAKind);

		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());

		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);

		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.KING);

		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);

		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.JACK);
	}
	
	@Test
	public void TestThreeOfAKind2(){
		ArrayList<Card> ThreeOfAKind = new ArrayList<Card>();
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.JACK, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		Collections.sort(ThreeOfAKind);
		Hand h = new Hand();
		h = SetHand(ThreeOfAKind, h);
		HandScore hs = new HandScore();
		
		boolean bActualIsHandThreeOfAKind = Hand.isHandThreeOfAKind(h, hs);
		boolean bExpectedIsHandThreeOfAKind = true;


		assertEquals(bActualIsHandThreeOfAKind, bExpectedIsHandThreeOfAKind);

		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());

		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);

		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.KING);

		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);

		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.JACK);
	}
	
	@Test
	public void TestThreeOfAKind3(){
		ArrayList<Card> ThreeOfAKind = new ArrayList<Card>();
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.JACK, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		Collections.sort(ThreeOfAKind);
		Hand h = new Hand();
		h = SetHand(ThreeOfAKind, h);
		HandScore hs = new HandScore();
		
		boolean bActualIsHandThreeOfAKind = Hand.isHandThreeOfAKind(h, hs);
		boolean bExpectedIsHandThreeOfAKind = true;

		// Did this evaluate to Three of a Kind?
		assertEquals(bActualIsHandThreeOfAKind, bExpectedIsHandThreeOfAKind);
		// Was the three of a kind an ACE?
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());
		// Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// Was it a King?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.KING);
		// Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// Was it a Jack?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.JACK);
	}
	
	@Test
	public void TestThreeOfAKindEval() {

		ArrayList<Card> ThreeOfAKind = new ArrayList<Card>();
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		ThreeOfAKind.add(new Card(eSuit.CLUBS, eRank.JACK, 0));

		Hand h = new Hand();
		h = SetHand(ThreeOfAKind, h);

		try {
			h = Hand.EvaluateHand(h);
		} catch (HandException e) {
			e.printStackTrace();
			fail("TestThreeOfAKindEval failed");
		}
		HandScore hs = h.getHandScore();

		boolean bActualHandIsThreeOfAKind = Hand.isHandThreeOfAKind(h, hs);
		boolean bExpectedIsHandThreeOfAKind = true;
		
		assertEquals(bActualHandIsThreeOfAKind, bExpectedIsHandThreeOfAKind);
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.KING);
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.JACK);
	}
	
	@Test
	public void TestFiveOfAKind() {
		
		HandScore hs = new HandScore();
		ArrayList<Card> FiveOfAKind = new ArrayList<Card>();
		FiveOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		FiveOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		FiveOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));		
		FiveOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		FiveOfAKind.add(new Card(eSuit.CLUBS,eRank.ACE,0));
		
		Hand h = new Hand();
		h = SetHand(FiveOfAKind,h);
		
		boolean bActualIsHandFivefAKind = Hand.isHandFiveOfAKind(h, hs);
		boolean bExpectedIsHandFiveOfAKind = true;
		
		//	Did this evaluate to Five of a Kind?
		assertEquals(bActualIsHandFivefAKind,bExpectedIsHandFiveOfAKind);		
		//	Was the four of a kind an Ace?
		assertEquals(hs.getHiHand(),eRank.ACE.getiRankNbr());		
	}
	
	@Test
	public void TestFullHouse(){
		ArrayList<Card> FullHouse = new ArrayList<Card>();
		FullHouse.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		FullHouse.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		FullHouse.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		FullHouse.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		FullHouse.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		Collections.sort(FullHouse);
		Hand h = new Hand();
		h = SetHand(FullHouse, h);
		HandScore hs = new HandScore();
		
		boolean bActualIsHandFullHouse = Hand.isHandThreeOfAKind(h, hs);
		boolean bExpectedIsHandFullHouse = true;

		// Did this evaluate to Three of a Kind?
		assertEquals(bActualIsHandFullHouse, bExpectedIsHandFullHouse);
		//High Card Check
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());
		//Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		//Was it a King?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.KING);
		//Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// Was it a King?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.KING);
	}
	
	@Test
	public void TestFullHouse2(){
		ArrayList<Card> FullHouse = new ArrayList<Card>();
		FullHouse.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		FullHouse.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		FullHouse.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		FullHouse.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		FullHouse.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		Collections.sort(FullHouse);
		Hand h = new Hand();
		h = SetHand(FullHouse, h);
		HandScore hs = new HandScore();
		
		boolean bActualIsHandFullHouse = Hand.isHandThreeOfAKind(h, hs);
		boolean bExpectedIsHandFullHouse = true;

		// Did this evaluate to Three of a Kind?
		assertEquals(bActualIsHandFullHouse, bExpectedIsHandFullHouse);
		//High Card Check
		assertEquals(hs.getHiHand(), eRank.KING.getiRankNbr());
		//Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		//Was it a ACE?
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.ACE);
		//Was it a Club?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		// Was it a ACE?
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.ACE);
	}
	
	@Test
	public void TestFlush(){
		ArrayList<Card> Flush = new ArrayList<Card>();
		Flush.add(new Card(eSuit.CLUBS, eRank.FOUR, 0));
		Flush.add(new Card(eSuit.CLUBS, eRank.SIX, 0));
		Flush.add(new Card(eSuit.CLUBS, eRank.SEVEN, 0));
		Flush.add(new Card(eSuit.CLUBS, eRank.JACK, 0));
		Flush.add(new Card(eSuit.CLUBS, eRank.QUEEN, 0));
		Collections.sort(Flush);
		Hand h = new Hand();
		h = SetHand(Flush, h);
		HandScore hs = new HandScore();
		
		boolean bActualIsHandFlush = Hand.isHandFlush(h, hs);
		boolean bExpectedIsHandFlush = true;


		assertEquals(bActualIsHandFlush, bExpectedIsHandFlush);
		assertEquals(hs.getHiHand(), eRank.QUEEN.getiRankNbr());
	}	
	
	@Test
	public void TestStraight(){
		ArrayList<Card> Straight = new ArrayList<Card>();
		Straight.add(new Card(eSuit.CLUBS, eRank.FIVE, 0));
		Straight.add(new Card(eSuit.CLUBS, eRank.SIX, 0));
		Straight.add(new Card(eSuit.CLUBS, eRank.SEVEN, 0));
		Straight.add(new Card(eSuit.CLUBS, eRank.EIGHT, 0));
		Straight.add(new Card(eSuit.CLUBS, eRank.NINE, 0));
		Collections.sort(Straight);
		
		Hand h = new Hand();
		h = SetHand(Straight, h);
		HandScore hs = new HandScore();
		
		boolean bActualIsHandStraight = Hand.isHandStraight(h, hs);
		boolean bExpectedIsHandStraight = true;


		assertEquals(bActualIsHandStraight, bExpectedIsHandStraight);
		assertEquals(hs.getHiHand(), eRank.NINE.getiRankNbr());
		}
	
	@Test
	public void TestStraightFlush(){
		ArrayList<Card> StraightFlush = new ArrayList<Card>();
		StraightFlush.add(new Card(eSuit.CLUBS, eRank.TWO, 0));
		StraightFlush.add(new Card(eSuit.CLUBS, eRank.THREE, 0));
		StraightFlush.add(new Card(eSuit.CLUBS, eRank.FOUR, 0));
		StraightFlush.add(new Card(eSuit.CLUBS, eRank.FIVE, 0));
		StraightFlush.add(new Card(eSuit.CLUBS, eRank.SIX, 0));
		Collections.sort(StraightFlush);
		
		Hand h = new Hand();
		h = SetHand(StraightFlush, h);
		HandScore hs = new HandScore();
		
		boolean bActualIsHandStraightFlush = Hand.isHandStraight(h, hs);
		boolean bExpectedIsHandStraightFlush = true;


		assertEquals(bActualIsHandStraightFlush, bExpectedIsHandStraightFlush);
		assertEquals(hs.getHiHand(), eRank.SIX.getiRankNbr());
	}
	
	@Test
	public void TestRoyalFlush(){
		ArrayList<Card> RoyalFlush = new ArrayList<Card>();
		RoyalFlush.add(new Card(eSuit.CLUBS, eRank.TEN, 0));
		RoyalFlush.add(new Card(eSuit.CLUBS, eRank.QUEEN, 0));
		RoyalFlush.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		RoyalFlush.add(new Card(eSuit.CLUBS, eRank.JACK, 0));
		RoyalFlush.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		Collections.sort(RoyalFlush);
		
		Hand h = new Hand();
		h = SetHand(RoyalFlush, h);
		HandScore hs = new HandScore();
		
		boolean bActualIsHandRoyalFlush = Hand.isHandRoyalFlush(h, hs);
		boolean bExpectedIsHandRoyalFlush = true;
		
		assertEquals(bActualIsHandRoyalFlush, bExpectedIsHandRoyalFlush);
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());
		
	}
	
	@Test
	public void TestTwoPairs1(){
		ArrayList<Card> TwoPairs = new ArrayList<Card>();
		TwoPairs.add(new Card(eSuit.CLUBS, eRank.JACK, 0));
		TwoPairs.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		TwoPairs.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		TwoPairs.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		TwoPairs.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		Collections.sort(TwoPairs);
		Hand h = new Hand();
		h = SetHand(TwoPairs, h);
		HandScore hs = new HandScore();
		
		boolean bActualIsHandTwoPairs = Hand.isHandTwoPair(h, hs);
		boolean bExpectedIsHandTwoPairs = true;

		assertEquals(bActualIsHandTwoPairs, bExpectedIsHandTwoPairs);
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.JACK);
	}
	
	@Test
	public void TestTwoPairs2(){
		ArrayList<Card> TwoPairs = new ArrayList<Card>();
		TwoPairs.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		TwoPairs.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		TwoPairs.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		TwoPairs.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		TwoPairs.add(new Card(eSuit.CLUBS, eRank.JACK, 0));
		Collections.sort(TwoPairs);
		Hand h = new Hand();
		h = SetHand(TwoPairs, h);
		HandScore hs = new HandScore();
		
		boolean bActualIsHandTwoPairs = Hand.isHandTwoPair(h, hs);
		boolean bExpectedIsHandTwoPairs = true;

		assertEquals(bActualIsHandTwoPairs, bExpectedIsHandTwoPairs);
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.JACK);
	}
	
	@Test
	public void TestTwoPairs3(){
		ArrayList<Card> TwoPairs = new ArrayList<Card>();
		TwoPairs.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		TwoPairs.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		TwoPairs.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		TwoPairs.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		TwoPairs.add(new Card(eSuit.CLUBS, eRank.JACK, 0));
		Collections.sort(TwoPairs);
		Hand h = new Hand();
		h = SetHand(TwoPairs, h);
		HandScore hs = new HandScore();
		
		boolean bActualIsHandTwoPairs = Hand.isHandTwoPair(h, hs);
		boolean bExpectedIsHandTwoPairs = true;

		assertEquals(bActualIsHandTwoPairs, bExpectedIsHandTwoPairs);
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.JACK);
	}
	
	@Test
	public void TestTwoPairs4(){
		ArrayList<Card> TwoPairs = new ArrayList<Card>();
		TwoPairs.add(new Card(eSuit.DIAMONDS, eRank.QUEEN, 0));
		TwoPairs.add(new Card(eSuit.CLUBS, eRank.QUEEN, 0));
		TwoPairs.add(new Card(eSuit.CLUBS, eRank.JACK, 0));
		TwoPairs.add(new Card(eSuit.DIAMONDS, eRank.KING, 0));
		TwoPairs.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		Collections.sort(TwoPairs);
		Hand h = new Hand();
		h = SetHand(TwoPairs, h);
		HandScore hs = new HandScore();
		
		boolean bActualIsHandTwoPairs = Hand.isHandTwoPair(h, hs);
		boolean bExpectedIsHandTwoPairs = true;

		assertEquals(bActualIsHandTwoPairs, bExpectedIsHandTwoPairs);
		assertEquals(hs.getHiHand(), eRank.KING.getiRankNbr());
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.JACK);
	}
	
	@Test
	public void TestPair1(){
		ArrayList<Card> Pair = new ArrayList<Card>();
		Pair.add(new Card(eSuit.CLUBS, eRank.TWO, 0));
		Pair.add(new Card(eSuit.DIAMONDS, eRank.TWO, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.THREE, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		Collections.sort(Pair);
		Hand h = new Hand();
		h = SetHand(Pair, h);
		HandScore hs = new HandScore();
		
		boolean bActualIsHandPair = Hand.isHandPair(h, hs);
		boolean bExpectedIsHandPair = true;

		assertEquals(bActualIsHandPair, bExpectedIsHandPair);
		assertEquals(hs.getHiHand(), eRank.TWO.getiRankNbr());
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.ACE);
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.KING);
		assertEquals(hs.getKickers().get(eCardNo.ThirdCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.ThirdCard.getCardNo()).geteRank(), eRank.THREE);
	}
	
	@Test
	public void TestPair2(){
		ArrayList<Card> Pair = new ArrayList<Card>();
		Pair.add(new Card(eSuit.CLUBS, eRank.THREE, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.TWO, 0));
		Pair.add(new Card(eSuit.DIAMONDS, eRank.TWO, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		Collections.sort(Pair);
		Hand h = new Hand();
		h = SetHand(Pair, h);
		HandScore hs = new HandScore();
		
		boolean bActualIsHandPair = Hand.isHandPair(h, hs);
		boolean bExpectedIsHandPair = true;

		assertEquals(bActualIsHandPair, bExpectedIsHandPair);
		assertEquals(hs.getHiHand(), eRank.TWO.getiRankNbr());
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.ACE);
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.KING);
		assertEquals(hs.getKickers().get(eCardNo.ThirdCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.ThirdCard.getCardNo()).geteRank(), eRank.THREE);
	}
	
	@Test
	public void TestPair3(){
		ArrayList<Card> Pair = new ArrayList<Card>();
		Pair.add(new Card(eSuit.CLUBS, eRank.THREE, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.TWO, 0));
		Pair.add(new Card(eSuit.DIAMONDS, eRank.TWO, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		Collections.sort(Pair);
		Hand h = new Hand();
		h = SetHand(Pair, h);
		HandScore hs = new HandScore();
		
		boolean bActualIsHandPair = Hand.isHandPair(h, hs);
		boolean bExpectedIsHandPair = true;

		assertEquals(bActualIsHandPair, bExpectedIsHandPair);
		assertEquals(hs.getHiHand(), eRank.TWO.getiRankNbr());
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.ACE);
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.KING);
		assertEquals(hs.getKickers().get(eCardNo.ThirdCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.ThirdCard.getCardNo()).geteRank(), eRank.THREE);
	}
	
	@Test
	public void TestPair4(){
		ArrayList<Card> Pair = new ArrayList<Card>();
		Pair.add(new Card(eSuit.CLUBS, eRank.THREE, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.TWO, 0));
		Pair.add(new Card(eSuit.DIAMONDS, eRank.TWO, 0));
		Collections.sort(Pair);
		Hand h = new Hand();
		h = SetHand(Pair, h);
		HandScore hs = new HandScore();
		
		boolean bActualIsHandPair = Hand.isHandPair(h, hs);
		boolean bExpectedIsHandPair = true;

		assertEquals(bActualIsHandPair, bExpectedIsHandPair);
		assertEquals(hs.getHiHand(), eRank.TWO.getiRankNbr());
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.ACE);
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.KING);
		assertEquals(hs.getKickers().get(eCardNo.ThirdCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.ThirdCard.getCardNo()).geteRank(), eRank.THREE);
	}
	
	@Test
	public void TestPair5(){
		ArrayList<Card> Pair = new ArrayList<Card>();
		Pair.add(new Card(eSuit.CLUBS, eRank.TWO, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.THREE, 0));
		Pair.add(new Card(eSuit.DIAMONDS, eRank.TWO, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		Pair.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		Collections.sort(Pair);
		Hand h = new Hand();
		h = SetHand(Pair, h);
		HandScore hs = new HandScore();
		
		boolean bActualIsHandPair = Hand.isHandPair(h, hs);
		boolean bExpectedIsHandPair = true;

		assertEquals(bActualIsHandPair, bExpectedIsHandPair);
		assertEquals(hs.getHiHand(), eRank.TWO.getiRankNbr());
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.ACE);
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.KING);
		assertEquals(hs.getKickers().get(eCardNo.ThirdCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.ThirdCard.getCardNo()).geteRank(), eRank.THREE);
	}
	
	@Test
	public void TestNoPair(){
		ArrayList<Card> NoPair = new ArrayList<Card>();
		NoPair.add(new Card(eSuit.CLUBS, eRank.TWO, 0));
		NoPair.add(new Card(eSuit.CLUBS, eRank.THREE, 0));
		NoPair.add(new Card(eSuit.DIAMONDS, eRank.FOUR, 0));
		NoPair.add(new Card(eSuit.CLUBS, eRank.KING, 0));
		NoPair.add(new Card(eSuit.CLUBS, eRank.ACE, 0));
		Collections.sort(NoPair);
		Hand h = new Hand();
		h = SetHand(NoPair, h);
		HandScore hs = new HandScore();
		
		boolean bActualIsNoPair = Hand.isHandHighCard(h, hs);
		boolean bExpectedNoPair = true;

		assertEquals(bActualIsNoPair, bExpectedNoPair);
		assertEquals(hs.getHiHand(), eRank.ACE.getiRankNbr());
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.FirstCard.getCardNo()).geteRank(), eRank.KING);
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteSuit(), eSuit.DIAMONDS);
		assertEquals(hs.getKickers().get(eCardNo.SecondCard.getCardNo()).geteRank(), eRank.FOUR);
		assertEquals(hs.getKickers().get(eCardNo.ThirdCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.ThirdCard.getCardNo()).geteRank(), eRank.THREE);
		assertEquals(hs.getKickers().get(eCardNo.FourthCard.getCardNo()).geteSuit(), eSuit.CLUBS);
		assertEquals(hs.getKickers().get(eCardNo.FourthCard.getCardNo()).geteRank(), eRank.TWO);
	}
}

package pokerBase;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pokerBase.Card;
import pokerEnums.eRank;
import pokerEnums.eSuit;

public class CardTest {
	
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

	@Test
	public void testGetters() {
		Card card = new Card(eSuit.CLUBS , eRank.FOUR, 4);
		assertTrue(card.geteRank() == eRank.FOUR);
		assertTrue(card.geteSuit() == eSuit.CLUBS);
		assertTrue(card.getiCardNbr() == 4);
	}
}
package esof322.a3;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MonopolyTests {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	//BANK
	
	@Test 
	public void testBankSellHouse()
	{
		Bank bank = new Bank();
		bank.sellHouse();
		
		assertEquals(31, bank.getHouses());
	}
	
	@Test 
	public void testBankBuyBackHouse()
	{
		Bank bank = new Bank();
		bank.sellHouse();
		bank.sellHouse();
		bank.sellHouse();
		
		bank.buybackHouse();
		
		assertEquals(30, bank.getHouses());
	}
	
	@Test
	public void testBankSellHotel()
	{
		Bank bank = new Bank();
		bank.sellHotel();
		
		assertEquals(11, bank.getHotels());
	}
	
	@Test
	public void testBankBuyBackHotel()
	{
		Bank bank = new Bank();
		bank.sellHotel();
		bank.sellHotel();
		bank.sellHotel();
		
		bank.buybackHotel();
		
		assertEquals(10, bank.getHotels());
	}
	
	@Test
	public void testGiveMoney()
	{
		Player player = new Player ("Test", "Test");
		Bank bank = new Bank();
		
		bank.giveMoney(player, 500);
		
		assertEquals(2000, player.getMoneyTotal());
	}
	
	//BOARD
	
	@Test
	public void testBoard ()
	{
		Board board = new Board();
		
		assertEquals("TennesseeAvenue", board.getSpace(18).getName());
		assertEquals("PennsylvaniaRailroad", board.getSpace(15).getName());
		assertEquals("ElectricCompany", board.getSpace(12).getName());
	}

	@Test
	public void testFalseCheckForMonopoly() {
		Player player = new Player("Test", "Test");
		Property prop = new Property("MediterraneanAvenue", 60, new int[] {2, 10, 30, 90, 160, 250}, 50, 30, 2, 1243, 1425, 0);
		
		player.buyProperty(prop);
		
		assertEquals(true, player.checkForMonopoly(prop));
	}
	
	@Test
	public void testTrueCheckForMonopoly() {
		Player player = new Player("Test", "Test");
		Property prop = new Property("MediterraneanAvenue", 60, new int[] {2, 10, 30, 90, 160, 250}, 50, 30, 2, 1243, 1425, 0);
		Property prop2 = new Property("BalticAvenue", 60, new int[] {4, 20, 60, 180, 320, 450}, 50, 30, 2, 994, 1425, 0);
		player.buyProperty(prop);
		player.buyProperty(prop2);
		
		assertEquals(1380, player.getMoneyTotal());
	}

}

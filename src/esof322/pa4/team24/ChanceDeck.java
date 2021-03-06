package esof322.pa4.team24;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;


public class ChanceDeck {	
	static ArrayList<Card> deck = new ArrayList<Card>();
	static final int deckSize = 16;
	static boolean getOutOfJailCard = true;
	private final int PASS_GO_BONUS = 200;
	private final int ELECTRIC_COMPANY = 12;
	private final int WATER_WORKS = 28;
	private final int UTILITY_MULTIPLYER = 10;
	private final int READING = 5;
	private final int PENNSYLVANIA = 15;
	private final int B_AND_O = 25;
	private final int SHORTLINE = 35;
	private final int numberStepsBack = 3;
	private final int HOUSE_REPAIR = 25;
	private final int HOTEL_REPAIR = 100;
	private final int CHAIRMAN_FEE = 50;
	private static Die die1 = new Die();
	private static Die die2 = new Die();
	private Player currentPlayer;
	private Board board;
	private Card card;
	private Space newSpace;
	private Player owner;
	
	public ChanceDeck(){
						//(String title, int type, int newIndex, int billAmount, int bonusAmount)
		deck.add(new Card ("Advance to go. Collect $200", 0, 0, 0, PASS_GO_BONUS));
		deck.add(new Card ("Advance to Illinois Ave. \nIf you pass Go, collect $200", 1, 24, 0, PASS_GO_BONUS));
		deck.add(new Card ("Advance to St. Charles Place.\nIf you pass Go, collect $200", 1, 11, 0, PASS_GO_BONUS));	
		deck.add(new Card ("Take a trip to Reading Railroad. \nIf you pass Go, collect $200", 1, 5, 0, PASS_GO_BONUS));
		deck.add(new Card ("Take a walk on the Boardwalk", 1, 39, 0, 0));
		deck.add(new Card ("Go to Jail. \nDo not pass Go, do not collect $200", 1, 10, 0, 0));
		deck.add(new Card ("Advance token to nearest Utility. \nIf unowned, you may buy it from the Bank. \nIf owned, throw dice and pay owner a total ten times the amount thrown", 2, 0, 0, PASS_GO_BONUS));		
		deck.add(new Card ("Advance token to the nearest Railroad \nand pay owner twice the rental to which \nhe/she {he} is otherwise entitled. If Railroad is unowned, \nyou may buy it from the Bank.", 3, 0, 0, PASS_GO_BONUS));
		deck.add(new Card ("Advance token to the nearest Railroad \nand pay owner twice the rental to which \nhe/she {he} is otherwise entitled. If Railroad is unowned, \nyou may buy it from the Bank.", 3, 0, 0, PASS_GO_BONUS));	
		deck.add(new Card ("Bank pays you dividend of $50", 4, 0, 0, 50));
		deck.add(new Card ("Pay poor tax of $15", 5, 0, 15, 0));
		deck.add(new Card ("Your building and loan matures, Collect $150", 4, 0, 0, 150));	
		deck.add(new Card ("You have been elected Chairman of the Board. \nPay each player $50", 8, 0, 50, 0));
		deck.add(new Card ("Go Back 3 Spaces", 6, 0, 0, 0));
		deck.add(new Card ("Make general repairs on all your property. \nFor each house pay $25 – For each hotel $100", 7, 0, 0, 0));
		deck.add(new Card ("Get out of Jail Free. \nThis card may be kept until needed.", 9, 0, 0, 0));
	}
	
	public void reShuffleCards(){
		if (getOutOfJailCard = true){	//check if get out of jail card is in the discard pile
			for (Card x : deck){
				deck.add(x);
			}
		}
		else{
			for (Card x : deck){
				deck.add(x);
			}
			deck.remove(15);
		}
	}
	
	public void drawCard(Player player, Board board){
		currentPlayer = player;
		this.board = board;
		if (deck.size()==0){
			reShuffleCards();
		}
		Random rand = new Random();
		int  randIndex = rand.nextInt(16);
		card = deck.get(randIndex);
		deck.remove(randIndex);
		JOptionPane.showMessageDialog(null, card.getTitle());
		cardResolution();
	}	
	
	public void cardResolution(){
		int funcType = card.getType();
		int newIndex;
		switch(funcType){
			case 0:		//Advance to Go
//				newSpace = getNewSpace();
				currentPlayer.setLocation(card.getNewIndex());
				checkIfPassGo();
                ImagePanel.getInstance().repaint();
				break;
				
			case 1: 	//move type
				newIndex = card.getNewIndex();
				checkIfPassGo();
				currentPlayer.setLocation(newIndex);
		        ImagePanel.getInstance().repaint();
				GameDriver.checkSpace();

				break;
				
			case 2:		//Advance to nearest Utility
				newIndex = setPropToClosetUtil();
				currentPlayer.setLocation(newIndex);
				newSpace = board.getSpace(newIndex);
				resolveUtil((Deed) newSpace);
				ImagePanel.getInstance().repaint();
				break;
				
			case 3: 	//Advance to Railroad
				newIndex = setPropToClosetRailroad();
				currentPlayer.setLocation(newIndex);
				newSpace = board.getSpace(newIndex);
				resolveRail((Deed) newSpace);
				ImagePanel.getInstance().repaint();
				break;
				
			case 4:		//bonus
				giveBonus();
				break;
				
			case 5:		//bill
				payBill();
				break;
				
			case 6:		//move back 3 spaces
				newIndex = currentPlayer.getLocation() - numberStepsBack;
				currentPlayer.setLocation(newIndex);
				GameDriver.checkSpace();
		        ImagePanel.getInstance().repaint();
				break;

			case 7:
				int houses = currentPlayer.getnumHouses();
				int hotels = currentPlayer.getnumHotels();
				currentPlayer.makePayment((houses * HOUSE_REPAIR) + (hotels * HOTEL_REPAIR));
				break;
				
			case 8:
				currentPlayer.makePayment(CHAIRMAN_FEE * GameDriver.getPlayers().length);
				for(Player x: GameDriver.getPlayers()){
					x.takePayment(CHAIRMAN_FEE);
				}		
				break;
			default: 			
				currentPlayer.setChanceGetOutOfJailCard();
				getOutOfJailCard =false;
		}
	}
	
	public void returnGetOutOfJailCard(){
		getOutOfJailCard = true;
	}

	public Space getNewSpace(){
		newSpace = board.getSpace(card.getNewIndex());
		return newSpace;
	}
	
	public void resolveUtil(Deed deed){
		owner = deed.getOwner();
		if ((owner != null) && (!deed.getMortgageStat())){		
			die1.rollDie();
			die2.rollDie();
			int rollTotal = die1.getDie() + die2.getDie();
			int rent = UTILITY_MULTIPLYER * rollTotal;
			currentPlayer.makePayment(rent);
            owner.takePayment(rent);
		}
		else{
			ButtonPanel.getInstance().enablePropertyButton();		
		}
	}
	
	public void resolveRail(Deed deed){
		owner = deed.getOwner();
		if ((owner != null) && (!deed.getMortgageStat())){		
			int rent = ((Railroad) deed).getRent(owner.getRailroadOwnedCount()) * 2;
			currentPlayer.makePayment(rent);
            owner.takePayment(rent);
		}
		else{
			ButtonPanel.getInstance().enablePropertyButton();		
		}
	}
	
	public int setPropToClosetUtil(){
		int closestIndex;
		
		if (currentPlayer.getLocation() < ELECTRIC_COMPANY){
			closestIndex = ELECTRIC_COMPANY;
		}
		else{
			closestIndex = WATER_WORKS;
		}
		if (closestIndex<currentPlayer.getLocation()){
			giveBonus();
		}
		return closestIndex;
	}
	
	public int setPropToClosetRailroad(){
		int closestRr;
		if(currentPlayer.getLocation() < READING){
			closestRr = READING;
		}
		else if(currentPlayer.getLocation() < PENNSYLVANIA){
			closestRr = PENNSYLVANIA;
		}
		else if(currentPlayer.getLocation() < B_AND_O){
			closestRr = B_AND_O;
		}
		else{
			closestRr = SHORTLINE;
		}
		if (closestRr<currentPlayer.getLocation()){
			giveBonus();
		}
		return closestRr;
	}
	
	
	public void checkIfPassGo(){
		if(currentPlayer.getLocation() > card.getNewIndex()){
			giveBonus();
		}
	}
	
	public void payBill(){
		currentPlayer.makePayment(card.getBonusAmount());
	}
	
	public void giveBonus(){
		currentPlayer.takePayment(card.getBonusAmount());
	}
}
	

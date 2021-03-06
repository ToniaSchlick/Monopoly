package esof322.pa4.team24;

import java.util.*;
import javax.swing.JOptionPane;

public class GameDriver{
	private static int numPlayers;				// Number of Players playing the game (2-4)
	private static Player[] players;			// List of players playing the game
	private static int turnLimit;				// Time limit for the game
	private static Tokens[] tokens;		//players tokens
	private static Board board;							//game board
	private static int currentPlayer = 0;					// Index of the current player for the player array
	private static Die die1 = new Die();
	private static Die die2 = new Die();
	private static int doublesInARow = 0;					// Number of times doubles has been rolled in a row
	private static int rollTotal = 0;						// Total for the dice roll
	private static int playersPreviousLocation = 0;			// Location of the current turn player prior to the dice roll
	private static Property propLoction = null;	// Placeholder for a property if the player lands on it
	private static Railroad rRLocation = null;	// Placeholder for railroad if the player lands on it
	private static Utility utilLocation = null;	// Placeholder for utility if the player lands on it
    private static int turns = 0;
    private static ChanceDeck chanceDeck = new ChanceDeck();
    private static CommunityChestDeck communityDeck = new CommunityChestDeck();
    private final static int GO_BONUS = 200;
    private final static int MAX_BOARD_INDEX = 39;


	public static void main(String[] args){
	   GuiFrame.getInstance();
	}

	public static String getSpaceName(){
		return board.getSpace(players[currentPlayer].getLocation()).getName();
	}
  
	public static int getDie1(){
		return die1.getDie();
	}
  
	public static int getDie2(){
		return die2.getDie();
	}
  
	//method to end a player's turn
	//checks if the game is over and resets variables
	//sets next player to be current player
	public static void endTurn(){
		if (currentPlayer == players.length -1){
			currentPlayer = 0;
	        turns++;

	        if (turns >= turnLimit){
	        	setWinnerList();
	        }
		}
		else{
			currentPlayer++;
		}
		doublesInARow = 0;
		rollTotal = 0;
	}
  
	//method to roll rollDice
	//returns roll total of 2 die
	public static int rollDice(){
		die1.rollDie();
		die2.rollDie();
		if(die1.getDie() == die2.getDie()){
			doublesInARow++;
		}
		else{
			doublesInARow = 0;
		}
			rollTotal = die1.getDie() + die2.getDie();
			return rollTotal;
	}

	//return number of doubles that have been rolled in a row
	public static int getDoublesInARow(){
		return doublesInARow;
	}

	//get number of total turns taken
	public static int getTurnsTaken(){
		return turns;
	}

	//move player token to their new location
	public static void movePlayerToken(){
		playersPreviousLocation = players[currentPlayer].getLocation();
		players[currentPlayer].moveToken(rollTotal);
	}
	
	public static void displayTurnInfo(){
		String playerInfo = ("Player: " + players[currentPlayer].getName().toString() +"\n");
		String turnInfo = getTurnInfo(board.getSpace(players[currentPlayer].getLocation()));
		String rollInfo = ("Roll: "+ die1.getDie() + " and " + die2.getDie() + "\nMove " + rollTotal + " spaces to: ");
		JOptionPane.showMessageDialog(null, playerInfo + rollInfo + turnInfo);
	}
	
	public static void passGo(){
	     if (playersPreviousLocation + rollTotal > MAX_BOARD_INDEX) {
	    	 JOptionPane.showMessageDialog(null, "You passes Go, Collect" + GO_BONUS);
	    	 players[currentPlayer].takePayment(GO_BONUS);
	     }
	}
	
	public static void checkSpace(){
		Space location = board.getSpace(players[currentPlayer].getLocation());
		propLoction = null;
        rRLocation = null;
        utilLocation = null;
		// Handle which type of the square the player landed on
		switch (players[currentPlayer].getLocation()){
			 // Go Space	
			case 0:		
				break;
				
			// Visiting Jail Square
		   case 10: 
			   break;
			   
			// Free Parking
		   case 20:  
				break;
				  
		    // Chance
		   case 7: case 22: case 36: 	 
				chanceDeck.drawCard(players[currentPlayer], board);
				break;
			
			// Community Chest
		   case 2: case 17: case 33: 	
				communityDeck.drawCard(players[currentPlayer]);
				break;	
					
			// Got to Jail
		   case 30: 		
				players[currentPlayer].setJailedStat(true);
				break;
	
			// Income Tax
		   case 4: 		
				players[currentPlayer].makePayment(((IncomeTax) location).getTaxAmount());
				break;
					
			// Luxury Tax
			case 38: 		
				players[currentPlayer].makePayment(((LuxuryTax) location).getTaxAmount());
				break;
	
			//Railroads
			case 5: case 15: case 25: case 35:  
				rRLocation = (Railroad) location;
				if ( (rRLocation.getOwner() != null) && (!rRLocation.getMortgageStat())){
					players[currentPlayer].makePayment(rRLocation.getRent(rRLocation.getOwner().getRailroadOwnedCount()));
				}
	            else if (rRLocation.getOwner() == null){
	               ButtonPanel.getInstance().enablePropertyButton();
				}
				break;
	
			// Utility
			case 12: case 28: 		
				utilLocation = (Utility) location;
	            if ((utilLocation.getOwner() != null) && (!utilLocation.getMortgageStat())) {                                          
	                players[currentPlayer].makePayment(utilLocation.getOwner().getUtilitysMultiplyer()*rollTotal);
	                utilLocation.getOwner().takePayment(utilLocation.getOwner().getUtilitysMultiplyer()*rollTotal);
	            }
	            else if (utilLocation.getOwner() == null){
	                ButtonPanel.getInstance().enablePropertyButton();
	            }
				break;
	
			// Handle if the player landed on a property
			default: 		
				propLoction = (Property) location;
				// Check if the property is owned and not mortgaged and if not, buy it if the user wants
				if ( (propLoction.getOwner() != null) && (!propLoction.getMortgageStat())){
					 
					// Check if the property is part of a monopoly and has no houses (Must double rent)
					if ( (propLoction.getOwner().checkForMonopoly(propLoction.getMonoColor())) && (propLoction.getNumHouses() == 0) ){
						players[currentPlayer].makePayment(2*propLoction.getRent());
						propLoction.getOwner().takePayment(2*propLoction.getRent());

					}
					else{
						players[currentPlayer].makePayment(propLoction.getRent());
						propLoction.getOwner().takePayment(propLoction.getRent());
					}
				}
	            	else if (propLoction.getOwner() == null){
	            		ButtonPanel.getInstance().enablePropertyButton();
				}
			 	break;
		}
		
	}
	
	public static String getTurnInfo(Space location){
		String info = (location.getName() + ".\n");
		if (location instanceof Deed){	
			Deed spaceDeed = (Deed) location;
			Player owner = null;
			owner = spaceDeed.getOwner();
			if(owner != null){
				if(owner == players[currentPlayer]){
					info = (info + "\nYou own this property.\n");
					return info;
				}
				int rent = getRentOwned(spaceDeed);	
				String rentInfo = ("You owe " + owner.getName() + " $"+ rent + ".\n");
				info = (info + rentInfo);
			}
			else{
				int price = spaceDeed.getPrice();
				String priceInfo = ("Property is Unowned. \nYou may buy it for $"+ price + ".\n");
				info = (info + priceInfo);
			}
		}
		else{
			int tax = 0;
			if(location instanceof LuxuryTax){
				LuxuryTax taxSpace = (LuxuryTax) location;
				tax = taxSpace.getTaxAmount();
				info = (info + "You are being taxed $" + tax + ".");
			}
			if(location instanceof IncomeTax){
				IncomeTax taxSpace = (IncomeTax) location;
				tax = taxSpace.getTaxAmount();
				info = (info + "You are being taxed $" + tax + ".");
			}		
		}
		return info;
	}
  
	public static int getRentOwned(Deed space){
		int rent;
		if (space instanceof Railroad){
			Railroad railSpace = (Railroad) space;
			rent = railSpace.getRent(players[currentPlayer].getRailroadOwnedCount());
		}
		if (space instanceof Utility){
			Utility utilSpace = (Utility) space;
			rent = (utilSpace.getOwner().getUtilitysMultiplyer() * rollTotal);
		}
		else{
			Property propSpace = (Property) space;
			rent = propSpace.getRent();
		}
		return rent;
	}
	
	public static void buyProperty(){
            if (propLoction == null && utilLocation == null){
            	players[currentPlayer].buyProperty(rRLocation);
            }
            else if (propLoction == null && rRLocation == null){
                players[currentPlayer].buyProperty(utilLocation);
            }
            else if (rRLocation == null && utilLocation == null){
                players[currentPlayer].buyProperty(propLoction);
            }
            else{
            	System.out.println("Not bought");
            }
	}

	// Function used by the GUI to get the number of players
	public static void setPlayers(Player[] players){
            GameDriver.players = players;
            numPlayers = players.length;
	}

	// Function used by the GUI to set the time limit of the game
	public static void setTurnLimit(int turnLimit){
            GameDriver.turnLimit = turnLimit;
            //endTime = System.currentTimeMillis() + (GameDriver.timeLimit*60)*1000;
	}

	//return the current player
	public static Player getCurrentPlayer(){
		return players[currentPlayer];
	}

	//return list of players
	public static Player[] getPlayers(){
		return players;
	}

	//return the current X coordinate of the player
	public static int getXCoordinate(Player p){
		//return board.getSpace(playersCurrentLocation).getX();
		int x = 0;
		for(int i = 0; i < players.length; i++){
			if (players[i] == p)
				x = board.getSpace(players[i].getLocation()).getX();
		}
		return x;
	}

	//return the current Y Coordiate of the player
	public static int getYCoordinate(Player p){
		//return board.getSpace(playersCurrentLocation).getY();
		int y = 0;
		for(int i = 0; i < players.length; i++){
			if (players[i] == p)
				y = board.getSpace(players[i].getLocation()).getY();
		}
		return y;
	}

	//set the Winner list at the end of the Game
	//winner(s) = player(s) with most money
	public static void setWinnerList(){
		ArrayList<Player> winners = new ArrayList<>();
        Player winner = players[0];
        winner.calculateTotalWorth();
        // Cycle through players and determine who has the most money
        for (int i = 1; i < players.length; i++){
        	players[i].calculateTotalWorth();
            if (players[i].getTotalWorth() > winner.getTotalWorth()){
                winner = players[i];
            }
        }
        winners.add(winner);
        for (int j = 0; j < players.length; j++){
            if (!players[j].getName().equals(winner.getName())){
                if (players[j].getTotalWorth() == winner.getTotalWorth()){
                    winners.add(players[j]);
                }
            }
        }
        ImagePanel.getInstance().declareWinner(winners);
	}

	//set the board and token based on theme user picked
	//use GameStyleFactory to set board and tokens
	//set theme in ImagePanel
	public static void setBoardandTokens(String type){
		GameStyleFactory factory = new GameStyleFactory();
		GameStyle style = factory.getStyle(type);

		board = style.createBoard();
		tokens = style.createTokens();
		ImagePanel.setType(type);
	}
}

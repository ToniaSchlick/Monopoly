package esof322.a3;

public class Property extends Deed
{
	
	private String name;				// Name of property
	private int price;					// Purchase price for property
	private Player owner;				// Current owner of property
	private int[] rentRates;			// Rent rates for a certain # of houses on property
	private int houseCost;				// Cost of purchasing a house / hotel
	private int numHouses;				// Number of houses built on property
	private int mortgageVal;			// Money gained from mortgaging property
	private int numberOfMonopolyParts;	// Number of parts needed for monopoly
	private int monoColor;				// Manages which properties go together to make a monopoly
	
	public Property(String name, int price, int[] rentRates, int houseCost, int mortgageVal, int numberOfMonopolyParts, int x, int y, int monoColor)
	{
		super(name, price, mortgageVal, x, y);
		this.name = name;
		this.price = price;
		this.rentRates = rentRates;
		this.houseCost = houseCost;
		this.mortgageVal = mortgageVal;
		this.numberOfMonopolyParts = numberOfMonopolyParts;
		this.monoColor = monoColor;
		
		owner = null;
		numHouses = 0;
	}
	
	public Player getOwner()
	{
		return owner;
	}
	
	public int getNumberOfHouses()
	{
		return numHouses;
	}
	
	public int getNumberOfMonopolyParts()
	{
		return numberOfMonopolyParts;
	}
	
	public int getRent()
	{
		return rentRates[numHouses];
	}
	
	public void buildHouse()
	{
		numHouses++;
	}
	
	public void sellHouse()
	{
		numHouses--;
	}
	
	public int getMonoColor(){
		return monoColor;
	}
}

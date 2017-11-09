package esof322.a3;

public class IncomeTax extends Space
{
	private int taxAmount;
	
	public IncomeTax(int amount)
	{
		super("IncomeTax");
		taxAmount = amount;
	}
	
	public void payIncomeTax(Player player)
	{
		player.makePayment(taxAmount);
	}
}

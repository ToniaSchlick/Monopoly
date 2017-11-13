package esof322.a3;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/*
  Class that creates button and determines what each does when clicked
*/
public class ButtonPanel extends JPanel implements ActionListener
{
    private static ButtonPanel instance;
    private final JButton takeTurn, endTurn, buyProperty, buyHouse, buyHotel, sellHouse, sellHotel, mortgage;

    public ButtonPanel()
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        takeTurn = new JButton("Take Turn");
        endTurn = new JButton("End Turn");
        buyProperty = new JButton("Buy Property");
        buyHouse = new JButton("Buy House");
        buyHotel = new JButton("Buy Hotel");
        sellHouse = new JButton("Sell House");
        sellHotel = new JButton("Sell Hotel");
        mortgage = new JButton("Mortgage");

        endTurn.setEnabled(false);
        buyProperty.setEnabled(false);
        buyHouse.setEnabled(false);
        buyHotel.setEnabled(false);
        sellHouse.setEnabled(false);
        sellHotel.setEnabled(false);
        mortgage.setEnabled(false);
        takeTurn.setActionCommand("Take Turn");
        setButton(takeTurn);
        setButton(endTurn);
        setButton(buyProperty);
        setButton(buyHouse);
        setButton(buyHotel);
        setButton(sellHouse);
        setButton(sellHotel);
        setButton(mortgage);
    }

    /*
      Method that formats, aligns, and adds button to the panel
    */
    private void setButton(JButton button)
    {
        button.addActionListener(this);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 50));
        add(Box.createRigidArea(new Dimension(25, 25)));
        button.setFont(new Font("Sans-Serif", Font.BOLD, 18));
        add(button);
    }

    public void enablePropertyButton()
    {
        buyProperty.setEnabled(true);
    }

    /*
      Method that returns instance of the button panel
    */
    public static ButtonPanel getInstance()
    {
        if (instance == null)
        {
            instance = new ButtonPanel();
        }
        return instance;
    }

    /*
      Method that determines what each button does when it is clicked
    */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        String command = ae.getActionCommand();
        switch (command) {
            case "Take Turn":
                if (GameDriver.getDoublesInARow() != 3)
                {
                    GameDriver.rollDice();
                    GameDriver.movePlayerToken();
                    ImagePanel.getInstance().repaint();
                    GameDriver.passGo();
                    GameDriver.checkSpace(GameDriver.getCurrentPlayer().getLocation());
                    PlayerInfoPanel.getInstance().updateInfo();
                }
                if (GameDriver.getDoublesInARow() == 0)
                {
                    takeTurn.setEnabled(false);
                    endTurn.setEnabled(true);
                }

                break;
                
            case "End Turn":
                GameDriver.endTurn();
                endTurn.setEnabled(false);
                takeTurn.setEnabled(true);
                buyProperty.setEnabled(false);
                takeTurn.setText("Take Turn: " + GameDriver.getCurrentPlayer().getName());
                PlayerInfoPanel.getInstance().updateInfo();
                if (GameDriver.getCurrentPlayer().getHouseBuildableProps().size() > 0)
                {
                	buyHouse.setEnabled(true);
                }
                else
                {
                	buyHouse.setEnabled(false);
                }
                if (GameDriver.getCurrentPlayer().getHotelBuildableProps().size() > 0)
                {
                	buyHotel.setEnabled(true);
                }
                else
                {
                	buyHotel.setEnabled(false);
                }
                
                if (GameDriver.getCurrentPlayer().getPropertiesOwned().size() > 0)
                {
                	for (int i = 0; i < GameDriver.getCurrentPlayer().getPropertiesOwned().size(); i++)
                	{
                		if (!GameDriver.getCurrentPlayer().getPropertiesOwned().get(i).mortgaged)
                		{
                			mortgage.setEnabled(true);
                		}
                	}
                }
                else
                {
                	mortgage.setEnabled(false);
                }
                break;
                
            case "Buy Property":
                GameDriver.buyProperty();
                buyProperty.setEnabled(false);
                PlayerInfoPanel.getInstance().updateInfo();
                break;
                
            case "Buy House":
            	String[] buildableHouses = new String[GameDriver.getCurrentPlayer().getHouseBuildableProps().size()];
            	for (int i = 0; i < buildableHouses.length; i++)
            	{
            		buildableHouses[i] = GameDriver.getCurrentPlayer().getHouseBuildableProps().get(i).getName() + " (" + GameDriver.getCurrentPlayer().getHouseBuildableProps().get(i).getNumHouses() + " Houses on Property)";
            	}
            	String buildHouseOnProperty = (String) JOptionPane.showInputDialog(GuiFrame.getInstance(), "Please Select a Property to Build Houes On", "Building Selection", JOptionPane.PLAIN_MESSAGE, null, buildableHouses, buildableHouses[0]);
            	for (int j = 0; j < buildableHouses.length; j++)
            	{
            		if (GameDriver.getCurrentPlayer().getHouseBuildableProps().get(j).getName().equals(buildHouseOnProperty))
            		{
            			GameDriver.getCurrentPlayer().buildHouse(GameDriver.getCurrentPlayer().getHouseBuildableProps().get(j));
            		}
            	}
                break;
                
            case "Buy Hotel":
            	String buildHotelOnProperty = (String) JOptionPane.showInputDialog(GuiFrame.getInstance(), "Please Select a Property to Build Hotel On", "Building Selection", JOptionPane.PLAIN_MESSAGE, null, GameDriver.getCurrentPlayer().getHotelBuildableProps().toArray(), GameDriver.getCurrentPlayer().getHotelBuildableProps().get(0));
            	for (int j = 0; j < GameDriver.getCurrentPlayer().getHotelBuildableProps().size(); j++)
            	{
            		if (GameDriver.getCurrentPlayer().getHotelBuildableProps().get(j).getName().equals(buildHotelOnProperty))
            		{
            			GameDriver.getCurrentPlayer().buildHotel(GameDriver.getCurrentPlayer().getHotelBuildableProps().get(j));
            		}
            	}
                break;
                
            case "Sell House":
            	String[] sellableHouses = new String[GameDriver.getCurrentPlayer().getPropWithSellable().size()];
            	for (int i = 0; i < sellableHouses.length; i++)
            	{
            		sellableHouses[i] = GameDriver.getCurrentPlayer().getPropWithSellable().get(i).getName() + " (" + GameDriver.getCurrentPlayer().getPropWithSellable().get(i).getNumHouses() + " Houses on Property)";
            	}
            	String sellHouseOnProperty = (String) JOptionPane.showInputDialog(GuiFrame.getInstance(), "Please Select a Property to Sell Houes On", "Selling Selection", JOptionPane.PLAIN_MESSAGE, null, sellableHouses, sellableHouses[0]);
            	for (int j = 0; j < sellableHouses.length; j++)
            	{
            		if (GameDriver.getCurrentPlayer().getPropWithSellable().get(j).getName().equals(sellHouseOnProperty))
            		{
            			GameDriver.getCurrentPlayer().getPropWithSellable().get(j).sellHouse();
            		}
            	}
                break;
            	
            case "Sell Hotel":
            	String[] sellableHotels = new String[GameDriver.getCurrentPlayer().getPropWithSellable().size()];
            	for (int i = 0; i < sellableHouses.length; i++)
            	{
            		sellableHouses[i] = GameDriver.getCurrentPlayer().getHouseBuildableProps().get(i).getName() + " (" + GameDriver.getCurrentPlayer().getHouseBuildableProps().get(i).getNumHouses() + " Houses on Property)";
            	}
            	String sellableHotels = (String) JOptionPane.showInputDialog(GuiFrame.getInstance(), "Please Select a Property to Sell Houes On", "Selling Selection", JOptionPane.PLAIN_MESSAGE, null, sellableHouses, sellableHouses[0]);
            	for (int j = 0; j < sellableHouses.length; j++)
            	{
            		if (GameDriver.getCurrentPlayer().getPropWithSellable().get(j).getName().equals(sellHouseOnProperty))
            		{
            			GameDriver.getCurrentPlayer().getPropWithSellable().get(j).sellHouse();
            		}
            	}
                break;
            	break;
            	
            case "Mortgage":
            	String[] properties = new String[GameDriver.getCurrentPlayer().getPropertiesOwned().size()];
            	for (int i = 0; i < GameDriver.getCurrentPlayer().getPropertiesOwned().size(); i++)
            	{
            		if (!GameDriver.getCurrentPlayer().getPropertiesOwned().get(i).mortgaged)
            		{
            			properties[i] = GameDriver.getCurrentPlayer().getPropertiesOwned().get(i).getName();
            		}
            	}
            	String mortgaged = (String) JOptionPane.showInputDialog(GuiFrame.getInstance(), "Please Select a Property to Mortgage", "Mortgage Selection", JOptionPane.PLAIN_MESSAGE, null, properties, properties[0]);
            	for (int j = 0; j < GameDriver.getCurrentPlayer().getPropertiesOwned().size(); j++)
            	{
            		if (GameDriver.getCurrentPlayer().getPropertiesOwned().get(j).getName().equals(mortgaged))
            		{
            			GameDriver.getCurrentPlayer().mortgage(GameDriver.getCurrentPlayer().getPropertiesOwned().get(j));
            		}
            		break;
            	}
            	PlayerInfoPanel.getInstance().updateInfo();
            	
            default:
                break;
        }
    }
}

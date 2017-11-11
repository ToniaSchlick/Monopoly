package esof322.a3;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.*;

public class Gui extends JFrame{

  //private GameDriver driver;

  public static void main(String[] args){
    new Gui();
  }

  public Gui() {
    startWindow();

  }
  /**
  * method to open starting window for monopoly gui
  * presents options to start a new game or exit the gui
  */
  public void startWindow() {
    setTitle("Monopoly");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    //button to begin a new game
    JButton newgame = new JButton("New Game");
    newgame.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              //hides current panel and replaces it with panel presenting options
              //to start a new game

              addOptionsPanel(centerPanel);
            }
        });
    centerPanel.add(newgame);
    //button to exit the game
    JButton close = new JButton("Exit Game");
    close.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              //closes gui
              System.exit(0);
            }
        });
    centerPanel.add(close);
    menu();
    add(centerPanel, BorderLayout.CENTER);
    setSize(500, 1000);
    setVisible(true);
  }
  /**
  * method to add a new JPanel to the monopoly gui
  * user enters number of players and time limit for the game
  * takes in the current panel as a parameter
  */
  public void addOptionsPanel(JPanel oldPanel) {
    JPanel playerPanel = new JPanel();
    playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
    //get human players
    JLabel hplabel = new JLabel("Number of Human Players");
    playerPanel.add(hplabel);
    Integer[] humanPlayerOptions = new Integer[] {2,3,4};
    Integer[] computerPlayerOptions = new Integer[] {0,1,2,3};
    //options to choose number of players
    JComboBox<Integer> humanPlayers = new JComboBox<Integer>(humanPlayerOptions);
    playerPanel.add(humanPlayers);
    //get computer players
    JLabel cplabel = new JLabel("Number of Computer Players");
    //playerPanel.add(cplabel);
    JComboBox<Integer> computerPlayers = new JComboBox<Integer>(computerPlayerOptions);
    //playerPanel.add(computerPlayers);
    //get time limit
    JLabel tlabel = new JLabel("Time Limit (minutes)");
    playerPanel.add(tlabel);
    Integer[] times = new Integer[] {5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60};
    //options to choose time limit for game
    JComboBox<Integer> timeLimit = new JComboBox<Integer>(times);
    playerPanel.add(timeLimit);
    //button to start new game with selected options
    JButton start = new JButton("Start");
    start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              //create players

              int humans = (int)humanPlayers.getSelectedItem();
              String[] humanNames = new String[humans];
              //for when we implement AI Players
              /*int computers = (int)computerPlayers.getSelectedItem();
              String[] computerNames = new String[computers]; */
              	for (int i = 0; i < humans; i++)
              	{
              		humanNames[i] = (String) JOptionPane.showInputDialog("Please Input a Name for Human Player " + (i+1));
              	}
                //for when we implement AI Players
                /*for(int j = 0; j < computers; j++){
                  computerNames[j] = (String) JOptionPane.showInputDialog("Please Input a Name for Computer Player " + (j+1));
                }*/
                //driver.setNumPlayers(humans)
                //driver.setPlayerNames(playernames);
                int time = (int)timeLimit.getSelectedItem();
              	//driver.setTimeLimit((long)timeLimit.getSelectedItem());
                addBoard(playerPanel);
                //for when we implement AI Players
            /*  else{
                JDialog error = new JOptionPane("Number of players must be between 2 and 8.", JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION).createDialog("Error");
                error.setAlwaysOnTop(true);
                error.setVisible(true);
                error.dispose();
                }*/
              }
        });
    playerPanel.add(start);
    //hide current panel to replace it
    oldPanel.setVisible(false);
    //add(timePanel);
    add(playerPanel);
  }

  public void addBoard(JPanel oldPanel1){
    oldPanel1.setVisible(false);
    JPanel boardPanel = new JPanel();
    try{
      BufferedImage boardImage = ImageIO.read(new File("../../../images/Board.png"));
      JLabel picLabel = new JLabel(new ImageIcon(boardImage));
      boardPanel.add(picLabel);
    }
    catch(IOException e){
      System.out.println(e);
    }
    boardPanel.setVisible(true);
    add(boardPanel);

  }
  /**
  * method to add the menu to the monopoly gui
  * presents option to exit the gui
  */
  public void menu(){
    JMenuBar bar = new JMenuBar();
    JMenu exitOption = new JMenu("Exit");
    JMenuItem exit = new JMenuItem("Exit Game");
    exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              //closes gui
              System.exit(0);
            }
        });
    exitOption.add(exit);
    bar.add(exitOption);
    setJMenuBar(bar);
  }
  /**
  * method to create a new panel in the monopoly gui
  * returns a JPanel
  */
  public JPanel createPanel(int x ,int y) {
    JPanel newpanel = new JPanel();
    newpanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    setSize(x,y);
    setVisible(true);
    return newpanel;
  }

}

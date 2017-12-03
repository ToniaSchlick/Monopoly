package esof322.a3;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Board implements GameStyleFactory
{
	private static Space[] board;  //Data structure for the spaces on the board
	//Property Parameters: name, buy price, rent rates, house cost, mortgage value, first second or third part of a monopoly, x coordinate on the board, y coordinate on the board, monopoly color, properties in monopoly
	//Railroad Parameters: Name, x coordinate, y coordinate
	//Utility Parameters: Name, x coordinate, y coordinate
  private static Image boardImage;

	@Override
	public Board createBoard(String type){
		if(type.equalsIgnoreCase("normal")){
			board = NormalBoard.getBoard();
			boardImage = NormalBoard.getImage();
		}
		else{
			board = HarryPotterBoard.getBoard();
			boardImage = HarryPotterBoard.getImage();
		}
	}

	public static Space getSpace(int location){
		return board[location];
	}
}


package BattleShip;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author Christian
 */
public class Game {

    private AbstractView gameBoardOne;
    private AbstractView gameBoardTwo;
    private AbstractController theController;
    //the controller will contain references to the players
    
    private static final int DEFAULT_GRID_SIZE = 11;
    
    public Game(){
        
        /*default constructor creates a one player game with the default
         * controller and game board
         */
        int numPlayers = howManyPlayers();
        
        if(numPlayers == JOptionPane.YES_OPTION){
            gameBoardOne = new DefaultView(DEFAULT_GRID_SIZE);
            gameBoardTwo = null;
            theController = new DefaultSinglePlayerController(gameBoardOne, DEFAULT_GRID_SIZE);
            gameBoardOne.setVisible(true);
        }
        else if(numPlayers == JOptionPane.NO_OPTION){
            gameBoardOne = new DefaultView(DEFAULT_GRID_SIZE);
            gameBoardTwo = new DefaultView(DEFAULT_GRID_SIZE);
            theController = new DefaultTwoPlayerController(gameBoardOne, gameBoardTwo, DEFAULT_GRID_SIZE);
            gameBoardOne.setVisible(true);
            gameBoardTwo.setVisible(false);
        }
       
    }
    
    public Game(AbstractView board, AbstractSinglePlayerController controller){
        gameBoardOne = board == null ? new DefaultView(DEFAULT_GRID_SIZE) : board;
        theController = controller == null ? new DefaultSinglePlayerController
                (gameBoardOne, DEFAULT_GRID_SIZE) : controller;
        
        gameBoardOne.setVisible(true);
    }
    
    public Game(AbstractView boardOne, AbstractView boardTwo, AbstractTwoPlayerController controller){
        
        gameBoardOne = boardOne == null ? new DefaultView(DEFAULT_GRID_SIZE) : boardOne;
        gameBoardOne = boardTwo == null ? new DefaultView(DEFAULT_GRID_SIZE) : boardTwo;
        theController = controller == null ? new DefaultTwoPlayerController
                (gameBoardOne, gameBoardTwo, DEFAULT_GRID_SIZE) : controller;
        
        gameBoardOne.setVisible(true);
        gameBoardTwo.setVisible(false);
        
    }
    
    
    private int howManyPlayers(){
        
        Object [] options = {"1 Player", "2 Players"};
        
        return JOptionPane.showOptionDialog(null, "How many playesr?", 
                "Player Select", JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE, null, options, null);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        Game theGame = new Game();
    }
    
}

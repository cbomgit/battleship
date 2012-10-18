
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
    private AbstractController controllerOne;
    private AbstractController controllerTwo;
    //the controller will contain references to the players
    
    private static final int DEFAULT_GRID_SIZE = 11;
    
    public Game(){
        
        /*default constructor creates a one player game with the default
         * controller and game board
         */
        int numPlayers = howManyPlayers();
        
        if(numPlayers == JOptionPane.YES_OPTION){
            gameBoardOne = new DefaultView(DEFAULT_GRID_SIZE);
            controllerOne = new DefaultController(gameBoardOne, DEFAULT_GRID_SIZE);
            gameBoardTwo = null;
            controllerTwo = null;
            gameBoardOne.setVisible(true);
        }
        else if(numPlayers == JOptionPane.NO_OPTION){
            gameBoardOne = new DefaultView(DEFAULT_GRID_SIZE);
            gameBoardTwo = new DefaultView(DEFAULT_GRID_SIZE);
            controllerOne = new DefaultController(gameBoardOne, DEFAULT_GRID_SIZE);
            gameBoardOne.setVisible(true);
            gameBoardTwo.setVisible(false);
        }
       
    }
    
    public Game(AbstractView board, AbstractController controller){
        
        gameBoardOne = board == null ? new DefaultView(DEFAULT_GRID_SIZE) 
                                     : board;
        
        controllerOne = controller == null ? 
            new DefaultController(gameBoardOne, DEFAULT_GRID_SIZE) 
            : controller;
        
        gameBoardTwo = null;
        controllerTwo = null;
        gameBoardOne.setVisible(true);
    }
    
    public Game(AbstractView boardOne, AbstractView boardTwo, 
            AbstractController c1, AbstractController c2){
        
        gameBoardOne = boardOne == null ? new DefaultView(DEFAULT_GRID_SIZE) 
                                        : boardOne;
        
        gameBoardOne = boardTwo == null ? new DefaultView(DEFAULT_GRID_SIZE) 
                                        : boardTwo;
        
        controllerOne = c1 == null ? 
            new DefaultController(gameBoardOne, DEFAULT_GRID_SIZE) : c1;
        
        controllerOne = c2 == null ? 
            new DefaultController(gameBoardOne, DEFAULT_GRID_SIZE) : c2;
        
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

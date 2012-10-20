
package BattleShip;


/**
 *
 * @author Christian
 */
public class Game {

    private AbstractView gameBoardOne;
    private DefaultController controller;
    //the controller will contain references to the players
    
    private static final int DEFAULT_GRID_SIZE = 11;
    
    public Game(){
        
        /*default constructor creates a one player game with the default
         * controller and game board
         */
         gameBoardOne = new DefaultView(DEFAULT_GRID_SIZE);
         gameBoardOne.setVisible(true);
         controller =  new DefaultController(gameBoardOne, DEFAULT_GRID_SIZE);

    }
    
    public Game(AbstractView myView){
       
       gameBoardOne = myView == null ? new DefaultView(DEFAULT_GRID_SIZE ) : myView;
       gameBoardOne.setVisible(true);
       controller = new DefaultController(gameBoardOne, DEFAULT_GRID_SIZE);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        Game theGame = new Game();
    }
    
}

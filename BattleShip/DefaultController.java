package BattleShip;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author Christian
 */
class DefaultController {
    
    
    //control variables prevent mouse exit event from interfering with
    //mouse click event
    private boolean verticalShipPreviewSet;
    private boolean horizontalShipPreviewSet;
    private int whichShip;
    
    private AbstractView board;
    private User playerOne;
    private Agent playerTwo;
    
    public DefaultController(AbstractView b, int gridSize){
        
        playerOne = new User(gridSize);
        playerTwo = new Agent(gridSize);  
        board = b;                       
        verticalShipPreviewSet = false;   
        horizontalShipPreviewSet = false;
        whichShip = 0;                   
        board.addListenerToShipGrid(new GameSetUpListener());
    }
    
    private class OpponentGridListener extends MouseAdapter{

        @Override
        public void mouseClicked(MouseEvent e) {
            
            Cell target = board.getCoordinatesOfMouseClick(e);

            /*must verify that user has not clicked on a square that has 
             * already been marked as a hit or a miss
             */
            if(playerOne.verifyNewTarget(target)){
                    
                //user takes a turn
                int result = playerTwo.opponentGuessedHere(target);
                playerOne.processResult(result, target);
                board.updateShotGrid(result, target);

                if(result == Player.ALL_SHIPS_SUNK)
                   giveOptionForNewGameOrExit(true, this);

                //computer opponent takes a turn
                target = playerTwo.generateTarget();
                result = playerOne.opponentGuessedHere(target);
                playerTwo.processResult(result, target);
                board.updateShipGrid(result, target);

                if(result == Player.ALL_SHIPS_SUNK)
                    giveOptionForNewGameOrExit(false, this);  
                
            }
        }
    }

    private class GameSetUpListener extends MouseAdapter{
        
        /* the purpose of this listener is to allow the user to allocate 
         * his ships to his own board. As the user moves the mouse around
         * on the board, the listener will display a preview of the ship in
         * both directions. The user will left click for a vertical
         * ship and right for a horizontal ship. 
         */

        @Override
         public void mouseClicked(MouseEvent e) {
            
            Cell target = board.getCoordinatesOfMouseClick(e);
            int shipSize = playerOne.getShipSize(whichShip);
            
            int direction = e.getButton() == MouseEvent.BUTTON1 ? 
                    Ship.VERTICAL : Ship.HORIZONTAL;
            
            if(direction == Ship.VERTICAL && verticalShipPreviewSet){
                
                if(horizontalShipPreviewSet)
                    board.unpaintHorizontalShip(new Cell(target.x + 1, target.y), shipSize - 1);
                playerOne.setVerticalShip(target, whichShip);

                whichShip++;
                verticalShipPreviewSet = horizontalShipPreviewSet = false;
            }
            else if(direction == Ship.HORIZONTAL && horizontalShipPreviewSet){
                if(verticalShipPreviewSet)
                    board.unpaintVerticalShip(new Cell(target.x, target.y + 1), shipSize - 1);
                playerOne.setHorizontalShip(target, whichShip);

                whichShip++;
                verticalShipPreviewSet = horizontalShipPreviewSet =false;
            }
            

            //gives user option to reconfigure ships or begin play
            if(whichShip == 5){
               
                int option = giveOptionToReconfigureShips();
                
                if(option == JOptionPane.YES_OPTION)
                    board.switchListenersToOpponentGrid(new OpponentGridListener(), this);
                else{
                    board.clearTheBoard();
                    playerOne.clearShipGrid();
                    whichShip = 0;
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            
            Cell target = board.getCoordinatesOfMouseClick(e);
            
            int shipSize = playerOne.getShipSize(whichShip);
            board.updateInstruction(Ship.shipName(playerOne.getShipSize(whichShip)));

            if(playerOne.canSetVerticalShip(target, shipSize)){
                board.paintVerticalShip(target, shipSize);
                verticalShipPreviewSet = true;
            }
            if(playerOne.canSetHorizontalShip(target, shipSize)){
                board.paintHorizontalShip(target, shipSize);
                horizontalShipPreviewSet = true;
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Cell target = board.getCoordinatesOfMouseClick(e);
            
            int shipSize = playerOne.getShipSize(whichShip);

            if(verticalShipPreviewSet)
                board.unpaintVerticalShip(target, shipSize);

            if(horizontalShipPreviewSet)
                board.unpaintHorizontalShip(target, shipSize);

            horizontalShipPreviewSet = verticalShipPreviewSet = false;
            
        }
    }
    
    //show menu that prompts user to confirm placement of ships or restart set
    //up
    public int giveOptionToReconfigureShips(){
       
       Object [] options = {"Go to War!", "Re-deploy"};
       return JOptionPane.showOptionDialog(null, "Deployment complete?", 
          "Set-up menu", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, 
           null, options, null);
    }
    
    //game is over.User can decide to ext app or begin a new game
    public void giveOptionForNewGameOrExit(boolean whoWon, MouseAdapter current){
       
      String message = whoWon ? "You win!!" : "You Lose!";
      Object [] options = {"Play Again", "Quit"};

      int option = JOptionPane.showOptionDialog(null, message, "Game Over", 
                   JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                   null, options, null);
       
       
      if(option == JOptionPane.NO_OPTION)
          System.exit(0);
      else{
          board.clearTheBoard();
          board.switchListenersToGameSetUp(current, new GameSetUpListener());
          playerOne = new User(playerOne.getGridSize());
          playerTwo = new Agent(playerTwo.getGridSize());
          whichShip = 0;
      }
     
    }
}

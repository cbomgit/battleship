package BattleShip;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Christian
 */
class DefaultController extends AbstractController {
    
    //defines event generated messages
    public static final String INSTRUCTION = "Left-Click for vertical ship."
            + " Right-Click for horizontal ship.";
    
    //control variables prevent mouse exit event from interfering with
    //mouse click event
    private boolean canSetVerticalShip;
    private boolean canSetHorizontalShip;
    private int whichShip;
    
    private AbstractView board;
    private User playerOne;
    private Agent playerTwo;
    
    public DefaultController(AbstractView b, int gridSize){
        
        playerOne = new User(gridSize);
        playerTwo = new Agent(gridSize);
        board = b;
        canSetVerticalShip = false;
        canSetHorizontalShip = false;
        whichShip = 0;
        board.addListener(new GameSetUpListener());
    }
    
    private class OpponentGridListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            
            Point cell = board.getCoordinatesOfMouseClick(e);

            /*must verify that user has not clicked on a square that has 
             * already been marked as a hit or a miss
             */
            if(playerOne.verifyNewTarget(cell.x, cell.y)){
                    
                //user takes a turn
                int result = playerTwo.opponentGuessedHere(cell.x, cell.y);
                playerOne.processResult(result, cell.x, cell.y);
                board.updateShotGrid(result, cell.x, cell.y);

                
                if(result == Player.ALL_SHIPS_SUNK)
                   giveOptionForNewGameOrExit(true);

                //computer opponent takes a turn
                cell = playerTwo.generateTarget();
                result = playerOne.opponentGuessedHere(cell.x, cell.y);
                playerTwo.processResult(result, cell.x, cell.y);
                board.updateShipGrid(result, cell.x, cell.y);

                if(result == Player.ALL_SHIPS_SUNK)
                    giveOptionForNewGameOrExit(false);               
                
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    private class GameSetUpListener implements MouseListener{
        
        /* the purpose of this listener is to allow the user to allocate 
         * his ships to his own board. As the user moves the mouse around
         * on the board, the listener will display a preview of the ship in
         * both directions. The user will left click for a vertical
         * ship and right for a horizontal ship. 
         */

        @Override
        public void mouseClicked(MouseEvent e) {
            
            Point cell = board.getCoordinatesOfMouseClick(e);
            
                
            int shipSize = playerOne.getShipSize(whichShip);
            
            if(e.getButton() == MouseEvent.BUTTON1){
                
                if(canSetVerticalShip){
                    board.paintVerticalShip(cell.x, cell.y, shipSize); 
                    if(playerOne.canSetHorizontalShip(cell.x, cell.y, shipSize))
                        board.removeHorizontalShip(cell.x + 1, cell.y, shipSize - 1);
                    playerOne.setVerticalShip(cell.x, cell.y, whichShip);

                    whichShip++;
                    canSetVerticalShip = false;
                    canSetHorizontalShip = false;
                }

            }
            else if(e.getButton() == MouseEvent.BUTTON3){
                
                if(canSetHorizontalShip){
                    board.paintHorizontalShip(cell.x, cell.y, shipSize);
                    if(playerOne.canSetVerticalShip(cell.x, cell.y, shipSize))
                        board.removeVerticalShip(cell.x, cell.y + 1, shipSize - 1);
                     playerOne.setHorizontalShip(cell.x, cell.y, whichShip);

                    whichShip++;
                    canSetVerticalShip = false;
                    canSetHorizontalShip = false;
                }
            }

            //gives user option to reconfigure ships or begin play
            if(whichShip == 5){
               
                int option = giveOptionToReconfigureShips();
                
                if(option == JOptionPane.YES_OPTION)
                    board.beginPlay(new OpponentGridListener(), this);
                else{
                    board.clearTheBoard();
                    playerOne.clearShipGrid();
                    whichShip = 0;
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            
            Point cell = board.getCoordinatesOfMouseClick(e);
            
            int shipSize = playerOne.getShipSize(whichShip);
            board.updateMessage(INSTRUCTION + " Set your " 
                    + GamePiece.shipName(playerOne.getShipSize(whichShip)), false);

            if(playerOne.canSetVerticalShip(cell.x, cell.y, shipSize)){
                board.paintVerticalShip(cell.x, cell.y, shipSize);
                canSetVerticalShip = true;
            }
            if(playerOne.canSetHorizontalShip(cell.x, cell.y, shipSize)){
                board.paintHorizontalShip(cell.x, cell.y, shipSize);
                canSetHorizontalShip = true;
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Point cell = board.getCoordinatesOfMouseClick(e);
            
            int shipSize = playerOne.getShipSize(whichShip);

            if(canSetVerticalShip)
                board.removeVerticalShip(cell.x, cell.y, shipSize);

            if(canSetHorizontalShip)
                board.removeHorizontalShip(cell.x, cell.y, shipSize);

            canSetHorizontalShip = canSetVerticalShip = false;
            
        }

        @Override
        public void mousePressed(MouseEvent e) {
            
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            
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
    public void giveOptionForNewGameOrExit(boolean whichPlayerWon){
       
       String message = whichPlayerWon ? "You win!!" : "You Lose!";
       Object [] options = {"Play Again", "Quit"};
       
       int option = JOptionPane.showOptionDialog(null, message, "Game Over", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, options, null);
       
       if(option == JOptionPane.NO_OPTION)
          System.exit(0);
       else{
          
       }
     
    }
}

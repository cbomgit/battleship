package BattleShip;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Christian
 */
class DefaultSinglePlayerController extends AbstractSinglePlayerController {
    
    //defines event generated messages
    public static final String INSTRUCTION = "Left-Click for vertical ship."
            + " Right-Click for horizontal ship.";
    
    //control variables prevent mouse exit event from interfering with
    //mouse click event
    private boolean canSetVerticalShip;
    private boolean canSetHorizontalShip;
    
    public DefaultSinglePlayerController(AbstractView b, int gridSize){
        
        super(gridSize);
        board = b;
        canSetVerticalShip = false;
        canSetHorizontalShip = false;
        whichShip = 0;
        board.addListener(new GameSetUpListener());
    }
    
    private class OpponentGridListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            
            Point cell = board.getCoordinates(e);

            /*must verify that user has not clicked on a square that has 
             * already been marked as a hit or a miss
             */
            if(canUserGuessHere(cell.x, cell.y)){
                    
                //user takes a turn
                int result = getResultFromAgent(cell.x, cell.y);
                updateUserShotGrid(result, cell.x, cell.y);
                board.updateShotGrid(result, cell.x, cell.y);

                
                if(result == Player.ALL_SHIPS_SUNK){
                    JOptionPane.showMessageDialog(board, "You win!!!");
                    System.exit(0);                
                }

                //computer opponent takes a turn
                Point target = getGuessFromAgent();
                result = getResultFromUser(target.x, target.y);
                sendResultToAgent(result, target.x, target.y);
                board.updateShipGrid(result, target.x, target.y);

                if(result == Player.ALL_SHIPS_SUNK){
                    JOptionPane.showMessageDialog(board, "You lose!!!");
                    System.exit(0);                
                }
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
         * ship and right for a horizontal ship
         */

        @Override
        public void mouseClicked(MouseEvent e) {
            
            Point cell = board.getCoordinates(e);
            
                
            int shipSize = getUserShipSize(whichShip);
            
            if(e.getButton() == MouseEvent.BUTTON1){
                
                if(canSetVerticalShip){
                    board.colorVerticalShip(cell.x, cell.y, shipSize); 
                    if(canUserSetHorizontalShipHere(cell.x, cell.y, shipSize))
                        board.removeHorizontalShip(cell.x + 1, cell.y, shipSize - 1);
                    playerOneSetVerticalShip(cell.x, cell.y, whichShip);

                    whichShip++;
                    canSetVerticalShip = false;
                    canSetHorizontalShip = false;
                }

            }
            else if(e.getButton() == MouseEvent.BUTTON3){
                
                if(canSetHorizontalShip){
                    board.colorHorizontalShip(cell.x, cell.y, shipSize);
                    if(canUserSetVerticalShipHere(cell.x, cell.y, shipSize))
                        board.removeVerticalShip(cell.x, cell.y + 1, shipSize - 1);
                    playerOneSetHorizontalShip(cell.x, cell.y, whichShip);

                    whichShip++;
                    canSetVerticalShip = false;
                    canSetHorizontalShip = false;
                }
            }

            //gives user option to reconfigure ships or begin play
            if(whichShip == 5){
                Object [] options = {"Go to War!", "Re-deploy"};
                int option = JOptionPane.showOptionDialog
                    (null, 
                    "Deployment complete?", 
                    "Set-up menu", 
                     JOptionPane.YES_NO_OPTION, 
                     JOptionPane.QUESTION_MESSAGE, 
                     null, options, null);
                
                if(option == JOptionPane.YES_OPTION)
                    board.beginPlay(new OpponentGridListener(), this);
                else{
                    restartSetUp();
                    whichShip = 0;
                }
            }
            
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            
            Point cell = board.getCoordinates(e);
            
            int shipSize = getUserShipSize(whichShip);
            board.updateMessage(INSTRUCTION + " Set your " 
                    + GamePiece.shipName(getUserShipSize(whichShip)), false);

            if(canUserSetVerticalShipHere(cell.x, cell.y, shipSize)){
                board.colorVerticalShip(cell.x, cell.y, shipSize);
                canSetVerticalShip = true;
            }
            if(canUserSetHorizontalShipHere(cell.x, cell.y, shipSize)){
                board.colorHorizontalShip(cell.x, cell.y, shipSize);
                canSetHorizontalShip = true;
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Point cell = board.getCoordinates(e);
            
            int shipSize = getUserShipSize(whichShip);

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
    

}

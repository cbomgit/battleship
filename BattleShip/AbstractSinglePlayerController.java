/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BattleShip;

/**
 *
 * @author Christian
 */
public abstract class AbstractSinglePlayerController extends AbstractController {
    
    private User playerOne;
    private Agent playerTwo;
    protected AbstractView board;
    protected int whichShip;
    
    public AbstractSinglePlayerController(int gridSize){
        playerOne = new User(gridSize);
        playerTwo = new Agent(gridSize);
        
    }
    
    public boolean canUserGuessHere(int x, int y){
        return playerOne.verifyNewTarget(x, y);
    }
    
    public int getResultFromAgent(int x, int y){
        return playerTwo.isAHit(x, y);
    }
    
    public void updateUserShotGrid(int result, int x, int y){
        playerOne.updateShotGrid(result, x, y);
    }
    
    public Point getGuessFromAgent(){
        return playerTwo.generateTarget();
    }
    
    public int getResultFromUser(int x, int y){
        return playerOne.isAHit(x, y);
    }
    
    public void sendResultToAgent(int result, int x, int y){
        playerTwo.processResult(result, x, y);
    }
    
    public int getUserShipSize(int whichShip){
        return playerOne.fleet[whichShip].size();
    }
    
    public boolean canUserSetHorizontalShipHere(int x, int y, int shipSize){
        return playerOne.canSetHorizontalShip(x, y, shipSize);
    }
    
    public boolean canUserSetVerticalShipHere(int x, int y, int shipSize){
        return playerOne.canSetVerticalShip(x, y, shipSize);
    }
    
    public void playerOneSetVerticalShip(int x, int y, int whichShip){
        playerOne.setVerticalShip(x, y, whichShip);
    }
    
    public void playerOneSetHorizontalShip(int x, int y, int whichShip){
        playerOne.setHorizontalShip(x, y, whichShip);
    }
}

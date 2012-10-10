/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Christian
 */
package BattleShip;

public abstract class AbstractTwoPlayerController extends AbstractController {
    
    private User playerOne;
    private User playerTwo;
    private AbstractView boardOne;
    private AbstractView boardTwo;
    
    public AbstractTwoPlayerController(AbstractView b1, AbstractView b2, int gridSize){
        playerOne = new User(gridSize);
        playerTwo = new User(gridSize);
        boardOne = b1;
        boardTwo = b2;
    }
}


class DefaultTwoPlayerController extends AbstractTwoPlayerController{
    
    
    DefaultTwoPlayerController(AbstractView b1, AbstractView b2, int gridSize){
        super(b1, b2, gridSize);
    }
}
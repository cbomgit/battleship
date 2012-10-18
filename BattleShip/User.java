
package BattleShip;

/**
 *
 * @author Christian
 */

 class User extends Player {
    
   
    User(int theSize){
        super(theSize);
    }
   
    //updates the users hit/miss grid with the results of the 
    //last attempt.
   @Override
    public void processResult(int result, int x, int y) {
        resultsGrid[x][y] = result;
    }

    /* returns false if user has already attempted
     * a shot at the square at (x, y). 
     */
   @Override
    boolean verifyNewTarget(int x, int y) {
        return resultsGrid[x][y] == UNKNOWN;
    }
   
   public int getShipSize(int whichShip){
        return fleet[whichShip].size();
    }
   
}

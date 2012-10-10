
package BattleShip;

/**
 *
 * @author Christian
 */

class User extends Player {
    
   
    User(int theSize){
        super(theSize);
    }
   
    //verifies that a ship starting at (x, y) and of length size
    //does not overlap any other ships and does not fall out of the grid
    boolean canSetHorizontalShip(int x, int y, int size) {
        
        if(x + size > gridSize)
            return false;
        
        for(int i = 0; i < size; i++)
            if(shipGrid[x + i][y] != null)
                return false;
        
        return true;
    }
   
    boolean canSetVerticalShip(int x, int y, int size) {
        
        if(y + size > gridSize)
            return false;
        
        for(int i = 0; i < size; i++)
            if(shipGrid[x][y + i] != null)
                return false;
        
        return true;
    }
    
    //these methods allocate a ship from the users fleet
    //to the users grid
    void setVerticalShip(int x, int y, int whichShip){
       
       for(int i = 0; i < fleet[whichShip].size(); i++)
           shipGrid[x][y + i] = fleet[whichShip];
    }
   
    void setHorizontalShip(int x, int y, int whichShip){
       
       for(int i = 0; i < fleet[whichShip].size(); i++)
           shipGrid[x + i][y] = fleet[whichShip];
    }
    
    //updates the users hit/miss grid with the results of the 
    //last attempt.
    void updateShotGrid(int result, int x, int y) {
        resultsGrid[x][y] = result;
    }

    /* returns false if user has already attempted
     * a shot at the square at (x, y). 
     */
    boolean verifyNewTarget(int x, int y) {
        return resultsGrid[x][y] == UNKNOWN;
    }
   
}

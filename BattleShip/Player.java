
package BattleShip;

/**
 *
 * @author Christian
 */
abstract class Player {

   int  [][] resultsGrid;//keep track of hits/misses/ships sunk
   int  gridSize; // size of the grid
   int  shipsActive;
   Ship [] fleet;//represents users own ships
   Ship [][] shipGrid;

   /**
    * 
    * @param theSize size of the grid
    */
   Player(int theSize){

       gridSize = theSize;
       shipsActive = 5;
       resultsGrid = new int[theSize][theSize];
       shipGrid = new Ship[theSize][theSize];
       fleet = new Ship[5];

       for(int y = 0; y < theSize; y++){
           for(int x = 0; x < theSize; x++){
               resultsGrid[x][y] = Point.UNEXPLORED;
               shipGrid[x][y] = null;
           }
       }

       createShips();
   }

   /**
    * create the player's ships.
    */
   private void createShips(){
       
       for(int i = 0; i < Ship.shipLengths.length; i++)
           fleet[i] = new Ship(Ship.shipLengths[i], i);

   }
   
   /**
    * @return size of the grid 
    */
   public int getGridSize(){
      return gridSize;
   }

   /**
    * Returns the result of an opponents guess.
    * @param x x coordinate of guess.
    * @param y y coordinate of guess.
    * @return result of guess. 
    */
   public int opponentGuessedHere(Point guess) {

       //no ship at target, return MISS
        if(shipGrid[guess.x][guess.y] == null)
           return Point.MISS;
        
        //determine if sunk ship was last active ship
        if(shipGrid[guess.x][guess.y].decrement() == Point.SHIP_SUNK)
            return --shipsActive == 0 ? Point.ALL_SHIPS_SUNK : Point.SHIP_SUNK;
        
        //last possible scenario
        return Point.HIT;
   }

   /**
    * clears the ship grid.
    */
   public void clearShipGrid(){
       for(int y = 0; y < gridSize; y++)
            for(int x = 0; x < gridSize; x++)
                shipGrid[x][y] = null;
   }
   
   /**
    * Method to add a ship to User's grid
    * @param target.x row where ship will be placed
    * @param target.y column where ship will be placed
    * @param whichShip index of fleet [] identifies ship to be allocated
    */
   public void setHorizontalShip(Point target, int whichShip) {

        for (int i = 0; i < fleet[whichShip].size(); i++) {
            shipGrid[target.x + i][target.y] = fleet[whichShip];
        }
    }
   
   /**
    * Same as setHorizontalShip, but for opposite direction
    * @param target.x row where ship will be placed
    * @param target.y column where ship will be place
    * @param whichShip index of fleet [] identifies ship to be allocated
    */
   public void setVerticalShip(Point target, int whichShip) {

        for (int i = 0; i < fleet[whichShip].size(); i++) 
            shipGrid[target.x][target.y + i] = fleet[whichShip];
        
   }
   
   /**
    * assert that a ship can be place on grid without violating and
    * of the game board constraints (ie not out of bounds, overlapping, etc).
    * @param target.x start x of ship.
    * @param target.y start y of ship.
    * @param size size of ship in question.
    * @return true if ship can be set, otherwise false.
    */
    public boolean canSetHorizontalShip(Point target, int size) {

        if(target.x + size > gridSize)
            return false;

        for(int i = 0; i < size; i++)
            if(shipGrid[target.x + i][target.y] != null)
                return false;

        return true;
    }

    /**
    * assert that a ship can be place on grid without violating and
    * of the game board constraints (ie not out of bounds, overlapping, etc).
    * @param target.x start x of ship.
    * @param target.y start y of ship.
    * @param size size of ship in question.
    * @return true if ship can be set, otherwise false.
    */
    public boolean canSetVerticalShip(Point target, int size) {

        if(target.y + size > gridSize)
            return false;

        for(int i = 0; i < size; i++)
            if(shipGrid[target.x][target.y + i] != null)
                return false;

        return true;
    }
    
    /**
     * assert that Player has not guessed at a location already
     * @param x x coordinate of cell in question
     * @param y y coordinate of cell in question
     * @return true if (x, y) is unexplored, otherwise false
     */

    public boolean verifyNewTarget(Point target){
       return resultsGrid[target.x][target.y] == Point.UNEXPLORED;
    }
    
    /**
     * Abstract method to process the result of a guess.
     * @param result result of guess
     * @param x x coordinate of guess
     * @param y y coordinate of guess
     */
    public abstract void processResult(int result, Point lastAttempt);
}
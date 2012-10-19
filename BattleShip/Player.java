
package BattleShip;

/**
 *
 * @author Christian
 */
abstract class Player {

   int [][] resultsGrid;//keep track of hits/misses/ships sunk
   int gridSize;
   int shipsActive;
   GamePiece [] fleet;//represents users own ships
   GamePiece[][] shipGrid;

   public static final int ALL_SHIPS_SUNK = -4;
   public static final int SHIP_SUNK = -3;
   public static final int UNKNOWN = 0;
   public static final int MISS = -1;
   public static final int HIT = -2;

   Player(int theSize){

       gridSize = theSize;
       shipsActive = 5;
       resultsGrid = new int[theSize][theSize];
       shipGrid = new GamePiece[theSize][theSize];
       fleet = new GamePiece[5];

       for(int y = 0; y < theSize; y++)
           for(int x = 0; x < theSize; x++){
               resultsGrid[x][y] = UNKNOWN;
               shipGrid[x][y] = null;
           }

       createShips();
   }

   private void createShips(){

       fleet[0] = new GamePiece("Aircraft carrier", 5);
       fleet[1] = new GamePiece("Destroyer", 4);
       fleet[2] = new GamePiece("Battleship", 3);
       fleet[3] = new GamePiece("Submarine", 3);
       fleet[4] = new GamePiece("Patrol boat", 2);

   }

   //returns the result from an opponents guess at x, y
   public int opponentGuessedHere(int x, int y) {

        if(shipGrid[x][y] == null)
           return MISS;
        else {
           if(shipGrid[x][y].takeDamage()){
               if(--shipsActive == 0)
                   return ALL_SHIPS_SUNK;
               else
                   return shipGrid[x][y].size();
           }

           return HIT;
        }
   }

   //sets an empty shipgrid
   public void clearShipGrid(){
       for(int y = 0; y < gridSize; y++)
            for(int x = 0; x < gridSize; x++)
                shipGrid[x][y] = null;
   }

   //allocates a ship from x, y to x + shipSize, y
   public void setHorizontalShip(int x, int y,  int whichShip) {

        for (int i = 0; i < fleet[whichShip].size(); i++) {
            shipGrid[x + i][y] = fleet[whichShip];
        }
    }
   
   //allocates a ship from x, y to x + shipSize, y
   public void setVerticalShip(int x, int y, int whichShip) {

        for (int i = 0; i < fleet[whichShip].size(); i++) {
            shipGrid[x][y + i] = fleet[whichShip];
        }
   }
   
   //verifies that a ship starting at (x, y) and of length size
    //does not overlap any other ships and does not fall out of the grid
    public boolean canSetHorizontalShip(int x, int y, int size) {

        if(x + size > gridSize)
            return false;

        for(int i = 0; i < size; i++)
            if(shipGrid[x + i][y] != null)
                return false;

        return true;
    }

    public boolean canSetVerticalShip(int x, int y, int size) {

        if(y + size > gridSize)
            return false;

        for(int i = 0; i < size; i++)
            if(shipGrid[x][y + i] != null)
                return false;

        return true;
    }
    
    /* returns false if user has already attempted
     * a shot at the square at (x, y). 
     */

    public boolean verifyNewTarget(int x, int y){
       return resultsGrid[x][y] == UNKNOWN;
    }
    
    public abstract void processResult(int result, int x, int y);
}
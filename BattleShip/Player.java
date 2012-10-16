
package BattleShip;

/**
 *
 * @author Christian
 */
abstract class Player {
    
   int [][] resultsGrid;
   int gridSize;
   int shipsActive;
   GamePiece [] fleet;
   GamePiece[][] shipGrid;
   
   public static final int ALL_SHIPS_SUNK = -4;
   public static final int SHIP_SUNK = -3;
   public static final int UNKNOWN = 0;
   public static final int MISS = -1;
   public static final int HIT = -2;
   
   Player(int theSize){
       
       gridSize = theSize;
       resultsGrid = new int[theSize][theSize];
       shipGrid = new GamePiece[theSize][theSize];
       fleet = new GamePiece[5];
       shipsActive = 5;
       
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
   
   int isAHit(int x, int y) {
        
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
   
   void clearShipGrid(){
       for(int y = 0; y < gridSize; y++)
            for(int x = 0; x < gridSize; x++)
                shipGrid[x][y] = null;
   }
}
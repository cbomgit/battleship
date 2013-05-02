/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BattleShip;

/**
 *
 * @author Christian
 */
class Ship {

   private int hitsLeft;
   private int size;
   private int name;
   
   public static final int AIRCRAFT_CARRIER = 5;
   public static final int DESTROYER        = 4;
   public static final int BATTLESHIP       = 3;
   public static final int SUBMARINE        = 3;
   public static final int PATROL_BOAT      = 2;
   
   public static final String [] shipNames  = { "Aircraft Carrier",
                                                "Destroyer",
                                                "BATTLESHIP",
                                                "SUBMARINE",
                                                "PATROL_BOAT"};
   
   public static final int [] shipLengths = {AIRCRAFT_CARRIER,
                                             DESTROYER,
                                             BATTLESHIP,
                                             SUBMARINE,                              
                                             PATROL_BOAT};
   

   public static final int VERTICAL   = 0;
   public static final int HORIZONTAL = 1;

   Ship(int shipLives, int shipName){
       
       size = hitsLeft = shipLives;
       name = shipName;
       
   }

   public int size(){
       return size;
   }
   
   
   int decrement(){

      return --hitsLeft == 0 ? Point.SHIP_SUNK : Point.HIT;
   }
   
   public String getName() {
       return shipNames[name];
   }
   
}
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
    
    private int lives;
    public int hits;
    private String name;
    
    public static final int AIRCRAFT_CARRIER = 5;
    public static final int DESTROYER = 4;
    public static final int BATTLESHIP = 3;
    public static final int SUBMARINE = 3;
    public static final int PATROL_BOAT = 2;
    
    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    
    Ship(String n, int shipLives){
        
        lives = shipLives;
        name = n;
        hits = 0;
    }
    
    public int size(){
        return lives;
    }
    
    public String getName(){
        return name;
    }
    
    boolean takeDamage(){
        
        if(++hits == lives)
            return true;
        
        return false;
    }
    
    public static String shipName(int size){
        
        if(size == AIRCRAFT_CARRIER)
            return "Aircraft Carrier";
        else if(size == DESTROYER)
            return "Destroyer";
        else if(size == SUBMARINE)
            return "Submarine or Battleship";
        else
            return "Patrol Boat";
    }
}
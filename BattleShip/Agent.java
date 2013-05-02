package BattleShip;

import java.util.Random;
/**
 *
 * @author Christian Boman
 */

class Agent extends Player {
    
    int [][] heatMap;      
    private LinkedList<Point []> shipList;
    private int [] enemyFleet = { Ship.AIRCRAFT_CARRIER,
                                  Ship.DESTROYER,
                                  Ship.BATTLESHIP,
                                  Ship.SUBMARINE,
                                  Ship.PATROL_BOAT };
    
    
    Agent(int theSize) {
        super(theSize);    
        heatMap = new int[gridSize][gridSize];
        
        shipList = new LinkedList<Point []>();
        updateHeatMap();
        setShipGrid();
    }
    
    /**
     * randomly allocates ships to Agent's grid.
     */
    private void setShipGrid() {

        int direction;
        Point location = new Point();
        Random r = new Random();
        
        for (int whichShip = 0; whichShip < 5;) {

            direction = r.nextInt(2);
            location.x = r.nextInt(Integer.MAX_VALUE) % gridSize;
            location.y = r.nextInt(Integer.MAX_VALUE) % gridSize;

            if (direction == Ship.VERTICAL 
                && canSetVerticalShip(location, fleet[whichShip].size())) {
                    setVerticalShip(location, whichShip);
                    whichShip++;
            } 
            else if (direction == Ship.HORIZONTAL 
                && canSetHorizontalShip(location, fleet[whichShip].size())) {
                    setHorizontalShip(location, whichShip);
                    whichShip++;
            }
        }
    }
    
    
    public Point generateTarget() {
        
        return null;
    }
    
    private Point guessFromHeatMap() {
        
        double maxValue = 0;
        int maxX = 0, maxY = 0;
        
        for(int y = 0; y < gridSize; y++)
            for(int x = 0; x < gridSize; x++) {
                if(heatMap[x][y] > maxValue) {
                    maxValue = heatMap[x][y];
                    maxX = x;
                    maxY = y;
                }                
            }
        
        return new Point(maxX, maxY, maxValue);
    }

   @Override
    public void processResult(int result, Point lastAttempt) {
            
       
    }
    
    
    private void growShipList(Point p) {
    	
        int largestShip = enemyFleet[enemyFleet.length - 1];
        int fromX = p.x - largestShip  < 0 ? 0 : p.x - largestShip + 1;
        int fromY = p.y - largestShip  < 0 ? 0 : p.y - largestShip + 1;
        
        for(int i = 0; i + fromX <= p.x; i++) {

            int whichShip = enemyFleet.length - 1;
            
            while(whichShip > -1) {
                if(isValidHorizontal(fromX + i, p.y, enemyFleet[whichShip]))
                    addShip(fromX + i, p.y, enemyFleet[whichShip], Ship.HORIZONTAL);
                whichShip--;
            }
        }
        
        for(int i = 0; i + fromY <= p.y; i++){

            int whichShip = enemyFleet.length - 1;	

            while(whichShip > -1) {
                if(isValidVertical(p.x, fromY + i, enemyFleet[whichShip]))
                    addShip(p.x, fromY + i, enemyFleet[whichShip], Ship.VERTICAL);
                whichShip--;
            }
        }
    }

    private void addShip(int x, int y, int size, int dir) {

       
    }		
		

    /*if isValidVertical/Horizontal returns true, a                count++;
 ship is "transposed" to a 
     * point x, y - the value at every point it overlaps is incremented by 1
     */
    private void colorHeatMap(int x, int y, int dir, int size){
        
        for(int i = 0; i < size; i++){
            if(dir == Ship.HORIZONTAL)
                heatMap[x + i][y]++;
            else
                heatMap[x][y + i]++;
        }
        
    }
    
    /*helper methods for set heatMap methods. Determine if a ship can
     * overlap location [x][y].
     */
    private boolean isValidHorizontal(int x, int y, int size){
        
        if(x + size > gridSize)
            return false;
        
        for(int i = 0; i < size; i++)
            if(resultsGrid[x + i][y] == Point.MISS ||
               resultsGrid[x + i][y] == Point.SHIP_SUNK)
                return false;
        
        return true;
    }
    
    private boolean isValidVertical(int x, int y, int size){
        
        if(y + size > gridSize)
            return false;
        
        for(int i = 0; i < size; i++)
            if(resultsGrid[x][y + i] == Point.MISS ||
               resultsGrid[x][y + i] == Point.SHIP_SUNK)
                return false;
        
        return true;
    }
    
    /* calculates the heat map for guessing by iterating through each point on 
     * the board and determining if a ship can overlap without violating any 
     * rules. If so, then the value of every point the ship overlaps is 
     * incremented by one. 
     */
    private void updateHeatMap(){
        
        clearHeatMap(); 
        //set up a loop to iterate through heatMap array
        for(int y = 0; y < gridSize; y++){
            for(int x = 0; x < gridSize; x++){
                
                
                int whichShip = 0;
                //iterate through each remaining ship in enemy fleet

                while(whichShip < enemyFleet.length &&
                        !isValidHorizontal(x, y, enemyFleet[whichShip]))
                    whichShip++; //skip ships that can't pass through x, y

                //"transpose" the rest to the heat map
                while(whichShip < enemyFleet.length) 
                    colorHeatMap(x, y, Ship.HORIZONTAL, enemyFleet[whichShip++]);

                whichShip = 0;

                //repeat for ships in the vertical direction
                while(whichShip < enemyFleet.length &&
                        !isValidVertical(x, y, enemyFleet[whichShip]))
                    whichShip++;

                while(whichShip < enemyFleet.length)
                    colorHeatMap(x, y, Ship.VERTICAL, enemyFleet[whichShip++]);

            }
        }    
    }

    
    //sets heatmap to 0
    private void clearHeatMap(){
        
        for(int i = 0; i < gridSize; i++)
            for(int j = 0; j < gridSize; j++)
                heatMap[j][i] = 0;
    }
}

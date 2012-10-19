package BattleShip;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author Christian
 */

class Agent extends Player {
    
    int [][] weightGrid;//likelihood of a ship overlapping x, y
    private LinkedList<Integer> enemyFleet;//keep track of which ships are sunk
    private Stack<Cell> hits;//keep track of previous hits
    private Stack<Cell> mustExplore;//cells that might have hits adjacent
    //tells agent it should search all cells around a cell x, y
    private boolean findAllDirections;
    
    Agent(int theSize) {
        
        super(theSize);    
        setEnemyFleet();
        weightGrid = new int[gridSize][gridSize];
        findAllDirections = false;
        
        hits = new Stack<Cell>();
        mustExplore = new Stack<Cell>();
        computeWeightGrid();
        setShipGrid(theSize);
    }
    
    private void setEnemyFleet(){
       
      enemyFleet = new LinkedList<Integer>();
      enemyFleet.add(Ship.AIRCRAFT_CARRIER);
      enemyFleet.add(Ship.DESTROYER);
      enemyFleet.add(Ship.BATTLESHIP);
      enemyFleet.add(Ship.SUBMARINE);
      enemyFleet.add(Ship.PATROL_BOAT);
    }
    
    //randomly allocates ships to Agent's shipGrid
    private void setShipGrid(int theSize) {

        int direction, x, y;
        Random r = new Random();

        for (int whichShip = 0; whichShip < 5;) {

            direction = r.nextInt(2);
            x = r.nextInt(Integer.MAX_VALUE) % theSize;
            y = r.nextInt(Integer.MAX_VALUE) % theSize;

            if (direction == Ship.VERTICAL 
                && canSetVerticalShip(x, y, fleet[whichShip].size())) {
                    setVerticalShip(x, y, whichShip);
                    whichShip++;
            } 
            else if (direction == Ship.HORIZONTAL 
                && canSetHorizontalShip(x, y, fleet[whichShip].size())) {
                    setHorizontalShip(x, y, whichShip);
                    whichShip++;
            }
        }
    }
    
    //choose the cell with the highest probability of containing a ship
    public Cell generateTarget() {
        
        Cell guess = new Cell(0, 0, 0);

        if(hits.isEmpty()){
            //no knowledge of any ships at this time, scan grid and guess
            for(int y = 0; y < gridSize; y++)
                for(int x = 0; x < gridSize; x++){
                    if(weightGrid[x][y] > guess.data)
                        guess.reset(x, y, weightGrid[x][y]);
                    weightGrid[x][y] = 0;
                }
        }
        else{
            //one or more ships have not been sunk
            guess = guessAroundAHit(hits.top().x, hits.top().y);
            
            /*all hits adjacent to hits.top() have been found. The agent now
             * looks at mustExplore to begin guessing in a particular direction
             * until a guess results in a MISS or a SHIP_SUNK
             */
            while(guess.data == 0){
                
                /*no more cells to explore. Must backtrack and examine all cells
                 * on hits stack, marking them as SHIP_SUNK and attempting a 
                 * guess on a cell adjacent to the top of the stack
                 */
                if(mustExplore.isEmpty())
                    resultsGrid[hits.top().x][hits.pop().y] = SHIP_SUNK;
                else
                    hits.push(mustExplore.pop());
                   
                guess = generateTarget();
            }
        }
        return guess;
    }
    
    //returns a Cell object adjacent to (x, y)
    private Cell guessAroundAHit(int x, int y){
             
        Cell a = new Cell(x + 1, y, 0);
        Cell b = new Cell(x - 1, y, 0);
        Cell c = new Cell(x , y - 1, 0);
        Cell d = new Cell(x, y + 1, 0);
        
        //only valid cells, and unexplored cells considered
        if(x < gridSize - 1 && resultsGrid[x + 1][y] == Player.UNKNOWN)
            a.data = weightGrid[x + 1][y];
        if(x > 0 && resultsGrid[x - 1][y] == Player.UNKNOWN)
            b.data = weightGrid[x - 1][y];
        if(y > 0 && resultsGrid[x][y - 1] == Player.UNKNOWN)
            c.data = weightGrid[x][y - 1];
        if(y < gridSize - 1 && resultsGrid[x][y + 1] == Player.UNKNOWN)
            d.data = weightGrid[x][y + 1];
        
        
        return Cell.max(a, b, c, d);
    }
    
    
    /* recieves the result of the last shot from the controller. This result is
     * used to decide how to compute a weight grid, based on one of three 
     * possible scenarios.
     */
   @Override
    public void processResult(int result, int x, int y) {
            
        resultsGrid[x][y] = result;
        
        if(result == MISS){
            if(hits.isEmpty())//no knowledge of any ships at this time
                computeWeightGrid();//continue guessing
            else{
                //place on hit stack. all adjacent hits must be found
                computeWeightGrid(hits.top().x, hits.top().y);
                findAllDirections = true;
            }
        }
        else if(result == HIT){
            if(findAllDirections)//place hit on stack, to be revisited later
                mustExplore.push(new Cell(x, y));
            else{//hit is pushed onto stack, and the weight grid is computed 
                 //around it. Adjacent hits in same direction are preferred
                findAllDirections = hits.isEmpty() ? true : false;
                hits.push(new Cell(x,y));
                computeWeightGrid(x, y);
            }
        }
        else{ //a ship has been sunk. Result == size of sunk ship
            resultsGrid[x][y] = SHIP_SUNK;
            enemyFleet.remove(new Integer(result)); 
            handleSunkShip(result - 1, x, y); //marks ship as sunk on results grid
        }
    }
    
    
    
    /*if isValidVertical/Horizontal returns true, a ship is "transposed" to a 
     * point x, y - the value at every cell where it located is incremented by 1
     */
    private void transpose(int x, int y, int dir, int size){
        
        for(int i = 0; i < size; i++){
            if(dir == Ship.HORIZONTAL)
                weightGrid[x + i][y]++;
            else
                weightGrid[x][y + i]++;
        }
        
    }
    
    /*helper methods for computeWeightGrid methods. Determine if a ship can
     * overlap location [x][y].
     */
    private boolean isValidHorizontal(int x, int y, int size){
        
        if(x + size > gridSize)
            return false;
        
        for(int i = 0; i < size; i++)
            if(resultsGrid[x + i][y] == Player.MISS ||
               resultsGrid[x + i][y] == Player.SHIP_SUNK)
                return false;
        
        return true;
    }
    
    private boolean isValidVertical(int x, int y, int size){
        
        if(y + size > gridSize)
            return false;
        
        for(int i = 0; i < size; i++)
            if(resultsGrid[x][y + i] == Player.MISS ||
               resultsGrid[x][y + i] == Player.SHIP_SUNK)
                return false;
        
        return true;
    }
    
    /*overloaded methods for computing the weight grid. If a ship can overlap
     * a cell - meaning it doesn't fall out of the grid and it doesn't overlap
     * any misses or sunk ships - then the value of all of the cells that ship
     * will overlap will increase by one. This is repeated for every cell on the
     * board, in both directions, and for all active enemy ships.
     */
    private void computeWeightGrid(){
        
        clearWeightGrid();
        Iterator<Integer> itr = enemyFleet.iterator();
        
        while(itr.hasNext()){
           
            int shipSize = itr.next();
            
            for(int y = 0; y < gridSize; y++){
                for(int x = 0; x < gridSize; x++){
                    if(isValidHorizontal(x, y, shipSize))
                        transpose(x, y, Ship.HORIZONTAL, shipSize);
                    if(isValidVertical(x, y, shipSize))
                        transpose(x, y, Ship.VERTICAL, shipSize);
                }
            }        
        } 
    }
    
    /*when a ship has been found, the entire weight grid is zeroed out, with
     * the exception of the row and column where the initial hit was made out
     * to enemyfleet[i] spaces.
     */
    private void computeWeightGrid(int x, int y){
       
        Iterator<Integer> itr = enemyFleet.iterator();
        
        while(itr.hasNext()){
           
            int largestEnemyShip = itr.next();
            int fromX = x - largestEnemyShip < 0 ? 0 : x - largestEnemyShip + 1;
            int fromY = y - largestEnemyShip < 0 ? 0 : y - largestEnemyShip + 1;

            for(int i = 0; i < gridSize && fromX + i <= x; i++) {
                if(isValidHorizontal(fromX + i, y, largestEnemyShip ))
                transpose(fromX + i, y, Ship.HORIZONTAL, largestEnemyShip);
            }

            for(int i = 0; i < gridSize && fromY + i <= y; i++) {
                if(isValidVertical(x, fromY + i, largestEnemyShip))
                    transpose(x, fromY + i, Ship.VERTICAL, largestEnemyShip);
            }
            
        }
        
        weightGrid[x][y] = Player.HIT;
    }
    
    //sets weightGrid to 0
    private void clearWeightGrid(){
        
        for(int i = 0; i < gridSize; i++)
            for(int j = 0; j < gridSize; j++)
                weightGrid[j][i] = 0;
    }
    
    private void handleSunkShip(int size, int lastHitX, int lastHitY) {
        
        int i;
        
        /*removes all hits in same column/row as lastHit
         * labeling them as SHIP_SUNK on results grid. 
         */
        for(i = 0; i < size && !hits.isEmpty(); i++)
            if(hits.top().x == lastHitX || hits.top().y == lastHitY)
                resultsGrid[hits.top().x][hits.pop().y] = SHIP_SUNK;
        
       /*ship may have been sunk while looking in adjacent cells
        *therefore, some hits may be on mustExplore stack
        *this is likely to occurr if the 2 or 3 size ship is found
        */
        if(i < size)
            resultsGrid[mustExplore.top().x][mustExplore.pop().y] = SHIP_SUNK;
        
        /*(must explore will be empty if an isolated ship was sunk
        however, ships stacked end to end (parallel, perpendicular, or 
        in a contiguous manner) will leave cells to be explored
        */
        if(mustExplore.isEmpty())
            computeWeightGrid();
        else{
            hits.push(mustExplore.pop());
            computeWeightGrid(hits.top().x, hits.top().y);
        }
            
        findAllDirections = false;
    }
}
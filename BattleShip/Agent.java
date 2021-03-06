package BattleShip;

import java.util.Random;

/**
 *
 * @author Christian Boman
 */

class Agent extends Player {
    
    double [][] weightGrid;                   //likelihood of a ship overlapping x, y
    private int [] enemyFleet;//keep track of which ships are sunk
    private Stack<Cell> hits;              //keep track of previous hits
    private Stack<Cell> mustExplore;       //cells that might have hits adjacent
    
    //tells agent it should search all cells around a cell x, y
    private boolean findAllDirections;
    
    Agent(int theSize) {
        
        super(theSize);    
        setEnemyFleet();
        weightGrid = new double[gridSize][gridSize];
        findAllDirections = false;
        
        hits = new Stack<Cell>();
        mustExplore = new Stack<Cell>();
        computeWeightGrid();
        setShipGrid();
    }
    
    /**
     * creates list of still active enemy ships.
     */
    private void setEnemyFleet(){
       
      enemyFleet = new int[5];
      
      enemyFleet[0] = Ship.AIRCRAFT_CARRIER;
      enemyFleet[1] = Ship.DESTROYER;
      enemyFleet[2] = Ship.BATTLESHIP;
      enemyFleet[3] = Ship.SUBMARINE;
      enemyFleet[4] = Ship.PATROL_BOAT;
    }
    
    /**
     * randomly allocates ships to Agent's grid.
     */
    private void setShipGrid() {

        int direction, x, y;
        Cell location = new Cell();
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
    
    /**
     * choose the cell with highest chance of being overlapped by a ship.
     * @return generated guess
     */
    public Cell generateTarget() {
        
        Cell guess = new Cell();

        if(hits.isEmpty()){
            //no knowledge of any ships at this time, scan grid and guess
            for(int y = 0; y < gridSize; y++)
                for(int x = 0; x < gridSize; x++){
                    if(weightGrid[x][y] > guess.data)
                        guess = new Cell(x, y, weightGrid[x][y]);
                    weightGrid[x][y] = 0;
                }
        }
        else{
            //one or more ships have not been sunk
            guess = guessAroundAHit(hits.top());
            
            /*all hits adjacent to hits.top() have been found. The agent now
             * looks at mustExplore to begin guessing in a particular direction
             * until a guess results in a MISS or a SHIP_SUNK
             */
            while(guess.data == 0){
                
                /*no more cells to explore. Must backtrack and examine all cells
                 * on hits stack, marking them as SHIP_SUNK and attempting a 
                 * guess on a cell adjacent to the top of the stack. This is 
                 * the worst case for Agent. Should be fairly uncommon.
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
    
    /**
     * @param hit.x x of hit
     * @param hit.y y of hit
     * @return highest rated cell around hit
     */
    private Cell guessAroundAHit(Cell hit){
             
        Cell a = new Cell(hit.x + 1, hit.y, 0.0);
        Cell b = new Cell(hit.x - 1, hit.y, 0.0);
        Cell c = new Cell(hit.x , hit.y - 1, 0.0);
        Cell d = new Cell(hit.x, hit.y + 1, 0.0);
        
        //only valid cells, and unexplored cells considered
        if(hit.x < gridSize - 1 && resultsGrid[hit.x + 1][hit.y] == Player.UNKNOWN)
            a.data = weightGrid[hit.x + 1][hit.y];
        if(hit.x > 0 && resultsGrid[hit.x - 1][hit.y] == Player.UNKNOWN)
            b.data = weightGrid[hit.x - 1][hit.y];
        if(hit.y > 0 && resultsGrid[hit.x][hit.y - 1] == Player.UNKNOWN)
            c.data = weightGrid[hit.x][hit.y - 1];
        if(hit.y < gridSize - 1 && resultsGrid[hit.x][hit.y + 1] == Player.UNKNOWN)
            d.data = weightGrid[hit.x][hit.y + 1];
        
        
        return Cell.max(a, b, c, d);
    }
    
   @Override
    public void processResult(int result, Cell lastAttempt) {
            
        resultsGrid[lastAttempt.x][lastAttempt.y] = result;
        
        if(result == MISS){
            if(hits.isEmpty())
                computeWeightGrid();
            else{
                computeWeightGrid(hits.top());
                findAllDirections = true;
            }
        }
        else if(result == HIT){
            if(findAllDirections)
                mustExplore.push(new Cell(lastAttempt.x, lastAttempt.y));
            else{
                findAllDirections = hits.isEmpty() ? true : false;
                hits.push(new Cell(lastAttempt.x, lastAttempt.y));
                computeWeightGrid(lastAttempt);
            }
        }
        else{ //a ship has been sunk. Result == size of sunk ship
            if(enemyFleet.length > 1){ //don't bother if the last ship is sunk
                resultsGrid[lastAttempt.x][lastAttempt.y] = SHIP_SUNK;
                markShipAsSunk(result); 
                handleSunkShip(result - 1, lastAttempt);
            }
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
        double sum = 0;
//        for(int whichShip = 0; whichShip < enemyFleet.length; whichShip++){
//                       
//            for(int y = 0; y < gridSize; y++){
//                for(int x = 0; x < gridSize; x++){
//                    if(isValidHorizontal(x, y, enemyFleet[whichShip]))
//                        transpose(x, y, Ship.HORIZONTAL, enemyFleet[whichShip]);
//                    if(isValidVertical(x, y, enemyFleet[whichShip]))
//                        transpose(x, y, Ship.VERTICAL, enemyFleet[whichShip]);
//                }
//            }        
//        }
        
        for(int y = 0; y < gridSize; y++){
            for(int x = 0; x < gridSize; x++){
                
                
                int whichShip = 0;
                
                while(whichShip < enemyFleet.length &&
                        !isValidHorizontal(x, y, enemyFleet[whichShip]))
                    whichShip++;
                
                while(whichShip < enemyFleet.length)
                    transpose(x, y, Ship.HORIZONTAL, enemyFleet[whichShip++]);
                
                whichShip = 0;
                
                while(whichShip < enemyFleet.length &&
                        !isValidVertical(x, y, enemyFleet[whichShip]))
                    whichShip++;
                
                while(whichShip < enemyFleet.length)
                    transpose(x, y, Ship.VERTICAL, enemyFleet[whichShip++]);
                
                sum+=weightGrid[x][y];
                
            }
        }
        
        for(int y = 0; y < gridSize; y++){
            for(int x = 0; x < gridSize; x++)
                weightGrid[x][y] /= sum;
        }
    }
    
    /*when a ship has been found, the entire weight grid is zeroed out, with
     * the exception of the row and column where the initial hit was made out
     * to enemyfleet[i] spaces.
     */
    private void computeWeightGrid(Cell hit){
       
        
//        for(int whichShip = 0; whichShip < enemyFleet.length; whichShip++){
//           
//            int fromX = hit.x - enemyFleet[whichShip] < 0 ? 
//                    0 : hit.x - enemyFleet[whichShip] + 1;
//            int fromY = hit.y - enemyFleet[whichShip] < 0 ? 
//                    0 : hit.y - enemyFleet[whichShip] + 1;
//
//            for(int i = 0; i < gridSize && fromX + i <= hit.x; i++) {
//                if(isValidHorizontal(fromX + i, hit.y, enemyFleet[whichShip] ))
//                    transpose(fromX + i, hit.y, Ship.HORIZONTAL, enemyFleet[whichShip]);
//            }
//
//            for(int i = 0; i < gridSize && fromY + i <= hit.y; i++) {
//                if(isValidVertical(hit.x, fromY + i, enemyFleet[whichShip]))
//                    transpose(hit.x, fromY + i, Ship.VERTICAL, enemyFleet[whichShip]);
//            }
//            
//        }
        
        
        int fromX = hit.x - enemyFleet[0] < 0 ? 0 : hit.x - enemyFleet[0] + 1;
        int fromY = hit.y - enemyFleet[0] < 0 ? 0 : hit.y - enemyFleet[0] + 1; 
        
        for(int i = 0; fromX + i <= hit.x; i++){
            
            int whichShip = 0;
            
            while(whichShip < enemyFleet.length &&
                    !isValidHorizontal(fromX + i, hit.y, enemyFleet[whichShip]))
                whichShip++;
                
            while(whichShip < enemyFleet.length)
                transpose(fromX + i, hit.y, Ship.HORIZONTAL, enemyFleet[whichShip++]);
        }
        
        for(int i = 0; fromY + i <= hit.y; i++){
            
            int whichShip = 0;
            while(whichShip < enemyFleet.length &&
                !isValidVertical(hit.x, fromY + i, enemyFleet[whichShip]))
                whichShip++;
                
            while(whichShip < enemyFleet.length)
                transpose(hit.x, fromY + i, Ship.VERTICAL, enemyFleet[whichShip++]);
        }
        
        weightGrid[hit.x][hit.y] = Player.HIT;
    }
    
    //sets weightGrid to 0
    private void clearWeightGrid(){
        
        for(int i = 0; i < gridSize; i++)
            for(int j = 0; j < gridSize; j++)
                weightGrid[j][i] = 0;
    }
    
    private void handleSunkShip(int size, Cell lastHit) {
        
        int i;
        
        /*removes all hits in same column/row as lastHit
         * labeling them as SHIP_SUNK on results grid. 
         */
        for(i = 0; i < size && !hits.isEmpty(); i++)
            if(hits.top().x == lastHit.x || hits.top().y == lastHit.y)
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
            computeWeightGrid(hits.top());
        }
          
        findAllDirections = false;
       
    }

    private void markShipAsSunk(int shipSize) {
        
        
        int [] outdatedEnemyFleet = enemyFleet;

        /* there is an annoying special case to consider when copying over the 
         * old enemy fleet array: there are two ships of the same size. This 
         * boolean allows us to ignore the first one and copy the second one
         * without the need for separate loops.
         */ 

        boolean searching = true; 

        enemyFleet = new int[outdatedEnemyFleet.length - 1];

        for(int i = 0, j = 0; i < outdatedEnemyFleet.length; i++){
            if(searching && outdatedEnemyFleet[i] == shipSize)
                searching = false;
            else
                enemyFleet[j++] = outdatedEnemyFleet[i];
        }

    }
}
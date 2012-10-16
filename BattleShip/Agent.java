package BattleShip;

import java.util.Random;

/**
 *
 * @author Christian
 */

class Agent extends Player {
    
    int [][] weightGrid;
    private int [] enemyFleet;
    private Stack<Point> hits;
    private Stack<Point> mustExplore;
    private boolean findAllDirections;
    
    Agent(int theSize) {
        
        super(theSize);      
        
        enemyFleet = new int[5];
        enemyFleet[0] = GamePiece.AIRCRAFT_CARRIER;
        enemyFleet[1] = GamePiece.DESTROYER;
        enemyFleet[2] = GamePiece.BATTLESHIP;
        enemyFleet[3] = GamePiece.SUBMARINE;
        enemyFleet[4] = GamePiece.PATROL_BOAT;
        
        weightGrid = new int[gridSize][gridSize];
        findAllDirections = false;
        
        hits = new Stack<Point>();
        mustExplore = new Stack<Point>();
        computeWeightGrid();
        setShipGrid(theSize);
    }
    
    
    //randomly allocates ships to Agent's shipGrid
    private void setShipGrid(int theSize) {

        int direction, x, y;
        Random r = new Random();

        for (int whichShip = 0; whichShip < 5;) {

            direction = r.nextInt(2);
            x = r.nextInt(Integer.MAX_VALUE) % theSize;
            y = r.nextInt(Integer.MAX_VALUE) % theSize;

            if (direction == GamePiece.VERTICAL 
                && canSetVerticalShip(x, y, whichShip)) {
                    setVerticalShip(x, y, whichShip);
                    whichShip++;
            } 
            else if (direction == GamePiece.HORIZONTAL 
                && canSetHorizontalShip(x, y, whichShip)) {
                    setHorizontalShip(x, y, whichShip);
                    whichShip++;
            }
        }
    }
    
    //verifies that a ship does not overlap other ships and does not fall 
    //outside of the grid. (x, y) is ships starting position and size is the 
    //ships length
    private boolean canSetVerticalShip(int x, int y, int whichShip) {

        if (y + fleet[whichShip].size() > gridSize) {
            return false;
        }

        for (int i = 0; i < fleet[whichShip].size(); i++) {
            if (shipGrid[x][y + i] != null) {
                return false;
            }
        }

        return true;
    }

    private boolean canSetHorizontalShip(int x, int y, int whichShip) {

        if (x + fleet[whichShip].size() > gridSize) {
            return false;
        }

        for (int i = 0; i < fleet[whichShip].size(); i++) {
            if (shipGrid[x + i][y] != null) {
                return false;
            }
        }

        return true;
    }
    
    
    //allocates a ship to the agent's ship grid
    private void setHorizontalShip(int x, int y,  int whichShip) {

        for (int i = 0; i < fleet[whichShip].size(); i++) {
            shipGrid[x + i][y] = fleet[whichShip];
        }
    }

    private void setVerticalShip(int x, int y, int whichShip) {

        for (int i = 0; i < fleet[whichShip].size(); i++) {
            shipGrid[x][y + i] = fleet[whichShip];
        }
    }
    
    Point generateTarget() {
        
        Point guess = new Point(0, 0, 0);

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
    
    private Point guessAroundAHit(int x, int y){
        
        //returns a Point object adjacent to (x, y)
        Point a = new Point(x + 1, y, 0);
        Point b = new Point(x - 1, y, 0);
        Point c = new Point(x , y - 1, 0);
        Point d = new Point(x, y + 1, 0);
        
        //only valid cells, and unexplored cells considered
        if(x < gridSize - 1 && resultsGrid[x + 1][y] == Player.UNKNOWN)
            a.data = weightGrid[x + 1][y];
        if(x > 0 && resultsGrid[x - 1][y] == Player.UNKNOWN)
            b.data = weightGrid[x - 1][y];
        if(y > 0 && resultsGrid[x][y - 1] == Player.UNKNOWN)
            c.data = weightGrid[x][y - 1];
        if(y < gridSize - 1 && resultsGrid[x][y + 1] == Player.UNKNOWN)
            d.data = weightGrid[x][y + 1];
        
        
        return Point.max(a, b, c, d);
    }
    
    
    /* recieves the result of the last shot from the controller. This result is
     * used to decide how to compute a weight grid, based on one of three 
     * possible scenarios.
     */
    void processResult(int result, int x, int y) {
            
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
                mustExplore.push(new Point(x, y));
            else{//hit is pushed onto stack, and the weight grid is computed 
                 //around it. Adjacent hits in same direction are preferred
                findAllDirections = hits.isEmpty() ? true : false;
                hits.push(new Point(x,y));
                computeWeightGrid(x, y);
            }
        }
        else{ //a ship has been sunk. Result == size of sunk ship
            resultsGrid[x][y] = SHIP_SUNK;
            updateEnemyFleet(result); 
            handleSunkShip(result - 1, x, y); //marks ship as sunk on results grid
        }
    }
    
    
    
    /*if isValidVertical/Horizontal returns true, a ship is "transposed" to a 
     * point x, y - the value at every cell where it located is incremented by 1
     */
    private void transpose(int x, int y, int dir, int size){
        
        for(int i = 0; i < size; i++){
            if(dir == GamePiece.HORIZONTAL)
                weightGrid[x + i][y]++;
            else
                weightGrid[x][y + i]++;
        }
        
    }
    
    /*helper methods for computeWeightGrid methods. Determine if a ship can be 
     * placed at a location [x][y].
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
     * a grid - meaning it doesn't fall out of the grid and it doesn't overlap
     * any misses or sunk ships - then the value of all of the cells that ship
     * will overlap will increase by one. This is repeated for every cell on the
     * board, in both directions, and for all five ships.
     */
    private void computeWeightGrid(){
        
        clearWeightGrid();
        
        for(int whichShip = 0; whichShip < enemyFleet.length; whichShip++){
            
            for(int y = 0; y < gridSize; y++){
                for(int x = 0; x < gridSize; x++){
                    if(isValidHorizontal(x, y, enemyFleet[whichShip]))
                        transpose(x, y, GamePiece.HORIZONTAL, enemyFleet[whichShip]);
                    if(isValidVertical(x, y, enemyFleet[whichShip]))
                        transpose(x, y, GamePiece.VERTICAL, enemyFleet[whichShip]);
                }
            }        
        } 
    }
    
    /*when a ship has been found, the entire weight grid is zeroed out, with
     * the exception of the row and column where the initial hit was made out
     * to enemyfleet[i] spaces.
     */
    private void computeWeightGrid(int x, int y){
        
        for(int k = 0; k < enemyFleet.length; k++){
        
            int fromX = x - enemyFleet[k] < 0 ? 0 : x - enemyFleet[k] + 1;
            int fromY = y - enemyFleet[k] < 0 ? 0 : y - enemyFleet[k] + 1;

            for(int i = 0; i < gridSize && fromX + i <= x; i++) {
                if(isValidHorizontal(fromX + i, y, enemyFleet[k] ))
                transpose(fromX + i, y, GamePiece.HORIZONTAL, enemyFleet[k]);
            }

            for(int i = 0; i < gridSize && fromY + i <= y; i++) {
                if(isValidVertical(x, fromY + i, enemyFleet[k]))
                    transpose(x, fromY + i, GamePiece.VERTICAL, enemyFleet[k]);
            }
            
        }
        
        weightGrid[x][y] = Player.HIT;
    }
    
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
    
    private void updateEnemyFleet(int size) {
        
        /*when a ship is sunk, the opponent returns the size of the 
         * sunk ship. This method uses that size to update the Agent's knowledge
         * of the opponent's still standing fleet. 
         */
        int ndx = 0;
        while(ndx < enemyFleet.length && enemyFleet[ndx] > size)
            ndx++;
        
        while(ndx + 1 < enemyFleet.length){
            enemyFleet[ndx] = enemyFleet[ndx + 1];
            ndx++;
        }
    }
}
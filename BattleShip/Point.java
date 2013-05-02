
package BattleShip;

class Point {
    
    public int x;
    public int y;
    public double info;
    
    public static final int MISS = 0;
    public static final int HIT = 1;
    public static final int SHIP_SUNK = 2;
    public static final int ALL_SHIPS_SUNK = 3;
    public static final int UNEXPLORED = 4;
    
    public Point(){
        this(0, 0);
    }
    
    public Point(int a, int b){
        x = a;
        y = b;
        info = Point.UNEXPLORED;
    }
    
    public Point(int a, int b, double s){
        x = a;
        y = b;
        info = s;
    }
    
    @Override
    public boolean equals(Object rhs) {

            if(!(rhs instanceof Point))
                    return false;

            Point p = (Point) rhs;

            return x == p.x && y == p.y;

    }


    public static boolean contains(Point p, Point [] arr) {
               
        for(int i = 0; i < arr.length; i++) {
            if(arr[i].equals(p)) {
                System.out.println(true);
                return true;	
            }
        }
        
        return false;
    }
    
    /*returns true if B is contained within A. Returns false if A < B and if
      not all of B is contained in A*/
    public static boolean isASubset(Point [] A, Point [] B) {
        
        if(A == null || B == null)
            return false;
        
        if(A.length < B.length)
            return false;
        
        for(int i = 0 ; i < B.length; i++) {
            
            int j = 0; 
            
            for( ; j < A.length && B[i] != A[i]; j++) 
                    ;
            
            if(j == A.length)
                return false;
            
        }
        
        return true;
    }
    
    @Override
    public String toString() {
        
        return x + ", " + y;
        
    }
}

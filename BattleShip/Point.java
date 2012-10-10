
package BattleShip;

/**
 *
 * @author Christian
 */
public class Point implements Comparable<Point>{
    
    public int x;
    public int y;
    public int data;
    
    public Point(int a, int b){
        x = a;
        y = b;
        data = Player.UNKNOWN;
    }
    
    public Point(int a, int b, int s){
        x = a;
        y = b;
        data = s;
    }
    
    
    public static Point max(Point w, Point x, Point y, Point z){
        
        Point temp;
        
        if(x.data > w.data){
            temp = w;
            w = x;
            x = temp;
        }
        if(y.data > w.data){
            temp = w;
            w = y;
            y = temp;
        }
        if(z.data > w.data){
            temp = w;
            w = z;
            z = temp;
        }
        return w;
    }

    @Override
    public int compareTo(Point rhs) {
        
        int diff = x - rhs.x;
        
        if(diff == 0)
            return y - rhs.y;
        
        else return diff;
    }
    
    @Override
    public String toString(){
        return x + ", " + y + " State: " + data;
    }

    void reset(int newX, int newY, int d) {
        x = newX;
        y = newY;
        data = d;
    }
}

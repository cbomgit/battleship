
package BattleShip;

/**
 *
 * @author Christian
 */
class Cell {
    
    public int x;
    public int y;
    public int data;
    
    public Cell(){
        this(0, 0);
    }
    
    public Cell(int a, int b){
        x = a;
        y = b;
        data = Player.UNKNOWN;
    }
    
    public Cell(int a, int b, int s){
        x = a;
        y = b;
        data = s;
    }
    
    
    public static Cell max(Cell w, Cell x, Cell y, Cell z){
        
        Cell temp;
        
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
}

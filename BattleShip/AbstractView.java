/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BattleShip;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;

/**
 *
 * @author Christian
 */
public abstract class AbstractView extends JFrame{
   
    
    public abstract void addListener(MouseListener m);
    
    public abstract void updateMessage(String s, boolean append);
    
    public abstract void updateShotGrid(int result, int x, int y);
    
    public abstract void updateShipGrid(int result, int x, int y);
    
    public abstract void paintVerticalShip(int x, int y, int size);
    
    public abstract void paintHorizontalShip(int x, int y, int size);
    
    public abstract void removeHorizontalShip(int x, int y, int size);
    
    public abstract void removeVerticalShip(int x, int y, int size);
        
    //changes the state of the game from set up to regular play
    public abstract void beginPlay(MouseListener newListener, MouseListener oldListener);
        
    public abstract Cell getCoordinatesOfMouseClick(MouseEvent e);

    public abstract void clearTheBoard(); 
    
    
}

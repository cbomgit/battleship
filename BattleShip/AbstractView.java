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
       
    public abstract void updateInstruction(String s);
    
    public abstract void updateShotGrid(int result, Cell target);
    
    public abstract void updateShipGrid(int result, Cell target);
    
    public abstract void paintVerticalShip(Cell target, int size);
    
    public abstract void paintHorizontalShip(Cell target, int size);
    
    public abstract void unpaintHorizontalShip(Cell target, int size);
    
    public abstract void unpaintVerticalShip(Cell target, int size);
    
    public abstract void addListenerToShipGrid(MouseListener m);
    
    public abstract void switchListenersToOpponentGrid(MouseListener old, MouseListener newListener);
    
    public abstract void switchListenersToGameSetUp(MouseListener old, MouseListener newListener);
        
    public abstract Cell getCoordinatesOfMouseClick(MouseEvent e);

    public abstract void clearTheBoard(); 
    
    
}

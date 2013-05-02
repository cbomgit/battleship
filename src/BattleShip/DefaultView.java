/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BattleShip;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
/**
 *
 * @author Christian
 */
class DefaultView extends AbstractView{
   
    private static final String INSTRUCTION = "Left-Click for vertical ship."
            + " Right-Click for horizontal ship.";
    
    private GridButton [][] shipGrid; //display User's ships
    private GridButton [][] hitMissGrid;//display users guesses
    JTextArea gameMessage;
    private int gridSize;
    
    public DefaultView(int theSize){
        
        gridSize = theSize;
        createGameBoard();
        layoutTheBoard();
    }
    
    //disables the users ship grid and enables the opponent grid
    //listener is removed from ship grid and a new listener is registered to the 
    //shot grid
    @Override
    public void switchListenersToOpponentGrid(MouseListener newListener, MouseListener oldListener){
        
      gameMessage.setText("Fire at your opponent");
         for(int y = 0; y < gridSize; y++)
            for(int x = 0; x < gridSize; x++){
               shipGrid[x][y].removeMouseListener(oldListener);
               shipGrid[x][y].setEnabled(false);
               hitMissGrid[x][y].setEnabled(true);
               hitMissGrid[x][y].addMouseListener(newListener);
            }
    }
    
    //cretes the objects for an empty game board
    private void createGameBoard(){
        
        shipGrid = new GridButton[gridSize][gridSize];
        hitMissGrid = new GridButton[gridSize][gridSize];
        gameMessage = new JTextArea(INSTRUCTION + " Set your Aircraft Carrier");
        
        for(int y = 0; y < gridSize; y++)
            for(int x = 0; x < gridSize; x++){
                shipGrid[x][y] = new GridButton(x, y);
                shipGrid[x][y].setEnabled(true);
                shipGrid[x][y].setBackground(Color.BLACK);
                hitMissGrid[x][y] = new GridButton(x, y);
                hitMissGrid[x][y].setEnabled(false);
                hitMissGrid[x][y].setBackground(Color.BLACK);
            }
        
        gameMessage.setEditable(false);
        gameMessage.setFont(new Font(null, Font.PLAIN, 16));
    }
    
    //lays out the board
    private void layoutTheBoard(){
        
        JPanel shipGridPanel = new JPanel();
        shipGridPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        shipGridPanel.setLayout(new GridLayout(gridSize, gridSize, 0, 0));
        
        JPanel shotGridPanel = new JPanel();
        shotGridPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        shotGridPanel.setLayout(new GridLayout(gridSize, gridSize, 0, 0));
        
        for(int y = 0; y < 11; y++)
             for(int x = 0; x < 11; x++){
                shipGridPanel.add(shipGrid[x][y]);
                shotGridPanel.add(hitMissGrid[x][y]);
            }
       
        
        //panel where all sub-panels will be placed
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(shipGridPanel, BorderLayout.EAST);
        mainPanel.add(shotGridPanel, BorderLayout.WEST);
        mainPanel.add(gameMessage, BorderLayout.NORTH);
        
        add(mainPanel);
        setTitle("BattleShip");
        setSize(800, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    //method to register a mouselistener with each grid button
    //each button contains a reference to the same listener
    @Override
    public void addListenerToShipGrid(MouseListener m){
        for(int y = 0; y < gridSize; y++)
            for(int x = 0; x < gridSize; x++)
                shipGrid[x][y].addMouseListener(m);
    }
    
    /*methods for highlighting, setting and removing a ship from the users 
     * ship grid
     */
    @Override
    public void paintVerticalShip(Cell ship, int size) {
        
        for(int i = 0; i < size; i++)
            shipGrid[ship.x][ship.y + i].setBackground(Color.GRAY);
    }
    
    @Override
    public void paintHorizontalShip(Cell ship, int size) {
        
        for(int i = 0; i < size; i++)
            shipGrid[ship.x + i][ship.y].setBackground(Color.GRAY);
    }
    
    @Override
    public void unpaintVerticalShip(Cell ship, int size){
        
        for(int i = 0; i < size; i++)
            shipGrid[ship.x][ship.y + i].setBackground(Color.BLACK);
    }
    
    @Override
    public void unpaintHorizontalShip(Cell ship, int size){
        for(int i = 0; i < size; i++)
            shipGrid[ship.x + i][ship.y].setBackground(Color.BLACK);
    }

    //refresh view to indicate result of opponent guesses
    @Override
    public void updateShotGrid(int result, Cell target) {
        
        if(result == User.MISS){
            hitMissGrid[target.x][target.y].setBackground(Color.WHITE);
            gameMessage.setText("You missed at " + target.x + ", " + target.y);
        }
        else{
            hitMissGrid[target.x][target.y].setBackground(Color.RED);
            if(result == Player.HIT)
                gameMessage.setText("You damaged CPU's ship at " + target.x + ", " + target.y);
            else
                gameMessage.setText("You sunk CPU's " + Ship.shipName(result));
        }
        
        
    }
    
    //refresh view to indicate result of user guesses
    @Override
    public void updateShipGrid(int result, Cell target) {
        if(result == User.MISS){
            shipGrid[target.x][target.y].setBackground(Color.WHITE);
            gameMessage.append("\nCPU missed at " + target.x + ", " + target.y);
        }
        else{
            shipGrid[target.x][target.y].setBackground(Color.RED);
            if(result == Player.HIT)
                gameMessage.append("\nCPU damaged your ship at " + target.x + ", " + target.y);
            else
                gameMessage.append("\nCPU sunk your " + Ship.shipName(result));
        }
        
        
        
    }
    
    
    @Override
    public void updateInstruction(String message){
        gameMessage.setText(INSTRUCTION + " " + message);
    }

    //mouse event represents a mouse click on a grid. 
    //returns (x, y) location of click
    @Override
    public Cell getCoordinatesOfMouseClick(MouseEvent e) {
        
        GridButton b = null;
        
        if(e.getComponent() instanceof GridButton)
            b = (GridButton) e.getComponent();
        
        return new Cell(b.x, b.y);
    }

    //erases the board
    @Override
    public void clearTheBoard() {
        for(int y = 0; y < gridSize; y++)
            for(int x = 0; x < gridSize; x++){
                shipGrid[x][y].setBackground(Color.BLACK);
                hitMissGrid[x][y].setBackground(Color.BLACK);
            }
    }

   @Override
   public void switchListenersToGameSetUp(MouseListener old, MouseListener newListener) {
      for(int y = 0; y < gridSize; y++)
         for(int x = 0; x < gridSize; x++){
            hitMissGrid[x][y].removeMouseListener(old);
            hitMissGrid[x][y].setEnabled(false);
            shipGrid[x][y].setEnabled(true);
            shipGrid[x][y].addMouseListener(newListener);
         }   
   }
    
    private static class GridButton extends JButton {
    
        public int x;
        public int y;
    
        GridButton(int a, int b){

            super();
            x = a;
            y = b;

        }
    }
}


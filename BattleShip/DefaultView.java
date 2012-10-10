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


//adapter class for JButton. Allows easy retrieval of a buttons position in grid
//when it is clicked.

//adapter class for JButton. Allows easy retrieval of a buttons position in grid
//when it is clicked.
/**
 *
 * @author Christian
 */
class DefaultView extends AbstractView{
    
    private GridButton [][] shipGrid;
    private GridButton [][] hitMissGrid;
    JTextArea gameMessage;
    private int gridSize;
    
    public DefaultView(int theSize){
        
        gridSize = theSize;
        resetGameBoard();
        layoutTheBoard();
    }
    
    //disables the users ship grid and enables the opponent grid
    @Override
    public void beginPlay(MouseListener newListener, MouseListener oldListener){
        
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
    private void resetGameBoard(){
        
        shipGrid = new GridButton[gridSize][gridSize];
        hitMissGrid = new GridButton[gridSize][gridSize];
        gameMessage = new JTextArea("Left-Click for vertical ship."
            + " Right-Click for horizontal ship.");
        gameMessage.append(" Set your Aircraft Carrier");
        
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
    public void addListener(MouseListener m){
        for(int y = 0; y < gridSize; y++)
            for(int x = 0; x < gridSize; x++)
                shipGrid[x][y].addMouseListener(m);
    }
    
    /*methods for highlighting, setting and removing a ship from the users 
     * ship grid
     */
    @Override
    public void colorVerticalShip(int x, int y, int size) {
        
        for(int i = 0; i < size; i++)
            shipGrid[x][y + i].setBackground(Color.GRAY);
    }
    
    @Override
    public void colorHorizontalShip(int x, int y, int size) {
        
        for(int i = 0; i < size; i++)
            shipGrid[x + i][y].setBackground(Color.GRAY);
    }
    
    @Override
    public void removeVerticalShip(int x, int y, int size){
        
        for(int i = 0; i < size; i++)
            shipGrid[x][y + i].setBackground(Color.BLACK);
    }
    
    @Override
    public void removeHorizontalShip(int x, int y, int size){
        for(int i = 0; i < size; i++)
            shipGrid[x + i][y].setBackground(Color.BLACK);
    }

    @Override
    public void updateShotGrid(int result, int x, int y) {
        
        if(result == User.MISS){
            hitMissGrid[x][y].setBackground(Color.WHITE);
            gameMessage.setText("You missed at " + x + ", " + y);
        }
        else{
            hitMissGrid[x][y].setBackground(Color.RED);
            if(result == Player.HIT)
                gameMessage.setText("You damaged CPU's ship at " + x + ", " + y);
            else
                gameMessage.setText("You sunk CPU's " + GamePiece.shipName(result));
        }
        
        
    }

    @Override
    public void updateShipGrid(int result, int x, int y) {
        if(result == User.MISS){
            shipGrid[x][y].setBackground(Color.WHITE);
            gameMessage.append("\nCPU missed at " + x + ", " + y);
        }
        else{
            shipGrid[x][y].setBackground(Color.RED);
            if(result == Player.HIT)
                gameMessage.append("\nCPU damaged your ship at " + x + ", " + y);
            else
                gameMessage.append("\nCPU sunk your " + GamePiece.shipName(result));
        }
        
        
        
    }
    
    @Override
    public void updateMessage(String message, boolean append){
        if(append)
            gameMessage.append(message);
        else
            gameMessage.setText(message);
    }

    @Override
    public Point getCoordinates(MouseEvent e) {
        
        GridButton b = null;
        if(e.getComponent() instanceof GridButton)
            b = (GridButton) e.getComponent();
        
        return new Point(b.x, b.y);
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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Christian
 */

package BattleShip;

import java.util.NoSuchElementException;


public class Stack<AnyType> {
    
    private Node<AnyType> topOfStack;
    
    public Stack(){
        topOfStack = null;
    }

    public void push(AnyType item) {

        topOfStack = new Node<AnyType>(item, topOfStack);
        
    }

    public AnyType pop() {

        if (isEmpty())
             throw new NoSuchElementException("Stack is empty");
        
        AnyType old = topOfStack.element;
        topOfStack = topOfStack.next;
        
        return old;
    }
    
    public void makeEmpty() {
        topOfStack = null;
    }

    public boolean isEmpty() {

        return topOfStack == null;

    }

    public AnyType top() {

            if (isEmpty())
                    throw new NoSuchElementException("Stack is empty");
            else
                    return topOfStack.element;

    }

    public void clearStack(){

        topOfStack = null;

    }

    AnyType next() {
        return topOfStack.next.element;
    }

    void printStack() {
        
        Node<AnyType> current = topOfStack;
        
        while(current != null){
            System.out.println(current.element);
            current = current.next;
        }
    }

    
    
    
    
}
class Node<AnyType>{
    
    public Node(AnyType item){
        this(item, null);

    }

    public Node(AnyType item, Node<AnyType> node){
        element = item;
        next = node;
    }

    public AnyType element;
    public Node<AnyType> next;
}
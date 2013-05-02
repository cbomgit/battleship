/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BattleShip;

/**
 *
 * @author christian
 */
public class LinkedList<AnyType> {
    
    
    private Node<AnyType> head;
    private Node<AnyType> tail;
    
    public LinkedList() {
        head = new Node<AnyType>(null, null, null);
        tail = new Node<AnyType>(null, null, head);
        head.next = tail;
    }
    
    public boolean isEmpty() {
        return head.next == tail;
    }
    
    public void makeEmpty() {
        head.next = tail;
        tail.previous = head;
    }
    
    Iterator<AnyType> zeroth() {
        return new Iterator<AnyType>(head);
    }
    
    Iterator<AnyType> first() {
        return new Iterator<AnyType>(head.next);
    }
    
    Iterator<AnyType> last() {
        return new Iterator<AnyType>(tail.previous);
    }
    
    Iterator<AnyType>tail() {
        return new Iterator<AnyType>(tail);
    }
    
    public void insertBefore(Iterator<AnyType> pos, AnyType x) {
        
        if(pos != null && pos.current != head) {
            Node<AnyType> newNode = new Node<AnyType>(x, pos.current, pos.current.previous);
            pos.current.previous.next = newNode;
            pos.current.previous = newNode;
        }
    }
    
    public void insertAfter(Iterator<AnyType> pos, AnyType x) {
        if(pos != null && pos.current != tail) {
            Node<AnyType> newNode = new Node<AnyType>(x, pos.current.next, pos.current);
            pos.current.next.previous = newNode;
            pos.current.next = newNode;
        }
    }
    
    public void removeNext(Iterator<AnyType> pos) {
        
        if(pos != null && pos.current != tail && pos.current != tail.previous) {
            pos.current.next.next.previous = pos.current;
            pos.current.next = pos.current.next.next;
        }
    }
    
    public void removePrevious(Iterator<AnyType> pos) {
        
        if(pos != null && pos.current != head && pos.current != head.next) {
            pos.current.previous.previous.next = pos.current;
            pos.current.previous = pos.current.previous.previous;
        }
    }
    
    private static class Node<AnyType> {
        
        private AnyType element;
        private Node<AnyType> next;
        private Node<AnyType> previous;
        
        public Node(AnyType elem, Node<AnyType> n, Node<AnyType> p) {
            
            element = elem;
            next = n;
            previous = p;
        }
        
        public Node(AnyType elem) {
            this(elem, null, null);
        }
    }
    
    public class Iterator<AnyType> {
        
        private Node<AnyType> current;
        
        Iterator(Node<AnyType> owner) {
            current = owner;
        }
        
        public boolean hasNext() {
            return current.next != tail;
        }
        
        public boolean hasPrevious() {
            return current.previous != head;
        }
        
        public AnyType getNext() {
            return hasNext() ? current.element : null;
        }
        
        public AnyType getPrevious() {
            return hasPrevious() ? current.element : null;
        }
        
        public void advance() {
            
            if(hasNext())
                current = current.next;
        }
        
        public void retreat() {
            
            if(hasPrevious())
                current = current.previous;
        }
    }
    
}

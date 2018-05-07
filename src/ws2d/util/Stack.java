package ws2d.util;

import java.util.Arrays;

/**
 * This class allows for stack operations on an array of objects.
 * 
 * @author Ian
 * @param <T> the type of data stored in the stack.
 */
public class Stack<T> {
    /**
     * The data.
     */
    private Object[] data;
    /**
     * The capacity of the stack.
     */
    private int capacity;
    /**
     * The current top element of the stack.
     */
    private int top;
    
    /**
     * The default capacity.
     */
    private static final int DEFAULT_CAPACITY = 16;
    
    /**
     * Constructs a new instance of <code>Stack</code> with a specified initial
     * capacity.
     * 
     * @param initialCapacity the initial capacity.
     */
    public Stack(int initialCapacity) {
        if(initialCapacity < 0)
            initialCapacity = 0;
        this.data = new Object[initialCapacity];
        this.capacity = initialCapacity;
        this.top = -1;
    }
    
    /**
     * Constructs a new instance of <code>Stack</code> with the default capacity.
     */
    public Stack() {
        this(DEFAULT_CAPACITY);
    }
    
    /**
     * Ensures that the stack can support the number of elements specified.
     * 
     * @param minCapacity the required minimum capacity.
     */
    protected void ensureCapacity(int minCapacity) {
        if(minCapacity - data.length > 0)
            grow(minCapacity);
    }
    
    /**
     * Grows the stack to fit the minimum required capacity.
     * 
     * @param minCapacity the new minimum capacity.
     */
    protected void grow(int minCapacity) {
        if(minCapacity < 0)
            throw new OutOfMemoryError();
        data = Arrays.copyOf(data, minCapacity);
    }
    
    /**
     * Ensures that the stack can support its current size plus the number of items
     * specified.
     * 
     * @param amount the amount to allocate.
     */
    public void allocate(int amount) {
        if(amount <= 0)
            return;
        ensureCapacity(capacity + amount);
    }
    
    /**
     * Shrinks the stack by the specified amount.
     * 
     * @param amount the amount to shrink the stack by.
     */
    public void deallocate(int amount) {
        if(amount <= 0)
            return;
        amount = Math.min(capacity - top - 1, amount);
        data = Arrays.copyOf(data, amount);
    }
    
    /**
     * Whether or not the stack is empty.
     * 
     * @return <code>true</code>, if the stack is empty, <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return top == -1;
    }
    
    /**
     * Whether or not the stack is full.
     * 
     * @return <code>true</code>, if the stack is full, <code>false</code> otherwise.
     */
    public boolean isFull() {
        return top == capacity - 1;
    }
    
    /**
     * Pushes an object on top of the stack.
     * 
     * @param object the object to push.
     */
    public void push(T object) {
        if(isFull())
            ensureCapacity(capacity + 1);
        data[++top] = object;
    }
    
    /**
     * Pops an object off the top of the stack.
     * 
     * @return the object on the top of the stack.
     */
    public T pop() {
        if(isEmpty())
            return null;
        return (T)data[top--];
    }
    
    /**
     * Returns the object on top of the stack, but does not remove it.
     * 
     * @return the object on top of the stack.
     */
    public T peek() {
        if(isEmpty())
            return null;
        return (T)data[top];
    }
}

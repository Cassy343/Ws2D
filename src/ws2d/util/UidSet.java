package ws2d.util;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * This class defines a set in which each object has a unique ID, and in which the
 * objects are accessed via their unique ID.
 * 
 * @author Ian
 * @param <T> the type of object to store.
 */
public class UidSet<T extends UniquelyIdentifiableObject> {
    /**
     * The data.
     */
    private Object[] data;
    /**
     * The capacity of the set.
     */
    private int capacity;
    /**
     * The last element in the set.
     */
    private int top;
    /**
     * Unique IDs which are available to be taken.
     */
    private Stack<Integer> openUids;
    
    /**
     * The default capacity.
     */
    private static final int DEFAULT_CAPACITY = 16;
    
    /**
     * Constructs a new instance of <code>UidSet</code> with a specified initial
     * capacity.
     * 
     * @param initialCapacity the initial capacity.
     */
    public UidSet(int initialCapacity) {
        if(initialCapacity < 0)
            initialCapacity = 0;
        this.data = new Object[initialCapacity];
        this.capacity = initialCapacity;
        this.top = -1;
        this.openUids = new Stack(initialCapacity);
    }
    
    /**
     * Constructs a new instance of <code>UidSet</code> with the default capacity.
     */
    public UidSet() {
        this(DEFAULT_CAPACITY);
    }
    
    /**
     * Ensures that the UID set can support the number of elements specified.
     * 
     * @param minCapacity the required minimum capacity.
     */
    protected void ensureCapacity(int minCapacity) {
        if(minCapacity - data.length > 0)
            grow(minCapacity);
    }
    
    /**
     * Grows the UID set to fit the minimum required capacity.
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
     * Shrinks the UID set by the specified amount.
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
     * Whether or not the UID set is full.
     * 
     * @return <code>true</code>, if the UID set is full, <code>false</code> otherwise.
     */
    public boolean isFull() {
        return top == capacity - 1 && openUids.isEmpty();
    }
    
    /**
     * Adds the object to the UID set and changes its UID to the appropriate value.
     * 
     * @param object the object to add.
     * @return the UID of the object.
     */
    public int add(T object) {
        if(isFull())
            ensureCapacity(capacity + 1);
        int uid;
        if(openUids.isEmpty())
            uid = ++ top;
        else
            uid = openUids.pop();
        object.setUid(uid);
        data[uid] = object;
        return uid;
    }
    
    /**
     * Inserts the object into the UID set while preserving the UID of the object
     * if possible.
     * 
     * @param object the object to add.
     * @return <code>true</code>, if the object was added, <code>false</code> otherwise.
     */
    public boolean insert(T object) {
        if(object.getUid() < 0 || object.getUid() >= capacity || isFull() || data[object.getUid()] != null)
            return false;
        data[object.getUid()] = object;
        return true;
    }
    
    /**
     * Gets an object by UID.
     * 
     * @param uid the object's UID.
     * @return the object with the specified UID, or <code>null</code> if that object
     * could not be found.
     */
    public T get(int uid) {
        return (T)data[uid];
    }
    
    /**
     * Removes an object from the UID set.
     * 
     * @param object the object to remove.
     */
    public void remove(T object) {
        remove(object.getUid());
    }
    
    /**
     * Removes an object from this UID set by UID.
     * 
     * @param uid the UID of the object to remove.
     * @return the object that was removed, or <code>null</code> if that object
     * was no removed.
     */
    public T remove(int uid) {
        if(data[uid] == null)
            return null;
        T object = (T)data[uid];
        data[uid] = null;
        openUids.push(uid);
        return object;
    }
    
    /**
     * Iterates through each of the elements in the UID set and performs an action
     * with it.
     * 
     * @param action the action to perform with each element.
     */
    public void forEach(Consumer<? super T> action) {
        for(Object obj : data) {
            if(obj != null)
                action.accept((T)obj);
        }
    }
    
    /**
     * Returns a stream of the elements in this UID set.
     * 
     * @return a stream of the elements in this UID set.
     */
    public Stream<T> stream() {
        return Arrays.<T>asList((T[])data).stream().filter(obj -> obj != null);
    }
}

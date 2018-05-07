package ws2d.util;

/**
 * Defines an object which can be uniquely identified through an integer unique
 * ID (UID).
 * 
 * @author Ian
 */
public abstract class UniquelyIdentifiableObject {
    /**
     * The object's unique ID.
     */
    protected int uid;
    
    /**
     * Constructs a new instance of <code>UniquelyIdentifiableObject</code> with
     * a specified unique ID.
     * 
     * @param uid the unique ID.
     */
    protected UniquelyIdentifiableObject(int uid) {
        this.uid = uid;
    }
    
    /**
     * Constructs a new instance of <code>UniquelyIdentifiableObject</code> with
     * a unique ID of <code>-1</code>.
     */
    protected UniquelyIdentifiableObject() {
        this(-1);
    }
    
    /**
     * Sets the unique ID (UID) of this object to the specified UID.
     * 
     * @param uid the new UID.
     */
    public final void setUid(int uid) {
        this.uid = uid;
    }
    
    /**
     * Returns the unique ID of this object.
     * 
     * @return the unique ID of this object.
     */
    public final int getUid() {
        return uid;
    }
}

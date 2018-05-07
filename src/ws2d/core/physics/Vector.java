package ws2d.core.physics;

/**
 * The basic mathematical value in the Ws2D physics engine.
 * 
 * @author Ian
 */
public class Vector {
    /**
     * The vector's x value.
     */
    public double x;
    /**
     * The vector's y value.
     */
    public double y;
    
    /**
     * Constructs a new instance of <code>Vector</code> with specified x and y values.
     * 
     * @param x the x value.
     * @param y the y value.
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Constructs a new instance of <code>Vector</code> with x and y values of
     * <code>0.0</code>.
     */
    public Vector() {
        this(0.0D, 0.0D);
    }
}

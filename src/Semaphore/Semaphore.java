package Semaphore;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A <code>Semaphore</code> object implements a semaphore (invented by Edsger
 * Dijkstra).
 * <p>
 * A semaphore controls access to resources. The number of resources currently
 * available is the Semaphore <em>value</em>.
 * </p>
 *
 * @see <a href="http://en.wikipedia.org/wiki/Semaphore_(programming)">wikipedia
 * article</a>
 * @author Mansur Ahmed
 */
public class Semaphore {
    private int value;

    /**
     * Create a semaphore.
     * @param value The initial value of the Semaphore ( must be &ge; 0).
     */
    public Semaphore(int value) {
        this.value = value;
    }
    /**
     * Increment the number of available resources.  This method never blocks.
     * It may wakeup a Thread waiting for the Semaphore. 
     */
    public synchronized void up(){
        if (this.value == 0) {
            notify();
        }
        this.value++;
    }
    
    /**
     * Request a resource. If no resources are available, the calling Thread
     * block until a resource controlled by the Semaphore becomes available.
     * @throws java.lang.InterruptedException
     */
    public synchronized void down() {
        if (this.value == 0) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Semaphore.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        this.value--;
    }
    
    public synchronized int getValue() {
        return this.value;
    }
}

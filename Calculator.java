import java.rmi.*;
import java.util.List;

public interface Calculator extends Remote {
    /** Pushes an integer value onto the shared stack. */
    void pushValue(int val) throws RemoteException;
    List<Integer> getStack() throws RemoteException;

    /** Returns true if the stack is empty, false otherwise. */
    boolean isEmpty() throws RemoteException;

    /** Pops and returns the top integer from the stack. */
    int pop() throws RemoteException;

    /**
     * Pushes an operator ("min", "max", "gcd", "lcm").
     * Server will pop ALL current values on the stack, compute the aggregate result,
     * and push that single result back onto the stack.
     */
    void pushOperation(String operator) throws RemoteException;

    /**
     * Sleeps for the given milliseconds, then performs a pop and returns it.
     * Does not hold the server lock during the sleep (i.e., stack can change while waiting).
     */
    int delayPop(int millis) throws RemoteException;
}
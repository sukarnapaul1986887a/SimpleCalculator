import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe implementation of the Calculator service.
 *
 * Concurrency notes:
 * - A single shared stack is maintained across all clients (as per base spec).
 * - Methods use a ReentrantLock to guard stack mutations.
 */
public class CalculatorImplementation extends UnicastRemoteObject implements Calculator {
    private final Deque<Integer> stack = new ArrayDeque<>();
    private final ReentrantLock lock = new ReentrantLock();

    public CalculatorImplementation() throws RemoteException {
        super();
    }
    
    @Override
    public void pushValue(int val) throws RemoteException {
        lock.lock();
        try {
            stack.push(val);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public synchronized List<Integer> getStack() throws RemoteException {
        return new ArrayList<>(stack);
    }

    @Override
    public boolean isEmpty() throws RemoteException {
        lock.lock();
        try {
            return stack.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int pop() throws RemoteException {
        lock.lock();
        try {
            if (stack.isEmpty()) {
                throw new RemoteException("pop called on empty stack");
            }
            return stack.pop();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void pushOperation(String operator) throws RemoteException {
        String op = operator == null ? "" : operator.trim().toLowerCase();
        List<Integer> values = new ArrayList<>();

        lock.lock();
        try {
            // Pop everything currently available on the stack
            while (!stack.isEmpty()) {
                values.add(stack.pop());
            }
        } finally {
            lock.unlock();
        }

        if (values.isEmpty()) {
            // Per assignment assumptions, this should not happen.
            // For robustness, push nothing and throw an error.
            throw new RemoteException("pushOperation called with empty stack");
        }

        // Compute aggregate result
        int result;
        switch (op) {
            case "min":
                result = values.stream().min(Integer::compareTo).get();
                break;
            case "max":
                result = values.stream().max(Integer::compareTo).get();
                break;
            case "gcd":
                result = values.stream().reduce(values.get(0), CalculatorImplementation::gcd);
                break;
            case "lcm":
                result = values.stream().reduce(values.get(0), CalculatorImplementation::lcm);
                break;
            default:
                throw new RemoteException("Unsupported operator: " + operator);
        }

        // Push the result back
        lock.lock();
        try {
            stack.push(result);
        } finally {
            lock.unlock();
        }
    }

    /** Greatest common divisor using Euclid's algorithm. */
    private static int gcd(int a, int b) {
        a = Math.abs(a); b = Math.abs(b);
        if (a == 0) return b;
        if (b == 0) return a;
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return a;
    }

    /** Least common multiple computed via gcd */
    private static int lcm(int a, int b) {
        a = Math.abs(a); b = Math.abs(b);
        if (a == 0 || b == 0) return 0;
        long prod = (long) a * (long) b;
        int g = gcd(a, b);
        long l = prod / g;
        return (int) l;
    }

    @Override
    public synchronized int delayPop(int millis) throws RemoteException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return pop();
    }
}

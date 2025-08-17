import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class CalculatorImpl extends UnicastRemoteObject implements Calculator {
    private final Deque<Integer> stack = new ArrayDeque<>();
    private final ReentrantLock lock = new ReentrantLock();

    public CalculatorImpl() throws RemoteException {
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
}

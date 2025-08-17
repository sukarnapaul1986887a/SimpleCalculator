import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalculatorServer {
    public static final String BIND_NAME = "CalculatorService";

    public static void main(String[] args) {
        try {
            try {
                LocateRegistry.createRegistry(1099);
                System.out.println("RMI registry started on port 1099.");
            } catch (Exception exp) {
                System.out.println("RMI registry  likely already running: " + exp.getMessage());
            }

            Calculator impl = new CalculatorImpl();
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(BIND_NAME, impl);
        } catch (Exception exp) {
            exp.printStackTrace();
            System.exit(1);
        }
    }
}

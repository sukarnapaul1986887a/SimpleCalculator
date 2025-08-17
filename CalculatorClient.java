import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalculatorClient {
    public static void main(String[] args) throws Exception {
        Calculator calc = lookup();
        System.out.println("Stack is empty: " + calc.isEmpty());
        calc.pushValue(49);
        System.out.println("Stack is empty: " + calc.isEmpty());
        System.out.println("Stack is " + calc.getStack());
    }

    public static Calculator lookup() throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        return (Calculator) registry.lookup(CalculatorServer.BIND_NAME);
    }

}

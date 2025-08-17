import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CalculatorClient {
    public static void main(String[] args) throws Exception {
        Calculator calc = lookup();
        System.out.println("Stack is empty: " + calc.isEmpty());
        calc.pushValue(49);
        System.out.println("Stack is empty: " + calc.isEmpty());
        System.out.println("Stack is " + calc.getStack());

        System.out.println("Pop is called");
        calc.pop();
        System.out.println("Stack is empty: " + calc.isEmpty());

        calc.pushValue(49);
        calc.pushValue(7);
        System.out.println("Stack is " + calc.getStack());
        calc.pushOperation("gcd");
        int gcd = calc.pop();
        System.out.println("gcd pop => " + gcd + " (expected 7)");
        System.out.println("Stack is empty: " + calc.isEmpty());

        calc.pushValue(49);
        calc.pushValue(7);
        System.out.println("Stack is " + calc.getStack());
        calc.pushOperation("lcm");
        int lcm = calc.pop();
        System.out.println("lcm pop => " + lcm + " (expected 49)");
        System.out.println("Stack is empty: " + calc.isEmpty());

        calc.pushValue(49);
        calc.pushValue(7);
        calc.pushValue(100);
        System.out.println("Stack is " + calc.getStack());
        calc.pushOperation("max");
        int maxNum = calc.pop();
        System.out.println("maxNum pop => " + maxNum + " (expected 100)");
        System.out.println("Stack is empty: " + calc.isEmpty());

        calc.pushValue(49);
        calc.pushValue(7);
        calc.pushValue(100);
        System.out.println("Stack is " + calc.getStack());
        calc.pushOperation("min");
        int minNum = calc.pop();
        System.out.println("minNum pop => " + minNum + " (expected 7)");
        System.out.println("Stack is empty: " + calc.isEmpty());
    }

    public static Calculator lookup() throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        return (Calculator) registry.lookup(CalculatorServer.BIND_NAME);
    }

}

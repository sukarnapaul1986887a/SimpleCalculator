import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Simple interactive/test client. By default runs a demo scenario.
 * Usage(Commands):
 *   java CalculatorClient                # run demo
 *   java CalculatorClient demo           # run demo
 *   java CalculatorClient multi 3        # spawn 3 concurrent client threads
 */
public class CalculatorClient {
    public static void main(String[] args) throws Exception {
        String mode = args.length > 0 ? args[0] : "demo";
        int n = args.length > 1 ? Integer.parseInt(args[1]) : 3;

        switch (mode) {
            case "demo":
                runDemo();
                break;
            case "multi":
                runMulti(n);
                break;
            default:
                System.out.println("Unknown mode: " + mode);
        }
    }

    /** Single-client demonstration of all methods. */
    private static void runDemo() throws Exception {
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

        calc.pushValue(4);
        calc.pushValue(10);
        calc.pushValue(6);
        calc.pushOperation("lcm");
        int lcm2 = calc.delayPop(300);
        System.out.println("delayPop(300) => " + lcm2 + " (expected 60)");

        System.out.println("Stack empty? " + calc.isEmpty());
        System.out.println("--- Demo complete ---");
    }

    public static Calculator lookup() throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        return (Calculator) registry.lookup(CalculatorServer.BIND_NAME);
    }

    /** Multi-client (multi-thread) test. Each thread behaves like a client. */
    private static void runMulti(int clients) throws Exception {
        System.out.println("--- Multi-client test with " + clients + " clients ---");
        Thread[] threads = new Thread[clients];

        for (int i = 0; i < clients; i++) {
            threads[i] = new Thread(() -> {
                try {
                    runDemo();      // function reused
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // start all threads
        for (Thread t : threads) {
            t.start();
        }

        // wait for all to finish
        for (Thread t : threads) {
            t.join();
        }

        // after all clients are done, print final stack
        Calculator calc = lookup();
        System.out.println("Final stack after multi-client run: " + calc.getStack());
    }
}

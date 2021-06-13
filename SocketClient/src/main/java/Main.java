public class Main {

    public static String ipAddr = "localhost";

    /**
     @param args
     */

    public static void main(String[] args) {
        int PORT = 0;
        try {
            if (args[0].startsWith("--port-server="))
                PORT = Integer.parseInt(args[0].substring(14));
            else
                throw new ArrayIndexOutOfBoundsException();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Add --port-server=$PORT");
            System.exit(1);
        }
        try {
            new ClientStructure(ipAddr, PORT);
        } catch (NullPointerException e) {
            System.out.println("Server closed");
        }
    }
}
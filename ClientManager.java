import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.IOException;
import java.net.Socket;

public class ClientManager {

    public static void main(String[] args) {
        System.out.println("Welcome to the best online Store!\n Please enter the action you want to execute.\n");
        Scanner scanner = new Scanner(System.in);
        int action = -1;
        Client client = new Client();

        do {
            System.out.println("These are the actions you can do:\n" +
                                "1. Sign in\n" +
                                "2. Sign up\n" +
                                "0. Disconnect\n\n");
            try {
                action = Integer.parseInt(scanner.next());
            } catch (NumberFormatException nfe) {
                System.out.println("Please enter a valid number.\n");
                continue;
            } 
            switch(action) {
                case 0 : 
                    System.out.println("Thank you for your time :). We hope you're visiting us again!\n");
                    System.exit(0);
                    break;
                case 1 : 
                    client = signIn(scanner);
                    break;
                case 2: 
                    client = signUp(scanner);
                    break;
                default: 
                    System.out.println("Please enter a valid number.\n");
                    continue;
            }
            break;
        } while(true);
        try {
            Socket socket = new Socket("localhost", 1234);
            ClientConnection clientConnection = new ClientConnection(socket, client);
            clientConnection.connect();
        } catch (IOException ioe) {}
        
        //scanner.close();
    }

    private static Client signIn(Scanner scanner) {
        String username = "";
        String password = "";
        
        System.out.printf("\n\nPlease enter your username: ");
        username = scanner.next();
        System.out.printf("\n\nNow please enter your password: ");
        password = scanner.next();
        
        Client nuevoCliente = new Client();
        nuevoCliente.setUsername(username);
        nuevoCliente.setPassword(password);
        return nuevoCliente;
    }

    private static Client signUp(Scanner scanner) {
        String name = "";
        String username = "";
        String password = "";
        
        System.out.printf("\n\nPlease enter your name (It doesn't need to be full name): ");
        name = scanner.next();
        System.out.printf("\n\nPlease enter your username: ");
        username = scanner.next();
        System.out.printf("\n\nNow please enter your password: ");
        password = scanner.next();

        return new Client();
    }
}
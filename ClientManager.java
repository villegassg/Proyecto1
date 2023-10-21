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
                    try {
                        Socket socket = new Socket("localhost", 1234);
                        ProxyClient proxy = new ProxyClient(client);
                        ClientConnection clientConnection = new ClientConnection(socket, proxy);
                        clientConnection.signIn();
                        //clientConnection.connect();
                    } catch (IOException ioe) {}
                    break;
                case 2: 
                    client = signUp(scanner);
                    try {
                        Socket socket = new Socket("localhost", 1234);
                        ProxyClient proxy = new ProxyClient(client);
                        ClientConnection clientConnection = new ClientConnection(socket, proxy);
                        clientConnection.signUp();
                        //clientConnection.connect();
                    } catch (IOException ioe) {}
                    break;
                default: 
                    System.out.println("Please enter a valid number.\n");
                    continue;
            }
            break;
        } while(true);
        
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
        long phoneNumber = 0;
        String address = "";
        long bankAccount = 0;
        String country = "";
        double money = 0;
        
        System.out.printf("\n\nPlease enter your name (It doesn't need to be full name): ");
        name = scanner.next();
        System.out.printf("\n\nPlease enter your username: ");
        username = scanner.next();
        System.out.printf("\n\nPlease enter your password: ");
        password = scanner.next();
        System.out.println("\n\nPlease enter your phone number (Without spaces, hyphens, etc.): ");
        phoneNumber = scanner.nextLong();
        System.out.println("\n\nPlease enter your address");
        address = scanner.next();
        System.out.println("\n\nPlease enter your bank account (No spaces, hyphens, etc.): ");
        bankAccount = scanner.nextLong();
        while (true) {
            System.out.println("\n\nPlease enter your country (México / United States / España): ");
            country = scanner.next();
            if (country.equals("México") || country.equals("Mexico") || 
                country.equals("United States") || country.equals("USA") ||
                country.equals("España")) 
                break;
            System.out.println("Invalid name for a country, please enter it with capital letter.");
        }
        System.out.println("\n\nPlease enter the money you would like to have in your account: ");
        money = scanner.nextDouble();

        return new Client(username, password, name, phoneNumber, address, bankAccount, country, money);
    }
}
import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientConnection {
    
    private Connection connection;
    private Client client;
    private boolean isConnected;

    public ClientConnection(Socket socket, Client client) {
        try {
            this.connection = new Connection(socket);
            connection.addListener((c, m) -> receivedMessage(c, m));
            isConnected = true;
            new Thread(() -> connection.receiveMessages()).start();
        } catch (IOException ioe) {
            System.out.println("\nClientConnectionException:\n");
            ioe.printStackTrace();
        }
        this.client = client;
    }

    public void receivedMessage(Connection connection, String message) {
        if (isConnected) {
            switch(message) {
                case "DATABASEREQUESTED" : 
                    System.out.println("No se puede solicitar la base de datos al cliente.\n");
                    break;
                case "DATABASERECEIVED" : 
                    break;
                case "DISCONNECT" : 
                    System.out.println("Thank you for your time! We hope you're visiting us again!\n");
                    System.exit(0);
                    break;
                case "STOPSERVICE" : break;
                case "INVALID" : 
                    System.out.println("Invalid message received, now the connection is insecure and we have to finish it.\n");
                    break;
                default : 
                    database(message);
                    break;
            }
        }
    }

    public void database(String message) {
        System.out.println(message);
    }

    public void connect() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nWhat would you like to do now?\n");
        int action = -1;
        
        do {
            System.out.println("Actions to do with the server:\n" + 
                                "1. Enter the store.\n" +
                                "2. Disconnect.\n" +
                                "3. StopService.\n");
            try {
                action = Integer.parseInt(scanner.next());
            } catch (NumberFormatException nfe) {
                System.out.println("NumberFormatException has ocurred. Please enter a valid action.\n");
            } catch (NoSuchElementException nsee) {
                break;
            }
            switch(action) {
                case 1: 
                    try {
                        connection.sendMessage("DATABASEREQUESTED");
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    break;
                case 2: 
                    try {
                        connection.sendMessage("DISCONNECT");
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    break;
                case 3: 
                    try {
                        connection.sendMessage("STOPSERVICE");
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    break;
                default :
                    System.out.println("Please enter a valid action.\n");
                    
                    break;
            }
            
        } while (true);
        scanner.close();
    }
}

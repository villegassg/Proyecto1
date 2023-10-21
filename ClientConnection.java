import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientConnection {
    
    private Connection connection;
    private ProxyClient client;
    private boolean isConnected;

    public ClientConnection(Socket socket, ProxyClient client) {
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
        if(isConnected) {
            if (message.startsWith("SIGNUP")) {
                System.out.println(message.substring("SIGNUP".length()));
            } else if (message.startsWith("CONNECT")) {
                connect();
            } else if (message.startsWith("DISCONNECT")) {
                System.out.println("Thank you for your time! We hope you're visiting us again!\n");
                System.exit(0);
            } else if (message.startsWith("INVALID")) {
                System.out.println(message.substring("INVALID".length()));
                System.out.println("Invalid message received, now the connection is insecure and we have to finish it.\n");
                try {
                    connection.sendMessage("INVALID");
                } catch (IOException ioe) {}
            } else {
                System.out.println(message);
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
                        connection.sendMessage("CLIENT".concat(client.toString()));
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

    public void signIn() {
        String signIn = "SIGNIN".concat(client.toString2());
        
        try {
            connection.sendMessage(signIn);
        } catch (IOException ioe) {
            System.out.println("Could not sign in.\n");
        }
    }

    public void signUp() {
        try {
            connection.sendMessage("SIGNUP".concat(client.toString2()));
        } catch (IOException ioe) {
            System.out.println("Could not sign up.\n");
        }
    }

    private boolean clientRequest() {
        try {
            connection.sendMessage("CLIENTREQUEST");
        } catch (IOException ioe) {
            System.err.println("\nCould not enter the store for some reason :(\n");
            return false;
        }
        return true;
    }
}

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
            } else if (message.startsWith("DATABASEREQUESTED")) {
                database(message.substring("DATABASEREQUESTED".length()));
            } else if (message.startsWith("CATALOGUE")) {
                database(message.substring("CATALOGUE".length()));
            } else if (message.startsWith("PURCHASEMODE")) {
                System.out.println("Entering the purchase mode.\n");
            } else if (message.startsWith("OPTIONS")) {
                String options = message.substring("OPTIONS".length());
                options = options.replaceAll("\t", "\n");
                System.out.println(options);
                purchase();
            } else if (message.startsWith("ADDEDTOCART")) {
                String success = message.substring("ADDEDTOCART".length());
                System.out.println(success);
            } else if (message.startsWith("FAILEDADDTOCART")) {
                String fail = message.substring("FAILEDADDTOCART".length());
                System.out.println(fail);
            } else if (message.startsWith("FAILEDREMOVEFROMCART")) {
                String fail = message.substring("FAILEDREMOVEFROMCART".length());
                System.out.println(fail);
            } else if (message.startsWith("REMOVEFROMSHOPPINGCART")) {
                String success = message.substring("REMOVEFROMSHOPPINGCART".length());
                System.out.println(success);
            } else if (message.startsWith("NOPRODUCT")) {
                String fail = message.substring("NOPRODUCT".length());
                System.out.println(fail);
            } else if (message.startsWith("PRINTSHOPPINGCART")) {
                String sc = message.substring("PRINTSHOPPINGCART".length());
                sc = sc.replaceAll("\t", "\n");
                System.out.println(sc);
            } else if (message.startsWith("EMPTYSHOPPINGCART")) {
                String fail = message.substring("EMPTYSHOPPINGCART".length());
                System.out.println(fail);
            } else if (message.startsWith("PURCHASESHOPPINGCART")) {
                String success = message.substring("PURCHASESHOPPINGCART".length());
                System.out.println(success);
            } else if (message.startsWith("NOTENOUGHMONEY")) {
                String fail = message.substring("NOTENOUGHMONEY".length());
                System.out.println(fail);
            } else if (message.startsWith("FAILEDPURCHASE")) {
                String fail = message.substring("FAILEDPURCHASE".length());
                System.out.println(fail);
            } else if (message.startsWith("PURCHASE")) {
                String success = message.substring("PURCHASE".length());
                System.out.println(success);
            } else if (message.startsWith("DISCONNECT")) {
                String m = message.substring("DISCONNECT".length());
                System.out.println(m + "\n");
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

    private void purchase() {
        Scanner scanner = new Scanner(System.in);
        int action = -1;

        try {
            action = Integer.parseInt(scanner.next());
        } catch (NumberFormatException nfe) {
            System.out.println("NumberFormatException has ocurred. Please enter a valid action.\n");
        } catch (NoSuchElementException nsee) {
            
        }
        switch(action) {
            case 1:
                System.out.println("\nPlease enter the name of the product you'd like " +
                                        "to add to your shopping cart: ");
                String product1 = scanner.next();
                try {
                    connection.sendMessage("ADDTOCART" + client.toString2() + 
                                            "Product: " + product1);
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }
                break;
            case 2:
                System.out.println("\nPlease enter the name of the product you'd like " +
                                        "to remove from your shopping cart: ");
                String product2 = scanner.next();
                try {
                    connection.sendMessage("REMOVEFROMSHOPPINGCART" + 
                                            client.toString2() +
                                            "Product: " + product2);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                break;
            case 3: 
                try {
                    connection.sendMessage("PRINTSHOPPINGCART" + client.toString2());
                } catch(IOException ioe) {}
                break;
            case 4: 
                try {
                    connection.sendMessage("PURCHASESHOPPINGCART" + client.toString2());
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }
                break;
            case 5 :
                System.out.println("\nPlease enter the name of the product you'd like to purchase directly: ");
                String product3 = scanner.next();
                try {
                    connection.sendMessage("PURCHASE" + client.toString2() + 
                                            "Product: " + product3);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                break;
            case 6:
                System.out.println("\nExiting from the purchase mode.\n");
                try {
                    connection.sendMessage("CONNECT");
                } catch (IOException ioe) {}
                break;
        }
            
    }

    public void connect() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nWhat would you like to do now?\n");
        int action = -1;
        
        System.out.println("Actions to do with the server:\n" + 
                            "1. Show Catalogue.\n" +
                            "2. Purchase.\n" +
                            "3. Disconnect.\n");
        try {
            action = Integer.parseInt(scanner.next());
        } catch (NumberFormatException nfe) {
            System.out.println("NumberFormatException has ocurred. Please enter a valid action.\n");
        } catch (NoSuchElementException nsee) {
            
        }
        switch(action) {
            case 1: 
                try {
                    connection.sendMessage("CATALOGUE");
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                break;
            case 2: 
                try {
                    connection.sendMessage("PURCHASEMODE");
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                break;
            case 3: 
                try {
                    connection.sendMessage("DISCONNECT");
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                break;
            default :
                System.out.println("Please enter a valid action.\n");
                
                break;
        }
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
}

import java.io.BufferedReader;
import java.io.IOException;
import java.util.NoSuchElementException;

public class ClientConnectionUSA implements ClientConnectionInterface {

    ProxyClient client;
    BufferedReader scanner;

    public ClientConnectionUSA(ProxyClient client, BufferedReader in) {
        this.client = client;
        this.scanner = in;
    }
 
    public void purchase(Connection connection) {
        int action = -1;

        try {
            action = Integer.parseInt(scanner.readLine());
        } catch (NumberFormatException nfe) {
            System.out.println("NumberFormatException has ocurred. Please enter a valid action.\n");
        } catch (IOException ioe) {

        } catch (NoSuchElementException nsee) {
            
        }
        do {
            switch(action) {
                case 1:
                    System.out.println("\nPlease enter the name of the product you'd like " +
                                            "to add to your shopping cart: ");
                    try {
                        String product1 = scanner.readLine();
                        connection.sendMessage("ADDTOCART" + client.toString2() + 
                                                "Product: " + product1);
                    } catch(IOException ioe) {
                        ioe.printStackTrace();
                    }
                    break;
                case 2:
                    System.out.println("\nPlease enter the name of the product you'd like " +
                                            "to remove from your shopping cart: ");
                    try {
                        String product2 = scanner.readLine();
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
                    try {
                        String product3 = scanner.readLine();
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
                default:
                    System.out.println("Please enter a valid action.\n");
                    continue;
            }   
            break;
        } while(true);
    }

    public void connect(Connection connection) {
        System.out.println("\nWhat would you like to do now?\n");
        int action = -1;
        
        System.out.println("Actions to do with the server:\n" + 
                            "1. Show Catalogue.\n" +
                            "2. Purchase.\n" +
                            "0. Disconnect.\n");
        try {
            action = Integer.parseInt(scanner.readLine());
        } catch (NumberFormatException nfe) {
            System.out.println("NumberFormatException has ocurred. Please enter a valid action.\n");
        } catch (IOException ioe) {

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
            case 0: 
                try {
                    connection.sendMessage("DISCONNECT");
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                break;
            default :
                System.out.println("Please enter a valid action.\n");
        }
    }

    public void purchaseMode() {
        System.out.println("Entering the purchase mode: \n");
    }
}

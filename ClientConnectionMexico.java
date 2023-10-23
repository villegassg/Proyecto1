import java.io.BufferedReader;
import java.io.IOException;
import java.util.NoSuchElementException;

public class ClientConnectionMexico implements ClientConnectionInterface {
    
    ProxyClient client;
    BufferedReader scanner;

    public ClientConnectionMexico(ProxyClient client, BufferedReader in) {
        this.client = client;
        this.scanner = in;
    }
 
    public void purchase(Connection connection) {
        int action = -1;

        try {
            action = Integer.parseInt(scanner.readLine());
        } catch (NumberFormatException nfe) {
            System.out.println("Ocurrió una NumberFormatException. " +
                                "Por favor ingresa una acción válida.\n");
        } catch (IOException ioe) {

        } catch (NoSuchElementException nsee) {
            
        }
        do {
            switch(action) {
                case 1:
                    System.out.println("\nPor favor ingresa el nombre del producto que " +
                                        "quieres agregar a tu carrito: ");
                    try {
                        String product1 = scanner.readLine();
                        connection.sendMessage("ADDTOCART" + client.toString2() + 
                                                    "Product: " + product1);
                    } catch(IOException ioe) {
                        ioe.printStackTrace();
                    }
                    break;
                case 2:
                    System.out.println("\nPor favor ingresa el nombre del producto que " +
                                        "quieres eliminar de tu carrito: ");
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
                        connection.sendMessage("PURCHASESHOPPINGCART");
                    } catch(IOException ioe) {
                        ioe.printStackTrace();
                    }
                    break;
                case 5 :
                    System.out.println("\nPor favor ingresa el nombre del producto que " +
                                        "quieres comprar directamente: ");
                    try {
                        String product3 = scanner.readLine();
                        connection.sendMessage("PURCHASE" + "Product: " + product3);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    break;
                case 6:
                    System.out.println("\nSaliendo del modo de compra.\n");
                    try {
                        connection.sendMessage("CONNECT");
                    } catch (IOException ioe) {}
                    break;
                default:
                    System.out.println("Por favor ingresa una acción valida.\n");
                    try {
                        connection.sendMessage("OPTIONS");
                    } catch (IOException ioe) {}
                    break;
            }   
            break;
        } while (true);
    }

    public void connect(Connection connection) {
        System.out.println("\n¿Qué quieres hacer ahora?\n");
        int action = -1;
        
        System.out.println("Acciones para hacer con el servidor:\n" + 
                            "1. Mostrar catálogo.\n" +
                            "2. Comprar.\n" +
                            "0. Desconectar.\n");
        try {
            action = Integer.parseInt(scanner.readLine());
        } catch (NumberFormatException nfe) {
            System.out.println("Ocurrió una NumberFormatException. " +
                                "Por favor ingresa una acción válida.\n");
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
                System.out.println("Por favor ingresa una acción válida.\n");
        }
    }

    public void purchaseMode() {
        System.out.println("Entrando al modo de compra: \n");
    }

    public void bankAccount1(Connection connection) {
        System.out.println("Ingresa otra vez tu cuenta de banco por favor (por seguridad): ");
        String bA = "";
        try {
            bA = scanner.readLine();
            connection.sendMessage("BANKACCOUNT1" + bA + "Client: " + client.toString2());
        } catch (IOException ioe) {}
    }

    public void bankAccount2(Connection connection, String product) {
        System.out.println("Ingresa otra vez tu cuenta de banco por favor (por seguridad): ");
        String bA = "";
        try {
            bA = scanner.readLine();
            connection.sendMessage("BANKACCOUNT2" + bA + product + "Client: " + client.toString2());
        } catch (IOException ioe) {}
    }
}

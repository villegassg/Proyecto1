import java.io.BufferedReader;
import java.io.IOException;
import java.util.NoSuchElementException;

public class ClientConnectionSpain implements ClientConnectionInterface {
    
    ProxyClient client;
    BufferedReader scanner;

    public ClientConnectionSpain(ProxyClient client, BufferedReader in) {
        this.client = client;
        this.scanner = in;
    }
 
    public void purchase(Connection connection) {
        int action = -1;

        try {
            action = Integer.parseInt(scanner.readLine());
        } catch (NumberFormatException nfe) {
            System.out.println("Ha ocurrido una NumberFormatException. " +
                                "Por favor ingresa una acción válida.\n");
        } catch (IOException ioe) {

        } catch (NoSuchElementException nsee) {
            
        }
        do {
            switch(action) {
                case 1:
                    System.out.println("\nPor favor ingresa el nombre del producto que te " +
                                        "gustaría agregar a tu carrito de compras: ");
                    try {
                        String product1 = scanner.readLine();
                        connection.sendMessage("ADDTOCART" + client.toString2() + 
                                                "Product: " + product1);
                    } catch(IOException ioe) {
                        ioe.printStackTrace();
                    }
                    break;
                case 2:
                    System.out.println("\nPor favor ingresa el nombre del producto que te " +
                                        "gustaría eliminar de tu carrito de compras: ");
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
                    System.out.println("\nPor favor ingresa el nombre del producto que te " +
                                        "gustaría comprar directamente: ");
                    try {
                        String product3 = scanner.readLine();
                        connection.sendMessage("PURCHASE" + client.toString2() + 
                                                "Product: " + product3);
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
                    continue;
            }   
            break;
        } while (true);
    }

    public void connect(Connection connection) {
        System.out.println("\n¿Qué te gustaría hacer ahora?\n");
        int action = -1;
        
        System.out.println("Acciones para hacer con el servidor:\n" + 
                            "1. Mostrar catálogo.\n" +
                            "2. Comprar.\n" +
                            "0. Desconectar.\n");
        try {
            action = Integer.parseInt(scanner.readLine());
        } catch (NumberFormatException nfe) {
            System.out.println("Ha ocurrido una NumberFormatException. " +
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
}

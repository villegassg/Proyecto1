import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class MexicoVirtualStore implements VirtualStore {

    private LinkedList<ProxyClient> clients;
    private Iterator<Product> iterator;

    public MexicoVirtualStore(Iterator<Product> iterator) {
        clients = new LinkedList<>();
        this.iterator = iterator;
    }

    public String sayHi() {
        return "¡Bienvenido a la Tienda Virtual Mexicana! " +
                            "¡Tenemos muchísimas cosas para ofrecerte!\n";
    }

    public String options() {
        String options = "Por favor ingresa la acción que quieras hacer: \t" +
                        "1. Añadir al carrito \t" +
                        "2. Quitar del carrito \t" +
                        "3. Ver carrito \t" +
                        "4. Comprar todo el carrito de compras \t" +
                        "5. Comprar un sólo producto \t" +
                        "6. Salir \n";
        return options;
    }

    public void addToCart(Connection connection, String name, Product product) {
        if (product == null) {
            String fail = "Lo sentimos, ha ocurrido un error al tratar de agregar el producto " +
                            "a tu carrito porque escribiste mal su nombre, inténtalo de nuevo.\n";
            try {
                connection.sendMessage("FAILEDADDTOCART" + fail);
                return;
            } catch (IOException e) {}
        }

        for (ProxyClient proxy : clients) 
            if (proxy.getName().equals(name))
                proxy.addToShoppingCart(product);

        String success = "¡Producto agregado al carrito con éxito!\n";
        try {
            connection.sendMessage("ADDEDTOCART" + success);
        } catch (IOException ioe) {}
    }

    public void removeFromCart(Connection connection, String name, Product product) {
        if (product == null) {
            String fail = "Error al tratar de eliminar el producto de tu carrito porque " +
                            "escribiste mal su nombre, inténtalo de nuevo.\n";
            try {
                connection.sendMessage("FAILEDADDTOCART" + fail);
                return;
            } catch (IOException e) {}
        }

        for (ProxyClient proxy : clients) 
            if (proxy.getName().equals(name))
                if (proxy.getShoppingCart().contains(product))
                    proxy.removeFromShoppingCart(product);
                else {
                    String fail = "No pudimos eliminar el producto " + product.getName() +
                                " porque no está en tu carrito.\n";
                    try {
                        connection.sendMessage("NOPRODUCT" + fail);
                        return;
                    } catch (IOException ioe) {}
                }

        String success = "¡Producto eliminado de tu carrito con éxito!\n";
        try {
            connection.sendMessage("REMOVEFROMSHOPPINGCART" + success);
        } catch(IOException ioe) {}
    }

    public void printShoppingCart(Connection connection, String name) {
        ProxyClient client = null;
        for (ProxyClient proxy : clients) 
            if (proxy.getName().equals(name))
                client = proxy;

        LinkedList<Product> shoppingCart = client.getShoppingCart();
        
        String sc = "";
        double total = 0;
        for (Product product : shoppingCart) {
            sc += product.toString2();
            total += product.getPrice();
        }
        
        sc += String.format("Total de la compra: %.2f\n", total); 

        try {
            connection.sendMessage("PRINTSHOPPINGCART" + sc);
        } catch (IOException ioe) {}
    }

    public void purchaseShoppingCart(Connection connection, String name) {
        LinkedList<Product> shoppingCart = new LinkedList<>();
        ProxyClient proxyClient = null;
        for (ProxyClient proxy : clients)
            if (proxy.getName().equals(name)) {
                proxyClient = proxy;
                shoppingCart = proxy.getShoppingCart();
            }

        if (shoppingCart.isEmpty()) {
            String fail = "Lo sentimos, pero tu carrito está vacío :(.\n";
            try {
                connection.sendMessage("EMPTYSHOPPINGCART" + fail);
                return;
            } catch(IOException ioe) {}
        }

        double total = 0;
        for (Product product : shoppingCart)
            total += product.getPrice();

        double money = proxyClient.getMoney();

        if (total > money) {
            String fail = "Lo sentimos, no tienes suficientes fondos :(. \n";
            try {
                connection.sendMessage("NOTENOUGHMONEY" + fail);
                return;
            } catch (IOException ioe) {

            }
        } else {
            proxyClient.setMoney(money-total);
            proxyClient.setOnRealValues();
            String success = "¡Compra completada con éxito!.\n";
            try {
                connection.sendMessage("PURCHASESHOPPINGCART" + success);
            } catch (IOException ioe) {}
        }
    }

    public void purchase(Connection connection, String name, Product product) {
        if (product == null) {
            String fail = "Error al tratar de comprar el producto porque escribiste mal su nombre.\n";
            try {
                connection.sendMessage("FAILEDPURCHASE" + fail);
            } catch (IOException e) {}
            return;
        }

        double price = product.getPrice();
        ProxyClient proxyClient = null;

        for (ProxyClient proxy : clients) 
            if (proxy.getName().equals(name)) 
                proxyClient = proxy;

        double money = proxyClient.getMoney();

        if (price > money) {
            String fail = "Lo sentimos, no tienes suficientes fondos :( \n";
            try {
                connection.sendMessage("NOTENOUGHMONEY" + fail);
                return;
            } catch (IOException ioe) {}
        } else {
            proxyClient.setMoney(money-price);
            proxyClient.setOnRealValues();
            String success = "¡Producto comprado con éxito!\n";
            try {
                connection.sendMessage("PURCHASE" + success);
                return;
            } catch(IOException ioe) {}
        }
    } 

    public String deliveryDate() {
        String deliveryDate = "";
        return deliveryDate;
    }

    public void sendOffers() {
        
    }

    public void goodBye() {
        System.out.println("¡Muchas gracias por habernos visitado! ¡Hasta la próxima!\n");
    }

    public void add(ProxyClient client) {
        clients.addFirst(client);
    }

    public void remove(ProxyClient client) {
        clients.remove(client);
    }

    public void products() {
        while (iterator.hasNext()) {
            Product product = iterator.next();
            System.out.println(product.toString());
        }
    }
}
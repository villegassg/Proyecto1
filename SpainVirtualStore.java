import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class SpainVirtualStore implements VirtualStore {
    
    private LinkedList<ProxyClient> clients;
    private Iterator<Product> iterator;
    private Random random;

    public SpainVirtualStore(Iterator<Product> iterator) {
        clients = new LinkedList<>();
        this.iterator = iterator;
        random = new Random();
    }

    public String sayHi() {
        return "¡Enhorabuena! ¡Bienvenido seas a la Tienda Virtual de España!\n";
    }
    
    public String options() {
        String options = "Por favor ingresa la acción que vos quieras ejecutar: \t" +
                        "1. Agregar al carrito de compras \t" +
                        "2. Quitar del carrito de compras \t" +
                        "3. Imprimir carrito de compras \t" +
                        "4. Comprar carrito de compras \t" +
                        "5. Comprar un sólo producto \t" +
                        "6. Salir \t";
        return options;
    }

    public void addToCart(Connection connection, String name, Product product) {
        if (product == null) {
            String fail = "Ostia, ha ocurrido un error al tratar de agregar el producto a tu " +
                            "carrito de compras porque has ingresado su nombre " +
                            "incorrectamente, inténtalo de nuevo.\n";
            try {
                connection.sendMessage("FAILEDADDTOCART" + fail);
                return;
            } catch (IOException e) {}
        }

        for (ProxyClient proxy : clients) 
            if (proxy.getName().equals(name))
                proxy.addToShoppingCart(product);

        String success = "¡Producto agregado a tu carrito de compras con éxito!\n";
        try {
            connection.sendMessage("ADDEDTOCART" + success);
        } catch (IOException ioe) {}
    }

    public void removeFromCart(Connection connection, String name, Product product) {
        if (product == null) {
            String fail = "Fallo al tratar de eliminar el producto de tu carrito de compras " +
                            "porque has ingresado mal su nombre, inténtalo de nuevo.\n";
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
                    String fail = "Imposible eliminar el producto " + product.getName() +
                                " porque no está en tu carrito de compras.\n";
                    try {
                        connection.sendMessage("NOPRODUCT" + fail);
                        return;
                    } catch (IOException ioe) {}
                }

        String success = "¡Producto eliminado de tu carrito de compras con éxito!\n";
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
        
        sc += String.format("Suma total: %.2f\n", total); 

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
            String fail = "Lo sentimos, pero al parecer tu carrito de compras está vacío :(.\n";
            try {
                connection.sendMessage("EMPTYSHOPPINGCART" + fail);
                return;
            } catch(IOException ioe) {}
        }

        double total = 0;
        int offer = random.nextInt(5);
        for (Product product : shoppingCart) {
            double price = product.getPrice(); 
            if (offer == 3 && product.getDepartment().equals("ELECTRODOMESTICO"))
                total += price-(price*.3);
            else 
                total += product.getPrice();
        }

        double money = proxyClient.getMoney();

        if (total > money) {
            String fail = "Lo sentimos, pero al parecer no tienes suficiente dinero en tu " +
                            "cuenta de banco :(. \n";
            try {
                connection.sendMessage("NOTENOUGHMONEY" + fail);
                return;
            } catch (IOException ioe) {

            }
        } else {
            proxyClient.setMoney(money-total);
            proxyClient.setOnRealValues();
            String success = offer == 3 ? "¡Compra con descuento en electrodomésticos completada " +
                                            "con éxito!.\n" : "¡Compra completada con éxito!.\n";
            try {
                connection.sendMessage("PURCHASESHOPPINGCART" + success);
            } catch (IOException ioe) {}
        }
    }

    public void purchase(Connection connection, String name, Product product) {
        if (product == null) {
            String fail = "La compra de tu producto no ha podido ser completa porque " +
                            "habeis ingresado mal su nombre.\n";
            try {
                connection.sendMessage("FAILEDPURCHASE" + fail);
            } catch (IOException e) {}
            return;
        }

        int offer = random.nextInt(5);
        double price = offer == 3 && product.getDepartment().equals("ELECTRODOMESTICO") ? 
                        product.getPrice()-(product.getPrice()*.3) : 
                        product.getPrice();

        ProxyClient proxyClient = null;

        for (ProxyClient proxy : clients) 
            if (proxy.getName().equals(name)) 
                proxyClient = proxy;

        double money = proxyClient.getMoney();

        if (price > money) {
            String fail = "Lo sentimos, al parecer no tienes suficiente dinero en tu cuenta de banco :( \n";
            try {
                connection.sendMessage("NOTENOUGHMONEY" + fail);
                return;
            } catch (IOException ioe) {}
        } else {
            proxyClient.setMoney(money-price);
            proxyClient.setOnRealValues();
            String success = offer == 3 && product.getDepartment().equals("ELECTRODOMESTICO") ? 
                                "¡Producto electrodoméstico con descuento comprado con éxito!\n" : 
                                "¡Producto comprado con éxito!\n";
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
        System.out.println("¡Muchas gracias por habernos visitado!, esperamos que haya sido " +
                            "de tu agrado. ¡Hasta luego!\n");
    }

    public void add(ProxyClient client) {
        clients.add(client);
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
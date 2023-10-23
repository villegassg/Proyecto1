import java.io.IOException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
//La clase USAVirtualStore implementa a VirtualStore
public class USAVirtualStore implements VirtualStore {
    //Creamos una lista privada del objeto ProxyCLient creando una variable clients
    private LinkedList<ProxyClient> clients;
    //Creamos una variable iterator del tipo Iterator pasandole como parametro el objeto Product
    private Iterator<Product> iterator;
    //Creamos una variable random del tipo Random
    private Random random;
//Creamos un constructor para inicializar una instancia de la clase USAVirtualStore
//La cual va a tener una lista de clientes via y un iterador de productos
    public USAVirtualStore(Iterator<Product> iterator) {
        clients = new LinkedList<>();
        this.iterator = iterator;
        random = new Random();
    }
    //El metodo sayHi regresa una cadena de texto 
    public String sayHi() {
        return "Welcome to USA Virtual Store! We're glad you're visiting us!\n";
    }
//EL método options del tipo String regresa el menú de opciones 
    public String options() {
        String options = "Please enter the action you'd like to do: \t" +
                        "1. Add to the cart \t" +
                        "2. Remove a product from the shopping cart \t" +
                        "3. Print shopping cart \t" +
                        "4. Purchase Shopping Cart \t" +
                        "5. Purchase \t" +
                        "6. Exit \n";
        return options;
    }
//El metodo addCart va a permitirle al cliente agregar los productos a su carrito de compras.
    public void addToCart(Connection connection, String name, Product product) {
        if (product == null) {
            String fail = "Could not add the product because you entered its name wrong, try again.\n";
            try {
                connection.sendMessage("FAILEDADDTOCART" + fail);
                return;
            } catch (IOException e) {}
        }

        for (ProxyClient proxy : clients) 
            if (proxy.getUsername().equals(name))
                proxy.addToShoppingCart(product);

        String success = "Product added to your cart succesfully!\n";
        try {
            connection.sendMessage("ADDEDTOCART" + success);
        } catch (IOException ioe) {}
    }
//El metodo removeFromCart va a permitirle al cliente eliminar los productos a su carrito de compras.
    public void removeFromCart(Connection connection, String name, Product product) {
        if (product == null) {
            String fail = "Could not remove the product because you entered its name wrong, try again.\n";
            try {
                connection.sendMessage("FAILEDADDTOCART" + fail);
                return;
            } catch (IOException e) {}
        }

        for (ProxyClient proxy : clients) 
            if (proxy.getUsername().equals(name))
                if (proxy.getShoppingCart().contains(product))
                    proxy.removeFromShoppingCart(product);
                else {
                    String fail = "We're sorry, we couldn't remove the product " + 
                                product.getName() + " because it is not in your shopping cart.\n";
                    try {
                        connection.sendMessage("NOPRODUCT" + fail);
                        return;
                    } catch (IOException ioe) {}
                }

        String success = "Product removed from your cart succesfully!\n";
        try {
            connection.sendMessage("REMOVEFROMSHOPPINGCART" + success);
        } catch(IOException ioe) {}
    }
//El método printShoppingCart imprime la informacion del carrito de compras del cliente
    public void printShoppingCart(Connection connection, String name) {
        ProxyClient client = null;
        for (ProxyClient proxy : clients) 
            if (proxy.getUsername().equals(name))
                client = proxy;

        if (client == null) {
            try {
                connection.sendMessage("DISCONNECT" + "Client not found.\n");
                return;
            } catch (IOException ioe) {}
        }

        LinkedList<Product> shoppingCart = client.getShoppingCart();
        
        String sc = "";
        double total = 0;
        for (Product product : shoppingCart) {
            sc += product.toString2();
            total += product.getPrice();
        }
        
        sc += String.format("Total price: %.2f\n", total); 

        try {
            connection.sendMessage("PRINTSHOPPINGCART" + sc);
        } catch (IOException ioe) {}
    }
//El metodo purchaseShoppingCart le va a permitir al cliente comprar su carrito de compras
    public void purchaseShoppingCart(Connection connection, String name, long bankAccount) {
        LinkedList<Product> shoppingCart = new LinkedList<>();
        ProxyClient proxyClient = null;
        for (ProxyClient proxy : clients)
            if (proxy.getUsername().equals(name)) {
                proxyClient = proxy;
                shoppingCart = proxy.getShoppingCart();
            }

        if (shoppingCart.isEmpty()) {
            String fail = "We're sorry, but your shopping cart is empty :(.\n";
            try {
                connection.sendMessage("EMPTYSHOPPINGCART" + fail);
                return;
            } catch(IOException ioe) {}
        }

        if (proxyClient.getBankAccount() != bankAccount) {
            try {
                connection.sendMessage("INVALID" + "The bank account you entered doesn't " +
                                        "match with the bank account in our database.\n");
                return;
            } catch (IOException ioe) {}
        }

        double total = 0;
        int offer = random.nextInt(5);
        for (Product product : shoppingCart) {
            double price = product.getPrice();
            if (offer == 3 && product.getDepartment().equals("ELECTRONICA"))
                total += price-(price*.30);
            else 
                total += product.getPrice();
        }

        double money = proxyClient.getMoney();

        if (total > money) {
            String fail = "We're sorry, you don't have enough money in your account :(. \n";
            try {
                connection.sendMessage("NOTENOUGHMONEY" + fail);
                return;
            } catch (IOException ioe) {

            }
        } else {
            proxyClient.setMoney(money-total);
            proxyClient.setOnRealValues();
            String success = offer == 3 ? "Purchase with great disccount on electronics " +
                                            "completed with success!.\n" : 
                                            "Purchase completed with success!.\n";
            String delivery = deliveryDate();
            try {
                connection.sendMessage("PURCHASESHOPPINGCART" + success);
                connection.sendMessage("DELIVERY" + delivery);
            } catch (IOException ioe) {}
            proxyClient.clearShoppingCart();
        }
    }
//El metodo purchase le permite al cliente comprar el producto sin necesidad de agregarlo al carrito
    public void purchase(Connection connection, Product product, String name, long bankAccount) {
        if (product == null) {
            String fail = "Could not purchase the product because you entered its name wrong, " +
                            "try again.\n";
            try {
                connection.sendMessage("FAILEDPURCHASE" + fail);
            } catch (IOException e) {}
            return;
        }

        int offer = random.nextInt(5);
        double price = offer == 3 && product.getDepartment().equals("ELECTRONICA") ? 
                        product.getPrice() - (product.getPrice()*.3) : 
                        product.getPrice();

        ProxyClient proxyClient = null;

        for (ProxyClient proxy : clients) 
            if (proxy.getUsername().equals(name)) 
                proxyClient = proxy;

        if (proxyClient.getBankAccount() != bankAccount) {
            try {
                connection.sendMessage("INVALID" + "The bank account you entered doesn't " +
                                        "match with the bank account in our database.\n");
                return;
            } catch (IOException ioe) {}
        }

        double money = proxyClient.getMoney();

        if (price > money) {
            String fail = "We're sorry, you don't have enough money in your account :( \n";
            try {
                connection.sendMessage("NOTENOUGHMONEY" + fail);
                return;
            } catch (IOException ioe) {}
        } else {
            proxyClient.setMoney(money-price);
            proxyClient.setOnRealValues();
            String success = offer == 3 && product.getDepartment().equals("ELECTRONICA") ? 
                            "Product purchased with great disccount succesfully!" : 
                            "Product purchased succesfully!\n";
            String delivery = deliveryDate();
            try {
                connection.sendMessage("PURCHASE" + success);
                connection.sendMessage("DELIVERY" + delivery);
                return;
            } catch(IOException ioe) {}
        }
    } 
//El metodo deliveryDate le va a indicar al cliente cuando se le va a entregar su compra

    public String deliveryDate() {
        LocalDate date = LocalDate.now();
        String deliveryDate = "Your order will be delivered on: " + 
                                date.plusWeeks(2).toString() + ".\n";
        return deliveryDate;
    }
//El metodo goodBye va a imprimir un mensaje de texto para el cliente
    public void goodBye() {
        System.out.println("Thank you for visiting the USA Virtual Store. We hope " +
                                "you're visiting us again!\n");
    }
//El metodo add va agergar a los clientes en una lista propia de la tienda 
    public void add(ProxyClient client) {
        clients.add(client);
    }
//El metodo remove va a eliminar a los clientes de la lista propia de la tienda
    public void remove(ProxyClient client) {
        clients.remove(client);
    }
//El metodo products va a recorrer la lista de los productos utilizando un iterador.    
        while (iterator.hasNext()) {
            Product product = iterator.next();
            System.out.println(product.toString());
        }
    }
}

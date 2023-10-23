import java.io.IOException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class USAVirtualStore implements VirtualStore {
    
    private LinkedList<ProxyClient> clients;
    private Iterator<Product> iterator;
    private Random random;

    public USAVirtualStore(Iterator<Product> iterator) {
        clients = new LinkedList<>();
        this.iterator = iterator;
        random = new Random();
    }

    public String sayHi() {
        return "Welcome to USA Virtual Store! We're glad you're visiting us!\n";
    }

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

    public void addToCart(Connection connection, String name, Product product) {
        if (product == null) {
            String fail = "Could not add the product because you entered its name wrong, try again.\n";
            try {
                connection.sendMessage("FAILEDADDTOCART" + fail);
                return;
            } catch (IOException e) {}
        }

        for (ProxyClient proxy : clients) 
            if (proxy.getName().equals(name))
                proxy.addToShoppingCart(product);

        String success = "Product added to your cart succesfully!\n";
        try {
            connection.sendMessage("ADDEDTOCART" + success);
        } catch (IOException ioe) {}
    }

    public void removeFromCart(Connection connection, String name, Product product) {
        if (product == null) {
            String fail = "Could not remove the product because you entered its name wrong, try again.\n";
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
        
        sc += String.format("Total price: %.2f\n", total); 

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
            String fail = "We're sorry, but your shopping cart is empty :(.\n";
            try {
                connection.sendMessage("EMPTYSHOPPINGCART" + fail);
                return;
            } catch(IOException ioe) {}
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
        }
    }

    public void purchase(Connection connection, String name, Product product) {
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
            if (proxy.getName().equals(name)) 
                proxyClient = proxy;

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

    public String deliveryDate() {
        LocalDate date = LocalDate.now();
        String deliveryDate = "Your order will be delivered on: " + 
                                date.plusWeeks(2).toString() + ".\n";
        return deliveryDate;
    }

    public void goodBye() {
        System.out.println("Thank you for visiting the USA Virtual Store. We hope " +
                                "you're visiting us again!\n");
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
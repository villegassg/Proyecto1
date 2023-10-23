import java.io.IOException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
//La clase SpainVirtualStore implementa a VirtualStore
public class SpainVirtualStore implements VirtualStore {
    //Creamos una lista privada del objeto ProxyCLient creando una variable clients
    private LinkedList<ProxyClient> clients;
        //Creamos una variable iterator del tipo Iterator pasandole como parametro el objeto Product
    private Iterator<Product> iterator;
        //Creamos una variable random del tipo Random
    private Random random;
//Creamos un constructor para inicializar una instancia de la clase SpainVirtualStore
    //La cual va a tener una lista de clientes via y un iterador de productos
    public SpainVirtualStore(Iterator<Product> iterator) {
        clients = new LinkedList<>();
        this.iterator = iterator;
        random = new Random();
    }
    //El metodo sayHi regresa una cadena de texto 
    public String sayHi() {
        return "¡Enhorabuena! ¡Bienvenido seas a la Tienda Virtual de España!\n";
    }
    //EL método options del tipo String regresa el menú de opciones 
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
//El metodo addCart va a permitirle al cliente agregar los productos a su carrito de compras.
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
            if (proxy.getUsername().equals(name))
                proxy.addToShoppingCart(product);

        String success = "¡Producto agregado a tu carrito de compras con éxito!\n";
        try {
            connection.sendMessage("ADDEDTOCART" + success);
        } catch (IOException ioe) {}
    }
//El metodo removeFromCart va a permitirle al cliente eliminar los productos a su carrito de compras.
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
            if (proxy.getUsername().equals(name))
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
//El método printShoppingCart imprime la informacion del carrito de compras del cliente
    public void printShoppingCart(Connection connection, String name) {
        ProxyClient client = null;
        for (ProxyClient proxy : clients) 
            if (proxy.getUsername().equals(name))
                client = proxy;

        if (client == null) {
            try {
                connection.sendMessage("DISCONNECT" + "El cliente no ha sido encontrado.\n");
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
        
        sc += String.format("Suma total: %.2f\n", total); 

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
            String fail = "Lo sentimos, pero al parecer tu carrito de compras está vacío :(.\n";
            try {
                connection.sendMessage("EMPTYSHOPPINGCART" + fail);
                return;
            } catch(IOException ioe) {}
        }

        if (proxyClient.getBankAccount() != bankAccount) {
            try {
                connection.sendMessage("INVALID" + "La cuenta de banco que ingresaste no " +
                                        "coincide con la cuenta de banco que tenemos en " +
                                        "nuestra base de datos.\n");
                return;
            } catch (IOException ioe) {}
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
            if (proxy.getUsername().equals(name)) 
                proxyClient = proxy;

        if (proxyClient.getBankAccount() != bankAccount) {
            try {
                connection.sendMessage("INVALID" + "La cuenta de banco que ingresaste no " +
                                        "coincide con la cuenta de banco que tenemos en " +
                                        "nuestra base de datos.\n");
                return;
            } catch (IOException ioe) {}
        }

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
        String deliveryDate = "Tu orden será entregada en: " + 
                                date.plusWeeks(2).toString() + ".\n";
        return deliveryDate;
    }
//El metodo goodBye va a imprimir un mensaje de texto para el cliente
    public void goodBye() {
        System.out.println("¡Muchas gracias por habernos visitado!, esperamos que haya sido " +
                            "de tu agrado. ¡Hasta luego!\n");
    }
//El metodo add va agergar a los clientes en una lista propia de la tienda 
    public void add(ProxyClient client) {
        if (!clients.contains(client))
            clients.add(client);
    }
//El metodo remove va a eliminar a los clientes de la lista propia de la tienda
    public void remove(ProxyClient client) {
        clients.remove(client);
    }
//El metodo products va a recorrer la lista de los productos utilizando un iterador.
    public void products() {
        while (iterator.hasNext()) {
            Product product = iterator.next();
            System.out.println(product.toString());
        }
    }
}

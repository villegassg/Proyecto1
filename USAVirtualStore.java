import java.util.Iterator;
import java.util.LinkedList;

public class USAVirtualStore implements VirtualStore {
    
    private LinkedList<ProxyClient> clients;
    private Iterator<Product> iterator;

    public USAVirtualStore(Iterator<Product> iterator) {
        clients = new LinkedList<>();
        this.iterator = iterator;
    }

    public void sayHi() {
        System.out.println("Welcome to USA Virtual Store! We're glad you're visiting us!\n");
    }

    public void options() {
        //menu de opciones
    }

    public void purchase(Product product) {

    } 

    public String deliveryDate() {
        String deliveryDate = "";
        return deliveryDate;
    }

    public void sendOffers() {

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
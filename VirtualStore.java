public interface VirtualStore {

    public String sayHi();

    public String options();

    public void addToCart(Connection connection, String name, Product product);

    public void removeFromCart(Connection connection, String name, Product product);

    public void printShoppingCart(Connection connection, String name);

    public void purchaseShoppingCart(Connection connection, String name);

    public void purchase(Connection connection, String client, Product product);

    public String deliveryDate();

    public void sendOffers();

    public void goodBye();

    public void add(ProxyClient client);

    public void remove(ProxyClient client);

    public void products();
}
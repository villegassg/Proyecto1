//Interfaz de la tienda virtual
public interface VirtualStore {
//Creamos una variable sayHi del tipo String
    public String sayHi();
//Creamos una variablo options del tipo String
    public String options();
//Creamos un metodo assToCart que nos va a permitir agregar productos al carrito
    public void addToCart(Connection connection, String name, Product product);
//Creamos un metodo removeFromCart que nos va a permitir eliminar productos al carrito
    public void removeFromCart(Connection connection, String name, Product product);
//Creamos un metodo printShoppingCart que nos va a permitir imprimir toda la lista de los productos del carrito
    public void printShoppingCart(Connection connection, String name);
//Creamos un metodo purchaseShoppingCart que nos va a permitir comprar todo el carrito de nuestros productos
    public void purchaseShoppingCart(Connection connection, String name, long bankAccount);
//Creamos el metodo purchase que nos permite comprar los articulos sin necesidad de agregarlos a un carrito de compras
    public void purchase(Connection connection, Product product, String name, long bankAccount);
//Creamos un metodo deliveryDate del tipo String
    public String deliveryDate();
//Creamos un metodo goodBye del tipo void 
    public void goodBye();
//Creamos el metodo add que parametriza client del objeto Proxyclient del tipo void
    public void add(ProxyClient client);
//Creamos el metodo remove que parametriza client del objeto Proxyclient del tipo void
    public void remove(ProxyClient client);
//Creamos un metodo products del tipo void
    public void products();
}

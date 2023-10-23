import java.util.LinkedList;
//Clase ProxyClient implementa a CLientInterface
public class ProxyClient implements ClientInterface {

    private Client client;
    private String username;
    private int password;
    private String name; 
    private long phoneNumber;
    private String address;
    private long bankAccount;
    private String country;
    private double money;
    private LinkedList<Product> shoppingCart;
//Se crea un constructor de ProxyClient que implemente a los clientes
    public ProxyClient(Client client) {
        this.client = client;
        username = client.getUsername();
        password = client.getPassword();
        name = client.getName();
        phoneNumber = client.getPhoneNumber();
        address = client.getAddress();
        bankAccount = client.getBankAccount();
        country = client.getCountry();
        money = client.getMoney();
        shoppingCart = new LinkedList<>();
    }
//Define el dinero de los clientes
    public void setMoney(double money) {
        this.money = money;
    }
//Regresa el nombre de usuario del cliente
    public String getUsername() {
        return username;
    }
    //Regresa la contraseña del cliente
    public int getPassword() {
        return password;
    }
//Regresa el nombre del cliente
    public String getName() {
        return name;
    }
//Regresa el numero telefonico del cliente
    public long getPhoneNumber() {
        return phoneNumber;
    }
//Regresa la direccion del cliente
    public String getAddress() {
        return address;
    }
    //Regresa la cuenta bancaria del cliebte

    public long getBankAccount() {
        return bankAccount;
    }
//Regresa un pais del cliente
    public String getCountry() {
        return country;
    }
//regresa el dinero del cliente
    public double getMoney() {
        return money;
    }
//Define el dinero del cliente
    public void setOnRealValues() {
        client.setMoney(money);
    }
//Agrega productos al carrito de compras del cliente
    public void addToShoppingCart(Product product) {
        shoppingCart.add(product);
    }
//Elimina productos del carrito del compras del cliente
    public void removeFromShoppingCart(Product product) {
        shoppingCart.remove(product);
    }
//regresa una lusta de los productos que estan en el carrito de compras del cliente
    public LinkedList<Product> getShoppingCart() {
        return shoppingCart;
    }
//Limpia el carrito de compras del cliente
    public void clearShoppingCart() {
        shoppingCart.clear();
    }
//Regresa una cadena de texto el método toString
    @Override public String toString() {
        String n = String.format("Name: %s\n", name);
        String u = String.format("Username: %s\n", username);
        String p = String.format("Password: %s\n", password);
        String pN = String.format("Phone number: %s\n", phoneNumber);
        String a = String.format("Address: %s\n", address);
        String b = String.format("Bank Account: %s\n", bankAccount);
        String c = String.format("Country: %s\n", country);
        String m = String.format("Money: %s\n", money);

        //return n.concat(u).concat(p).concat(pN).concat(a).concat(b).concat(c).concat(m);
        return n + u + p + pN + a + b + c + m;
    }
//regresa una cadena de texto el método toString2
    public String toString2() {
        String n = String.format("Name: %s", name);
        String u = String.format("Username: %s", username);
        String p = String.format("Password: %s", password);
        String pN = String.format("Phone number: %s", phoneNumber);
        String a = String.format("Address: %s", address);
        String b = String.format("Bank Account: %s", bankAccount);
        String c = String.format("Country: %s", country);
        String m = String.format("Money: %s", money);

        return n + u + p + pN + a + b + c + m;
    }
//Compara dos componentes de Procy
    @Override public boolean equals(Object object) {
        if (!(object instanceof ProxyClient))
            return false;

        ProxyClient proxy = (ProxyClient)object;
        return this.name.equals(proxy.getName()) &&
                this.username.equals(proxy.getUsername()) &&
                this.password == proxy.getPassword() &&
                this.phoneNumber == proxy.getPhoneNumber() &&
                this.address.equals(proxy.getAddress()) &&
                this.bankAccount == proxy.getBankAccount() &&
                this.country.equals(proxy.getCountry()) &&
                this.money == proxy.getMoney();
    }
}

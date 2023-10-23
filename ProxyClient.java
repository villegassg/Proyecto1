import java.util.LinkedList;

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

    public void setMoney(double money) {
        this.money = money;
    }

    public String getUsername() {
        return username;
    }
    
    public int getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public long getBankAccount() {
        return bankAccount;
    }

    public String getCountry() {
        return country;
    }

    public double getMoney() {
        return money;
    }

    public void setOnRealValues() {
        client.setMoney(money);
    }

    public void addToShoppingCart(Product product) {
        shoppingCart.add(product);
    }

    public void removeFromShoppingCart(Product product) {
        shoppingCart.remove(product);
    }

    public LinkedList<Product> getShoppingCart() {
        return shoppingCart;
    }

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
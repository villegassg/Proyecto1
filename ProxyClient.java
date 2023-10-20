import java.util.LinkedList;

public class ProxyClient implements ClientInterface {

    private Client client;
    private String username;
    private String password;
    private String name; 
    private long phoneNumber;
    private String address;
    private long bankAccount;
    private String country;
    private double money;
    private LinkedList<Product> shoppingCart;

    public ProxyClient(Client client) {
        this.client = client;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
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
}
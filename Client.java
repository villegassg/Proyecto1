/*Clase cliente que va a representar a los clientes.Los clientes tienen nombre, contraseña
*, nombre de usuario, llave, dinero, implementa a cliente interface*/
public class Client implements ClientInterface {
    //
    private String username;
    private int password;
    private String name; 
    private long phoneNumber;
    private String address;
    private long bankAccount;
    private String country;
    private double money;

    public Client() {}

    public Client(String username, String password, String name, Long phoneNumber, 
                    String address, long bankAccount, String country, double money) {
        this.username = username;
        this.password = password.hashCode();
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.bankAccount = bankAccount;
        this.country = country;
        this.money = money;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password.hashCode();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBankAccount(long bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void setCountry(String country) {
        this.country = country;
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
}

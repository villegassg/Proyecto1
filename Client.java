/*Clase cliente que va a representar a los clientes.Los clientes tienen nombre, contraseña
*, dirección, nombre de usuario, llave, dinero, país, número telefónico y cuenta
*bancaria, así mismo la clase cliente implementa a cliente interface*/
public class Client implements ClientInterface {
    //Nombre de usuario del cliente
    private String username;
    //Contraseña del cliente
    private int password;
    //Nombre del cliente
    private String name; 
    //Numero telefónico
    private long phoneNumber;
    //Direccion del usuario
    private String address;
    //Cuenta bancaria
    private long bankAccount;
    //Pais del cliente
    private String country;
    //Dinero con el que cuenta el cliente
    private double money;

    public Client() {}
    
    /**
     * Define el estado inicial del cliente.
     * @param username el .
     * @param cuenta el número de cuenta del estudiante.
     * @param promedio el promedio del estudiante.
     * @param edad la edad del estudiante.
     */
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

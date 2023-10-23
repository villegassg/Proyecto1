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

    //Clase cliente
    public Client() {}
    
    /**
     * Define el estado inicial del cliente.
     * @param username el el nombre del usuario
     * @param password la contraseña del cliente.
     * @param phoneNUmbre el número de telofóno del cliente.
     * @param address la contraseña del cliente.
     *@param bankAccount la cuenta bancaria del cliente
     *@param country el pais del cliente
     *@param money el dinero del cliente.
     */
    public Client(String username, String password, String name, Long phoneNumber, 
                    String address, long bankAccount, String country, double money) {
        this.username = username;
        this.password = hash(password);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.bankAccount = bankAccount;
        this.country = country;
        this.money = money;
    }

    /**
     * Define el estado inicial del cliente.
     * @param username el el nombre del usuario
     * @param password la contraseña del cliente.
     * @param phoneNUmbre el número de telofóno del cliente.
     * @param address la contraseña del cliente.
     * @param bankAccount la cuenta bancaria del cliente
     * @param country el pais del cliente
     * @param money el dinero del cliente.
     */
    public Client(String username, int password, String name, Long phoneNumber, 
                    String address, long bankAccount, String country, double money) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.bankAccount = bankAccount;
        this.country = country;
        this.money = money;
    }

    //Define el nonbre de usuario del cliente
    public void setUsername(String username) {
        this.username = username;
    }

    //Define la contraseña del cliente
    public void setPassword(String password) {
        this.password = hash(password);
    }

    //Define el nombre del cliente
    public void setName(String name) {
        this.name = name;
    }

   //Define el número telefónico del cliente
    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

   //Define la contraseña del cliente
    public void setAddress(String address) {
        this.address = address;
    }

   //Define la cuenta bancaria del cliente
    public void setBankAccount(long bankAccount) {
        this.bankAccount = bankAccount;
    }

    //Define el país del cliente
    public void setCountry(String country) {
        this.country = country;
    }

   //Define el dinero del cliente
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

   //Regresa el número telefónico del cliente
    public long getPhoneNumber() {
        return phoneNumber;
    }
//Regresa la contraseña del cliente
    public String getAddress() {
        return address;
    }
//Regresa la cuenta bancaria del cliente
    public long getBankAccount() {
        return bankAccount;
    }
//Regresa el país del cliente
    public String getCountry() {
        return country;
    }
//Regresa el dinero del cliente
    public double getMoney() {
        return money;
    }

    /**
     * Función de dispersión para contraseñas.
     * @param str la contraseña.
     * @return un entero positivo que es la dispersión de la contraseña.
     */
    public int hash(String str) {
        int hash = 5381;
        
        for (int i = 0; i < str.length(); i++) {
            hash = ((hash << 5) + hash) + str.charAt(i);
        }
        
        return hash & 0x7FFFFFFF; // To make sure the result is a positive integer.
    }
}

//Interfaz ClientInterface
public interface ClientInterface {
//EL metodo setMoney va a reciir un paramedtro money de tipo double
    public void setMoney(double money);
//El método getUsername, va a definir el nombre del usuario del cliente.
    public String getUsername();
//El método getPassword va a definir la contraseña del cliente.
    public int getPassword();
//El método getBankAccount va a definir la cuenta de banco del cliente
    public long getBankAccount();
//El método getCountry va a definir el país del cliente
    public String getCountry();
//El metodo getMoney va a definir el dinero del cliente
    public double getMoney();
}

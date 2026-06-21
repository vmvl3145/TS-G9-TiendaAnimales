public class Mascota{
    protected String especie;
    protected double precio;
    protected int Salud;

    public Mascota(String especie, double precio, int Salud){
        this.especie = especie;
        this.precio = precio;
        this.Salud = 100;
    }

    public String getEspecie(){
        return especie;
    }
    public double getPrecio(){
        return precio;
    }
    public int getSalud(){
        return Salud;
    }

    public abstract void makeSound();
}
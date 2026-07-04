package Suministros;
import Mascotas.Mascota;
import java.io.Serializable;

public abstract class Suministro implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String nombre;
    protected double precio;

    public Suministro(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }

    public abstract void usar(Mascota mascota);
}
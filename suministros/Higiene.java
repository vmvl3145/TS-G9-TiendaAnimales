package suministros;
import mascotas.Mascota;

public class Higiene extends Suministro {
    private static final long serialVersionUID = 1L;
    private int valorLimpieza;

    public Higiene(String nombre, double precio, int valorLimpieza) {
        super(nombre, precio);
        this.valorLimpieza = valorLimpieza;
    }

    @Override
    public void usar(Mascota mascota) {
        System.out.println("Limpiando al " + mascota.getEspecie() + " con " + this.nombre + ".");
        mascota.setNivelHigiene(mascota.getNivelHigiene() + this.valorLimpieza);
    }
}

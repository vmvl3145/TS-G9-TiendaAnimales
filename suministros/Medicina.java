package suministros;
import mascotas.Mascota;

public class Medicina extends Suministro {
    private static final long serialVersionUID = 1L;
    private int poderCurativo;

    public Medicina(String nombre, double precio, int poderCurativo) {
        super(nombre, precio);
        this.poderCurativo = poderCurativo;
    }

    @Override
    public void usar(Mascota mascota) {
        System.out.println("Tratando al " + mascota.getEspecie() + " con " + this.nombre + ".");
        mascota.setNivelSalud(mascota.getNivelSalud() + this.poderCurativo);
    }
}
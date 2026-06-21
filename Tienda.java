import java.util.ArrayList;
import java.util.List;

public class Tienda {
    private static Tienda tienda;
    private double presupuesto;
    private List<Mascota> inventario;

    private Tienda(double presupuesto) {
        this.presupuesto = 1000.0;
        this.inventario = new ArrayList<>();
    }

    public static Tienda getTienda() {
        if (tienda == null) {
            tienda = new Tienda();
        }
        return tienda;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public void agregarMascota(Mascota mascota) {
        inventario.add(mascota);
        System.out.println("Has agragedo a un/a " + mascota.getEspecie() + " a la tienda" );
    }

    public void mostrarHudTienda() {
        System.out.println("HUD TIENDA");
        System.out.println("Presupuesto: $" + presupuesto);
        System.out.println("Inventario de mascotas: " + inventario.size());
        System.out.println("------------------------------\n");
    }
}
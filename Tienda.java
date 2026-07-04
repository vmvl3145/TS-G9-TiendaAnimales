import java.io.*;
import java.util.ArrayList;
import java.util.List;
import excepciones.*;
import mascotas.Mascota;
import suministros.Suministro;

public class Tienda {
    private static Tienda tienda;
    private double presupuesto;
    private List<Mascota> inventario;
    private final int inventariomaximo = 5; // Limite de para test de mascotas

    private Tienda() {
        this.presupuesto = 1000.0; // Presupuesto test
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

    public void agregarMascota(Mascota mascota) throws CapacidadMaximaException{
        inventario.add(mascota);
        System.out.println("Has agragedo a un/a " + mascota.getEspecie() + " a la tienda" );
    }

    public void venderMascota(String especieMascota) throws MascotaNoEncontradaException, MascotaEnfermaException{
        Mascota mascotaVender = null;

        for (Mascota m : inventario) {
            if (m.getEspecie().equalsIgnoreCase(especieMascota)) {
                mascotaVender = m;
                break;
            }
        }
        if (mascotaVender == null) {
            throw new MascotaNoEncontradaException("No tienes ningún " + especieMascota + " en tu inventario para la venta.");
        }
        if (mascotaVender.getNivelSalud() < 50) {
            throw new MascotaEnfermaException("El " + especieMascota + " esta demasiado débil para adoptarlo.");
        }

        inventario.remove(mascotaVender);
        this.presupuesto += mascotaVender.getPrecio() * 1.5;
        System.out.println("Has vendido a un " + especieMascota + " por" + this.presupuesto " exitosamente!" );
    }

    public void mostrarHudTienda() {
        System.out.println("HUD TIENDA");
        System.out.println("Presupuesto: $" + presupuesto);
        System.out.println("Inventario de mascotas: " + inventario.size());
        System.out.println("------------------------------\n");
    }
}
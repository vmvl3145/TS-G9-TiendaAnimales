import java.io.*;
import java.util.ArrayList;
import java.util.List;
import Excepciones.*;
import Mascotas.Mascota;
import Suministros.Suministro;

public class Tienda implements Serializable {
    private static final long serialVersionUID = 1L;

    // La instancia Singleton no se serializa automáticamente al ser estática
    private static Tienda tienda;

    private double presupuesto;
    private List<Mascota> inventarioMascotas;
    private List<Suministro> inventarioSuministros;
    private final int inventarioMaximo = 5;

    // Constructor privado para el patrón Singleton
    private Tienda() {
        this.presupuesto = 1000.0;
        this.inventarioMascotas = new ArrayList<>();
        this.inventarioSuministros = new ArrayList<>();
    }

    // Método de acceso global (Singleton)
    public static Tienda getTienda() {
        if (tienda == null) {
            tienda = new Tienda();
        }
        return tienda;
    }

    // Getters
    public double getPresupuesto() { return presupuesto; }
    public List<Mascota> getInventarioMascotas() { return inventarioMascotas; }
    public List<Suministro> getInventarioSuministros() { return inventarioSuministros; }

    // LÓGICA DE NEGOCIO

    public void agregarMascota(Mascota mascota) throws CapacidadMaximaException {
        if (inventarioMascotas.size() >= inventarioMaximo) {
            throw new CapacidadMaximaException("No hay espacio en la tienda para un/a " + mascota.getEspecie());
        }
        inventarioMascotas.add(mascota);
    }

    public void venderMascota(String especieMascota) throws MascotaNoEncontradaException, MascotaEnfermaException {
        Mascota mascotaVender = null;
        for (Mascota m : inventarioMascotas) {
            if (m.getEspecie().equalsIgnoreCase(especieMascota)) {
                mascotaVender = m;
                break;
            }
        }

        if (mascotaVender == null) {
            throw new MascotaNoEncontradaException("No tienes ningún " + especieMascota + " para la venta.");
        }
        if (mascotaVender.getNivelSalud() < 50) {
            throw new MascotaEnfermaException("El " + especieMascota + " está demasiado débil para adoptarlo.");
        }

        inventarioMascotas.remove(mascotaVender);
        double precioVenta = mascotaVender.getPrecio() * 1.5;
        this.presupuesto += precioVenta;
        System.out.println(">> Venta exitosa. +$" + precioVenta);
    }

    public void agregarSuministro(Suministro suministro) throws DineroInsuficienteException {
        if (this.presupuesto >= suministro.getPrecio()) {
            this.presupuesto -= suministro.getPrecio();
            inventarioSuministros.add(suministro);
            System.out.println(">> Suministro comprado: " + suministro.getNombre() + " por $" + suministro.getPrecio());
        } else {
            throw new DineroInsuficienteException("Presupuesto insuficiente. Tienes $" +
                    this.presupuesto + " pero " + suministro.getNombre() +
                    " cuesta $" + suministro.getPrecio());
        }
    }

    // --- MOTOR DE TIEMPO (Semana 2) ---
    public void pasarTiempo() {
        System.out.println("\n[Pasando el tiempo en la tienda... Los animales sienten necesidades]");
        for (Mascota m : inventarioMascotas) {
            m.setNivelHambre(m.getNivelHambre() + 15);
            m.setNivelHigiene(m.getNivelHigiene() - 10);

            if (m.getNivelHambre() > 80) {
                m.setNivelSalud(m.getNivelSalud() - 20);
            }
            if (m.getNivelHigiene() < 20) {
                m.setNivelSalud(m.getNivelSalud() - 10);
            }
        }
    }

    // --- HUD ---
    public void mostrarHud() {
        System.out.println("\n===== ESTADO DE LA TIENDA =====");
        System.out.println("Presupuesto: $" + presupuesto);
        System.out.println("Mascotas (" + inventarioMascotas.size() + "/" + inventarioMaximo + "):");
        if (inventarioMascotas.isEmpty()) {
            System.out.println("  (sin mascotas)");
        } else {
            for (Mascota m : inventarioMascotas) {
                System.out.println("  - " + m.getEspecie()
                        + " | Salud: " + m.getNivelSalud()
                        + " | Hambre: " + m.getNivelHambre()
                        + " | Higiene: " + m.getNivelHigiene());
            }
        }
        System.out.println("Suministros en stock: " + inventarioSuministros.size());
        System.out.println("================================\n");
    }

    // --- PERSISTENCIA ---
    public void guardarPartida(String ruta) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
            oos.writeObject(this);
            System.out.println(">> Partida guardada en " + ruta);
        } catch (IOException e) {
            System.err.println("Error al guardar la partida: " + e.getMessage());
        }
    }

    public static void cargarPartida(String ruta) {
        File f = new File(ruta);
        if (!f.exists()) {
            System.out.println(">> No hay partida previa. Comenzando una nueva.");
            tienda = new Tienda();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {
            tienda = (Tienda) ois.readObject();
            System.out.println(">> Partida cargada desde " + ruta);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar la partida: " + e.getMessage() + ". Comenzando nueva.");
            tienda = new Tienda();
        }
    }
}

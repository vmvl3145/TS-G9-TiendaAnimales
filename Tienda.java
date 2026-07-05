import java.io.*;
import java.util.ArrayList;
import java.util.List;
import Excepciones.*;
import Mascotas.FabricaMascotas;
import Mascotas.Mascota;
import Suministros.Suministro;

public class Tienda implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Tienda tienda;
    private double presupuesto;
    private List<Mascota> inventarioMascotas;
    private List<Suministro> inventarioSuministros;
    private final int inventarioMaximo = 5;
    private transient List<ObservadorTienda> observadores;

    // Constructor Singleton
    private Tienda() {
        this.presupuesto = 1000.0;
        this.inventarioMascotas = new ArrayList<>();
        this.inventarioSuministros = new ArrayList<>();
        this.observadores = new ArrayList<>();
    }

    // Singleton
    public static Tienda getTienda() {
        if (tienda == null) {
            tienda = new Tienda();
        }
        return tienda;
    }

    // Getters
    public double getPresupuesto() {
        return presupuesto;
    }

    public List<Mascota> getInventarioMascotas() {
        return inventarioMascotas;
    }

    public List<Suministro> getInventarioSuministros() {
        return inventarioSuministros;
    }

    // Observadores
    public void agregarObservador(ObservadorTienda obs) {
        if (observadores == null) observadores = new ArrayList<>();
        if (!observadores.contains(obs)) observadores.add(obs);
    }

    public void eliminarObservador(ObservadorTienda obs) {
        if (observadores != null) observadores.remove(obs);
    }

    private void notificarObservadores() {
        if (observadores == null) return;
        for (ObservadorTienda obs : observadores) {
            obs.actualizar();
        }
    }

    // Factory Method Comercial
    public void comprarMascota(String tipo) throws DineroInsuficienteException, CapacidadMaximaException {
        if (inventarioMascotas.size() >= inventarioMaximo) {
            throw new CapacidadMaximaException("No hay espacio en la tienda. Vende una mascota primero.");
        }
        Mascota nueva = FabricaMascotas.crearMascotaComprada(tipo, this.presupuesto);
        this.presupuesto -= nueva.getPrecio();
        inventarioMascotas.add(nueva);

        notificarObservadores();
    }

    // Factory Method Rescate
    public void rescatarMascota(String tipo) throws CapacidadMaximaException {
        if (inventarioMascotas.size() >= inventarioMaximo) {
            throw new CapacidadMaximaException("No hay espacio en la tienda. Vende una mascota primero.");
        }
        Mascota rescatada = FabricaMascotas.crearMascotaRescatada(tipo);
        inventarioMascotas.add(rescatada);

        notificarObservadores();
    }

    public void agregarMascota(Mascota mascota) throws CapacidadMaximaException {
        if (inventarioMascotas.size() >= inventarioMaximo) {
            throw new CapacidadMaximaException("No hay espacio en la tienda para un/a " + mascota.getEspecie());
        }
        inventarioMascotas.add(mascota);

        notificarObservadores();
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

        notificarObservadores();
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

        notificarObservadores();
    }

    // TIEMPO
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

        notificarObservadores();
    }

    public void usarSuministroEnMascota(Suministro sum, Mascota masc) {
        sum.usar(masc);
        this.inventarioSuministros.remove(sum);
        notificarObservadores();
    }

    // HUD CONSOLA
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

    // GUARDADO Y CARGADO DE PARTIDA
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

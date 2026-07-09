import java.io.*;
import java.util.ArrayList;
import java.util.List;
import excepciones.*;
import mascotas.FabricaMascotas;
import mascotas.Mascota;
import suministros.Suministro;

/**
 * Clase principal que actúa como el motor central del simulador de la tienda de mascotas.
 * Implementa el patrón Singleton para garantizar una única instancia global.
 * Maneja la lógica de negocio (compras, ventas, inventarios), el paso del tiempo
 * in-game y la persistencia de datos (guardado/carga).
 * <p>
 * Funciona como el "Sujeto" (Observable) en el patrón Observer, notificando a las
 * vistas (interfaces gráficas) cuando ocurre un cambio en el estado.
 */
public class Tienda implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Instancia única de la tienda (Patrón Singleton). */
    private static Tienda tienda;

    /** Dinero disponible para realizar transacciones. */
    private double presupuesto;

    /** Lista actual de mascotas alojadas en la tienda. */
    private List<Mascota> inventarioMascotas;

    /** Lista actual de suministros (comida, medicina, etc.) disponibles para usar. */
    private List<Suministro> inventarioSuministros;

    /** Capacidad máxima de mascotas que la tienda puede albergar simultáneamente. */
    private final int inventarioMaximo = 5;

    /** * Lista de observadores (ventanas) escuchando cambios.
     * Es 'transient' para que no se guarde en el archivo al serializar la partida.
     */
    private transient List<ObservadorTienda> observadores;

    /** Nombre del usuario actual gestionando la tienda. */
    private String nombreJugador;

    /** Indicador de si existen modificaciones en la partida que no han sido guardadas. */
    private transient boolean cambiosSinGuardar = false;

    // --- VARIABLES DE TIEMPO ---
    private int horaActual = 8;
    private int horaCierre = 20;
    private int diaActual = 1;
    private boolean tiendaAbierta = true;

    /**
     * Constructor privado para forzar el patrón Singleton.
     * Inicializa los inventarios vacíos y establece un presupuesto base.
     */
    private Tienda() {
        this.presupuesto = 1000.0;
        this.inventarioMascotas = new ArrayList<>();
        this.inventarioSuministros = new ArrayList<>();
        this.observadores = new ArrayList<>();
        this.nombreJugador = "";
    }

    /**
     * Obtiene la instancia global y única de la tienda.
     * Si no existe, la crea (Lazy Initialization).
     *
     * @return La instancia actual de {@code Tienda}.
     */
    public static Tienda getTienda() {
        if (tienda == null) {
            tienda = new Tienda();
        }
        return tienda;
    }

    // --- GETTERS & SETTERS ---

    public double getPresupuesto() { return presupuesto; }
    public List<Mascota> getInventarioMascotas() { return inventarioMascotas; }
    public List<Suministro> getInventarioSuministros() { return inventarioSuministros; }
    public String getNombreJugador() { return nombreJugador; }
    public void setNombreJugador(String nombreJugador) { this.nombreJugador = nombreJugador; }
    public boolean isCambiosSinGuardar() { return cambiosSinGuardar; }
    public void setCambiosSinGuardar(boolean cambiosSinGuardar) { this.cambiosSinGuardar = cambiosSinGuardar; }

    public int getHoraActual() {
        if (horaCierre == 0) initDefaults(); // Para partidas viejas guardadas
        return horaActual;
    }

    public int getDiaActual() {
        if (horaCierre == 0) initDefaults();
        return diaActual;
    }

    public boolean isTiendaAbierta() {
        if (horaCierre == 0) initDefaults();
        return tiendaAbierta;
    }

    /**
     * Inicializa los valores por defecto del sistema de tiempo.
     * Útil como parche de retrocompatibilidad al cargar partidas de versiones anteriores.
     */
    private void initDefaults() {
        this.horaActual = 8;
        this.horaCierre = 20;
        this.diaActual = 1;
        this.tiendaAbierta = true;
    }

    /**
     * Verifica que el establecimiento se encuentre operando actualmente.
     *
     * @throws TiendaCerradaException Si la hora in-game superó la hora de cierre.
     */
    public void verificarAbierta() throws TiendaCerradaException {
        if (!isTiendaAbierta()) {
            throw new TiendaCerradaException("La tienda está cerrada. Pasa al siguiente día para continuar.");
        }
    }

    // --- PATRÓN OBSERVER ---

    /**
     * Suscribe un nuevo observador (como una ventana Swing) a los eventos de la tienda.
     *
     * @param obs El observador a registrar.
     */
    public void agregarObservador(ObservadorTienda obs) {
        if (observadores == null) observadores = new ArrayList<>();
        if (!observadores.contains(obs)) observadores.add(obs);
    }

    /**
     * Elimina un observador previamente suscrito.
     *
     * @param obs El observador a remover.
     */
    public void eliminarObservador(ObservadorTienda obs) {
        if (observadores != null) observadores.remove(obs);
    }

    /**
     * Avisa a todos los observadores suscritos que el estado interno ha cambiado
     * y marca la partida con cambios pendientes de guardado.
     */
    private void notificarObservadores() {
        this.cambiosSinGuardar = true;
        if (observadores == null) return;
        for (ObservadorTienda obs : observadores) {
            obs.actualizar();
        }
    }

    // --- LÓGICA DE NEGOCIO ---

    /**
     * Adquiere una nueva mascota sana mediante una transacción comercial.
     *
     * @param tipo La especie deseada (ej. "Perro", "Gato").
     * @throws DineroInsuficienteException Si el costo supera el presupuesto actual.
     * @throws CapacidadMaximaException Si el inventario alcanzó su límite.
     * @throws TiendaCerradaException Si se intenta operar fuera del horario comercial.
     */
    public void comprarMascota(String tipo)
            throws DineroInsuficienteException, CapacidadMaximaException, TiendaCerradaException {
        verificarAbierta();
        if (inventarioMascotas.size() >= inventarioMaximo) {
            throw new CapacidadMaximaException("No hay espacio en la tienda. Vende una mascota primero.");
        }
        Mascota nueva = FabricaMascotas.crearMascotaComprada(tipo, this.presupuesto);
        this.presupuesto -= nueva.getPrecio();
        inventarioMascotas.add(nueva);
        notificarObservadores();
    }

    /**
     * Ingresa una mascota en estado crítico de salud al inventario de forma gratuita.
     *
     * @param tipo La especie del animal rescatado.
     * @throws CapacidadMaximaException Si el inventario alcanzó su límite.
     * @throws TiendaCerradaException Si se intenta operar fuera del horario comercial.
     */
    public void rescatarMascota(String tipo) throws CapacidadMaximaException, TiendaCerradaException {
        verificarAbierta();
        if (inventarioMascotas.size() >= inventarioMaximo) {
            throw new CapacidadMaximaException("No hay espacio en la tienda. Vende una mascota primero.");
        }
        Mascota rescatada = FabricaMascotas.crearMascotaRescatada(tipo);
        inventarioMascotas.add(rescatada);
        notificarObservadores();
    }

    /**
     * Agrega una mascota ya instanciada directamente al inventario (bypass de fábrica).
     *
     * @param mascota El objeto mascota a ingresar.
     * @throws CapacidadMaximaException Si el inventario alcanzó su límite.
     * @throws TiendaCerradaException Si la tienda está cerrada.
     */
    public void agregarMascota(Mascota mascota) throws CapacidadMaximaException, TiendaCerradaException {
        verificarAbierta();
        if (inventarioMascotas.size() >= inventarioMaximo) {
            throw new CapacidadMaximaException("No hay espacio en la tienda para un/a " + mascota.getEspecie());
        }
        inventarioMascotas.add(mascota);
    }

    /**
     * Vende una mascota del inventario, validando su estado de salud.
     * El precio de venta será un 150% del precio base de la mascota.
     *
     * @param especieMascota La especie a buscar en el inventario para vender.
     * @throws MascotaNoEncontradaException Si no se posee ninguna mascota de esa especie.
     * @throws MascotaEnfermaException Si la mascota está en estado crítico (EstadoEnfermo).
     * @throws TiendaCerradaException Si la tienda está cerrada.
     */
    public void venderMascota(String especieMascota)
            throws MascotaNoEncontradaException, MascotaEnfermaException, TiendaCerradaException {
        verificarAbierta();
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

        mascotaVender.getEstado().verificarVenta(mascotaVender); // Llama al patrón State

        inventarioMascotas.remove(mascotaVender);
        double precioVenta = mascotaVender.getPrecio() * 1.5;
        this.presupuesto += precioVenta;
        System.out.println(">> Venta exitosa: " + mascotaVender.getEspecie() + " por $" + precioVenta);
        notificarObservadores();
    }

    /**
     * Adquiere un suministro para el inventario de la tienda.
     *
     * @param suministro El objeto suministro a comprar.
     * @throws DineroInsuficienteException Si el costo del suministro supera el presupuesto.
     * @throws TiendaCerradaException Si la tienda está cerrada.
     */
    public void agregarSuministro(Suministro suministro) throws DineroInsuficienteException, TiendaCerradaException {
        verificarAbierta();
        if (this.presupuesto >= suministro.getPrecio()) {
            this.presupuesto -= suministro.getPrecio();
            inventarioSuministros.add(suministro);
            System.out.println(">> Suministro comprado: " + suministro.getNombre() + " por $" + suministro.getPrecio());
            notificarObservadores();
        } else {
            throw new DineroInsuficienteException("Presupuesto insuficiente. Tienes $" +
                    this.presupuesto + " pero " + suministro.getNombre() +
                    " cuesta $" + suministro.getPrecio());
        }
    }

    // --- SISTEMA DE TIEMPO ---

    /**
     * Avanza el reloj interno de la tienda en una hora y desencadena
     * las necesidades fisiológicas de las mascotas. Cierra la tienda al final del día.
     */
    public void avanzarReloj() {
        if (!isTiendaAbierta()) return;
        horaActual++;
        if (horaActual >= horaCierre) {
            tiendaAbierta = false;
            System.out.println("\n[Las 20:00! La tienda ha cerrado. Finaliza el día]");
        }
        pasarTiempo();
    }

    /**
     * Reinicia el ciclo diario, abriendo la tienda a las 08:00 AM y avanzando el calendario.
     */
    public void empezarSiguienteDia() {
        this.diaActual++;
        this.horaActual = 8;
        this.tiendaAbierta = true;
        System.out.println("\n[¡Nuevo Día! Día " + this.diaActual + " iniciado]");
        notificarObservadores();
    }

    /**
     * Modifica gradualmente los atributos de las mascotas (salud, hambre, higiene)
     * simulando el paso del tiempo. Las mascotas desatendidas pueden enfermar.
     */
    public void pasarTiempo() {
        System.out.println("\n[Ha pasado una hora in-game... Las mascotas sienten necesidades]");
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

    // --- CONSOLA / HUD ---

    /**
     * Imprime por consola el estado actual y detallado de los recursos
     * e inventarios de la tienda.
     */
    public void mostrarHud() {
        System.out.println("\n===== ESTADO DE LA TIENDA =====");
        System.out.println("Presupuesto: $" + presupuesto);
        System.out.println("Mascotas (" + inventarioMascotas.size() + "/" + inventarioMaximo + "):");
        if (inventarioMascotas.isEmpty()) {
            System.out.println("  (sin mascotas)");
        } else {
            for (Mascota m : inventarioMascotas) {
                System.out.println("  - " + m.getEspecie()
                        + " | Estado: [" + m.getEstado().describir() + "]"
                        + " | Salud: " + m.getNivelSalud()
                        + " | Hambre: " + m.getNivelHambre()
                        + " | Higiene: " + m.getNivelHigiene());
            }
        }
        System.out.println("Suministros en stock: " + inventarioSuministros.size());
        System.out.println("================================\n");
    }

    // --- PERSISTENCIA DE DATOS ---

    /**
     * Serializa y guarda la instancia actual de la tienda en un archivo.
     *
     * @param ruta La ubicación del sistema de archivos donde se generará el guardado.
     */
    public void guardarPartida(String ruta) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
            oos.writeObject(this);
            this.cambiosSinGuardar = false;
            System.out.println(">> Partida guardada en " + ruta);
        } catch (IOException e) {
            System.err.println("Error al guardar la partida: " + e.getMessage());
        }
    }

    /**
     * Carga y deserializa una partida guardada previamente desde un archivo.
     * Si el archivo no existe o está corrupto, inicializa una tienda completamente nueva.
     *
     * @param ruta La ubicación del archivo de guardado a leer.
     */
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
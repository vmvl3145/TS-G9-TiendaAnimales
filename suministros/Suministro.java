package suministros;
import mascotas.Mascota;
import java.io.Serializable;

/**
 * Clase base abstracta que representa un suministro genérico en la tienda.
 * Define los atributos y comportamientos fundamentales que comparten todos los
 * tipos de suministros (como comida, higiene o medicina).
 * Implementa {@link Serializable} para permitir que el inventario de suministros
 * se guarde correctamente en el archivo de la partida.
 */
public abstract class Suministro implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String nombre;
    protected double precio;

    /**
     * Construye un nuevo suministro inicializando sus atributos básicos.
     * Al ser una clase abstracta, este constructor solo es invocado por las
     * clases hijas (subclases) mediante la palabra clave super().
     *
     * @param nombre El nombre del suministro (ej. "Vitaminas").
     * @param precio El costo de adquisición del suministro.
     */
    public Suministro(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    /**
     * Obtiene el nombre del suministro.
     *
     * @return La cadena de texto con el nombre del suministro.
     */
    public String getNombre() { return nombre; }

    /**
     * Obtiene el precio comercial del suministro.
     *
     * @return El valor monetario del suministro.
     */
    public double getPrecio() { return precio; }

    /**
     * Aplica el efecto del suministro sobre una mascota.
     * Al ser un método abstracto, delega la responsabilidad de la implementación
     * a cada clase hija, permitiendo que cada suministro afecte estadísticas
     * distintas (salud, hambre, higiene, etc.).
     *
     * @param mascota La mascota sobre la cual se consumirá o utilizará el suministro.
     */
    public abstract void usar(Mascota mascota);
}

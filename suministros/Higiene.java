package suministros;
import mascotas.Mascota;

/**
 * Representa un suministro destinado al aseo dentro de la tienda.
 * Al utilizarse sobre una mascota, incrementa su nivel de higiene
 * en base a su poder de limpieza.
 * Hereda de la clase base {@link Suministro}.
 */

public class Higiene extends Suministro {

    private static final long serialVersionUID = 1L;
    private int valorLimpieza;

    /**
     * Construye un nuevo suministro de tipo higiene.
     *
     * @param nombre        El nombre descriptivo del producto (ej. "Champú Antipulgas").
     * @param precio        El costo comercial del suministro en la tienda.
     * @param valorLimpieza Los puntos de higiene que se sumarán al utilizarlo.
     */

    public Higiene(String nombre, double precio, int valorLimpieza) {
        super(nombre, precio);
        this.valorLimpieza = valorLimpieza;
    }

    /**
     * Aplica el efecto del producto de limpieza sobre una mascota específica.
     * Imprime un mensaje por consola y aumenta el nivel de higiene
     * de la mascota según el valor de limpieza del suministro.
     *
     * @param mascota La mascota que va a ser aseada con este producto.
     */

    @Override
    public void usar(Mascota mascota) {
        System.out.println("Limpiando al " + mascota.getEspecie() + " con " + this.nombre + ".");
        mascota.setNivelHigiene(mascota.getNivelHigiene() + this.valorLimpieza);
    }
}

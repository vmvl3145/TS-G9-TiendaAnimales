package suministros;
import mascotas.Mascota;

/**
 * Representa un suministro de tipo comida dentro de la tienda.
 * Al utilizarse sobre una mascota, reduce su nivel de hambre en base
 * a su valor nutricional.
 * Hereda de la clase base {@link Suministro}.
 */

public class Comida extends Suministro {
    private static final long serialVersionUID = 1L;
    private int valorNutricional;

    /**
     * Construye un nuevo suministro de tipo comida.
     *
     * @param nombre           El nombre descriptivo del alimento.
     * @param precio           El costo comercial del suministro en la tienda.
     * @param valorNutricional Los puntos de hambre que se restarán al utilizarlo.
     */

    public Comida(String nombre, double precio, int valorNutricional) {
        super(nombre, precio);
        this.valorNutricional = valorNutricional;
    }

    /**
     * Aplica el efecto de la comida sobre una mascota específica.
     * Imprime un mensaje por consola y disminuye el nivel de hambre
     * de la mascota según el valor nutricional del alimento.
     *
     * @param mascota La mascota que va a consumir este suministro.
     */

    @Override
    public void usar(Mascota mascota) {
        System.out.println("Alimentando al " + mascota.getEspecie() + " con " + this.nombre + ".");
        // Restamos hambre porque acaba de comer
        mascota.setNivelHambre(mascota.getNivelHambre() - this.valorNutricional);
    }
}

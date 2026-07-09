package suministros;
import mascotas.Mascota;

/**
 * Representa un suministro de tipo médico dentro de la tienda.
 * Al utilizarse sobre una mascota, incrementa su nivel de salud
 * en base a su poder curativo, siendo vital para mascotas rescatadas o enfermas.
 * Hereda de la clase base {@link Suministro}.
 */

public class Medicina extends Suministro {
    private static final long serialVersionUID = 1L;
    private int poderCurativo;

    /**
     * Construye un nuevo suministro de tipo medicina.
     *
     * @param nombre        El nombre descriptivo del medicamento (ej. "Vitaminas", "Antibióticos").
     * @param precio        El costo comercial del suministro en la tienda.
     * @param poderCurativo Los puntos de salud que se sumarán a la mascota al utilizarlo.
     */
    public Medicina(String nombre, double precio, int poderCurativo) {
        super(nombre, precio);
        this.poderCurativo = poderCurativo;
    }

    /**
     * Aplica el efecto del medicamento sobre una mascota específica.
     * Imprime un mensaje por consola y aumenta el nivel de salud
     * de la mascota según el poder curativo del suministro.
     *
     * @param mascota La mascota que va a recibir este tratamiento médico.
     */
    @Override
    public void usar(Mascota mascota) {
        System.out.println("Tratando al " + mascota.getEspecie() + " con " + this.nombre + ".");
        mascota.setNivelSalud(mascota.getNivelSalud() + this.poderCurativo);
    }
}

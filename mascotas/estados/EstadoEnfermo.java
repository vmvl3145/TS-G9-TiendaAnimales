package mascotas.estados;

import excepciones.MascotaEnfermaException;
import mascotas.Mascota;

/**
 * Representa el estado "Enfermo" de una mascota.
 * Implementa el patrón State a través de la interfaz {@link EstadoMascota}.
 * En este estado, la mascota tiene un nivel de salud crítico y sus acciones
 * comerciales (como la venta) están restringidas.
 */

public class EstadoEnfermo implements EstadoMascota {

    /**
     * Identificador de versión para asegurar la compatibilidad durante
     * la serialización y deserialización de la partida guardada.
     */

    private static final long serialVersionUID = 1L;

    /**
     * Verifica si la mascota es apta para ser vendida o adoptada.
     * Al encontrarse en estado "Enfermo", la mascota no puede ser comercializada
     * y el método bloqueará la acción.
     *
     * @param mascota La instancia de la mascota que se intenta evaluar.
     * @throws MascotaEnfermaException Siempre es lanzada en este estado, advirtiendo
     * al usuario que la mascota necesita medicina urgente.
     */

    @Override
    public void verificarVenta(Mascota mascota) throws MascotaEnfermaException {
        throw new MascotaEnfermaException(
                "El/La " + mascota.getEspecie() +
                        " está demasiado débil para adoptarlo (Salud: " +
                        mascota.getNivelSalud() + "/100). ¡Necesita medicina!");
    }

    /**
     * Proporciona una descripción breve y en texto del estado actual.
     * Se utiliza típicamente para mostrar la condición de la mascota en la interfaz gráfica (HUD).
     *
     * @return La cadena de texto "Enfermo".
     */

    @Override
    public String describir() {
        return "Enfermo";
    }
}

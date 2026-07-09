package mascotas.estados;

import excepciones.MascotaEnfermaException;
import mascotas.Mascota;

/**
 * Representa el estado "Sano" de una mascota.
 * Implementa el patrón State a través de la interfaz {@link EstadoMascota}.
 * En este estado, la mascota goza de buena salud y no tiene restricciones
 * para realizar acciones comerciales como la venta o adopción.
 */
public class EstadoSano implements EstadoMascota {
    /**
     * Identificador de versión para asegurar la compatibilidad durante
     * la serialización y deserialización de la partida guardada.
     */

    private static final long serialVersionUID = 1L;

    /**
     * Verifica si la mascota es apta para ser vendida o adoptada.
     * Al encontrarse en estado "Sano", la mascota puede ser comercializada
     * sin problemas, por lo que este método aprueba la acción silenciosamente
     * sin interrumpir el flujo del programa.
     *
     * @param mascota La instancia de la mascota que se intenta evaluar.
     * @throws MascotaEnfermaException No se lanza en este estado, ya que la mascota cumple
     * los requisitos de salud para ser vendida.
     */

    @Override
    public void verificarVenta(Mascota mascota) throws MascotaEnfermaException {
        // Este bloque se encuentra vacío porque la venta de las mascotas sanas está permitida.
    }

    /**
     * Proporciona una descripción breve y en texto del estado actual.
     * Se utiliza principalmente para mostrar la condición de la mascota
     * de forma legible en la interfaz gráfica (HUD) o en la consola.
     *
     * @return La cadena de texto "Sano".
     */
    @Override
    public String describir() {
        return "Sano";
    }
}

package mascotas.estados;

import excepciones.MascotaEnfermaException;
import mascotas.Mascota;
import java.io.Serializable;

/**
 * Interfaz que define el contrato para los distintos estados de salud de una mascota.
 * Es la pieza central de la implementación del patrón de diseño State, permitiendo
 * que el comportamiento de la mascota (como su capacidad de ser vendida) cambie
 * dinámicamente según su condición actual.
 * * Hereda de {@link Serializable} para garantizar que el estado activo pueda
 * guardarse correctamente en el archivo de guardado de la tienda.
 */

public interface EstadoMascota extends Serializable {
    /**
     * Evalúa si el estado actual permite que la mascota sea vendida o adoptada.
     * Las clases que implementen esta interfaz definirán las reglas específicas
     * (por ejemplo, aprobar la venta silenciosamente si está sana, o bloquearla
     * si está enferma).
     *
     * @param mascota La instancia de la mascota sobre la cual se realiza la verificación.
     * @throws MascotaEnfermaException Si el estado actual prohíbe la transacción comercial
     * debido a problemas de salud.
     */

    void verificarVenta(Mascota mascota) throws MascotaEnfermaException;

    /**
     * Obtiene una representación breve y en texto del estado actual.
     * Se utiliza principalmente para mostrar la condición de la mascota
     * de forma legible en la interfaz gráfica (HUD) o en la consola.
     *
     * @return Una cadena de texto descriptiva (ej. "Sano", "Enfermo").
     */

    String describir();
}

package excepciones;

/**
 * Excepción que se lanza cuando se intenta realizar una operación comercial
 * (como vender o adoptar) con una mascota que tiene un nivel de salud crítico.
 */

public class MascotaEnfermaException extends Exception {
    /**
     * Construye una nueva excepción con un mensaje detallado sobre el estado del animal.
     * @param mensaje El detalle del error
     */
    public MascotaEnfermaException(String mensaje) {
        super(mensaje);
    }
}
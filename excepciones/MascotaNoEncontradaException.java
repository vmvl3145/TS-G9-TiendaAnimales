package excepciones;

/**
 * Excepción que se lanza cuando la mascota solicitada no se
 * encuentra disponible en el inventario.
 */
public class MascotaNoEncontradaException extends Exception {
    /**
     * Construye una nueva excepción con un mensaje detallado.
     * @param mensaje El texto descriptivo del error.
     */
    public MascotaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
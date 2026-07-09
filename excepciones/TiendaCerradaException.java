package excepciones;

/**
 * Excepcion que se lanza cuando la tienda se encuentra cerrada
 */
public class TiendaCerradaException extends Exception {
    /**
     * Construye una nueva excepción con un mensaje detallado.
     * @param mensaje El texto descriptivo del error.
     */
    public TiendaCerradaException(String mensaje) {
        super(mensaje);
    }
}

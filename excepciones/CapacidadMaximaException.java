package excepciones;

/**
 * Excepción que se lanza cuando ya se alcanzó la capacidad máxima
 * de cierto elemento o mascota
 */
public class CapacidadMaximaException extends Exception {
    /**
     * Construye una nueva excepción con un mensaje detallado
     * @param mensaje El texto descriptivo del error
     */
    public CapacidadMaximaException(String mensaje) {
        super(mensaje);
    }
}
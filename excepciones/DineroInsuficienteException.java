package excepciones;

/**
 * Excepción que se lanza cuando el presupuesto es insuficiente para comprar
 * cierto elemento o mascota
 */
public class DineroInsuficienteException extends Exception {
    /**
     * Construye una nueva excepcion con un mensaje detallado
     * @param mensaje El texto descriptivo del error
     */
    public DineroInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
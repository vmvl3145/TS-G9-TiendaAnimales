/**
 * Interfaz Observer del patrón Observer (GoF) — Comportamiento.
 *
 * Define el contrato de suscripción que deben implementar todos
 * los componentes que quieran recibir notificaciones automáticas
 * de cambios de estado en la Tienda (Subject/Publisher).
 *
 * Desacoplamiento garantizado: Tienda solo conoce esta interfaz,
 * nunca la clase concreta VentanaTienda. Esto permite agregar
 * cualquier tipo de vista adicional (consola, red, log, etc.)
 * sin modificar una sola línea del Modelo.
 */
public interface ObservadorTienda {

    /**
     * Método de callback invocado automáticamente por Tienda
     * cada vez que su estado cambia (transacción económica,
     * paso del tiempo, etc.). El Observer debe refrescar
     * su representación del estado aquí.
     */
    void actualizar();
}

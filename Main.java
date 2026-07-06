import javax.swing.SwingUtilities;

/**
 * Punto de entrada de la aplicación.
 *
 * Responsabilidades:
 *   1. Cargar la partida previa desde disco (persistencia, Semana 2).
 *   2. Lanzar la interfaz gráfica en el Event Dispatch Thread (EDT),
 *      siguiendo las buenas prácticas de concurrencia de Java SWING.
 *
 * No contiene lógica de negocio: esta clase únicamente inicializa
 * el Modelo (Tienda) y delega la presentación a la Vista (VentanaTienda).
 */
public class Main {
    public static void main(String[] args) {
        final String ARCHIVO_GUARDADO = "partida_tienda.dat";

        // Cargar el estado previo antes de crear la UI
        Tienda.cargarPartida(ARCHIVO_GUARDADO);

        // Lanzar la ventana en el Event Dispatch Thread (obligatorio en SWING)
        SwingUtilities.invokeLater(() -> {
            VentanaTienda ventana = new VentanaTienda(ARCHIVO_GUARDADO);
            ventana.setVisible(true);
        });
    }
}
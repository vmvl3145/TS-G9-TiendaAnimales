import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        final String ARCHIVO_GUARDADO = "partida_tienda.dat";

        // Cargar el estado previo antes de crear la UI
        Tienda.cargarPartida(ARCHIVO_GUARDADO);

        // Lanzar la ventana en el Event Dispatch Thread (obligatorio en Swing)
        SwingUtilities.invokeLater(() -> {
            VentanaTienda ventana = new VentanaTienda(ARCHIVO_GUARDADO);
            ventana.setVisible(true);
        });
    }
}
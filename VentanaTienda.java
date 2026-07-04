import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import Excepciones.*;
import Mascotas.Mascota;
import Suministros.Suministro;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Vista principal de la Tienda de Mascotas — interfaz gráfica Swing (Semana 3).
 *
 * Responsabilidades:
 *   - Construir y mostrar la ventana principal de la aplicación.
 *   - Delegar todas las acciones de negocio a Tienda (Modelo).
 *   - Actualizar el HUD manualmente tras cada acción.
 *
 * Patrones aplicados: MVC (Vista), Singleton (acceso a Tienda).
 */
public class VentanaTienda extends JFrame {

    // PALETA DE COLORES
    private static final Color C_FONDO      = new Color(15, 17, 26);
    private static final Color C_PANEL      = new Color(22, 27, 44);
    private static final Color C_HUD_BG     = new Color(10, 12, 22);
    private static final Color C_HUD_TEXTO  = new Color(80, 250, 123);
    private static final Color C_LOG_BG     = new Color(12, 14, 24);
    private static final Color C_LOG_TEXTO  = new Color(180, 190, 210);
    private static final Color C_BORDE      = new Color(40, 50, 80);
    private static final Color C_SUBTITULO  = new Color(120, 135, 170);

    // COLORES por categoría de botón
    private static final Color C_BTN_COMPRA     = new Color(22, 163, 74);
    private static final Color C_BTN_RESCATE    = new Color(234, 88, 12);
    private static final Color C_BTN_VENTA      = new Color(79, 70, 229);
    private static final Color C_BTN_SUMINISTRO = new Color(14, 165, 233);
    private static final Color C_BTN_TIEMPO     = new Color(147, 51, 234);
    private static final Color C_BTN_SISTEMA    = new Color(71, 85, 105);

    // TIPOGRAFÍAS
    private static final Font F_TITULO  = new Font("SansSerif", Font.BOLD, 20);
    private static final Font F_SECCION = new Font("SansSerif", Font.BOLD, 11);
    private static final Font F_BOTON   = new Font("SansSerif", Font.BOLD, 12);
    private static final Font F_HUD     = new Font("Monospaced", Font.PLAIN, 13);
    private static final Font F_LOG     = new Font("Monospaced", Font.PLAIN, 11);

    // ESTADO
    private Tienda tienda;
    private final String archivoGuardado;

    // Componentes de UI reutilizables
    private JTextArea hudArea;
    private JTextArea logArea;
    private JPanel mascotasPanel;
    private final Map<String, ImageIcon> cacheIconos = new HashMap<>();

    // CONSTRUCTOR
    public VentanaTienda(String archivoGuardado) {
        this.tienda = Tienda.getTienda();
        this.archivoGuardado = archivoGuardado;
        construirUI();
        actualizarHUD();
    }

    // =========================================================
    // CONSTRUCCIÓN DE LA UI
    // =========================================================

    private void construirUI() {
        setTitle("Tienda de Mascotas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 680);
        setMinimumSize(new Dimension(800, 580));
        setLocationRelativeTo(null);
        getContentPane().setBackground(C_FONDO);

        JPanel raiz = new JPanel(new BorderLayout(12, 12));
        raiz.setBackground(C_FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        raiz.add(construirHeader(), BorderLayout.NORTH);
        raiz.add(construirCentro(), BorderLayout.CENTER);
        raiz.add(construirPanelLog(), BorderLayout.SOUTH);

        add(raiz);
    }

    private JPanel construirHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_PANEL);
        p.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(C_BORDE, 1, true),
                BorderFactory.createEmptyBorder(10, 18, 10, 18)));

        JLabel titulo = new JLabel("🐾  TIENDA DE MASCOTAS");
        titulo.setFont(F_TITULO);
        titulo.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Semana 3 — Interfaz Gráfica Swing");
        sub.setFont(new Font("SansSerif", Font.ITALIC, 12));
        sub.setForeground(C_SUBTITULO);

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 2));
        textos.setBackground(C_PANEL);
        textos.add(titulo);
        textos.add(sub);
        p.add(textos, BorderLayout.WEST);
        return p;
    }

    private JPanel construirCentro() {
        JPanel centro = new JPanel(new BorderLayout(12, 0));
        centro.setBackground(C_FONDO);
        centro.add(construirHUD(), BorderLayout.CENTER);
        centro.add(construirPanelBotones(), BorderLayout.EAST);
        return centro;
    }

    private JPanel construirHUD() {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setBackground(C_FONDO);

        JLabel etiqueta = new JLabel("  📊 ESTADO ACTUAL DE LA TIENDA");
        etiqueta.setFont(F_SECCION);
        etiqueta.setForeground(C_SUBTITULO);
        p.add(etiqueta, BorderLayout.NORTH);

        hudArea = new JTextArea();
        hudArea.setEditable(false);
        hudArea.setFont(F_HUD);
        hudArea.setBackground(C_HUD_BG);
        hudArea.setForeground(C_HUD_TEXTO);
        hudArea.setCaretColor(C_HUD_TEXTO);
        hudArea.setBorder(BorderFactory.createEmptyBorder(12, 14, 6, 14));
        hudArea.setLineWrap(false);

        mascotasPanel = new JPanel();
        mascotasPanel.setLayout(new BoxLayout(mascotasPanel, BoxLayout.Y_AXIS));
        mascotasPanel.setBackground(C_HUD_BG);
        mascotasPanel.setBorder(BorderFactory.createEmptyBorder(0, 14, 12, 14));

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(C_HUD_BG);
        contenedor.add(hudArea, BorderLayout.NORTH);
        contenedor.add(mascotasPanel, BorderLayout.CENTER);

        JScrollPane scroll = new JScrollPane(contenedor);
        scroll.setBorder(new LineBorder(C_BORDE, 1));
        scroll.getViewport().setBackground(C_HUD_BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel construirPanelBotones() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(C_FONDO);
        p.setPreferredSize(new Dimension(215, 0));

        p.add(seccion("ADQUISICIÓN", new JButton[]{
                boton("Comprar Mascota",   C_BTN_COMPRA,     e -> accionComprar()),
                boton("Rescatar Mascota",  C_BTN_RESCATE,    e -> accionRescatar())
        }));
        p.add(Box.createVerticalStrut(8));
        p.add(seccion("VENTAS", new JButton[]{
                boton("Vender Mascota",    C_BTN_VENTA,      e -> accionVender())
        }));
        p.add(Box.createVerticalStrut(8));
        p.add(seccion("SUMINISTROS", new JButton[]{
                boton("Comprar Suministro", C_BTN_SUMINISTRO, e -> accionComprarSuministro()),
                boton("Usar Suministro",    C_BTN_SUMINISTRO, e -> accionUsarSuministro())
        }));
        p.add(Box.createVerticalStrut(8));
        p.add(seccion("TIEMPO", new JButton[]{
                boton("Pasar el Tiempo",   C_BTN_TIEMPO,     e -> accionPasarTiempo())
        }));
        p.add(Box.createVerticalStrut(8));
        p.add(seccion("SISTEMA", new JButton[]{
                boton("Guardar Partida",   C_BTN_SISTEMA,    e -> accionGuardar()),
                boton("Cargar Partida",    C_BTN_SISTEMA,    e -> accionCargar())
        }));
        p.add(Box.createVerticalGlue());
        return p;
    }

    private JPanel construirPanelLog() {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(C_FONDO);
        p.setPreferredSize(new Dimension(0, 105));

        JLabel etiqueta = new JLabel("REGISTRO DE EVENTOS");
        etiqueta.setFont(F_SECCION);
        etiqueta.setForeground(C_SUBTITULO);
        p.add(etiqueta, BorderLayout.NORTH);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(F_LOG);
        logArea.setBackground(C_LOG_BG);
        logArea.setForeground(C_LOG_TEXTO);
        logArea.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setBorder(new LineBorder(C_BORDE, 1));
        scroll.getViewport().setBackground(C_LOG_BG);

        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    // =========================================================
    // HELPERS DE CONSTRUCCIÓN DE COMPONENTES
    // =========================================================

    private JPanel seccion(String titulo, JButton[] botones) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(C_PANEL);
        p.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(C_BORDE, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        JLabel lbl = new JLabel(titulo);
        lbl.setFont(F_SECCION);
        lbl.setForeground(C_SUBTITULO);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 2, 6, 0));
        p.add(lbl);

        for (JButton btn : botones) {
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
            p.add(btn);
            p.add(Box.createVerticalStrut(4));
        }
        return p;
    }

    private JButton boton(String texto, Color color, ActionListener listener) {
        JButton btn = new JButton(texto);
        btn.setFont(F_BOTON);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(listener);
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(color.brighter()); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(color); }
        });
        return btn;
    }

    // =========================================================
    // HUD — Renderizado del estado de la tienda
    // =========================================================

    public void actualizarHUD() {
        StringBuilder sb = new StringBuilder();
        sb.append("SIMULADOR DE TIENDA DE MASCOTAS\n");
        sb.append(String.format("Presupuesto disponible : $%-17.2f\n", tienda.getPresupuesto()));
        sb.append(String.format("Inventario mascotas    : %d / 5\n", tienda.getInventarioMascotas().size()));
        sb.append(String.format("Suministros en stock   : %-18d\n", tienda.getInventarioSuministros().size()));

        if (!tienda.getInventarioSuministros().isEmpty()) {
            sb.append("\nSUMINISTROS DISPONIBLES\n");
            for (Suministro s : tienda.getInventarioSuministros()) {
                sb.append(String.format("  • %-28s $%-7.2f\n", s.getNombre(), s.getPrecio()));
            }
        }
        hudArea.setText(sb.toString());
        hudArea.setCaretPosition(0);

        mascotasPanel.removeAll();
        if (tienda.getInventarioMascotas().isEmpty()) {
            JLabel vacio = new JLabel("  (no hay mascotas en inventario)");
            vacio.setForeground(C_HUD_TEXTO);
            vacio.setFont(F_HUD);
            mascotasPanel.add(vacio);
        } else {
            for (Mascota m : tienda.getInventarioMascotas()) {
                mascotasPanel.add(construirTarjetaMascota(m));
                mascotasPanel.add(Box.createVerticalStrut(6));
            }
        }
        mascotasPanel.revalidate();
        mascotasPanel.repaint();
    }

    // =========================================================
    // TARJETAS DE MASCOTAS
    // =========================================================

    private JPanel construirTarjetaMascota(Mascota m) {
        JPanel tarjeta = new JPanel(new BorderLayout(10, 0));
        tarjeta.setBackground(C_HUD_BG);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(C_BORDE, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel imagen = new JLabel(cargarIcono(m.getEspecie()));
        imagen.setPreferredSize(new Dimension(64, 64));
        imagen.setHorizontalAlignment(SwingConstants.CENTER);
        tarjeta.add(imagen, BorderLayout.WEST);

        boolean sano = m.getEstado().describir().equals("Sano");
        String estadoTag = sano ? "✓ SANO" : "✗ ENFERMO";
        JTextArea detalle = new JTextArea(
                String.format("%-5s  [%s]  Precio: $%-8.1f\n", m.getEspecie(), estadoTag, m.getPrecio())
              + String.format("Salud: %3d%%   %s\n", m.getNivelSalud(), barra(m.getNivelSalud()))
              + String.format("Hambre: %3d%%  Higiene: %3d%%", m.getNivelHambre(), m.getNivelHigiene()));
        detalle.setEditable(false);
        detalle.setFocusable(false);
        detalle.setFont(F_HUD);
        detalle.setBackground(C_HUD_BG);
        detalle.setForeground(C_HUD_TEXTO);
        tarjeta.add(detalle, BorderLayout.CENTER);
        return tarjeta;
    }

    private ImageIcon cargarIcono(String especie) {
        if (cacheIconos.containsKey(especie)) return cacheIconos.get(especie);
        ImageIcon icono;
        File archivo = new File("imagenes/" + especie.toLowerCase() + ".png");
        if (archivo.exists()) {
            Image escalada = new ImageIcon(archivo.getPath())
                    .getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            icono = new ImageIcon(escalada);
        } else {
            icono = iconoReemplazo();
        }
        cacheIconos.put(especie, icono);
        return icono;
    }

    private ImageIcon iconoReemplazo() {
        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(C_BORDE);
        g.fillRoundRect(0, 0, 64, 64, 12, 12);
        g.setColor(Color.WHITE);
        g.setFont(F_TITULO);
        g.drawString("?", 26, 40);
        g.dispose();
        return new ImageIcon(img);
    }

    private String barra(int valor) {
        int llenos = (int) Math.round(valor / 10.0);
        return "[" + "█".repeat(llenos) + "░".repeat(10 - llenos) + "]";
    }

    // =========================================================
    // HELPERS DE LOG Y ERRORES
    // =========================================================

    private void log(String mensaje) {
        logArea.append("> " + mensaje + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // =========================================================
    // ACCIONES DE BOTONES (stub — se implementarán en próximos commits)
    // =========================================================

    private void accionComprar() {
        Object[] opciones = { "Perro  ($150)", "Gato   ($100)", "Pez    ($50)" };
        Object sel = JOptionPane.showInputDialog(
                this,
                "<html><b>Modelo Comercial</b><br>El animal llegará <b>sano</b>.<br>"
                        + "El precio se descuenta de tu presupuesto.</html>",
                "Comprar Mascota",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (sel == null) return;

        String tipo = sel.toString().split("\\s")[0];
        try {
            tienda.comprarMascota(tipo);
            log("✓ Mascota comprada: " + tipo + " (-$" + precioBase(tipo) + ")");
        } catch (DineroInsuficienteException | CapacidadMaximaException ex) {
            mostrarError(ex.getMessage());
            log("✗ Error al comprar " + tipo + ": " + ex.getMessage());
        }
        actualizarHUD();
    }

    private void accionRescatar() {
        Object[] opciones = { "Perro", "Gato", "Pez" };
        Object sel = JOptionPane.showInputDialog(
                this,
                "<html><b>Modelo de Rescate / Refugio</b><br>"
                        + "Adquisición <b>gratuita</b>, pero el animal llegará en <b>estado crítico</b>.<br>"
                        + "Necesitará Medicina antes de poder ser vendido.</html>",
                "Rescatar Mascota",
                JOptionPane.WARNING_MESSAGE, null, opciones, opciones[0]);
        if (sel == null) return;

        try {
            tienda.rescatarMascota(sel.toString());
            log("♥ Mascota rescatada: " + sel + " (¡necesita cuidados urgentes!)");
        } catch (CapacidadMaximaException ex) {
            mostrarError(ex.getMessage());
            log("✗ " + ex.getMessage());
        }
        actualizarHUD();
    }

    private void accionVender() {
        // TODO: implementar venta de mascota
    }

    private void accionComprarSuministro() {
        // TODO: implementar compra de suministro
    }

    private void accionUsarSuministro() {
        // TODO: implementar uso de suministro
    }

    private void accionPasarTiempo() {
        // TODO: implementar avance del tiempo
    }

    private void accionGuardar() {
        // TODO: implementar guardado de partida
    }

    private void accionCargar() {
        // TODO: implementar carga de partida
    }
}

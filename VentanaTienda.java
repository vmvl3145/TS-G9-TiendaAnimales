import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import excepciones.*;
import mascotas.Mascota;
import suministros.Suministro;
import suministros.Comida;
import suministros.Medicina;
import suministros.Higiene;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.ChangeEvent;

public class VentanaTienda extends JFrame implements ObservadorTienda {

    private static final Color C_FONDO = new Color(15, 17, 26);
    private static final Color C_PANEL = new Color(22, 27, 44);
    private static final Color C_HUD_BG = new Color(10, 12, 22);
    private static final Color C_HUD_TEXTO = new Color(80, 250, 123); // verde terminal
    private static final Color C_LOG_BG = new Color(12, 14, 24);
    private static final Color C_LOG_TEXTO = new Color(180, 190, 210);
    private static final Color C_BORDE = new Color(40, 50, 80);
    private static final Color C_SUBTITULO = new Color(120, 135, 170);

    // COLORES por categoría de botón
    private static final Color C_BTN_COMPRA = new Color(22, 163, 74); // verde
    private static final Color C_BTN_RESCATE = new Color(234, 88, 12); // naranja
    private static final Color C_BTN_VENTA = new Color(79, 70, 229); // moradoso
    private static final Color C_BTN_SUMINISTRO = new Color(14, 165, 233); // celeste
    private static final Color C_BTN_TIEMPO = new Color(147, 51, 234); // violeta
    private static final Color C_BTN_SISTEMA = new Color(71, 85, 105); // gris pizarra

    // TIPOGRAFIAS - cambiables, no ocupar personalizadas
    private static final Font F_TITULO = new Font("SansSerif", Font.BOLD, 20);
    private static final Font F_SECCION = new Font("SansSerif", Font.BOLD, 11);
    private static final Font F_BOTON = new Font("SansSerif", Font.BOLD, 12);
    private static final Font F_HUD = new Font("Monospaced", Font.PLAIN, 13);
    private static final Font F_LOG = new Font("Monospaced", Font.PLAIN, 11);

    // ESTADO
    private Tienda tienda;
    private final String archivoGuardado;

    // Componentes de UI reutilizables
    private JTextArea hudArea;
    private JTextArea logArea;
    private JPanel mascotasPanel;
    private final Map<String, ImageIcon> cacheIconos = new HashMap<>();
    private JButton btnTiempo;
    private Timer timerTiempo;

    // CONSTRUCTOR
    public VentanaTienda(String archivoGuardado) {
        this.tienda = Tienda.getTienda();
        this.archivoGuardado = archivoGuardado;

        String nombre = this.tienda.getNombreJugador();
        if (nombre == null || nombre.trim().isEmpty()) {
            nombre = JOptionPane.showInputDialog(null,
                    "¡Bienvenido a la Tienda de Mascotas!\nIngresa tu nombre para comenzar:",
                    "Nueva Partida", JOptionPane.QUESTION_MESSAGE);
            if (nombre == null || nombre.trim().isEmpty()) {
                nombre = "Jefe/a de la tienda";
            }
            this.tienda.setNombreJugador(nombre.trim());
            this.tienda.setCambiosSinGuardar(true);
        }

        construirUI();
        // Observer
        this.tienda.agregarObservador(this);
        actualizarHUD();
        // Audio: musica de fondo via ControlAudio
        ControlAudio.reproducirFondo("sonidos/fondo.wav");
    }

    // CONSTRUCCIЩN DE LA UI
    private void construirUI() {
        setTitle("Tienda de Mascotas - " + tienda.getNombreJugador());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (Tienda.getTienda().isCambiosSinGuardar()) {
                    int confirm = JOptionPane.showConfirmDialog(VentanaTienda.this,
                            "Tienes cambios sin guardar. ¿Deseas salir de todas formas?",
                            "Confirmar Salida", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (confirm == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                } else {
                    System.exit(0);
                }
            }
        });

        setSize(1000, 780);
        setMinimumSize(new Dimension(800, 780));
        setLocationRelativeTo(null);
        getContentPane().setBackground(C_FONDO);

        JPanel raiz = new JPanel(new BorderLayout(12, 12));
        raiz.setBackground(C_FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        raiz.add(construirHeader(), BorderLayout.NORTH);
        raiz.add(construirCentro(), BorderLayout.CENTER);
        raiz.add(construirPanelLog(), BorderLayout.SOUTH);

        // Timer Automático: 10 segundos
        timerTiempo = new Timer(10000, e -> {
            if (Tienda.getTienda().isTiendaAbierta()) {
                Tienda.getTienda().avanzarReloj();
            }
        });
        timerTiempo.start();

        add(raiz);
    }

    // Header
    private JPanel construirHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_PANEL);
        p.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(C_BORDE, 1, true),
                BorderFactory.createEmptyBorder(10, 18, 10, 18)));

        JLabel titulo = new JLabel("🐾  TIENDA DE MASCOTAS");
        titulo.setFont(F_TITULO);
        titulo.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Jefe/a: " + tienda.getNombreJugador());
        sub.setFont(new Font("SansSerif", Font.ITALIC, 12));
        sub.setForeground(C_SUBTITULO);

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 2));
        textos.setBackground(C_PANEL);
        textos.add(titulo);
        textos.add(sub);
        p.add(textos, BorderLayout.WEST);

        // Panel de volumen
        JPanel panelVolumen = new JPanel(new BorderLayout(6, 0));
        panelVolumen.setBackground(C_PANEL);

        JLabel iconoVol = new JLabel("Vol:");
        iconoVol.setFont(new Font("SansSerif", Font.BOLD, 11));
        iconoVol.setForeground(C_SUBTITULO);

        JSlider sliderVolumen = new JSlider(JSlider.HORIZONTAL, 0, 100,
                (int) (ControlAudio.getVolumen() * 100));
        sliderVolumen.setPreferredSize(new Dimension(140, 26));
        sliderVolumen.setBackground(C_PANEL);
        sliderVolumen.setFocusable(false);
        sliderVolumen.setToolTipText("Ajustar volumen de la musica");

        JLabel lblPct = new JLabel((int) (ControlAudio.getVolumen() * 100) + "%");
        lblPct.setFont(new Font("Monospaced", Font.PLAIN, 11));
        lblPct.setForeground(C_SUBTITULO);
        lblPct.setPreferredSize(new Dimension(35, 20));

        sliderVolumen.addChangeListener((ChangeEvent e) -> {
            float vol = sliderVolumen.getValue() / 100f;
            ControlAudio.setVolumen(vol);
            lblPct.setText(sliderVolumen.getValue() + "%");
        });

        panelVolumen.add(iconoVol, BorderLayout.WEST);
        panelVolumen.add(sliderVolumen, BorderLayout.CENTER);
        panelVolumen.add(lblPct, BorderLayout.EAST);

        JPanel derechaHeader = new JPanel(new BorderLayout(0, 3));
        derechaHeader.setBackground(C_PANEL);
        JLabel lblVolTitulo = new JLabel("Volumen musica");
        lblVolTitulo.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblVolTitulo.setForeground(C_SUBTITULO);
        derechaHeader.add(lblVolTitulo, BorderLayout.NORTH);
        derechaHeader.add(panelVolumen, BorderLayout.CENTER);

        p.add(derechaHeader, BorderLayout.EAST);

        return p;
    }

    // Zona central: HUD + Panel de botones
    private JPanel construirCentro() {
        JPanel centro = new JPanel(new BorderLayout(12, 0));
        centro.setBackground(C_FONDO);
        centro.add(construirHUD(), BorderLayout.CENTER);
        centro.add(construirPanelBotones(), BorderLayout.EAST);
        return centro;
    }

    // HUD (texto lectura)
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
        hudArea.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        hudArea.setLineWrap(false);

        hudArea.setBorder(BorderFactory.createEmptyBorder(12, 14, 6, 14)); // antes tenía 12 abajo, ahora 6

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

    // Panel lateral de botones
    private JPanel construirPanelBotones() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(C_FONDO);
        p.setPreferredSize(new Dimension(215, 0));

        p.add(seccion("ADQUISICIÓN", new JButton[] {
                boton("Comprar Mascota", C_BTN_COMPRA, e -> accionComprar()),
                boton("Rescatar Mascota", C_BTN_RESCATE, e -> accionRescatar())
        }));
        p.add(Box.createVerticalStrut(8));
        p.add(seccion("VENTAS", new JButton[] {
                boton("Vender Mascota", C_BTN_VENTA, e -> accionVender())
        }));
        p.add(Box.createVerticalStrut(8));
        p.add(seccion("SUMINISTROS", new JButton[] {
                boton("Comprar Suministro", C_BTN_SUMINISTRO, e -> accionComprarSuministro()),
                boton("Usar Suministro", C_BTN_SUMINISTRO, e -> accionUsarSuministro())
        }));
        p.add(Box.createVerticalStrut(8));
        btnTiempo = boton("Terminar Día", C_BTN_TIEMPO, e -> accionPasarTiempo());
        p.add(seccion("TIEMPO", new JButton[] {
                btnTiempo
        }));
        p.add(Box.createVerticalStrut(8));
        p.add(seccion("SISTEMA", new JButton[] {
                boton("Guardar Partida", C_BTN_SISTEMA, e -> accionGuardar()),
                boton("Cargar Partida", C_BTN_SISTEMA, e -> accionCargar())
        }));
        p.add(Box.createVerticalGlue());
        return p;
    }

    // Panel log
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

    // HELPERS DE CONSTRUCCIÓN DE COMPONENTES
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

        // Efecto hover
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
        return btn;
    }

    // PATRÓN OBSERVER — Implementación del contrato ObservadorTienda
    @Override
    public void actualizar() {
        actualizarHUD();
    }

    // HUD — Renderizado del estado
    public void actualizarHUD() {
        StringBuilder sb = new StringBuilder();
        sb.append("SIMULADOR DE TIENDA DE MASCOTAS\n");
        String estadoStr = tienda.isTiendaAbierta() ? "ABIERTA" : "CERRADA";
        sb.append(String.format("DÍA: %d  |  RELOJ: %02d:00  |  ESTADO: %s\n\n",
                tienda.getDiaActual(), tienda.getHoraActual(), estadoStr));
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

        if (btnTiempo != null) {
            if (tienda.isTiendaAbierta()) {
                btnTiempo.setText("Terminar Día");
            } else {
                btnTiempo.setText("Empezar Siguiente Día");
            }
        }
    }

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
        if (cacheIconos.containsKey(especie))
            return cacheIconos.get(especie);
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

    // barra progreso ASCII 10 caracteres para el HUD
    private String barra(int valor) {
        int llenos = (int) Math.round(valor / 10.0);
        return "[" + "█".repeat(llenos) + "░".repeat(10 - llenos) + "]";
    }

    private void log(String mensaje) {
        logArea.append("> " + mensaje + "\n");
        // Auto-scroll al final del log
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    // ACCIONES BOTONES
    private void accionComprar() {
        Object[] opciones = { "Perro  ($150)", "Gato   ($100)", "Pez    ($50)", "Cthulhu    ($9999)" };
        Object sel = JOptionPane.showInputDialog(
                this,
                "<html><b>Modelo Comercial</b><br>El animal llegará <b>sano</b>.<br>"
                        + "El precio se descuenta de tu presupuesto.</html>",
                "Comprar Mascota",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (sel == null)
            return;

        String tipo = sel.toString().split("\\s")[0];
        try {
            tienda.comprarMascota(tipo);
            ControlAudio.reproducirSFX("sonidos/compra.wav");
            log("✓ Mascota comprada: " + tipo + " (-$" + precioBase(tipo) + ")");
        } catch (DineroInsuficienteException | CapacidadMaximaException | TiendaCerradaException ex) {
            mostrarError(ex.getMessage());
            log("✗ Error al comprar " + tipo + ": " + ex.getMessage());
        }
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
        if (sel == null)
            return;

        try {
            tienda.rescatarMascota(sel.toString());
            log("♥ Mascota rescatada: " + sel + " (¡necesita cuidados urgentes!)");
        } catch (CapacidadMaximaException | TiendaCerradaException ex) {
            mostrarError(ex.getMessage());
            log("✗ " + ex.getMessage());
        }
    }

    private void accionVender() {
        if (tienda.getInventarioMascotas().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay mascotas en el inventario.", "Inventario vacío",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        Object[] opciones = tienda.getInventarioMascotas().stream()
                .map(m -> m.getEspecie()
                        + "  [" + m.getEstado().describir() + "]"
                        + "  →  precio venta: $" + (m.getPrecio() * 1.5))
                .toArray();

        Object sel = JOptionPane.showInputDialog(
                this,
                "<html>Selecciona la mascota a vender<br>"
                        + "<small>(precio venta = precio base × 1.5, solo mascotas <b>SANAS</b>)</small></html>",
                "Vender Mascota",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (sel == null)
            return;

        String tipo = sel.toString().split("\\s")[0];
        try {
            tienda.venderMascota(tipo);
            ControlAudio.reproducirSFX("sonidos/venta.wav");
            log("💰 Mascota vendida: " + tipo + " (+$" + (precioBase(tipo) * 1.5) + ")");
        } catch (MascotaEnfermaException | TiendaCerradaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Venta bloqueada",
                    JOptionPane.WARNING_MESSAGE);
            log("✗ Venta bloqueada: " + ex.getMessage());
        } catch (MascotaNoEncontradaException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void accionComprarSuministro() {
        Object[] opciones = {
                "Comida — Croquetas Basicas       ($15 | hambre -30)",
                "Comida — Croquetas Premium       ($25 | hambre -50)",
                "Comida — Extra Proteina          ($35 | hambre -70)",
                "Medicina — Vitaminas             ($30 | salud +25)",
                "Medicina — Antibiotico           ($60 | salud +50)",
                "Medicina — Tratamiento Intensivo ($90 | salud +75)",
                "Higiene — Cepillo y Shampoo      ($20 | higiene +40)",
                "Higiene — Spa Completo           ($50 | higiene +100)"
        };
        Object sel = JOptionPane.showInputDialog(
                this, "Selecciona el suministro a comprar:",
                "Comprar Suministro", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (sel == null)
            return;

        Suministro s = crearSuministro(sel.toString());
        if (s == null)
            return;

        try {
            tienda.agregarSuministro(s);
            log("🛒 Suministro comprado: " + s.getNombre() + " (-$" + s.getPrecio() + ")");
        } catch (DineroInsuficienteException | TiendaCerradaException ex) {
            mostrarError(ex.getMessage());
            log("✗ Error: " + ex.getMessage());
        }
    }

    private void accionUsarSuministro() {
        if (!tienda.isTiendaAbierta()) {
            mostrarError("La tienda está cerrada. Pasa al siguiente día para continuar.");
            return;
        }
        if (tienda.getInventarioSuministros().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay suministros en stock. Compra primero.", "Sin stock",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tienda.getInventarioMascotas().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay mascotas en el inventario.", "Sin mascotas",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // elegir suministro
        Object[] sumOpts = tienda.getInventarioSuministros().stream()
                .map(Suministro::getNombre).toArray();
        Object sumSel = JOptionPane.showInputDialog(
                this, "Paso 1 de 2 — ¿Qué suministro deseas usar?",
                "Usar Suministro", JOptionPane.QUESTION_MESSAGE, null, sumOpts, sumOpts[0]);
        if (sumSel == null)
            return;

        // elegir mascota
        Object[] mascOpts = tienda.getInventarioMascotas().stream()
                .map(m -> m.getEspecie()
                        + "  Salud:" + m.getNivelSalud()
                        + "  Hambre:" + m.getNivelHambre()
                        + "  [" + m.getEstado().describir() + "]")
                .toArray();
        Object mascSel = JOptionPane.showInputDialog(
                this, "Paso 2 de 2 — ¿En qué mascota lo usas?",
                "Usar Suministro", JOptionPane.QUESTION_MESSAGE, null, mascOpts, mascOpts[0]);
        if (mascSel == null)
            return;

        Suministro sumObj = tienda.getInventarioSuministros().stream()
                .filter(s -> s.getNombre().equals(sumSel.toString()))
                .findFirst().orElse(null);
        Mascota mascObj = tienda.getInventarioMascotas().stream()
                .filter(m -> mascSel.toString().startsWith(m.getEspecie()))
                .findFirst().orElse(null);

        if (sumObj != null && mascObj != null) {
            sumObj.usar(mascObj);
            tienda.getInventarioSuministros().remove(sumObj);
            log("> " + sumObj.getNombre() + " → " + mascObj.getEspecie()
                    + " | Estado ahora: [" + mascObj.getEstado().describir() + "]"
                    + " | Salud: " + mascObj.getNivelSalud());
            actualizarHUD();
        }
    }

    private void accionPasarTiempo() {
        if (tienda.isTiendaAbierta()) {
            int horasRestantes = 20 - tienda.getHoraActual();
            for (int i = 0; i < horasRestantes; i++) {
                tienda.avanzarReloj();
            }
            log(">> Terminaste el día anticipadamente.");
        } else {
            tienda.empezarSiguienteDia();
        }
    }

    private void accionGuardar() {
        tienda.guardarPartida(archivoGuardado);
        log(">> Partida guardada en: " + archivoGuardado);
        JOptionPane.showMessageDialog(this,
                "Partida guardada correctamente en:\n" + archivoGuardado,
                "Partida Guardada", JOptionPane.INFORMATION_MESSAGE);
    }

    private void accionCargar() {
        int conf = JOptionPane.showConfirmDialog(
                this,
                "¿Cargar la partida guardada?\nSe perderán todos los cambios no guardados.",
                "Cargar Partida", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (conf != JOptionPane.YES_OPTION)
            return;

        Tienda.cargarPartida(archivoGuardado);
        this.tienda = Tienda.getTienda();
        this.tienda.agregarObservador(this);
        log(">> Partida cargada desde: " + archivoGuardado);
        actualizarHUD();
    }

    // HELPERS PRIVADOS
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private double precioBase(String tipo) {
        switch (tipo.toLowerCase()) {
            case "perro":
                return 150.0;
            case "gato":
                return 100.0;
            case "pez":
                return 50.0;
            default:
                return 0.0;
        }
    }

    // Instancia el Suministro correspondiente según el texto del JOptionPane
    private Suministro crearSuministro(String opcion) {
        if (opcion.contains("Croquetas Basicas"))
            return new Comida("Croquetas Basicas", 15.0, 30);
        if (opcion.contains("Croquetas Premium"))
            return new Comida("Croquetas Premium", 25.0, 50);
        if (opcion.contains("Extra Proteina"))
            return new Comida("Extra Proteina", 35.0, 70);
        if (opcion.contains("Vitaminas"))
            return new Medicina("Vitaminas", 30.0, 25);
        if (opcion.contains("Antibiotico"))
            return new Medicina("Antibiotico", 60.0, 50);
        if (opcion.contains("Tratamiento Intensivo"))
            return new Medicina("Tratamiento Intensivo", 90.0, 75);
        if (opcion.contains("Cepillo y Shampoo"))
            return new Higiene("Cepillo y Shampoo", 20.0, 40);
        if (opcion.contains("Spa Completo"))
            return new Higiene("Spa Completo", 50.0, 100);
        return null;
    }
}

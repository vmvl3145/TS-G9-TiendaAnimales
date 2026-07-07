import javax.sound.sampled.*;
import java.io.*;

/**
 * Control de audio
 * Maneja música de fondo en loop y efectos de sonido (SFX) para eventos de juego.
 * Control de volumen: FloatControl.Type.MASTER_GAIN convierte el porcentaje del
 * JSlider (0-100) a la escala logarítmica en decibeles que exige la API.
 */
public class ControlAudio {

    private static Clip clipFondo;
    private static FloatControl volumenFondo;
    private static float volumenActual = 0.7f; // 70% por defecto

    /** Inicia la música de fondo en loop continuo. */
    public static void reproducirFondo(String ruta) {
        try {
            if (clipFondo != null && clipFondo.isRunning())
                return; // ya está sonando

            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(ruta));
            clipFondo = AudioSystem.getClip();
            clipFondo.open(ais);

            if (clipFondo.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumenFondo = (FloatControl) clipFondo.getControl(FloatControl.Type.MASTER_GAIN);
                aplicarVolumen(volumenActual);
            }

            clipFondo.loop(Clip.LOOP_CONTINUOUSLY);
            clipFondo.start();
        } catch (Exception e) {
            System.err.println("[Audio] No se pudo cargar fondo: " + e.getMessage());
        }
    }

    /** Detiene la música de fondo. */
    public static void detenerFondo() {
        if (clipFondo != null && clipFondo.isRunning()) {
            clipFondo.stop();
        }
    }

    // Efectos SFX
    /** Reproduce un efecto de sonido de forma asíncrona. */
    public static void reproducirSFX(String ruta) {
        new Thread(() -> {
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File(ruta));
                Clip clip = AudioSystem.getClip();
                clip.open(ais);

                if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl ctrl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    aplicarGain(ctrl, volumenActual);
                }

                clip.start();
                // Liberar recursos cuando termine
                clip.addLineListener(e -> {
                    if (e.getType() == LineEvent.Type.STOP)
                        clip.close();
                });
            } catch (Exception e) {
                System.err.println("[Audio] No se pudo reproducir SFX: " + e.getMessage());
            }
        }).start();
    }

    // Control volumen
    /**
     * Ajusta el volumen global (0.0 = silencio, 1.0 = máximo).
     * Convierte la escala lineal a decibeles (db) (escala logarítmica).
     * Llamado desde el JSlider de VentanaTienda.
     */
    public static void setVolumen(float porcentaje) {
        volumenActual = Math.max(0f, Math.min(1f, porcentaje));
        if (volumenFondo != null) {
            aplicarGain(volumenFondo, volumenActual);
        }
    }

    public static float getVolumen() {
        return volumenActual;
    }

    private static void aplicarVolumen(float porcentaje) {
        if (volumenFondo != null)
            aplicarGain(volumenFondo, porcentaje);
    }

    private static void aplicarGain(FloatControl ctrl, float porcentaje) {
        float dB;
        if (porcentaje <= 0f) {
            dB = ctrl.getMinimum(); // silencio total
        } else {
            dB = (float) (20.0 * Math.log10(porcentaje));
            dB = Math.max(ctrl.getMinimum(), Math.min(ctrl.getMaximum(), dB));
        }
        ctrl.setValue(dB);
    }
}

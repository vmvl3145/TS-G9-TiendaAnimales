import javax.sound.sampled.*;
import java.io.*;

/**
 * Gestor centralizado de audio para la aplicación.
 * Maneja la reproducción de música de fondo en loop continuo y la emisión de
 * efectos de sonido (SFX) para eventos específicos del juego.
 * <p>
 * Incluye lógica para convertir escalas de volumen lineales (0.0 a 1.0)
 * a la escala logarítmica en decibeles (dB) que requiere la API de Java Sound.
 */
public class ControlAudio {

    private static Clip clipFondo;
    private static FloatControl volumenFondo;
    private static float volumenActual = 0.7f; // 70% por defecto

    /** * Inicia la reproducción de la música de fondo en un loop continuo.
     * Si ya hay una pista de fondo reproduciéndose, el método ignora la nueva petición.
     * @param ruta La ruta relativa o absoluta del archivo de audio (ej. .wav) a reproducir.
     */
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

    /** * Detiene por completo la música de fondo actual si se está reproduciendo.
     */
    public static void detenerFondo() {
        if (clipFondo != null && clipFondo.isRunning()) {
            clipFondo.stop();
        }
    }

    /** * Reproduce un efecto de sonido (SFX) de forma asíncrona.
     * Lanza un nuevo hilo (Thread) para evitar que la interfaz gráfica se congele
     * durante la carga y reproducción del clip. El clip se cierra automáticamente
     * al terminar para liberar recursos de memoria.
     * @param ruta La ruta del archivo de audio del efecto de sonido.
     */
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

    /**
     * Ajusta el volumen global tanto para la música de fondo como para los próximos SFX.
     * Suele ser llamado dinámicamente desde un componente de interfaz, como un JSlider.
     * @param porcentaje Nivel de volumen deseado en escala lineal, donde 0.0f es
     * silencio total y 1.0f es el volumen máximo.
     */
    public static void setVolumen(float porcentaje) {
        volumenActual = Math.max(0f, Math.min(1f, porcentaje));
        if (volumenFondo != null) {
            aplicarGain(volumenFondo, volumenActual);
        }
    }

    /**
     * Obtiene el nivel de volumen global actual configurado en la tienda.
     * @return El porcentaje de volumen actual (entre 0.0f y 1.0f).
     */
    public static float getVolumen() {
        return volumenActual;
    }

    /**
     * Método auxiliar privado para aplicar el volumen actual específicamente a la música de fondo.
     * @param porcentaje El porcentaje de volumen a aplicar (0.0f a 1.0f).
     */
    private static void aplicarVolumen(float porcentaje) {
        if (volumenFondo != null)
            aplicarGain(volumenFondo, porcentaje);
    }

    /**
     * Convierte la escala de volumen lineal a decibeles (dB) y la aplica al controlador de audio.
     * La audición humana es logarítmica, por lo que una reducción matemática lineal
     * no sonaría natural sin esta conversión de ganancia (Gain).
     * @param ctrl       El controlador de ganancia (Master Gain) del clip de audio.
     * @param porcentaje El porcentaje lineal (0.0f a 1.0f) a convertir y aplicar.
     */
    private static void aplicarGain(FloatControl ctrl, float porcentaje) {
        float dB;
        if (porcentaje <= 0f) {
            dB = ctrl.getMinimum(); // silencio total
        } else {
            // Conversión logarítmica estándar para audio
            dB = (float) (20.0 * Math.log10(porcentaje));
            dB = Math.max(ctrl.getMinimum(), Math.min(ctrl.getMaximum(), dB));
        }
        ctrl.setValue(dB);
    }
}
package mascotas.estados;

import excepciones.MascotaEnfermaException;
import mascotas.Mascota;
import java.io.Serializable;

public interface EstadoMascota extends Serializable {
    void verificarVenta(Mascota mascota) throws MascotaEnfermaException;

    String describir();
}

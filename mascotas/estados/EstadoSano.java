package mascotas.estados;

import excepciones.MascotaEnfermaException;
import mascotas.Mascota;

public class EstadoSano implements EstadoMascota {

    private static final long serialVersionUID = 1L;

    @Override
    public void verificarVenta(Mascota mascota) throws MascotaEnfermaException {
    }

    @Override
    public String describir() {
        return "Sano";
    }
}

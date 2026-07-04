package Mascotas.estados;

import Excepciones.MascotaEnfermaException;
import Mascotas.Mascota;

public class EstadoEnfermo implements EstadoMascota {

    private static final long serialVersionUID = 1L;

    @Override
    public void verificarVenta(Mascota mascota) throws MascotaEnfermaException {
        throw new MascotaEnfermaException(
                "El/La " + mascota.getEspecie() + " está demasiado débil para adoptarlo (Salud: "
                        + mascota.getNivelSalud() + "/100. Necesita Cuidados!!!)");
    }

    @Override
    public String toString() {
        return "Enfermo";
    }
}

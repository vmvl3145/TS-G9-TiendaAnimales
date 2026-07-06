package mascotas.estados;

import excepciones.MascotaEnfermaException;
import mascotas.Mascota;

public class EstadoEnfermo implements EstadoMascota {

    private static final long serialVersionUID = 1L;

    @Override
    public void verificarVenta(Mascota mascota) throws MascotaEnfermaException {
        throw new MascotaEnfermaException(
                "El/La " + mascota.getEspecie() +
                        " está demasiado débil para adoptarlo (Salud: " +
                        mascota.getNivelSalud() + "/100). ¡Necesita medicina!");
    }

    @Override
    public String describir() {
        return "Enfermo";
    }
}

package Mascotas.estados;

import Excepciones.MascotaEnfermaException;
import Mascotas.Mascota;

public class EstadoSano implements EstadoMascota {

    private static final long serialVersionUID = 1L;

    @Override
    public void verificarVenta(Mascota mascota) throws MascotaEnfermaException {
    }

    @Override
    public String toString() {
        return "Sano";
    }
}

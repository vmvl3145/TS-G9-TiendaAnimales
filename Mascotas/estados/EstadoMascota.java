package Mascotas.estados;

import java.io.Serializable;

import Excepciones.MascotaEnfermaException;
import Mascotas.Mascota;

public interface EstadoMascota extends Serializable {
    void verificarVenta(Mascota mascota) throws MascotaEnfermaException;

    String toString();
}

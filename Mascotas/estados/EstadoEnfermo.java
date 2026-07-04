package Mascotas.estados;

import Mascotas.Mascota;

public class EstadoEnfermo implements EstadoMascota {

    @Override
    public boolean verificarVenta(Mascota mascota) {
        // Una mascota enferma no puede venderse
        return false;
    }

    @Override
    public String getDescripcion() {
        return "Enfermo";
    }
}

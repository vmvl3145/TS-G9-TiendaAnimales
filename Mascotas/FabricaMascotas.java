package Mascotas;

import Excepciones.*;

public class FabricaMascotas {

    // Compra, nace sana
    public static Mascota crearMascotaComprada(String tipo, double presupuestoActual)
            throws DineroInsuficienteException {
        Mascota nueva = instanciarMascota(tipo);
        if (presupuestoActual < nueva.getPrecio()) {
            throw new DineroInsuficienteException(
                    "Presupuesto Insuficiente para comprar un/a " + nueva.getEspecie() + ". Necesitas $"
                            + nueva.getPrecio() + ", actualmente tienes $" + presupuestoActual);
        }
        System.out.println(">> Se ha comprado un/a " + nueva.getEspecie() + " por $" + nueva.getPrecio());
        return nueva;
    }

    // Rescate, nace enferma
    public static Mascota crearMascotaRescatada(String tipo) {
        Mascota nueva = instanciarMascota(tipo);
        nueva.setNivelSalud(10); // activa automáticamente EstadoEnfermo
        nueva.setNivelHambre(90); // set hambre al 90
        nueva.setNivelHigiene(20);
        System.out.println(">> Se ha rescatado un/a " + nueva.getEspecie() + " (necesita cuidados urgentes)");
        return nueva;
    }

    public static Mascota instanciarMascota(String tipo) {
        switch (tipo) {
            case "Perro":
                return new Perro();
            case "Gato":
                return new Gato();
            case "Pez":
                return new Pez();
            default:
                throw new IllegalArgumentException("Especie no reconocida: " + tipo);
        }
    }

    public static Mascota crearMascota(String especie, double precio) throws IllegalArgumentException {
        if (especie == null || precio <= 0)
            throw new IllegalArgumentException("Datos inválidos");

        double precioPorDefecto = 1.0;
        return crearMascota(especie, precioPorDefecto);
    }
}
package mascotas;

import excepciones.DineroInsuficienteException;

/**
 * Implementación del patrón Factory Method (GoF) - Creacional.
 *
 * Problema que resuelve: el código cliente (Tienda) hacía "new Perro()" directamente,
 * acoplando la lógica de construcción con la lógica de negocio y sin aplicar
 * ninguna regla económica al momento de adquirir una mascota.
 *
 * Solución: FabricaMascotas centraliza toda la construcción de mascotas,
 * ofreciendo dos "productos" con reglas de negocio diferentes pero devolviendo
 * siempre el tipo abstracto Mascota, ocultando las clases concretas al cliente.
 *
 * Dos modos de adquisición:
 *   - COMERCIAL (crearMascotaComprada): Paga precio base al criador. Nace sana.
 *   - RESCATE   (crearMascotaRescatada): Gratis, pero llega en estado crítico.
 *                Obliga al jugador a usar Medicina antes de poder venderla.
 */
public class FabricaMascotas {

    // -------------------------------------------------------------------------
    // MODO 1: MODELO COMERCIAL — Comprar para vender
    // -------------------------------------------------------------------------

    /**
     * Crea una mascota sana adquirida de un criador.
     * Descuenta el precio base del presupuesto de la tienda.
     * La mascota nace en EstadoSano (por defecto del constructor de Mascota).
     *
     * @param tipo   "Perro", "Gato" o "Pez" (insensible a mayúsculas).
     * @param presupuestoActual Presupuesto actual de la tienda para validar solvencia.
     * @return La nueva mascota sana lista para el inventario.
     * @throws DineroInsuficienteException si el presupuesto no alcanza para el precio base.
     * @throws IllegalArgumentException    si el tipo de mascota no es reconocido.
     */
    public static Mascota crearMascotaComprada(String tipo, double presupuestoActual)
            throws DineroInsuficienteException {

        Mascota nueva = instanciar(tipo);

        if (presupuestoActual < nueva.getPrecio()) {
            throw new DineroInsuficienteException(
                "Presupuesto insuficiente para comprar un/a " + nueva.getEspecie() +
                ". Necesitas $" + nueva.getPrecio() +
                " y tienes $" + presupuestoActual
            );
        }

        // La mascota nace 100% sana (EstadoSano por defecto del constructor de Mascota)
        System.out.println(">> [FÁBRICA] Mascota comprada a criador: " +
                           nueva.getEspecie() + " por $" + nueva.getPrecio());
        return nueva;
    }

    // -------------------------------------------------------------------------
    // MODO 2: MODELO DE RESCATE — Adquisición gratuita con trampa
    // -------------------------------------------------------------------------

    /**
     * Crea una mascota rescatada en estado crítico. Adquisición completamente gratuita,
     * pero llega con salud al 10%, hambre al 90% e higiene al 20%.
     *
     * El setter setNivelSalud(10) dispara automáticamente la transición a EstadoEnfermo
     * (integración con el patrón State del Paso 1), bloqueando su venta hasta que
     * el jugador invierta en Medicina y su salud supere el umbral de 50.
     *
     * @param tipo "Perro", "Gato" o "Pez" (insensible a mayúsculas).
     * @return La nueva mascota en estado crítico lista para el inventario.
     * @throws IllegalArgumentException si el tipo de mascota no es reconocido.
     */
    public static Mascota crearMascotaRescatada(String tipo) {

        Mascota rescatada = instanciar(tipo);

        // Aplicar condición de rescate: estado crítico
        // NOTA: setNivelSalud(10) dispara automáticamente -> EstadoEnfermo (Patrón State)
        rescatada.setNivelSalud(10);
        rescatada.setNivelHambre(90);
        rescatada.setNivelHigiene(20);

        System.out.println(">> [FÁBRICA] Mascota rescatada (¡necesita atención urgente!): " +
                           rescatada.getEspecie() +
                           " | Estado: [" + rescatada.getEstado().describir() + "]" +
                           " | Salud: " + rescatada.getNivelSalud());
        return rescatada;
    }

    // -------------------------------------------------------------------------
    // Método privado de instanciación — oculta el 'new' concreto al exterior
    // -------------------------------------------------------------------------

    /**
     * Instancia la clase concreta correspondiente al tipo solicitado.
     * Es el único lugar del sistema donde se hace "new Perro/Gato/Pez()".
     *
     * @throws IllegalArgumentException si el tipo no coincide con ninguna especie conocida.
     */
    private static Mascota instanciar(String tipo) {
        switch (tipo.toLowerCase()) {
            case "perro": return new Perro();
            case "gato":  return new Gato();
            case "pez":   return new Pez();
            case "cthulhu":   return new Cthulhu();
            default:
                throw new IllegalArgumentException(
                    "Especie desconocida: '" + tipo + "'. Opciones: Perro, Gato, Pez, Cthulhu."
                );
        }
    }
}

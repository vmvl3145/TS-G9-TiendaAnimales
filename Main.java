import Excepciones.*;
import Mascotas.*;
import Suministros.*;

public class Main {
    public static void main(String[] args) {
        final String ARCHIVO_GUARDADO = "partida_tienda.dat";

        System.out.println("INICIANDO TIENDA");

        // cargar la partida previa
        Tienda.cargarPartida(ARCHIVO_GUARDADO);
        Tienda miTienda = Tienda.getTienda();

        // Mostrar el estado justo después de cargar
        miTienda.mostrarHud();

        // tienda está vacía (partida nueva), compramos animales directamente
        if (miTienda.getInventarioMascotas().isEmpty()) {
            System.out.println("Llenando inventario inicial...");
            try {
                miTienda.agregarMascota(new Perro()); // instanciación directa
                miTienda.agregarMascota(new Gato());
            } catch (CapacidadMaximaException e) {
                System.err.println(e.getMessage());
            }
        }
        //tiempo
        miTienda.pasarTiempo();
        miTienda.mostrarHud();

        if (!miTienda.getInventarioMascotas().isEmpty()) {
            Mascota primerAnimal = miTienda.getInventarioMascotas().get(0);
            Suministro alimentoPremium = new Comida("Croquetas Premium", 25.0, 40);

            try {
                miTienda.agregarSuministro(alimentoPremium);
                alimentoPremium.usar(primerAnimal); // Solo se alimenta si la compra fue exitosa
            } catch (DineroInsuficienteException e) {
                System.err.println("Error de compra: " + e.getMessage());
            }
        }

        miTienda.mostrarHud();
    }
}
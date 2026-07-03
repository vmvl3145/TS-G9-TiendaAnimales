import Excepciones.*;
import mascotas.*;

public class Main {
    public static void main(String[] args) {
        Tienda miTienda = Tienda.getTienda();
        System.out.println("INICIANDO SIMULADOR TIENDA");
        System.out.println("T1: llenar la tienda");
        try {
            miTienda.agregarMascota(new Perro());
            miTienda.agregarMascota(new Gato());
            miTienda.agregarMascota(new Pez());
            miTienda.agregarMascota(new Gato());
            miTienda.agregarMascota(new Perro());

            // test exception capmaxima
            System.out.println("Intentando ingresar al sexto animal");
            miTienda.agregarMascota(new Perro());
        } catch (CapacidadMaximaException e) {
            System.err.println("Error inventario:" + e.getMessage());
        }

        System.out.println("T2: Vender un animal que no existe");
        try {
            miTienda.venderMascota("Elefante");
        } catch (MascotaNoEncontradaException | MascotaEnfermaException e) {
            System.err.println("Error de Venta: " + e.getMessage());
        }

        System.out.println("T3: Vender un perro enfermo");
        try {
            Mascota perroEnfermo = new Perro();
            perroEnfermo.setNivelSalud(30);
            miTienda.agregarMascota(perroEnfermo);
            miTienda.venderMascota("Perro");
        } catch (Exception e) {
            System.err.println("Error de venta: " + e.getMessage());
        }
        miTienda.mostrarHudTienda();
        System.out.println("PRIMER TEST OK!!");
    }
}
package mascotas;

/**
 * Representa a la deidad cósmica Cthulhu como una mascota adquirible en la tienda.
 * Esta clase actúa como un "Easter egg" dentro del simulador, destacando por su
 * precio inalcanzable y su naturaleza de horror cósmico.
 * Hereda de la clase base {@link Mascota}.
 */

public class Cthulhu extends Mascota{
    public Cthulhu(){
    super("Cthulhu", 9999.0);
    }
    @Override
    public void makeSound(){
        System.out.println("No hay palabras humanas para esto. Tu mente traduce el sonido como el crujir de estrellas muertas y el peso de un mar difunto...");
    }
}

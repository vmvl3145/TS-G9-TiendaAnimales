package mascotas;

/**
 * Representa a un Gato como una mascota adquirible y gestionable dentro de la tienda.
 * Hereda los atributos y comportamientos básicos de la clase abstracta o base {@link Mascota}.
 */

public class Gato extends Mascota{
    public Gato(){
        super("Gato", 100.0);
    }
    @Override
    public void makeSound(){
        System.out.println("¡Meow Meow! El gato ronronea.");
    }
}

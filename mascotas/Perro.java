package mascotas;

/**
 * Representa a un Perro como mascota adquirible y gestionable en la tienda.
 * Hereda los atributos y comportamientos básicos de la clase abstracta {@link Mascota}
 */

public class Perro extends Mascota{
    public Perro(){
        super("Perro", 150.0);
    }
    @Override
    public void makeSound(){
        System.out.println("¡Woof Woof! El perro se mueve emocionado.");
    }
}

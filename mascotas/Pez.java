package mascotas;

/**
 * Representa a un Pez como una mascota adquirible dentro de la tienda.
 * Hereda los atributos y comportamientos básicos de la clase abstracta {@link Mascota}
 */

public class Pez extends Mascota{
    public Pez(){
        super("Pez", 50.0);
    }
    @Override
    public void makeSound(){
        System.out.println("El pescado nada en su pecera.");
    }
}

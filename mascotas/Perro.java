package mascotas;

public class Perro extends Mascota{
    public Perro(){
        super("Perro", 150.0);
    }
    @Override
    public void makeSound(){
        System.out.println("¡Woof Woof! El perro se mueve emocionado.");
    }
}

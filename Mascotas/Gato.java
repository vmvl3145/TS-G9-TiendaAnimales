package mascotas;

public class Gato extends Mascota{
    public Gato(){
        super("Gato", 100.0);
    }
    @Override
    public void makeSound(){
        System.out.println("¡Meow Meow! El gato ronronea.");
    }
}
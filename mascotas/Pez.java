package Mascotas;

public class Pez extends Mascota{
    public Pez(){
        super("Pez", 50.0);
    }
    @Override
    public void makeSound(){
        System.out.println("El pescado nada en su pecera.");
    }
}
package mascotas;

public class Cthulhu extends Mascota{
    public Cthulhu(){
    super("Cthulhu", 9999.0);
    }
    @Override
    public void makeSound(){
        System.out.println("No hay palabras humanas para esto. Tu mente traduce el sonido como el crujir de estrellas muertas y el peso de un mar difunto...");
    }
}

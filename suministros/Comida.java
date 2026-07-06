package Suministros;
import Mascotas.Mascota;

public class Comida extends Suministro {
    private static final long serialVersionUID = 1L;
    private int valorNutricional;

    public Comida(String nombre, double precio, int valorNutricional) {
        super(nombre, precio);
        this.valorNutricional = valorNutricional;
    }

    @Override
    public void usar(Mascota mascota) {
        System.out.println("Alimentando al " + mascota.getEspecie() + " con " + this.nombre + ".");
        // Restamos hambre porque acaba de comer
        mascota.setNivelHambre(mascota.getNivelHambre() - this.valorNutricional);
    }
}
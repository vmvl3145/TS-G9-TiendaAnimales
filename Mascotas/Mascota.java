package mascotas;

public abstract class Mascota{
    protected String especie;
    protected double precio;
    protected int nivelSalud;

    public Mascota(String especie, double precio){
        this.especie = especie;
        this.precio = precio;
        this.nivelSalud = 100; // valor inicial todos al 100
    }

    public String getEspecie(){
        return especie;
    }
    public double getPrecio(){
        return precio;
    }
    public int getNivelSalud(){
        return nivelSalud;
    }

    public void setNivelSalud(int salud)
        this.nivelSalud = salud;
    }
    public abstract class void makeSound();
}
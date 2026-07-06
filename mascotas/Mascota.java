
package mascotas;

import java.io.Serializable;
import mascotas.estados.EstadoMascota;
import mascotas.estados.EstadoSano;
import mascotas.estados.EstadoEnfermo;

/**
 * Contexto del patr├│n State (GoF).
 * Delega el comportamiento variable (ej: si puede venderse) al objeto
 * EstadoMascota que tenga asignado en cada momento.
 *
 * Regla de transici├│n autom├ítica (disparada por setNivelSalud):
 *   nivelSalud >= 50  ΓåÆ  EstadoSano
 *   nivelSalud <  50  ΓåÆ  EstadoEnfermo
 */
public abstract class Mascota implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String especie;
    protected double precio;
    protected int nivelSalud;
    protected int nivelHambre;
    protected int nivelHigiene;

    // --- Patr├│n State: estado actual de la mascota ---
    private EstadoMascota estado;

    // Constructor base: toda mascota nace sana
    public Mascota(String especie, double precio) {
        this.especie = especie;
        this.precio = precio;
        this.nivelSalud = 100;
        this.nivelHambre = 0;   // 0 significa que no tiene hambre
        this.nivelHigiene = 100;
        this.estado = new EstadoSano(); // estado inicial
    }

    // ----- Getters -----
    public String getEspecie()      { return especie; }
    public double getPrecio()       { return precio; }
    public int getNivelSalud()      { return nivelSalud; }
    public int getNivelHambre()     { return nivelHambre; }
    public int getNivelHigiene()    { return nivelHigiene; }
    public EstadoMascota getEstado(){ return estado; }

    // ----- Setters defensivos -----

    /**
     * Setter de salud con l├│gica de transici├│n de estado integrada.
     * Act├║a como el trigger del patr├│n State: cualquier cambio de salud
     * que cruce el umbral de 50 dispara el cambio de estado autom├íticamente.
     */
    public void setNivelSalud(int salud) {
        this.nivelSalud = Math.max(0, Math.min(100, salud));

        // Transici├│n de estado autom├ítica seg├║n umbral de salud
        if (this.nivelSalud < 50) {
            this.estado = new EstadoEnfermo();
        } else {
            this.estado = new EstadoSano();
        }
    }

    public void setNivelHambre(int hambre) {
        this.nivelHambre = Math.max(0, Math.min(100, hambre));
    }

    public void setNivelHigiene(int higiene) {
        this.nivelHigiene = Math.max(0, Math.min(100, higiene));
    }

    /**
     * Permite inyectar un estado directamente (usado por FabricaMascotas en Paso 2).
     * ├Ütil para crear mascotas rescatadas que nacen en EstadoEnfermo.
     */
    public void setEstado(EstadoMascota estado) {
        this.estado = estado;
    }

    public abstract void makeSound();
}

package Mascotas;

import java.io.Serializable;
import Mascotas.estados.*;
import Excepciones.*;

public abstract class Mascota implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String especie;
    protected double precio;
    protected int nivelSalud;
    protected int nivelHambre;
    protected int nivelHigiene;
    private EstadoMascota estado;

    // Toda Mascota nace sana
    public Mascota(String especie, double precio) {
        this.especie = especie;
        this.precio = precio;
        this.nivelSalud = 100;
        this.nivelHambre = 0; // 0 significa que no tiene hambre
        this.nivelHigiene = 100;
        this.estado = new EstadoSano();
    }

    // Getters
    public String getEspecie() {
        return especie;
    }

    public double getPrecio() {
        return precio;
    }

    public int getNivelSalud() {
        return nivelSalud;
    }

    public int getNivelHambre() {
        return nivelHambre;
    }

    public int getNivelHigiene() {
        return nivelHigiene;
    }

    public EstadoMascota getEstadoMascota() {
        return estado;
    }

    // Setters defensivos
    public void setNivelSalud(int salud) {
        this.nivelSalud = Math.max(0, Math.min(100, salud));
    }

    public void setNivelHambre(int hambre) {
        this.nivelHambre = Math.max(0, Math.min(100, hambre));
    }

    public void setNivelHigiene(int higiene) {
        this.nivelHigiene = Math.max(0, Math.min(100, higiene));
    }

    public void setEstado(EstadoMascota estado) {
        this.estado = estado;
    }

    public abstract void makeSound();
}
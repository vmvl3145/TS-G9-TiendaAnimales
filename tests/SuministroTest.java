import mascotas.*;
import suministros.*;
import org.junit.jupiter.api.Test;
import excepciones.*;
import static org.junit.jupiter.api.Assertions.*;

public class SuministroTest {
    @Test
    public void testComidaReduceHambre() throws DineroInsuficienteException {
        Mascota perro = FabricaMascotas.crearMascotaComprada("Perro", 1000.0);
        perro.setNivelHambre(50);
        assertEquals(50, perro.getNivelHambre());
        
        Comida croquetas = new Comida("Croquetas Básicas", 15.0, 30);
        croquetas.usar(perro);
        
        assertEquals(20, perro.getNivelHambre(), "El hambre debería reducirse en 30");
    }

    @Test
    public void testMedicinaCuraSalud() throws DineroInsuficienteException {
        Mascota gato = FabricaMascotas.crearMascotaComprada("Gato", 1000.0);
        gato.setNivelSalud(50);
        assertEquals(50, gato.getNivelSalud());
        
        Medicina antibiotico = new Medicina("Antibiotico", 60.0, 50);
        antibiotico.usar(gato);
        
        assertEquals(100, gato.getNivelSalud(), "La salud debería restaurarse a 100");
    }

    @Test
    public void testHigieneLimpia() throws DineroInsuficienteException {
        Mascota pez = FabricaMascotas.crearMascotaComprada("Pez", 1000.0);
        pez.setNivelHigiene(60);
        assertEquals(60, pez.getNivelHigiene());
        
        Higiene shampoo = new Higiene("Shampoo", 20.0, 40);
        shampoo.usar(pez);
        
        assertEquals(100, pez.getNivelHigiene(), "La higiene debería restaurarse a 100");
    }
}

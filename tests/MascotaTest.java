import mascotas.*;
import mascotas.estados.*;
import org.junit.jupiter.api.Test;
import excepciones.*;
import static org.junit.jupiter.api.Assertions.*;

public class MascotaTest {
    
    @Test
    public void testCreacionYEstadoInicial() throws DineroInsuficienteException {
        Mascota perro = FabricaMascotas.crearMascotaComprada("Perro", 1000.0);
        assertNotNull(perro);
        assertEquals("Perro", perro.getEspecie());
        assertEquals(100, perro.getNivelSalud());
        assertEquals(0, perro.getNivelHambre());
        assertEquals(100, perro.getNivelHigiene());
        assertTrue(perro.getEstado() instanceof EstadoSano);
    }

    @Test
    public void testTransicionEstadoEnfermo() throws DineroInsuficienteException {
        Mascota pez = FabricaMascotas.crearMascotaComprada("Pez", 1000.0);
        pez.setNivelSalud(40);
        assertTrue(pez.getEstado() instanceof EstadoEnfermo, "El pez debería estar enfermo al bajar la salud a 40");
    }
    
    @Test
    public void testLimitesEstadisticas() throws DineroInsuficienteException {
        Mascota perro = FabricaMascotas.crearMascotaComprada("Perro", 1000.0);
        perro.setNivelHambre(500); 
        assertEquals(100, perro.getNivelHambre(), "El hambre no puede ser mayor a 100");
        
        perro.setNivelSalud(-50);
        assertEquals(0, perro.getNivelSalud(), "La salud no puede ser menor a 0");
    }
}

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import excepciones.*;
import mascotas.*;
import suministros.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

public class TiendaTest {

    @BeforeEach
    public void setUp() {
        File f = new File("test_partida.dat");
        if (f.exists()) {
            f.delete();
        }
        Tienda.cargarPartida("test_partida.dat");
        // Reiniciar inventarios y estado manualmente para pruebas independientes
        Tienda.getTienda().getInventarioMascotas().clear();
        Tienda.getTienda().getInventarioSuministros().clear();
    }

    @Test
    public void testSingleton() {
        Tienda t1 = Tienda.getTienda();
        Tienda t2 = Tienda.getTienda();
        assertSame(t1, t2, "Tienda debe ser un Singleton");
    }

    @Test
    public void testComprarMascotaExito() {
        Tienda tienda = Tienda.getTienda();
        double dineroInicial = tienda.getPresupuesto();

        assertDoesNotThrow(() -> {
            tienda.comprarMascota("Perro");
        });

        assertEquals(1, tienda.getInventarioMascotas().size());
        assertTrue(tienda.getPresupuesto() < dineroInicial, "El presupuesto debe disminuir al comprar");
    }

    @Test
    public void testCapacidadMaximaException() {
        Tienda tienda = Tienda.getTienda();
        assertDoesNotThrow(() -> {
            tienda.comprarMascota("Pez");
            tienda.comprarMascota("Pez");
            tienda.comprarMascota("Pez");
            tienda.comprarMascota("Pez");
            tienda.comprarMascota("Pez");
        });

        assertThrows(CapacidadMaximaException.class, () -> {
            tienda.comprarMascota("Pez");
        }, "Debe lanzar CapacidadMaximaException al exceder 5 mascotas");
    }

    @Test
    public void testRescatarMascota() {
        Tienda tienda = Tienda.getTienda();
        double dineroInicial = tienda.getPresupuesto();

        assertDoesNotThrow(() -> {
            tienda.rescatarMascota("Gato");
        });

        assertEquals(1, tienda.getInventarioMascotas().size());
        assertEquals(dineroInicial, tienda.getPresupuesto(), "Rescatar no debe costar dinero");

        Mascota rescatado = tienda.getInventarioMascotas().get(0);
        assertTrue(rescatado.getEstado() instanceof mascotas.estados.EstadoEnfermo,
                "La mascota rescatada debe estar en estado Enfermo");
    }

    @Test
    public void testVenderMascotaEnfermaException() {
        Tienda tienda = Tienda.getTienda();
        assertDoesNotThrow(() -> tienda.rescatarMascota("Perro"));
        assertThrows(MascotaEnfermaException.class, () -> {
            tienda.venderMascota("Perro");
        }, "No se puede vender una mascota que no esté sana");
    }

    @Test
    public void testTiendaCerradaException() {
        Tienda tienda = Tienda.getTienda();
        while (tienda.isTiendaAbierta()) {
            tienda.avanzarReloj();
        }

        assertFalse(tienda.isTiendaAbierta());
        assertThrows(TiendaCerradaException.class, () -> {
            tienda.comprarMascota("Pez");
        }, "Debe lanzar TiendaCerradaException fuera de horario");
    }
}

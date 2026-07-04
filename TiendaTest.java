import Excepciones.*;
import Mascotas.Gato;
import Mascotas.Perro;
import Suministros.Comida;
import Suministros.Medicina;
import Suministros.Suministro;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.lang.reflect.Field;

class TiendaTest {

    private static final String ARCHIVO_TEST = "test_partida.dat";
    // Resetea Singleton por reflexión antes de cada test para aislar el estado.
    @BeforeEach
    void resetSingleton() throws Exception {
        new File(ARCHIVO_TEST).delete();
        Field campo = Tienda.class.getDeclaredField("tienda");
        campo.setAccessible(true);
        campo.set(null, null);
    }

    @AfterAll
    static void limpiarArchivos() {
        new File(ARCHIVO_TEST).delete();
    }


    @Test
    @DisplayName("guardarPartida + cargarPartida restauran presupuesto e inventario")
    void persistencia_restauraEstadoCompleto() throws Exception {
        Tienda original = Tienda.getTienda();
        original.agregarMascota(new Perro());
        original.agregarMascota(new Gato());
        original.agregarSuministro(new Comida("Croquetas", 100.0, 30));

        double presupuestoEsperado = original.getPresupuesto();
        int mascotasEsperadas = original.getInventarioMascotas().size();
        int suministrosEsperados = original.getInventarioSuministros().size();

        original.guardarPartida(ARCHIVO_TEST);

        // Simulamos reiniciar el programa para dejar Singleton en null.
        Field campo = Tienda.class.getDeclaredField("tienda");
        campo.setAccessible(true);
        campo.set(null, null);

        Tienda.cargarPartida(ARCHIVO_TEST);
        Tienda cargada = Tienda.getTienda();

        assertEquals(presupuestoEsperado, cargada.getPresupuesto(), 0.001,
                "El presupuesto debe sobrevivir al reinicio");
        assertEquals(mascotasEsperadas, cargada.getInventarioMascotas().size(),
                "El inventario de mascotas debe recuperarse");
        assertEquals(suministrosEsperados, cargada.getInventarioSuministros().size(),
                "El inventario de suministros debe recuperarse");
    }

    @Test
    @DisplayName("cargarPartida con archivo inexistente inicia una tienda nueva")
    void persistencia_archivoInexistente_creaTiendaNueva() {
        Tienda.cargarPartida("archivo_que_no_existe_" + System.nanoTime() + ".dat");
        Tienda t = Tienda.getTienda();

        assertNotNull(t);
        assertEquals(1000.0, t.getPresupuesto(), 0.001);
        assertTrue(t.getInventarioMascotas().isEmpty());
    }


    // tiempo, pasarTiempo altera las estadística

    @Test
    @DisplayName("pasarTiempo aumenta el hambre y reduce la higiene")
    void tiempo_degradaHambreEHigiene() throws Exception {
        Tienda t = Tienda.getTienda();
        Perro p = new Perro();
        t.agregarMascota(p);

        int hambreInicial = p.getNivelHambre();
        int higieneInicial = p.getNivelHigiene();

        t.pasarTiempo();
        assertTrue(p.getNivelHambre() > hambreInicial,
                "El hambre debe subir tras pasar el tiempo");
        assertTrue(p.getNivelHigiene() < higieneInicial,
                "La higiene debe caer tras pasar el tiempo");
    }

    @Test
    @DisplayName("Mascota con hambre extrema pierde salud al pasar el tiempo")
    void tiempo_hambreExtremaCastigaSalud() throws Exception {
        Tienda t = Tienda.getTienda();
        Perro p = new Perro();
        p.setNivelHambre(85); // Ya supera el umbral (>80) tras el tick.
        t.agregarMascota(p);

        int saludInicial = p.getNivelSalud();
        t.pasarTiempo();

        assertTrue(p.getNivelSalud() < saludInicial,
                "La salud debe caer cuando el hambre está muy alta");
    }

    @Test
    @DisplayName("Los niveles de estadísticas nunca superan sus límites (0-100)")
    void tiempo_estadisticasSeMantienenEnRango() throws Exception {
        Tienda t = Tienda.getTienda();
        Perro p = new Perro();
        t.agregarMascota(p);

        for (int i = 0; i < 20; i++) t.pasarTiempo();

        assertTrue(p.getNivelHambre() >= 0 && p.getNivelHambre() <= 100);
        assertTrue(p.getNivelHigiene() >= 0 && p.getNivelHigiene() <= 100);
        assertTrue(p.getNivelSalud() >= 0 && p.getNivelSalud() <= 100);
    }

    // DineroInsuficienteException
    @Test
    @DisplayName("Comprar por encima del presupuesto lanza DineroInsuficienteException")
    void dinero_compraSinSaldo_lanzaExcepcion() {
        Tienda t = Tienda.getTienda();
        Suministro carisimo = new Comida("Alimento de Oro", 999_999.0, 100);

        assertThrows(DineroInsuficienteException.class,
                () -> t.agregarSuministro(carisimo));
    }

    @Test
    @DisplayName("Compra fallida no modifica presupuesto ni inventario (rollback lógico)")
    void dinero_compraSinSaldo_noAlteraEstado() {
        Tienda t = Tienda.getTienda();
        double presupuestoInicial = t.getPresupuesto();
        int suministrosPrevios = t.getInventarioSuministros().size();

        Suministro carisimo = new Comida("Alimento de Oro", 999_999.0, 100);

        assertThrows(DineroInsuficienteException.class,
                () -> t.agregarSuministro(carisimo));

        assertEquals(presupuestoInicial, t.getPresupuesto(), 0.001,
                "El presupuesto NO debe cambiar si la compra falla");
        assertEquals(suministrosPrevios, t.getInventarioSuministros().size(),
                "El inventario NO debe crecer si la compra falla");
        assertTrue(t.getPresupuesto() >= 0,
                "El presupuesto nunca debe quedar en negativo");
    }

    @Test
    @DisplayName("Compra dentro del presupuesto descuenta el precio correcto")
    void dinero_compraValida_descuentaCorrecto() throws DineroInsuficienteException {
        Tienda t = Tienda.getTienda();
        double presupuestoInicial = t.getPresupuesto();
        Suministro comida = new Comida("Alimento estándar", 50.0, 20);

        t.agregarSuministro(comida);

        assertEquals(presupuestoInicial - 50.0, t.getPresupuesto(), 0.001);
        assertEquals(1, t.getInventarioSuministros().size());
    }


    // Capacidad y uso de suministros
    @Test
    @DisplayName("Superar la capacidad máxima lanza CapacidadMaximaException")
    void capacidad_llena_lanzaExcepcion() throws CapacidadMaximaException {
        Tienda t = Tienda.getTienda();
        for (int i = 0; i < 5; i++) {
            t.agregarMascota(new Perro());
        }
        assertThrows(CapacidadMaximaException.class,
                () -> t.agregarMascota(new Gato()));
    }

    @Test
    @DisplayName("Comida.usar reduce el nivel de hambre por su valor nutricional")
    void comida_usar_reduceHambre() {
        Perro p = new Perro();
        p.setNivelHambre(50);
        Comida snack = new Comida("Snack", 10.0, 30);

        snack.usar(p);

        assertEquals(20, p.getNivelHambre());
    }

    @Test
    @DisplayName("Medicina.usar aumenta el nivel de salud por su poder curativo")
    void medicina_usar_aumentaSalud() {
        Gato g = new Gato();
        g.setNivelSalud(50);
        Medicina antibiotico = new Medicina("Antibiótico", 40.0, 30);

        antibiotico.usar(g);

        assertEquals(80, g.getNivelSalud());
    }
}

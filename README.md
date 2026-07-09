# Simulador de Tienda de Mascotas 🐾

Este proyecto es un simulador interactivo de gestión de una tienda de mascotas, desarrollado progresivamente aplicando buenas prácticas de Ingeniería de Software, como bajo acoplamiento, alta cohesión y la integración de patrones de diseño GoF (Gang of Four).

## Avances Semana 3: De Consola a Interfaz Gráfica (SWING) 🖥️

El objetivo principal de esta semana fue **transicionar el simulador desde un entorno de consola basado en texto hacia una aplicación de escritorio interactiva**. Para lograr esto sin romper la lógica central previamente validada, se priorizó la separación de responsabilidades mediante la integración de **Java SWING**. 

Este proceso exigió reforzar primero el backend con **patrones de diseño GoF** que garantizaran un modelo robusto antes de exponerlo en una vista gráfica, siguiendo una arquitectura inspirada en **MVC (Modelo-Vista-Controlador)**.

---

### Diagrama UML - Arquitectura Semana 3
![Diagrama UML Semana 3](imagenes/SEMANA3UML.png)

---

### Actualización de Patrones de Diseño 🛠️

#### 1. Patrón State (Comportamiento): Sistema de Estados de Salud 🩺
En la iteración anterior, la lógica que determinaba si una mascota podía ser vendida residía en la clase `Tienda`, mediante un bloque condicional que inspeccionaba directamente el atributo `nivelSalud`. Esto violaba el principio de que ningún objeto debería exponer su estado interno para que otro decida por él, generando código frágil y violando el Principio Abierto/Cerrado (OCP).

Se eligió el **patrón State** para encapsular el comportamiento variable de la mascota según su condición de salud, delegando las decisiones sobre el objeto propio que posee el estado:
*   **`EstadoMascota` (State / Interfaz):** Contrato con `verificarVenta(Mascota)` y `describir()`. Extiende `Serializable`.
*   **`EstadoSano` (ConcreteState):** `verificarVenta()` no hace nada, la venta procede libremente.
*   **`EstadoEnfermo` (ConcreteState):** `verificarVenta()` lanza `MascotaEnfermaException`, bloqueando la transacción.
*   **`Mascota` (Context):** Mantiene una referencia al estado actual. Toda mascota nace en `EstadoSano`.

**Mecanismo de transición automática:** La transición está integrada en el setter defensivo `setNivelSalud()` de `Mascota`. Si `nivelSalud < 50` pasa a `EstadoEnfermo`; si `nivelSalud >= 50` pasa a `EstadoSano`.

#### 2. Patrón Factory Method (Creacional): Fábrica de Mascotas 🏭
Hasta esta semana, las mascotas se instanciaban directamente con `new Perro()` en el cliente. Este acoplamiento directo rompía el encapsulamiento.

Se eligió el **patrón Factory Method** para encapsular dos lógicas económicas de adquisición diferenciadas, creando la clase `FabricaMascotas`:
*   **`crearMascotaComprada(tipo, presupuestoActual)`:** Descuenta el precio del dinero actual. La mascota nace en `EstadoSano`. Lanza `DineroInsuficienteException` si el presupuesto no alcanza.
*   **`crearMascotaRescatada(tipo)`:** Gratuita. La mascota nace con `EstadoEnfermo` inyectado vía `setEstado()`, obligando al jugador a usar Medicina/Higiene antes de poder venderla.

Ambos devuelven el tipo abstracto `Mascota`, ocultando la clase concreta al cliente.

#### 3. Patrón Observer (Estructural de Comportamiento): Sincronización Vista-Modelo 📡
Para evitar que la Vista deba consultar activamente al modelo en cada acción (polling), se implementó el **patrón Observer**. El modelo notifica a sus suscriptores ante cualquier cambio de estado.

*   **`ObservadorTienda` (Observer):** Define el contrato `actualizar()`.
*   **`VentanaTienda` (ConcreteObserver):** Implementa `actualizar()` para refrescar el JTextArea del HUD con el estado actual.
*   **`Tienda` (Subject/Publisher):** Mantiene una lista de observadores. Llama a `notificarObservadores()` al finalizar cada transacción (venderMascota, pasarTiempo, agregarSuministro, etc.).

> **Nota sobre Serialización (`transient`):** La lista de observadores en `Tienda` fue declarada como `transient` ya que `VentanaTienda` (JFrame) no es serializable. Al guardar, la lista se descarta y al cargar la partida, la ventana se re-registra automáticamente mediante el método `accionCargar()`.

---

### Interfaz Gráfica SWING 🎨
Para no contaminar la lógica de negocio, se creó `VentanaTienda` como una capa de Vista independiente (solo recibe datos y renderiza). La UI se divide en:
*   **Header:** Título y slider de Control de Volumen (Audio In-Game).
*   **HUD Central (Terminal):** Estado completo: presupuesto, barras de salud/hambre/higiene y suministros.
*   **Panel de botones (Derecha):** Acciones (Adquisición, Ventas, Suministros, Tiempo, Sistema).
*   **Registro de Eventos (Abajo):** Log cronológico interactivo.

Adicionalmente, se integró soporte nativo de audio (efectos de SFX y música de fondo).
# Simulador de Tienda de Mascotas 🐾

Simulador interactivo de gestión de una tienda de mascotas desarrollado por el Grupo 9

## Integrantes (Grupo 9)
|Nombre|Matrícula|
|------|---|
|Joaquín Adauy|2025405466|
|Joaquín Navarrete|2025420121|
|Vicente Vergara|2025431734|

---

### Diagramas UML - Avance de Semana tras Semana

Aquí puedes revisar la evolución de la arquitectura en cada iteración del proyecto:
* 📄 [UML Semana 1 (PDF)](documentos/UML_SEMANA1.pdf)
* 📄 [UML Semana 2 (PDF)](documentos/UML_SEMANA2.pdf)
* 📄 [UML Semana 3 (PDF)](documentos/UML_SEMANA3.pdf)

**Arquitectura UML Final:**

![Diagrama UML Final](documentos/UML_final.pdf)
*(Si su navegador no permite ver la visualización del PDF, [haz clic aquí para abrir el UML](documentos/UML_final.pdf))*

## Interfaz Gráfica (Screenshots)
A continuación se presentan las vistas principales del simulador en funcionamiento:

### 1. Dashboard Principal (Horario Comercial)
![Dashboard Principal](documentos/readme/captura_dashboard.png)
> *Vista general de la tienda en funcionamiento. A la izquierda se observa el HUD dinámico con el reloj, el presupuesto y el contador de inventario. En el centro, las tarjetas visuales de las mascotas y, en la parte inferior, la consola inmutable de registro de eventos (logs).*

### 2. Gestión de Crisis (Rescate de Animales)
![Rescate y Estado Crítico](documentos/readme/captura_estado_critico.png)
> *Demostración del patrón State en acción. Una mascota recién rescatada aparece marcada en rojo indicando `[X ENFERMO]` y con sus barras de estadística al mínimo, requiriendo atención urgente (uso de suministros) antes de poder integrarse a la economía de la tienda.*

### 3. Cierre de Jornada y Validaciones de Negocio
![Tienda Cerrada y Excepciones](documentos/readme/captura_excepciones.png)
> *El simulador cuenta con validaciones estrictas. Esta captura muestra cómo reacciona el sistema a las 20:00 hrs: los botones contextuales cambian a "Empezar Siguiente Día" y se disparan ventanas emergentes (alertas visuales de Excepciones) si se intenta comprar sin dinero o fuera del horario.*

### 4. Sistema de Audio y Opciones
![Menú de Opciones y Volumen](documentos/readme/captura_audio.png)
> *Menú lateral derecho que demuestra el sistema `ControlAudio` implementado en hilos independientes, permitiendo ajustar el volumen de la música de fondo y los efectos (SFX) en tiempo real sin congelar el reloj del juego.*

## Gameplay

Este simulador te pone al mando de tu propia tienda de mascotas.

¡Tu objetivo principal es mantener un balance económico positivo mientras cuidas del bienestar de los animales en tu inventario!

El flujo de juego consiste en:
1. **Adquirir Animales:** Puedes comprar mascotas (nacen sanas pero cuestan dinero) o rescatarlas (son gratis pero vienen enfermas, lo que te impide venderlas inmediatamente).
2. **Gestionar Recursos:** Los animales desarrollan hambre, pierden higiene y salud con el paso del tiempo. Debes comprar suministros (comida, medicina, higiene) y usarlos estratégicamente para restaurar sus estadísticas.
3. **Paso del Tiempo:** Al presionar "Pasar el Tiempo", el reloj del juego avanza. Los animales empeoran sus necesidades. Si la salud de una mascota baja del 50%, pasa automáticamente a estar *Enferma*.
4. **Venta:** Solo las mascotas **Sanas** pueden ser vendidas. Venderlas aumenta tu presupuesto para que puedas seguir comprando suministros y expandiendo tu tienda.

¡Cuidado! Tu tienda tiene una capacidad máxima de 5 mascotas. Debes vender para hacer espacio a nuevos rescates o compras.


## Características Principales

Podemos destacar las siguientes características del simulador:

* **Sistema de Personalización:** Al iniciar una nueva partida, el juego pide el nombre del jugador para personalizar el HUD
* **Persistencia de Datos (Guardado/Cargado):** Puedes guardar tu progreso en un archivo local (`partida_tienda.dat`) y retomarlo más tarde. Incluye protección contra cierre accidental si hay cambios sin guardar.
* **Sistema de Audio Interactivo:** Implementación de un `ControlAudio` que permite tener música de fondo y efectos de sonido (SFX) vinculados a las acciones del juego, junto con un slider para ajustar el volumen en tiempo real.
* **HUD Dinámico:** Un panel de estado que se actualiza automáticamente, mostrando inventario, presupuesto y el estado detallado de cada mascota (salud, hambre, higiene).
* **Suministros Variados:** Se crearon items para el tratamiento de mascotas para suplir la necesidad de higiene, además de incluir medicina, comida y juguetes.
* **Estados de Salud:** Las mascotas que bajan del 50% de salud (o son rescatadas) se marcan como `[X ENFERMO]`. En este estado, el sistema bloquea su venta hasta que sean tratadas con medicina, agregando una capa de dificultad.
* **Registro de Eventos (Log del Sistema):** Un panel inferior que registra un historial detallado de todas las acciones del jugador (gastos, errores, avisos del sistema y transacciones de mascotas).
* **Adquisición Diferenciada:** Dos estrategias de juego distintas. *Comprar* es rápido pero costoso, mientras que *Rescatar* es gratuito pero introduce un animal enfermo que requiere inversión de recursos y tiempo.
* **Manejo Robusto de Reglas de Negocio:** El juego previene errores del jugador mediante excepciones visuales (alertas en caso de fondos insuficientes, intentar superar el límite de 5 mascotas, o interactuar con un inventario vacío).
* **Variedad de Mascotas:** El jugador puede gestionar distintos tipos de animales, incluyendo perros, gatos, peces, e incluso un *Cthulhu* como mascota especial (easter egg).

---

## Documentación Final

Para conocer todos los detalles técnicos de la última iteración, las decisiones de diseño arquitectónico y la autocrítica del equipo, puede consultar nuestro informe completo:

* 📄 **[Informe Técnico y Autocrítica Final (PDF)](documentos/InformeFinalTS-G9.pdf)**

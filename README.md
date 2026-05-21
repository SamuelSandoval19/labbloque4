# Laboratorio Axity BLOQUE 4
# C_36 Samuel Sandoval Mandujano

# Parque Turistico de Dinosaurios

Simulacion secuencial por consola de un parque tematico, donde se administran turistas, dinosaurios, trabajadores, vehiculos y zonas. La simulacion avanza paso a paso, dispara eventos aleatorios y persiste ingresos, gastos y eventos en PostgreSQL mediante Liquibase.

# Herramientas utilizadas

- Java 17
- Maven 3.8 o superior
- PostgreSQL 13 o superior
- Liquibase 4.27
- JUnit 5 y Mockito para pruebas
- JaCoCo para cobertura
- PLANTUML (diagramas)

Todo el proyecto opera por consola, sin interfaz grafica como se menciono en la sesión del laboratorio.

# Configuracion

# 1. Crear la base de datos

En PostgreSQL, desde pgAdmin

Se creó la base de datos con el nomnbre "parque_dinosaurios"

# 2. Ajustar credenciales

Para ingresar las credenciales de mi Postgre cree `src/main/resources/configuracion.properties` con los datos de mi PostgreSQL:

properties
db.url=jdbc:postgresql://localhost:5432/parque_dinosaurios
db.usuario=postgres
db.contrasena=samuel19
db.driver=org.postgresql.Driver


Hice lo mismo en `src/main/resources/liquibase.properties` (es el archivo que usa el plugin de Liquibase que nos pidió el Profesor Luis).

# 3. Otros parametros del parque

En el mismo `configuracion.properties` ajusté la capacidad del parque, cantidad de turistas iniciales, dinosaurios, precios, pasos de simulacion, probabilidad de eventos aleatorios, etc.

# Ejecucion

# Compilar y ejecutar usé lo siguiente

mvn clean package
mvn exec:java "-Dexec.mainClass=com.parque.dinosaurios.Aplicacion"

# Pruebas

mvn verify

Esto corre las pruebas y verifica la cobertura. El umbral minimo requerido fue de 65%. La cobertura actual del proyecto es del 79%.

El reporte detallado de cobertura se guardo  en `target/site/jacoco/index.html`.

# Estructura del proyecto

parque-dinosaurios/
  pom.xml
  README.md
  docs/                      diagramas hechos con PLANTUML, se debería ver correctamente los PNG que realicé, de igual forma la imagen del JACOCO
  src/main/java/com/parque/dinosaurios/
    Aplicacion.java          clase de arranque
    configuracion/           cargador de propiedades (Singleton)
    modelo/                  entidades del dominio
    zonas/                   las 5 zonas del parque
    eventos/                 los 5 eventos aleatorios
    monitoreo/               sistema de monitoreo (Observer)
    parque/                  clase central (Singleton)
    persistencia/            conexion JDBC y repositorios
    simulacion/              motor de simulacion
    menu/                    menu interactivo
  src/main/resources/
    configuracion.properties
    liquibase.properties
    db/changelog/            changesets de Liquibase
  src/test/java/             pruebas unitarias


# Como funciona

Al iniciar la aplicacion se carga la configuracion, se conecta a PostgreSQL y Liquibase aplica las migraciones (se crearon las tablas `ingreso`, `gasto` y `evento`). Despues se construye la instancia unica del parque y se registran los observadores que escuchan los cambios.

Desde el menu el usuario puede ejecutar la simulacion o consultar el estado del parque y los datos persistidos.

Durante la simulacion, cada paso:

1. Atiende la fila de entrada y vende boletos.
2. Mueve turistas a una zona aleatoria (Recinto Central, Sanitarios o Recinto de Observacion).
3. Cobra los servicios consumidos (souvenirs, SPA, entradas a recintos).
4. Consume energia de la planta y registra el costo.
5. Avanza el tiempo de uso en los sanitarios.
6. Aplica encuestas en los recintos de observacion.
7. Con cierta probabilidad dispara un evento aleatorio.
8. Algunos turistas deciden retirarse.
9. Cada N pasos publica un reporte de monitoreo.

Las zonas son las cinco que pide el laboratorio: `ZonaArribo`, `RecintoCentral`, `ZonaSanitarios`, `PlantaEnergia` y `RecintoObservacion`. Los eventos aleatorios tambien son cinco: escape de dinosaurio, apagon masivo, tormenta torrencial, hora de ofertas y falla de vehiculos.

Toda la informacion financiera y los eventos quedan persistidos en tres tablas administradas por Liquibase.

# Patrones de diseno

# Singleton

Se aplica en dos clases:

`ConfiguracionParque`: la configuracion del sistema vive en una sola instancia compartida. Lee el archivo `configuracion.properties` una sola vez y entrega valores tipados.
`Parque`: el parque es uno solo durante toda la ejecucion. Manteniendo una sola instancia garantiza que turistas, dinosaurios, zonas y acumulados financieros sean siempre los mismos.

Ambos representan recursos globales y unicos. El patron evita que se creen multiples copias inconsistentes.

# Observer

Implementado por:

Interfaz `Observador` con dos metodos por defecto.
Clase abstracta `SujetoObservable` que mantiene la lista de observadores y los notifica.
`Parque` extiende `SujetoObservable`.
Dos observadores concretos: `MonitorConsola` (imprime el estado y los eventos en consola) y `RegistroEventos` (persiste los eventos en la base de datos via `EventoRepositorio`).

El sistema de monitoreo necesita reaccionar ante cambios del parque sin que este conozca a sus observadores. Con este patron se pueden agregar nuevos observadores (por ejemplo, un monitor a archivo o uno web) sin tocar el codigo del parque.

# Diagramas

Se encuentran en docs/, los realicé con la herramienta PLANTUML


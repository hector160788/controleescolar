# Control Escolar 2026

Sistema de control escolar desarrollado con Spring Boot 4, Thymeleaf, Spring Security y PostgreSQL.

---

## Requisitos previos

| Herramienta | Versión mínima | Notas |
|---|---|---|
| Java JDK | 25 | [Descargar Temurin 25](https://adoptium.net/) |
| Maven | 3.9+ | O usar el wrapper incluido (`mvnw`) |
| Docker Desktop | 4.x | Incluye Docker Compose v2 |

---

## Estructura del repositorio

```
controlescolar2026/
├── docker-compose.yml        # Orquesta BD + App en contenedores
├── scripts/
│   └── init.sql              # Script inicial de la base de datos
└── controlescolar/           # Proyecto Spring Boot
    ├── Dockerfile
    ├── pom.xml
    └── src/
```

---

## Opción A — Con Docker (recomendado)

Levanta la base de datos **y** la aplicación juntas en contenedores.

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd controlescolar2026
```

### 2. Compilar y levantar todo

```bash
docker compose up --build
```

Docker realizará automáticamente:
1. Levanta PostgreSQL y espera a que esté listo.
2. Compila el proyecto Java dentro del contenedor (Maven multi-etapa).
3. Inicia la aplicación Spring Boot.

### 3. Verificar que los contenedores estén corriendo

```bash
docker compose ps
```

Debes ver `controlescolar-app` y `bdcontrolescolar` con estado `running`.

### 4. Acceder a la aplicación

```
http://localhost:8080
```

### 5. Detener los contenedores

```bash
# Detener sin borrar datos
docker compose down

# Detener y borrar el volumen de la BD (datos se pierden)
docker compose down -v
```

---

## Opción B — Desarrollo local (BD en Docker, app en IDE/Maven)

Útil para desarrollar y depurar con hot reload.

### 1. Levantar solo la base de datos

Desde la raíz del repositorio (`controlescolar2026/`):

```bash
docker compose up postgres -d
```

Esto levanta PostgreSQL en `localhost:5432` con las credenciales del `docker-compose.yml` y ejecuta `scripts/init.sql` al iniciar por primera vez.

### 2. Verificar que la BD está lista

```bash
docker compose ps
# El servicio "postgres" debe mostrar "(healthy)"
```

### 3. Compilar el proyecto

Desde la carpeta `controlescolar/`:

```bash
# Con el wrapper de Maven incluido (no necesita Maven instalado)
./mvnw clean package -DskipTests

# O con Maven instalado globalmente
mvn clean package -DskipTests
```

El JAR compilado quedará en `controlescolar/target/controlescolar-0.0.1-SNAPSHOT.jar`.

### 4. Ejecutar la aplicación

**Opción 4a — Ejecutar el JAR directamente:**

```bash
java -jar target/controlescolar-0.0.1-SNAPSHOT.jar
```

**Opción 4b — Con Maven (incluye hot reload con DevTools):**

```bash
./mvnw spring-boot:run
```

**Opción 4c — Desde un IDE (IntelliJ / VS Code):**

Ejecutar la clase principal:
```
com.mx.controlescolar.ControlescolarApplication
```

### 5. Acceder a la aplicación

```
http://localhost:8080
```

---

## Variables de entorno / Configuración

La configuración por defecto está en `controlescolar/src/main/resources/application.yml`.  
Para sobreescribir valores sin modificar el archivo, usa variables de entorno:

| Variable de entorno | Valor por defecto |
|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/controlescolar` |
| `SPRING_DATASOURCE_USERNAME` | `controlescolar` |
| `SPRING_DATASOURCE_PASSWORD` | `password123` |
| `SERVER_PORT` | `8080` |

Ejemplo:

```bash
SPRING_DATASOURCE_PASSWORD=miPassword ./mvnw spring-boot:run
```

---

## Comandos útiles de Docker

```bash
# Ver logs de la aplicación en tiempo real
docker compose logs -f app

# Ver logs solo de la base de datos
docker compose logs -f postgres

# Conectarse a la BD dentro del contenedor
docker exec -it bdcontrolescolar psql -U controlescolar -d controlescolar

# Reconstruir solo la imagen de la app (tras cambios en el código)
docker compose up --build app
```

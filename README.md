
# Proyecto-final-Academia-Java-con-copilot.
En este repositorio se encuentra el proyecto final de Java y Spring con uso de Copilot
=======
# Spring Batch — Proyecto Final de Calificaciones (con GitHub Copilot)

Backend en Spring Boot que procesa calificaciones de estudiantes usando Spring Batch, 
expone una API REST sobre los datos, y cuenta con pruebas unitarias. Todo el código 
fue generado dirigiendo a GitHub Copilot (ver `PROMPTS.md` para el detalle de prompts 
y correcciones).

## Qué hace

- **Step 1:** Lee `estudiantes.csv` → calcula el promedio de cada estudiante → guarda en MySQL (`estudiantes_procesados`).
- **Step 2:** Lee de MySQL → determina estado (APROBADO/REPROBADO, umbral >= 70) → guarda en MongoDB (`reportes_estudiantes`).
- **API REST:** endpoints sobre estudiantes y reportes (GET, POST, PUT, PATCH, DELETE).
- **Tests:** JUnit 5 + Mockito sobre los processors y el servicio.

## Requisitos

- Java 17
- Maven
- Docker (contenedores de MySQL y MongoDB)

## Puertos utilizados

- **MySQL:** `localhost:3307` (usuario `alumno`, password `alumno123`, base `academia`)
- **MongoDB:** `localhost:27017` (usuario `root`, password `root123`, base `academia`)

> Nota: estos puertos son distintos a los de la guía original del taller (3306 y 27018), 
> ajustados según los contenedores Docker reales usados en este proyecto.

## Cómo correrlo

1. Levanta los contenedores de Docker:
```bash
   docker start mysql-academia mongo-academia
```

2. Verifica que la tabla de MySQL exista (si no, créala):
```sql
   CREATE TABLE IF NOT EXISTS estudiantes_procesados (
     id INT PRIMARY KEY AUTO_INCREMENT,
     nombre VARCHAR(100) NOT NULL,
     grupo VARCHAR(10) NOT NULL,
     nota1 DECIMAL(5,2) NOT NULL,
     nota2 DECIMAL(5,2) NOT NULL,
     nota3 DECIMAL(5,2) NOT NULL,
     promedio DECIMAL(5,2) NOT NULL
   );
```

3. Corre el batch (primera vez, con `spring.batch.job.enabled=true`):
```bash
   mvn spring-boot:run
```
   Resultado esperado: 10 estudiantes procesados → 7 APROBADO / 3 REPROBADO.

4. Para trabajar con la API sin duplicar datos, en `application.properties` 
   pon `spring.batch.job.enabled=false` y vuelve a correr:
```bash
   mvn spring-boot:run
```

5. Corre las pruebas:
```bash
   mvn test
```

## Endpoints principales

- `GET /api/estudiantes` — listar todos
- `GET /api/estudiantes/{id}` — obtener uno (404 si no existe)
- `GET /api/estudiantes/aprobados/total` — conteo de aprobados
- `POST /api/estudiantes` — crear (201)
- `PUT /api/estudiantes/{id}` — reemplazar (200/404)
- `PATCH /api/estudiantes/{id}` — actualizar grupo (200/404)
- `DELETE /api/estudiantes/{id}` — eliminar (204/404)
- `GET /api/reportes` — listar todos los reportes
- `GET /api/reportes/estado/{estado}` — filtrar por estado (APROBADO/REPROBADO)

## Evidencia de uso de Copilot

Ver `PROMPTS.md` para el detalle completo de prompts usados, y `evidencia/` para capturas de pantalla del proceso.
>>>>>>> c1a770d22e82be2fa4467d7b1edf153b472c97a5

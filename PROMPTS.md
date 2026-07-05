# Evidencia de uso de GitHub Copilot — Abril

## Prompt 1 — pom.xml
- **Prompt:** Genera un pom.xml para un proyecto Spring Boot 3.2.2 con Java 17 y estas dependencias: spring-boot-starter-batch, mysql-connector-j (scope runtime), spring-boot-starter-data-mongodb, spring-boot-starter-web, spring-boot-starter-data-jpa y spring-boot-starter-test (scope test). groupId com.academia, artifactId spring-batch-final-calificaciones, versión 1.0.0. Incluye el spring-boot-maven-plugin.
- **Modalidad:** Chat
- **Resultado:** aceptado sin corrección — las 6 dependencias y el parent estaban correctos.

## Prompt 2 — Modelo Estudiante
- **Prompt:** En la clase Estudiante, genera: campos nombre (String), grupo (String), nota1, nota2, nota3 y promedio (todos double). Incluye constructor vacío, getters y setters de todos los campos, y un toString que muestre nombre, grupo y promedio.
- **Modalidad:** Chat
- **Resultado:** aceptado sin corrección — cumplió con todos los campos, constructor, getters/setters y toString pedidos.

## Prompt 3 — EstudianteProcessor
- **Prompt:** En la clase EstudianteProcessor, que implementa ItemProcessor<Estudiante, Estudiante> de Spring Batch: en el método process, calcula el promedio como (nota1 + nota2 + nota3) / 3, asigna el promedio al estudiante con setPromedio, registra un log con SLF4J "Step 1 - Procesando: {estudiante}" y devuelve el estudiante.
- **Modalidad:** Chat
- **Resultado:** aceptado sin corrección — la división resultó correctamente decimal porque los campos nota1/nota2/nota3 son double.

## Prompt 4 — EstudianteReporte
- **Prompt:** En la clase EstudianteReporte, documento de MongoDB (@Document collection = "reportes_estudiantes") con: id (String, anotado con @Id), nombre, grupo, promedio (double) y estado (String). Constructor vacío, getters, setters y toString.
- **Modalidad:** Chat
- **Resultado:** aceptado sin corrección — verifiqué que los imports fueran de org.springframework.data.mongodb.core.mapping.Document y org.springframework.data.annotation.Id (correctos, no confundió paquetes).

## Prompt 5 — ReporteEstudianteProcessor (umbral)
- **Prompt:** En la clase ReporteEstudianteProcessor, que implementa ItemProcessor<Estudiante, EstudianteReporte>: convierte un Estudiante en un EstudianteReporte copiando nombre, grupo y promedio, y asigna estado "APROBADO" si el promedio es >= 70, o "REPROBADO" si es menor. Loguea "Step 2 - Reporte: {reporte}" y devuelve el reporte.
- **Modalidad:** Chat
- **Resultado:** aceptado sin corrección — verifiqué específicamente el umbral (>= 70) porque el taller advierte que Copilot suele generar > 70 por error; en este caso lo generó correctamente.

## Prompt 6 — BatchConfig
- **Prompt:** Genera una clase @Configuration de Spring Batch (Spring Boot 3.2) llamada BatchConfig con... (pega el prompt completo)
- **Modalidad:** Chat
- **Resultado:** CORREGIDO — Copilot usó el import inexistente org.springframework.batch.item.mongodb.MongoItemWriter y el método inexistente setMongoTemplate(). Lo corregí al paquete real org.springframework.batch.item.data.MongoItemWriter y al método setTemplate(). Esto es justo el tipo de "alucinación" que advierte el Cap. 1-2 del taller.

## Prompt 6 — BatchConfig
- **Prompt:** Genera una clase @Configuration de Spring Batch (Spring Boot 3.2) llamada BatchConfig con... (pega el prompt completo del taller)
- **Modalidad:** Chat
- **Resultado:** CORREGIDO — Copilot usó el import inexistente org.springframework.batch.item.mongodb.MongoItemWriter y el método inexistente setMongoTemplate(). Le pedí la corrección en el Chat y generó el import real org.springframework.batch.item.data.MongoItemWriter y el método setTemplate(). Compiló correctamente después del ajuste.

## Prompt 7 — SpringBatchApplication
- **Prompt:** Genera una clase principal de Spring Boot llamada SpringBatchApplication con @SpringBootApplication y el método main que arranca la aplicación con SpringApplication.run.
- **Modalidad:** Chat
- **Resultado:** aceptado — Copilot la ubicó en el paquete raíz com.academia en vez de com.academia.batch, pero es válido porque @SpringBootApplication escanea subpaquetes automáticamente. Borré la clase Main.java de ejemplo para no duplicar entry points.

## Prompt 8 — application.properties
- **Prompt:** Genera un application.properties para Spring Boot que se conecte a MySQL en jdbc:mysql://localhost:3307/academia (usuario alumno, password alumno123), inicialice el esquema de Spring Batch siempre, ejecute el Job al arrancar, y se conecte a MongoDB en mongodb://root:root123@localhost:27017/academia?authSource=admin.
- **Modalidad:** Chat
- **Resultado:** aceptado sin corrección — usé mis puertos reales de Docker (MySQL 3307, MongoDB 27017) en vez de los del PDF del taller (3306, 27018), porque verifiqué con `docker ps` que esos eran los puertos reales de mis contenedores.
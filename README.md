# EstudiaGT — Registro de estudiantes en la nube

Aplicación web de dos pantallas, sin login, construida con Java 17, Spring Boot 3.5.6, Maven, PostgreSQL, JPA/Hibernate, Thymeleaf y JavaScript.

## Funciones

- Formulario con todas las validaciones solicitadas.
- Botón deshabilitado hasta que el formulario y reCAPTCHA sean válidos.
- Web Service REST `POST /api/estudiantes`.
- Consulta REST `GET /api/estudiantes?q=`.
- Tabla con buscador, actualización y todas las columnas.
- reCAPTCHA v2 validado en navegador y backend.
- Tablas creadas automáticamente con Hibernate.
- Dockerfile y `render.yaml` para publicar en Render.

## Ejecutar localmente

1. En PostgreSQL ejecute:

```sql
CREATE DATABASE estudiantes_db;
```

2. Revise usuario y contraseña en `src/main/resources/application.properties`.
3. Ejecute:

```bash
mvn clean spring-boot:run
```

En Windows también puede abrir `INICIAR.bat`.

4. Abra `http://localhost:8080`.

Las claves reCAPTCHA incluidas son las claves oficiales de prueba de Google. Funcionan localmente y muestran una advertencia de prueba. Para publicar, use claves reales.

## Publicar gratis: Supabase + Render

### 1. Crear PostgreSQL en Supabase

1. Cree un proyecto gratuito en https://supabase.com.
2. Abra **Connect** y seleccione **Session pooler**.
3. Copie host, puerto, usuario y contraseña.
4. No necesita ejecutar `schema.sql`: Hibernate crea la tabla al arrancar. El archivo se incluye como respaldo.

La variable de Render debe quedar con formato JDBC:

```text
DB_URL=jdbc:postgresql://HOST_DEL_POOLER:5432/postgres?sslmode=require
DB_USERNAME=postgres.REFERENCIA_DEL_PROYECTO
DB_PASSWORD=SU_CONTRASEÑA
```

### 2. Crear reCAPTCHA

1. Entre a https://www.google.com/recaptcha/admin/create.
2. Seleccione **reCAPTCHA v2 — Casilla No soy un robot**.
3. Agregue `localhost` y el dominio final `su-app.onrender.com`.
4. Guarde la clave del sitio y la clave secreta.

### 3. Subir a GitHub

```bash
git init
git add .
git commit -m "Sistema de registro de estudiantes"
git branch -M main
git remote add origin URL_DE_SU_REPOSITORIO
git push -u origin main
```

### 4. Publicar en Render

1. Cree una cuenta en https://render.com.
2. Seleccione **New > Blueprint** y conecte el repositorio.
3. Render detectará `render.yaml`.
4. Ingrese `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `RECAPTCHA_SITE_KEY` y `RECAPTCHA_SECRET_KEY`.
5. Cree el servicio y espere el mensaje de despliegue exitoso.
6. Abra la URL `https://su-app.onrender.com`.

## Probar el Web Service

El formulario usa POST automáticamente. También puede probar el endpoint con Postman, pero necesita un token reCAPTCHA válido y de un solo uso:

```http
POST /api/estudiantes
Content-Type: application/json
```

```json
{
  "carne":"09001234",
  "nombres":"Dylan Ricardo",
  "apellidos":"Gálvez Mijangos",
  "correo":"dylan@example.com",
  "carrera":"Ingeniería en sistemas",
  "telefono":"5555-1234",
  "fechaNacimiento":"2000-01-15",
  "genero":"Masculino",
  "captchaToken":"TOKEN_GENERADO_POR_RECAPTCHA"
}
```

Para una prueba temporal desde Postman puede iniciar con `RECAPTCHA_ENABLED=false`; vuelva a `true` para la entrega.

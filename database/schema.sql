CREATE TABLE IF NOT EXISTS estudiantes (
    id BIGSERIAL PRIMARY KEY,
    carne VARCHAR(30) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo VARCHAR(160) NOT NULL UNIQUE,
    carrera VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    genero VARCHAR(12) NOT NULL CHECK (genero IN ('Masculino','Femenino','Otro')),
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_estudiante_edad CHECK (fecha_nacimiento <= CURRENT_DATE - INTERVAL '16 years')
);
CREATE INDEX IF NOT EXISTS idx_estudiantes_nombre ON estudiantes(apellidos, nombres);
CREATE INDEX IF NOT EXISTS idx_estudiantes_carrera ON estudiantes(carrera);

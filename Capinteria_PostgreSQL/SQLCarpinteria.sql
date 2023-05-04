CREATE DATABASE "Carpinteria02"
	with
	OWNER = postgres
	ENCODING = 'UTF8'
	CONNECTION LIMIT = -1
	IS_TEMPLATE = FALSE;
	
CREATE SCHEMA Persona 
CREATE SCHEMA Empresa 
CREATE SCHEMA Proyecto

CREATE TABLE Persona.Cliente(
	id_Cliente BIGSERIAL NOT NULL,
	Nombre_Cliente VARCHAR(100) NOT NULL,
	Telefono_Cliente VARCHAR(200) NOT NULL,
	Direccion_Cliente VARCHAR(200) NOT NULL,
	Correo_Cliente VARCHAR(100) NOT NULL,
	fecha_registro DATE NOT NULL,
	num_pedidos INT NOT NULL,
	
	CONSTRAINT ID_CLIENTE PRIMARY KEY (id_Cliente)
)

CREATE TABLE Persona.Empleado(
	id_Empleado BIGSERIAL NOT NULL,
	tipo_empleado VARCHAR(100) NOT NULL,
	Nombre_Empleado VARCHAR(100) NOT NULL,
	Telefono_Empleado VARCHAR(200) NOT NULL,
	Direccion_Empleado VARCHAR(200) NOT NULL,
	Sueldo Money NOT NULL,
	Empleado_desde DATE NOT NULL,
	Antiguedad INT NOT NULL,
	num_proyectos INT NOT NULL,
	
	CONSTRAINT PK_EMPLEADO PRIMARY KEY(id_Empleado)
)

--Antiguedad Empleado
CREATE OR REPLACE FUNCTION calcular_antiguedad() RETURNS TRIGGER AS $$
DECLARE
    idempleado BIGINT;
BEGIN
    IF TG_OP = 'INSERT' THEN
        idempleado := NEW.idEmpleado;
    ELSE
        idempleado := OLD.idEmpleado;
    END IF;
    
    UPDATE Persona.Empleado SET
        antiguedad = EXTRACT(YEAR FROM age(empleado_desde)) - 
               CASE WHEN DATE_TRUNC('year', empleado_desde) + 
                         INTERVAL '1 year' > now() 
                    THEN 1 
                    ELSE 0 
               END
    WHERE idEmpleado = idempleado;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;






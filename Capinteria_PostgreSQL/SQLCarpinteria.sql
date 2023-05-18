CREATE DATABASE "Carpinteria02"
	with
	OWNER = postgres
	ENCODING = 'UTF8'
	CONNECTION LIMIT = -1
	IS_TEMPLATE = FALSE;
	
CREATE SCHEMA Persona;
CREATE SCHEMA Empresa;
CREATE SCHEMA Proyecto;

CREATE TABLE Persona.Cliente(
	id_Cliente BIGSERIAL NOT NULL,
	Nombre_Cliente VARCHAR(100) NOT NULL,
	Telefono_Cliente VARCHAR(200) NOT NULL,
	Direccion_Cliente VARCHAR(200) NOT NULL,
	Correo_Cliente VARCHAR(100) NOT NULL,
	fecha_registro DATE NOT NULL,
	num_pedidos INT NOT NULL,
	
	CONSTRAINT ID_CLIENTE PRIMARY KEY (id_Cliente)
);

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
);

CREATE TABLE Proyecto.Insumo(
	idInsumo BIGSERIAL NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	cantidad INT NOT NULL,
	precio MONEY NOT NULL,
	
	CONSTRAINT PK_INSUMO PRIMARY KEY(idInsumo)
);

CREATE TABLE Empresa.Proveedor(
	idProveedor BIGSERIAL NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	telefono VARCHAR(100) NOT NULL,
	direccion VARCHAR(100) NOT NULL,
	correo VARCHAR(100) NOT NULL,
	
	CONSTRAINT PK_PROVEEDOR PRIMARY KEY(idProveedor)
);

DROP TABLE Proyecto.Tipo_Proyecto;

CREATE TABLE Proyecto.Tipo_Proyecto(
	idTipo_Proyecto BIGSERIAL NOT NULL,
	Nombre_Proyecto VARCHAR(50) NOT NULL,
	Precio MONEY NOT NULL,

	CONSTRAINT PK_TIPO_Proyecto PRIMARY KEY(idTipo_Proyecto)
)

--Antiguedad Empleado
CREATE OR REPLACE FUNCTION calcular_antiguedad()
RETURNS TRIGGER AS $$
BEGIN
    NEW.Antiguedad := EXTRACT(YEAR FROM AGE(CURRENT_DATE, NEW.Empleado_desde));
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

--Asisociar el trigger a la tabla empleado
CREATE TRIGGER trigger_calcular_antiguedad
BEFORE INSERT OR UPDATE ON Persona.Empleado
FOR EACH ROW
EXECUTE FUNCTION calcular_antiguedad();


DROP TRIGGER IF EXISTS trigger_calcular_antiguedad ON Persona.Empleado;
DROP FUNCTION IF EXISTS calcular_antiguedad();






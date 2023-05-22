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
	nombre VARCHAR(100) NOT NULL UNIQUE,
	cantidad_disponible INT NOT NULL DEFAULT 0,
	precio MONEY NOT NULL,
	
	CONSTRAINT PK_INSUMO PRIMARY KEY(idInsumo)
);

CREATE TABLE Proyecto.Herramienta(
    idHerramienta BIGSERIAL NOT NULL,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    tipo VARCHAR(100) NOT NULL,
    estado VARCHAR(100) NOT NULL,
    cantidad_disponible INT NOT NULL DEFAULT 0,
	precio MONEY NOT NULL,
    
    CONSTRAINT PK_HERRAMIENTA PRIMARY KEY(idHerramienta)
);
CREATE TABLE Empresa.Proveedor(
	idProveedor BIGSERIAL NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	telefono VARCHAR(100) NOT NULL,
	direccion VARCHAR(100) NOT NULL,
	correo VARCHAR(100) NOT NULL,
	
	CONSTRAINT PK_PROVEEDOR PRIMARY KEY(idProveedor)
);


CREATE TABLE Orden.Orden(
	idOrden BIGSERIAL NOT NULL,
	idProveedor BIGINT NOT NULL,
	fechaOrden DATE NOT NULL,
	total MONEY NOT NULL,

	CONSTRAINT PK_ORDEN PRIMARY KEY(idOrden),
    CONSTRAINT UQ_ORDEN_UNIQUE_COMBINATION UNIQUE (fechaOrden, idProveedor),
	CONSTRAINT FK_ORDEN_PROVEEDOR FOREIGN KEY(idProveedor) REFERENCES Empresa.Proveedor(idProveedor)
);

CREATE TABLE Orden.DetalleOrden(
    idOrden BIGINT NOT NULL,
    idInsumo BIGINT,
    idHerramienta BIGINT,
    cantidad INT NOT NULL,
    subtotal MONEY,

    CONSTRAINT FK_ID_ORDEN FOREIGN KEY(idOrden) REFERENCES Orden.Orden(idOrden),
    CONSTRAINT FK_ID_INSUMO FOREIGN KEY(idInsumo) REFERENCES Proyecto.Insumo(idInsumo),
    CONSTRAINT FK_ID_HERRAMIENTA FOREIGN KEY(idHerramienta) REFERENCES Proyecto.Herramienta(idHerramienta),

    CONSTRAINT CK_DETALLE_ORDEN_INSUMO_ID_ORDEN CHECK (
        (idInsumo IS NULL AND idHerramienta IS NOT NULL) OR
        (idInsumo IS NOT NULL AND idHerramienta IS NULL)
    ),
    CONSTRAINT UQ_DETALLE_ORDEN_INSUMO_ID_ORDEN UNIQUE (idInsumo, idOrden),
    CONSTRAINT UQ_DETALLE_ORDEN_HERRAMIENTA_ID_ORDEN UNIQUE (idHerramienta, idOrden)
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



CREATE OR REPLACE FUNCTION fn_tr_insumo_orden() RETURNS TRIGGER AS
$$
DECLARE 
	id_producto BIGINT;
	id_orden BIGINT;
BEGIN
	IF NEW.subtotal IS NOT NULL AND NEW.subtotal > '0'::money THEN
		RAISE NOTICE 'ACTUALIZANDO EL TOTAL DE LA ORDEN';
		id_orden := NEW.idOrden;

		UPDATE Orden.Orden
		SET total = total + NEW.subtotal
		WHERE idOrden = id_orden;
	END IF;

	IF NEW.idInsumo IS NOT NULL THEN
		id_producto := NEW.idInsumo;
		RAISE NOTICE 'Actualizando la cantidad de insumos';
		UPDATE Proyecto.Insumo 
		SET cantidad_disponible = cantidad_disponible + NEW.cantidad
		WHERE idInsumo = id_producto;

	ELSE
		id_producto := NEW.idHerramienta;
		RAISE NOTICE 'Actualizando la cantidad de herramientas';
		UPDATE Proyecto.Herramienta 
		SET cantidad_disponible = cantidad_disponible + NEW.cantidad
		WHERE idHerramienta = id_producto;
	END IF;

	RETURN NEW;
END;
$$ LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS TR_INSUMO_ORDEN ON Orden.DetalleOrden;


CREATE TRIGGER TR_INSUMO_ORDEN
AFTER INSERT OR UPDATE ON Orden.DetalleOrden
FOR EACH ROW EXECUTE FUNCTION fn_tr_insumo_orden();





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
ALTER TABLE Persona.Cliente
ADD CONSTRAINT UQ_CLIENTEUNICO1 UNIQUE(Telefono_Cliente);

ALTER TABLE Persona.Cliente
ADD CONSTRAINT UQ_CLIENTEUNICO3 UNIQUE(Correo_Cliente );


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

CREATE TABLE Proyecto.Proyecto(
	idProyecto BIGSERIAL NOT NULL,
	id_emp_supervisor INT NOT NULL,
	idCliente INT NOT NULL,
	idTipo_proyecto INT NOT NULL,
	estado VARCHAR(100) NOT NULL,
	fecha_estimada DATE NOT NULL,
	fecha_entrega VARCHAR(100) NOT NULL,
	descuento INT NOT NULL,
	Total MONEY NOT NULL,

	CONSTRAINT PK_Proyecto PRIMARY KEY(idProyecto),
	CONSTRAINT FK_Emp_Supervisor FOREIGN KEY(id_emp_supervisor) REFERENCES Persona.Empleado(id_Empleado),
	CONSTRAINT FK_Cliente FOREIGN KEY(idCliente) REFERENCES Persona.Cliente(id_Cliente),
	CONSTRAINT FK_TIPO_PROYECTO FOREIGN KEY(idTipo_proyecto) REFERENCES Proyecto.Tipo_Proyecto(idTipo_proyecto)
)
CREATE TABLE Proyecto.InsumoProyecto(
    idProyecto INT NOT NULL,
    idInsumo INT NOT NULL,
    cantidad INT NOT NULL,
    subtotal MONEY NOT NULL,

    CONSTRAINT PK_INSUMO_PROYECTO PRIMARY KEY(idProyecto, idInsumo),
    CONSTRAINT FK_INSUMO_PROYECTO_PROYECTO FOREIGN KEY(idProyecto) REFERENCES Proyecto.Proyecto(idProyecto),
    CONSTRAINT FK_INSUMO_PROYECTO_INSUMO FOREIGN KEY(idInsumo) REFERENCES Proyecto.Insumo(idInsumo)
);
DROP TABLE Proyecto.Proyecto

ALTER TABLE Proyecto.Herramienta ADD CONSTRAINT UQ_HERRAMIENTA_UNIQUE_NOMBRE UNIQUE (nombre);
ALTER TABLE empresa.proveedor ADD CONSTRAINT UQ_PROVEEDOR_UNIQUE_NOMBRE UNIQUE (nombre);
ALTER TABLE empresa.proveedor ADD CONSTRAINT UQ_PROVEEDOR_UNIQUE_TELEFONO UNIQUE (telefono);
ALTER TABLE empresa.proveedor ADD CONSTRAINT UQ_PROVEEDOR_UNIQUE_DIRECCION UNIQUE (direccion);
ALTER TABLE empresa.proveedor ADD CONSTRAINT UQ_PROVEEDOR_UNIQUE_CORREO UNIQUE (correo);


CREATE TABLE Proyecto.Tipo_Proyecto(
	idTipo_Proyecto BIGSERIAL NOT NULL,
	Nombre_Proyecto VARCHAR(50) NOT NULL,
	Precio MONEY NOT NULL,

	CONSTRAINT PK_TIPO_Proyecto PRIMARY KEY(idTipo_Proyecto)
)
-- Restricciones
	ALTER TABLE Persona.Empleado
	ADD CONSTRAINT UQ_EMPLEADOUNICO1 UNIQUE(Telefono_Empleado);
	
	ALTER TABLE Proyecto.Tipo_Proyecto
	ADD CONSTRAINT UQ_NombreProyecto UNIQUE(Nombre_Proyecto)

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

--Actualizar Total en proyecto
CREATE OR REPLACE FUNCTION actualizar_total_proyecto()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE Proyecto.Proyecto
    SET Total = (SELECT Precio FROM Proyecto.Tipo_Proyecto WHERE idTipo_Proyecto = NEW.idTipo_proyecto)
    WHERE idProyecto = NEW.idProyecto;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

--Asociar el trigger a la tabla proyecto
CREATE TRIGGER asignar_precio_proyecto
AFTER INSERT OR UPDATE OF idTipo_proyecto on Proyecto.Proyecto
FOR EACH ROW
EXECUTE FUNCTION actualizar_total_proyecto();


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


--Numero proyectos clientes
CREATE OR REPLACE FUNCTION incrementar_num_pedidos()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE Persona.Cliente
    SET num_pedidos = num_pedidos + 1
    WHERE id_Cliente = NEW.idCliente;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

--Asociar trigger a la tabla proyecto
CREATE TRIGGER incrementar_pedidos_proyecto
AFTER INSERT ON Proyecto.Proyecto
FOR EACH ROW
EXECUTE FUNCTION incrementar_num_pedidos();


--Consultas
SELECT CONCAT(p.idProyecto, '-' ,t.nombre_proyecto) AS NombreProyecto,CONCAT(e.Nombre_Empleado,'-',e.antiguedad) AS EmpleadoSupervisor, 
                CONCAT(c.Nombre_Cliente,'-',c.Correo_Cliente) AS Cliente , p.estado AS Estado, p.fecha_estimada AS FechaEstimada, p.fecha_entrega AS FechadeEntrega, p.descuento AS Descuento, p.Total AS TotalManodeobra
                FROM Proyecto.Proyecto p
                INNER JOIN Persona.Cliente c ON p.idCliente = c.id_Cliente
                INNER JOIN Proyecto.Tipo_Proyecto t ON p.idTipo_proyecto = t.idTipo_proyecto
                INNER JOIN Persona.Empleado e ON p.id_emp_supervisor = e.id_Empleado ORDER BY idProyecto ASC
				
SELECT * FROM Proyecto.Proyecto 

CREATE USER jose WITH PASSWORD 'password1';
GRANT USAGE ON SCHEMA empresa TO jose;
GRANT USAGE ON SCHEMA orden TO jose;
GRANT USAGE ON SCHEMA proyecto TO jose;
GRANT USAGE ON SCHEMA persona TO jose;

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA empresa TO jose;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA orden TO jose;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA proyecto TO jose;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA persona TO jose;

CREATE USER maria WITH PASSWORD 'password2';
GRANT USAGE ON SCHEMA empresa TO maria;
GRANT USAGE ON SCHEMA orden TO maria;
GRANT USAGE ON SCHEMA proyecto TO maria;
GRANT USAGE ON SCHEMA persona TO maria;

GRANT SELECT, INSERT, UPDATE ON ALL TABLES IN SCHEMA empresa TO maria;
GRANT SELECT, INSERT, UPDATE ON ALL TABLES IN SCHEMA orden TO maria;
GRANT SELECT, INSERT, UPDATE ON ALL TABLES IN SCHEMA proyecto TO maria;
GRANT SELECT, INSERT, UPDATE ON ALL TABLES IN SCHEMA persona TO maria;

CREATE USER javier WITH PASSWORD 'password3';
GRANT USAGE ON SCHEMA empresa TO javier;
GRANT USAGE ON SCHEMA orden TO javier;
GRANT USAGE ON SCHEMA proyecto TO javier;
GRANT USAGE ON SCHEMA persona TO javier;

REVOKE ALL ON ALL TABLES IN SCHEMA empresa FROM javier;
REVOKE ALL ON ALL TABLES IN SCHEMA orden FROM javier;
REVOKE ALL ON ALL TABLES IN SCHEMA proyecto FROM javier;
REVOKE ALL ON ALL TABLES IN SCHEMA persona FROM javier;

GRANT SELECT ON ALL TABLES IN SCHEMA empresa TO javier;
GRANT SELECT ON ALL TABLES IN SCHEMA orden TO javier;
GRANT SELECT ON ALL TABLES IN SCHEMA proyecto TO javier;
GRANT SELECT ON ALL TABLES IN SCHEMA persona TO javier;

SELECT privilege_type as privilegios
FROM information_schema.table_privileges
WHERE grantee = 'maria'
GROUP BY privilege_type;

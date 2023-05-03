
CREATE DATABASE Carpinteria

USE Carpinteria

CREATE SCHEMA Persona 
CREATE SCHEMA Empresa 
CREATE SCHEMA Proyecto
DROP TABLE Persona.Cliente
DROP TABLE Persona.Empleado

CREATE TABLE Persona.Cliente(
	idCliente INT IDENTITY(1,1) NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	telefono VARCHAR(200) NOT NULL,
	direccion VARCHAR(200) NOT NULL,
	correo VARCHAR(100) NOT NULL,
	fecha_registro DATE NOT NULL,
	num_pedidos INT NOT NULL,
	CONSTRAINT PK_CLIENTE PRIMARY KEY(idCliente)
)

SELECT * FROM Persona.Cliente
SELECT * FROM Persona.Cliente WHERE Cliente.nombre = 'J'

CREATE TABLE Persona.Empleado(
	idEmpleado INT IDENTITY(1,1) NOT NULL,
	tipo_empleado VARCHAR(100) NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	telefono VARCHAR(200) NOT NULL,
	direccion VARCHAR(200) NOT NULL,
	sueldo MONEY NOT NULL,
	empleado_desde DATE NOT NULL,
	antiguedad INT NOT NULL,
	num_proyectos INT NOT NULL

	CONSTRAINT PK_EMPLEADO PRIMARY KEY(idEmpleado)
)

SELECT * FROM Persona.Empleado 
SELECT * FROM Persona.Empleado WHERE tipo_empleado = 'Lider'
DELETE FROM Proyecto.Empleado_Proyecto

SELECT e.nombre AS 'id_empleado', h.nombre AS 'id_herramienta', p.fecha_prestamo, p.fecha_devolucion
                FROM Proyecto.Prestamo p 
                INNER JOIN Persona.Empleado e ON p.id_empleado = e.idEmpleado 
                INNER JOIN Proyecto.Herramienta h ON p.id_herramienta = h.idHerramienta

CREATE TABLE Proyecto.Tipo_Proyecto(
	idTipo_proyecto INT IDENTITY(1,1) NOT NULL,
	nombre_proyecto INT NOT NULL,
	precio MONEY NOT NULL,

	CONSTRAINT PK_TIPO_Proyecto PRIMARY KEY(idTipo_proyecto),
)

ALTER TABLE Proyecto.Tipo_Proyecto
ADD CONSTRAINT UQ_TIPO_PROYECTO
UNIQUE (nombre_PROYECTO);

CREATE TABLE Proyecto.Proyecto(
	idProyecto INT IDENTITY(1,1) NOT NULL,
	id_emp_supervisor INT NOT NULL,
	idCliente INT NOT NULL,
	idTipo_proyecto INT NOT NULL,
	--nombre_proyecto VARCHAR(100) NOT NULL,
	estado VARCHAR(100) NOT NULL,
	fecha_estimada DATE NOT NULL,
	fecha_entrega DATE NOT NULL,
	descuento INT NOT NULL,
	Total MONEY NOT NULL,

	CONSTRAINT PK_Proyecto PRIMARY KEY(idProyecto),
	CONSTRAINT FK_Emp_Supervisor FOREIGN KEY(id_emp_supervisor) REFERENCES Persona.Empleado(idEmpleado),
	CONSTRAINT FK_Cliente FOREIGN KEY(idCliente) REFERENCES Persona.Cliente(idCliente),
	CONSTRAINT FK_TIPO_PROYECTO FOREIGN KEY(idTipo_proyecto) REFERENCES Proyecto.Tipo_Proyecto(idTipo_proyecto),
)

ALTER TABLE Proyecto.Proyecto
ALTER COLUMN fecha_entrega VARCHAR(50) NOT NULL;


SELECT * FROM Proyecto.Proyecto
SELECT p.idProyecto,CONCAT(e.nombre,'-',e.antiguedad) AS 'id_emp_supervisor' ,CONCAT(c.nombre,'-',c.correo) AS 'idCliente', p.nombre_proyecto, p.estado, p.fecha_entrega, p.fecha_estimada, p.descuento, p.Total
FROM Proyecto.Proyecto p 
INNER JOIN Persona.Cliente c ON p.idCliente = c.idCliente
INNER JOIN Persona.Empleado e ON p.id_emp_supervisor = e.idEmpleado



CREATE TABLE Proyecto.Herramienta(
	idHerramienta INT IDENTITY(1,1) NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	tipo VARCHAR(100) NOT NULL,
	estado VARCHAR(100) NOT NULL,
	cantidad_disponible INT NOT NULL,

	CONSTRAINT PK_Herramienta PRIMARY KEY(idHerramienta)
)

SELECT * FROM Proyecto.Herramienta WHERE cantidad_disponible > 0
DELETE FROM Proyecto.Herramienta
INSERT INTO Proyecto.Herramienta (nombre,tipo,estado,cantidad_disponible) VALUES ('Nivel de gota','Medir','Nuevo',4)
UPDATE Proyecto.Herramienta SET cantidad_disponible = 1 WHERE idHerramienta = 7

CREATE TABLE Proyecto.Prestamo(
	id_empleado INT NOT NULL,
	id_herramienta INT NOT NULL,
	fecha_prestamo VARCHAR(30) NOT NULL,
	fecha_devolucion VARCHAR(30) NOT NULL,

	CONSTRAINT FK_Empleado FOREIGN KEY(id_empleado) REFERENCES Persona.Empleado(idEmpleado),
	CONSTRAINT FK_Herramienta FOREIGN KEY(id_herramienta) REFERENCES Proyecto.Herramienta(idHerramienta)
)

DROP TABLE Proyecto.Prestamo

ALTER TABLE Proyecto.Herramienta
ADD CONSTRAINT UQ_NOMBRE_HERRA
UNIQUE (nombre);

CREATE TABLE Proyecto.Empleado_Proyecto(
	id_empleado INT NOT NULL,
	id_proyecto INT NOT NULL,
	actividad VARCHAR(200) NOT NULL,
	comision INT NOT NULL,

	CONSTRAINT FK_Empleado2 FOREIGN KEY(id_empleado) REFERENCES Persona.Empleado(idEmpleado),
	CONSTRAINT FK_Proyecto FOREIGN KEY(id_proyecto) REFERENCES Proyecto.Proyecto(idProyecto)
)

SELECT * FROM Proyecto.Empleado_Proyecto

DROP TABLE Proyecto.InsumoProyecto

CREATE TABLE Proyecto.Insumo(
	idInsumo INT IDENTITY(1,1) NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	cantidad INT NOT NULL,
	precio MONEY NOT NULL,

	CONSTRAINT PK_Insumo PRIMARY KEY(idInsumo)
)


CREATE TABLE Proyecto.InsumoProyecto(
	idInsumo INT NOT NULL,
	idProyecto INT NOT NULL,
	cantidad INT NOT NULL,
	subtotal MONEY NOT NULL,

	CONSTRAINT FK_Insumo FOREIGN KEY(idInsumo) REFERENCES Proyecto.Insumo(idInsumo),
	CONSTRAINT FK_Proyecto2 FOREIGN KEY(idProyecto) REFERENCES Proyecto.Proyecto(idProyecto)
)

CREATE TABLE Empresa.Proveedor(
	idProveedor INT IDENTITY(1,1) NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	direccion VARCHAR(200) NOT NULL,
	telefono VARCHAR(200) NOT NULL,
	correo VARCHAR(100) NOT NULL,

	CONSTRAINT PK_Proveedor PRIMARY KEY(idProveedor)
)

--Antiguedad de empleado
CREATE TRIGGER TR_EDAD ON Persona.Empleado

fOR INSERT,UPDATE AS
DECLARE @idempleado as BIGINT

BEGIN 
	IF EXISTS (SELECT * FROM inserted)
	BEGIN 
		SELECT @idempleado = idEmpleado FROM inserted

		UPDATE Persona.Empleado SET
		antiguedad = DATEDIFF(YEAR,empleado_desde,GETDATE())-(CASE WHEN DATEADD(YY,DATEDIFF(YEAR,empleado_desde,GETDATE()),empleado_desde)>GETDATE() THEN 1 ELSE 0 END)
		WHERE idEmpleado = @idempleado
	END
END

--Resta de producto
DROP TRIGGER RESTAR_PRODCREATE 
CREATE TRIGGER TR_REST ON Proyecto.Prestamo

AFTER INSERT AS
DECLARE @articuloId as BIGINT

BEGIN 
	IF EXISTS (SELECT * FROM inserted)
	BEGIN 
		SELECT @articuloId = id_herramienta FROM inserted


		UPDATE Proyecto.Herramienta
		SET cantidad_disponible = cantidad_disponible - 1
		WHERE idHerramienta = @articuloId
	END
END

--Suma de producto
DROP TRIGGER RESTAR_PRODCREATE 
CREATE TRIGGER TR_SUM ON Proyecto.Prestamo

FOR UPDATE AS
DECLARE @articuloId as BIGINT

BEGIN 
	IF EXISTS (SELECT * FROM inserted)
	BEGIN 
		SELECT @articuloId = id_herramienta FROM inserted


		UPDATE Proyecto.Herramienta
		SET cantidad_disponible = cantidad_disponible + 1
		WHERE idHerramienta = @articuloId
	END
END

--Insertar datos a tabla empleados proyecto
CREATE TRIGGER TR_INSERT_EMP ON Proyecto.Proyecto
 AFTER INSERT
 AS
 BEGIN 
	INSERT into Proyecto.Empleado_Proyecto(id_empleado, id_proyecto, actividad, comision)
	Select id_emp_supervisor, idProyecto, 'Supervisar', 1500 from INSERTED
 END 

 --Modificar lider de proyecto 
CREATE TRIGGER TR_MODIF_LIDER ON Proyecto.Empleado_Proyecto

FOR UPDATE AS
DECLARE @proyectoId as BIGINT
DECLARE @empleadoId as INT

BEGIN 
	IF EXISTS (SELECT * FROM inserted)
	BEGIN 
		SELECT @proyectoId = id_proyecto FROM inserted
		SELECT @empleadoId = id_empleado FROM inserted

		UPDATE Proyecto.Proyecto
		SET id_emp_supervisor = @empleadoId
		WHERE idProyecto = @proyectoId
	END
END

--Descuento cliente
CREATE TRIGGER TR_DESC ON Proyecto.Proyecto

FOR INSERT AS
DECLARE @clienteId as BIGINT
DECLARE @numPedidos as INT

BEGIN 
	IF EXISTS (SELECT * FROM inserted)
	BEGIN 
		SELECT @clienteId = id_emp_supervisor FROM inserted
		SELECT @numPedidos = num_pedidos FROM Persona.Cliente

		UPDATE Proyecto.Proyecto
		SET descuento = 10
		WHERE idProyecto = @clienteId AND @numPedidos >= 10
	END
END

--Suma de proyecto a cliente
DROP TRIGGER RESTAR_PRODCREATE 
CREATE TRIGGER TR_SUM_PROYECT ON Proyecto.Proyecto

FOR UPDATE AS
DECLARE @clienteId as BIGINT

BEGIN 
	IF EXISTS (SELECT * FROM inserted)
	BEGIN 
		SELECT @clienteId = idCliente FROM inserted


		UPDATE Persona.Cliente
		SET num_pedidos = num_pedidos + 1
		WHERE idCliente = @clienteId
	END
END

--Suma de proyecto a empleado
DROP TRIGGER RESTAR_PRODCREATE 
CREATE TRIGGER TR_SUM_PROYECT_EMPL ON Proyecto.Empleado_Proyecto

FOR INSERT AS
DECLARE @empladoId as BIGINT

BEGIN 
	IF EXISTS (SELECT * FROM inserted)
	BEGIN 
		SELECT @empladoId = id_empleado FROM inserted


		UPDATE Persona.Empleado
		SET num_proyectos = num_proyectos + 1
		WHERE idEmpleado = @empladoId
	END
END

--Eliminacion Proyecto
CREATE TRIGGER TR_DELET_PROYECT ON Proyecto.Proyecto

FOR DELETE AS
DECLARE @proyectoId as BIGINT

BEGIN 
	IF EXISTS (SELECT * FROM inserted)
	BEGIN 
		SELECT @proyectoId = idProyecto FROM inserted


		DELETE Proyecto.Empleado_Proyecto
		WHERE id_proyecto = @proyectoId
	END
END

--Subtotal Insumo_Proyecto
CREATE TRIGGER TR_SUBTOTAL_INSUMO_PROYECTO 
ON Proyecto.InsumoProyecto
AFTER INSERT
AS
DECLARE @idInsumo AS INT
DECLARE @cantidad AS INT
BEGIN
	
	SELECT @idInsumo = idInsumo FROM inserted
	SELECT @cantidad= cantidad FROM inserted
	
	UPDATE Proyecto.InsumoProyecto
	SET subtotal = (SELECT precio FROM Proyecto.Insumo WHERE idInsumo = @idInsumo) * @cantidad
	FROM inserted
	WHERE InsumoProyecto.idInsumo = inserted.idInsumo AND InsumoProyecto.idProyecto = inserted.idProyecto
		
END;

--Trigger que actualiza la cantidad disponible de insumos
CREATE TRIGGER TR_INSUMO_PROYECTO ON Proyecto.InsumoProyecto
FOR INSERT AS
	DECLARE @idInsumo AS INT
	BEGIN
		IF EXISTS (SELECT * FROM inserted)
		BEGIN
			SELECT @idInsumo = idInsumo FROM inserted

			UPDATE Proyecto.Insumo 
			SET Insumo.cantidad = (Insumo.cantidad - (SELECT cantidad FROM inserted))
			WHERE Insumo.idInsumo = @idInsumo
		END
	END
GO

--Trigger que actualiza los insumos despues de eliminar un registro en InsumoProyecto
CREATE TRIGGER TR_CANTIDAD_INSUMO
ON Proyecto.InsumoProyecto
AFTER DELETE
AS
DECLARE @idInsumo AS INT
BEGIN
	
	SELECT @idInsumo = idInsumo FROM deleted
	UPDATE Proyecto.Insumo 
	SET cantidad = cantidad + (SELECT cantidad FROM deleted)
	WHERE idInsumo = @idInsumo
END;

-- CÓDIGO SQL NECESARIO PARA LAS PANTALLAS DE ORDENES
CREATE SCHEMA Orden;
USE ORDEN;
CREATE TABLE Orden.Orden(
	idOrden BIGINT IDENTITY (1,1) NOT NULL,
	idProveedor INT NOT NULL,
	fechaOrden DATE NOT NULL,
	total MONEY,

	CONSTRAINT PK_ID_ORDEN PRIMARY KEY (idOrden),
	CONSTRAINT FK_ID_PROVEEDOR FOREIGN KEY (idProveedor) REFERENCES Proveedor.Proveedor(idProveedor)
)

go;

CREATE TABLE Orden.DetalleOrden(
	idOrden BIGINT  NOT NULL,
	idHerramienta INT,
	idInsumo INT,
	cantidad INT NOT NULL,
	subtotal MONEY NOT NULL,

	CONSTRAINT FK_ID_ORDEN FOREIGN KEY (idOrden) REFERENCES Orden.Orden(idOrden),
	CONSTRAINT FK_ID_HERRAMIENTA FOREIGN KEY (idHerramienta) REFERENCES Proyecto.Herramienta(idHerramienta),
	CONSTRAINT FK_ID_INSUMO FOREIGN KEY (idInsumo) REFERENCES Proyecto.Insumo(idInsumo),
)
go


ALTER TABLE Proyecto.Herramienta
ADD precio MONEY;

ALTER TABLE Proyecto.Herramienta
ALTER COLUMN precio MONEY NOT NULL;

--Trigger que actualiza la cantidad de insumos
CREATE TRIGGER TR_INSUMO_ORDEN 
ON Orden.DetalleOrden
AFTER INSERT AS
	DECLARE @idProducto AS BIGINT
	DECLARE @idOrden AS BIGINT
	BEGIN
		IF EXISTS (SELECT 1 FROM inserted WHERE subtotal IS NOT NULL AND subtotal > 0)
		BEGIN
			PRINT 'ACTUALIZANDO EL TOTAL DE LA ORDEN'
			SELECT @idOrden = idOrden FROM inserted

			UPDATE Orden.Orden
			SET Orden.total = Orden.total + (SELECT subtotal FROM inserted)
			WHERE Orden.idOrden = @idOrden
		END

		IF EXISTS (SELECT 1 FROM inserted WHERE idInsumo IS NOT NULL)
		BEGIN
			SELECT @idProducto = idInsumo FROM inserted
			PRINT 'Actualizando la cantidad de insumos'
			UPDATE Proyecto.Insumo 
			SET Insumo.cantidad = (Insumo.cantidad + (SELECT cantidad FROM inserted))
			WHERE Insumo.idInsumo = @idProducto

		END
		ELSE
		BEGIN
			SELECT @idProducto = idHerramienta FROM inserted
			PRINT 'Actualizando la cantidad de herramientas'
			UPDATE Proyecto.Herramienta 
			SET Herramienta.cantidad_disponible = (Herramienta.cantidad_disponible + (SELECT cantidad FROM inserted))
			WHERE Herramienta.idHerramienta = @idProducto
		END
	END

GO
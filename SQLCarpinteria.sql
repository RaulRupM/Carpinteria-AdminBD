
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

CREATE TABLE Proyecto.Proyecto(
	idProyecto INT IDENTITY(1,1) NOT NULL,
	id_emp_supervisor INT NOT NULL,
	idCliente INT NOT NULL,
	nombre_proyecto VARCHAR(100) NOT NULL,
	estado VARCHAR(100) NOT NULL,
	fecha_estimada DATE NOT NULL,
	fecha_entrega DATE NOT NULL,
	descuento INT NOT NULL,
	Total MONEY NOT NULL,

	CONSTRAINT PK_Proyecto PRIMARY KEY(idProyecto),
	CONSTRAINT FK_Emp_Supervisor FOREIGN KEY(id_emp_supervisor) REFERENCES Persona.Empleado(idEmpleado),
	CONSTRAINT FK_Cliente FOREIGN KEY(idCliente) REFERENCES Persona.Cliente(idCliente)
)

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

SELECT * FROM Proyecto.Herramienta

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

CREATE TABLE Proyecto.Insumo(
	idInsumo INT IDENTITY(1,1) NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	cantidad INT NOT NULL,
	precio MONEY NOT NULL,

	CONSTRAINT PK_Insumo PRIMARY KEY(idInsumo)
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
		edad = DATEDIFF(YEAR,empleado_desde,GETDATE())-(CASE WHEN DATEADD(YY,DATEDIFF(YEAR,empleado_desde,GETDATE()),empleado_desde)>GETDATE() THEN 1 ELSE 0 END)
		/(datediff(day,[empleado_desde],getdate()))/(365)/
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


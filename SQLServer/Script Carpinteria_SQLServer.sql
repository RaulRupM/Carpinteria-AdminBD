
CREATE DATABASE Carpinteria

USE Carpinteria

CREATE SCHEMA Persona 
CREATE SCHEMA Empresa 
CREATE SCHEMA Proyecto
CREATE SCHEMA Orden

--------------------/*Creacion de tablas SIN llaves foraneas*/--------------------
--Tabla Cliente.
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

--Tabla Empleado.
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

--Tabla Herramienta.
CREATE TABLE Proyecto.Herramienta(
	idHerramienta INT IDENTITY(1,1) NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	tipo VARCHAR(100) NOT NULL,
	estado VARCHAR(100) NOT NULL,
	cantidad_disponible INT NOT NULL,
	precio MONEY NOT NULL,

	CONSTRAINT PK_Herramienta PRIMARY KEY(idHerramienta)
)

--Tabla Insumo.
CREATE TABLE Proyecto.Insumo(
	idInsumo INT IDENTITY(1,1) NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	cantidad INT NOT NULL,
	precio MONEY NOT NULL,

	CONSTRAINT PK_Insumo PRIMARY KEY(idInsumo)
)

--Tabla Proveedor.
CREATE TABLE Empresa.Proveedor(
	idProveedor INT IDENTITY(1,1) NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	direccion VARCHAR(200) NOT NULL,
	telefono VARCHAR(200) NOT NULL,
	correo VARCHAR(100) NOT NULL,

	CONSTRAINT PK_Proveedor PRIMARY KEY(idProveedor)
)

--Tabla Tipo proyecto.
CREATE TABLE Proyecto.Tipo_Proyecto(
	idTipo_proyecto INT IDENTITY(1,1) NOT NULL,
	nombre_proyecto VARCHAR(50) NOT NULL,
	precio MONEY NOT NULL,

	CONSTRAINT PK_TIPO_Proyecto PRIMARY KEY(idTipo_proyecto),
)

--------------------/*Creacion de tablas CON llaves foraneas*/--------------------
--Tabla Proyecto
CREATE TABLE Proyecto.Proyecto(
	idProyecto INT IDENTITY(1,1) NOT NULL,
	id_emp_supervisor INT NOT NULL,
	idCliente INT NOT NULL,
	idTipo_proyecto INT NOT NULL,
	estado VARCHAR(100) NOT NULL,
	fecha_estimada DATE NOT NULL,
	fecha_entrega VARCHAR(50) NOT NULL,
	descuento INT NOT NULL,
	Total MONEY NOT NULL,

	CONSTRAINT PK_Proyecto PRIMARY KEY(idProyecto),
	CONSTRAINT FK_Emp_Supervisor FOREIGN KEY(id_emp_supervisor) REFERENCES Persona.Empleado(idEmpleado),
	CONSTRAINT FK_Cliente FOREIGN KEY(idCliente) REFERENCES Persona.Cliente(idCliente),
	CONSTRAINT FK_TIPO_PROYECTO FOREIGN KEY(idTipo_proyecto) REFERENCES Proyecto.Tipo_Proyecto(idTipo_proyecto),
)

--Tabla Prestamo.
CREATE TABLE Proyecto.Prestamo(
	id_empleado INT NOT NULL,
	id_herramienta INT NOT NULL,
	fecha_prestamo VARCHAR(30) NOT NULL,
	fecha_devolucion VARCHAR(30) NOT NULL,

	CONSTRAINT FK_Empleado FOREIGN KEY(id_empleado) REFERENCES Persona.Empleado(idEmpleado),
	CONSTRAINT FK_Herramienta FOREIGN KEY(id_herramienta) REFERENCES Proyecto.Herramienta(idHerramienta)
)

--Tabla Empleado Proyecto.
CREATE TABLE Proyecto.Empleado_Proyecto(
	id_empleado INT NOT NULL,
	id_proyecto INT NOT NULL,
	actividad VARCHAR(200) NOT NULL,
	comision INT NOT NULL,

	CONSTRAINT FK_Empleado2 FOREIGN KEY(id_empleado) REFERENCES Persona.Empleado(idEmpleado),
	CONSTRAINT FK_Proyecto FOREIGN KEY(id_proyecto) REFERENCES Proyecto.Proyecto(idProyecto)
)

--Tabla Insumo proyecto.
CREATE TABLE Proyecto.InsumoProyecto(
	idInsumo INT NOT NULL,
	idProyecto INT NOT NULL,
	cantidad INT NOT NULL,
	subtotal MONEY NOT NULL,

	CONSTRAINT FK_Insumo FOREIGN KEY(idInsumo) REFERENCES Proyecto.Insumo(idInsumo),
	CONSTRAINT FK_Proyecto2 FOREIGN KEY(idProyecto) REFERENCES Proyecto.Proyecto(idProyecto)
)

--Tabla Orden.
CREATE TABLE Orden.Orden(
	idOrden BIGINT IDENTITY (1,1) NOT NULL,
	idProveedor INT NOT NULL,
	fechaOrden DATE NOT NULL,
	total MONEY,

	CONSTRAINT PK_ID_ORDEN PRIMARY KEY (idOrden),
	CONSTRAINT FK_ID_PROVEEDOR FOREIGN KEY (idProveedor) REFERENCES Empresa.Proveedor(idProveedor)
)

--Tabla Detalle Orden.
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

--------------------/* RULE */---------------------
--Restriccion sueldo empleado no mayor a 15000
CREATE RULE RG_sueldo_intervalo AS @sueldo between 200 and 15000
EXEC sp_bindrule          
 'RG_sueldo_intervalo',
 'Persona.Empleado.sueldo'
GO

--Restriccion a tipo de empleado ya sea "Lider" o "Empleado"
CREATE RULE RG_tipo_empelado AS @tipo IN ('Lider', 'Empleado')
EXEC sp_bindrule          
 'RG_tipo_empelado',
 'Persona.Empleado.tipo_empleado'
GO

--------------------/* UNIQUE */-------------------
--UNIQUE Tipo proyecto
ALTER TABLE Proyecto.Tipo_Proyecto ADD CONSTRAINT UQ_TIPO_PROYECTO UNIQUE (nombre_PROYECTO);
--UNIQUE Herramienta
ALTER TABLE Proyecto.Herramienta ADD CONSTRAINT UQ_NOMBRE_HERRA UNIQUE (nombre);
--UNIQUE Proyecto
ALTER TABLE Proyecto.Proyecto ADD CONSTRAINT UQ_NOM_PROYECTO UNIQUE (idCliente, idTipo_proyecto, fecha_estimada);
--UNIQUE Proyecto Empleado
ALTER TABLE Proyecto.Empleado_Proyecto ADD CONSTRAINT UQ_EMPLPROY UNIQUE (id_empleado, id_proyecto);


--------------------/* Disparadores */--------------------
/*Disparador de la tabla Empleado*/
--Antiguedad de empleado, calcula la antiguedad del empleado
--con la fecha de ingreso del empleado.
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

/*Disparadores de la tabla proyecto */
--Insertar datos a tabla empleados proyecto, al insertar un proyecto los datos del empleado supervisor
--se insertaran en la tabla empleado proyecto, con su comision por default.
CREATE TRIGGER TR_INSERT_EMP ON Proyecto.Proyecto
 AFTER INSERT
 AS
 BEGIN 
	INSERT into Proyecto.Empleado_Proyecto(id_empleado, id_proyecto, actividad, comision)
	Select id_emp_supervisor, idProyecto, 'Supervisar', 1500 from INSERTED
 END 

--Actualizacion de datos Proyecto a Empleado_Proyecto, al momento de modificar 
--al empleado supervisor de un proyecto, se modificara a dicho empelado en la tabla 
--Empleado proyecto.
CREATE TRIGGER UPDATE_SUPERV ON Proyecto.Proyecto
FOR UPDATE AS
DECLARE @empleadoid as BIGINT
DECLARE @proyectoid as BIGINT

BEGIN 
	IF EXISTS (SELECT * FROM inserted)
	BEGIN 
		SELECT @empleadoid = id_emp_supervisor FROM inserted
		SELECT @proyectoid = idProyecto FROM inserted

		UPDATE Proyecto.Empleado_Proyecto
		SET id_empleado = @empleadoid
		WHERE id_proyecto = @proyectoid
	END
END

--Descuento cliente, al insertar un proyecto se verificara que el cliente tengo 
--un descuento del 10% (tendra que contar con 10 proyectos ya ordenados anteriormente)
--de la man de obra del proyecto.
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

--Suma de proyecto a cliente, al insertar un nuevo proyecto, en la tabla cliente
--se incrementara el nuemro de pedido.
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

/* Disparadores de la tabla Prestamo*/
--Resta de producto, resta la cantidad disponible de la tabla herramienta.
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

--Suma de producto, suma la cantidad disponible de la tabla herramienta.
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

/*Disparadores de la tabla Insumo Proyecto*/
--Subtotal Insumo_Proyecto, incrementa el subtotal de insumos seleccionados
--que se utilizaran para la inplementacion de un proyecto.
--Y en la tabla proyecto en el campo total se sumara el subtotal.
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

	SELECT @idProyecto = idProyecto FROM InsumoProyecto
	SELECT @subtotal = subtotal FROM InsumoProyecto

	UPDATE Proyecto.Proyecto
	SET Total = Total + @subtotal
	WHERE idProyecto = @idProyecto
		
END;

--Trigger que actualiza la cantidad disponible de insumos.
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

--Trigger que actualiza los insumos despues de eliminar un registro en InsumoProyecto.
--Y en la tabla proyecto en el campo total se restara el subtotal eliminado.
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

	SELECT @idProyecto = idProyecto FROM deleted
	UPDATE Proyecto.Proyecto
	SET Total = Total - (SELECT subtotal FROM deleted)
	WHERE idProyecto = @idProyecto
END;

/*Disparadores de la tabla Detalle Orden */
--Trigger que actualiza la cantidad de insumos.
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


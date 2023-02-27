CREATE DATABASE Carpinteria

USE Carpinteria

CREATE SCHEMA Persona 
CREATE SCHEMA Proveedor 
CREATE SCHEMA Proyecto

CREATE TABLE Persona.Cliente(
	idCliente INT IDENTITY(1,1) NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	telefono INT NOT NULL,
	direccion VARCHAR(200) NOT NULL,
	correo VARCHAR(100) NOT NULL,
	fecha_registro DATE NOT NULL,
	num_pedidos INT NOT NULL,
	CONSTRAINT PK_CLIENTE PRIMARY KEY(idCliente)
)

CREATE TABLE Persona.Empleado(
	idEmpleado INT IDENTITY(1,1) NOT NULL,
	tipo_empleado VARCHAR(100) NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	telefono INT NOT NULL,
	direccion VARCHAR(200) NOT NULL,
	sueldo MONEY NOT NULL,
	empleado_desde DATE NOT NULL,
	antiguedad INT NOT NULL,
	num_proyectos INT NOT NULL

	CONSTRAINT PK_EMPLEADO PRIMARY KEY(idEmpleado)
)

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

CREATE TABLE Proyecto.Herramienta(
	idHerramienta INT IDENTITY(1,1) NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	tipo VARCHAR(100) NOT NULL,
	estado VARCHAR(100) NOT NULL,
	cantidad_disponible INT NOT NULL,

	CONSTRAINT PK_Herramienta PRIMARY KEY(idHerramienta)
)


CREATE TABLE Proyecto.Insumo(
	idInsumo INT IDENTITY(1,1) NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	cantidad INT NOT NULL,
	precio MONEY NOT NULL,

	CONSTRAINT PK_Insumo PRIMARY KEY(idInsumo)
)

CREATE TABLE Proveedor.Proveedor(
	idProveedor INT IDENTITY(1,1) NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	direccion VARCHAR(200) NOT NULL,
	telefono INT NOT NULL,
	correo VARCHAR(100) NOT NULL,

	CONSTRAINT PK_Proveedor PRIMARY KEY(idProveedor)
)

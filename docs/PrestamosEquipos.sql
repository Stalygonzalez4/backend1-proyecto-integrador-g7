CREATE DATABASE SistemaPrestamosEquipos;
GO

USE SistemaPrestamosEquipos;
GO

CREATE TABLE Usuarios (
    IdUsuario INT IDENTITY(1,1) PRIMARY KEY,
    TipoDocumento VARCHAR(20) NOT NULL,
    NumeroDocumento VARCHAR(30) NOT NULL,
    Nombres VARCHAR(100) NOT NULL,
    Apellidos VARCHAR(100) NOT NULL,
    Telefono VARCHAR(20),
    Correo VARCHAR(150) NOT NULL UNIQUE,
    ContrasenaHash VARCHAR(255) NOT NULL,
    RolUsuario VARCHAR(20) NOT NULL,
    Activo BIT NOT NULL DEFAULT 1,

    CONSTRAINT UQ_Usuarios_Documento
        UNIQUE (TipoDocumento, NumeroDocumento),

    CONSTRAINT CK_Usuarios_Rol
        CHECK (RolUsuario IN ('Usuario', 'Administrador'))
);
GO

CREATE TABLE Equipos (
    IdEquipo INT IDENTITY(1,1) PRIMARY KEY,
    CodigoEquipo VARCHAR(30) NOT NULL UNIQUE,
    NombreEquipo VARCHAR(100) NOT NULL,
    Tipo VARCHAR(50),
    Marca VARCHAR(50),
    Descripcion VARCHAR(255),
    Ubicacion VARCHAR(100),
    Estado VARCHAR(30) NOT NULL
);
GO

CREATE TABLE Prestamos (
    IdPrestamo INT IDENTITY(1,1) PRIMARY KEY,
    IdUsuario INT NOT NULL,
    IdEquipo INT NOT NULL,
    IdAdministrador INT NOT NULL,
    FechaPrestamo DATETIME NOT NULL DEFAULT GETDATE(),
    FechaDevolucionPrevista DATETIME NOT NULL,
    Estado VARCHAR(30) NOT NULL,

    CONSTRAINT FK_Prestamos_Usuarios
        FOREIGN KEY (IdUsuario)
        REFERENCES Usuarios(IdUsuario),

    CONSTRAINT FK_Prestamos_Equipos
        FOREIGN KEY (IdEquipo)
        REFERENCES Equipos(IdEquipo),

    CONSTRAINT FK_Prestamos_Administrador
        FOREIGN KEY (IdAdministrador)
        REFERENCES Usuarios(IdUsuario),

    CONSTRAINT CK_Prestamos_Fechas
        CHECK (FechaDevolucionPrevista >= FechaPrestamo),

    CONSTRAINT CK_Prestamos_Usuarios
        CHECK (IdUsuario <> IdAdministrador)
);
GO

CREATE TABLE Devoluciones (
    IdDevolucion INT IDENTITY(1,1) PRIMARY KEY,
    IdPrestamo INT NOT NULL UNIQUE,
    FechaDevolucion DATETIME NOT NULL DEFAULT GETDATE(),
    EstadoEquipo VARCHAR(50) NOT NULL,
    Observaciones VARCHAR(500),

    CONSTRAINT FK_Devoluciones_Prestamos
        FOREIGN KEY (IdPrestamo)
        REFERENCES Prestamos(IdPrestamo)
);
GO

CREATE INDEX IX_Prestamos_IdUsuario
ON Prestamos(IdUsuario);
GO

CREATE INDEX IX_Prestamos_IdEquipo
ON Prestamos(IdEquipo);
GO

CREATE INDEX IX_Prestamos_IdAdministrador
ON Prestamos(IdAdministrador);
GO

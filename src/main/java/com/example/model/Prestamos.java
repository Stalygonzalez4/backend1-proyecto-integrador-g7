package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "Prestamos")
public class Prestamos {

    private Long id;
    private String idUsuario;
    private String idEquipo;
    private String rol;
    private Date fechaPrestamo;
    private Date fechaDevolucionPrevista;
    private String estado;

    public Prestamo() {
    }

    public Prestamo(String idUsuario, String idEquipo, String rol,
                    Date fechaPrestamo, Date fechaDevolucionPrevista, String estado) {
        this(null, idUsuario, idEquipo, rol, fechaPrestamo,
                fechaDevolucionPrevista, estado);
    }

    public Prestamo(Long id, String usuario, String equipo, String rol,
                    Date fechaPrestamo, Date fechaDevolucionPrevista, String estado) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idEquipo = idEquipo;
        this.rol = rol;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEequipo = idEquipo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Date getFechaDevolucionPrevista() {
        return fechaDevolucionPrevista;
    }

    public void setFechaDevolucionPrevista(Date fechaDevolucionPrevista) {
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Prestamo [id=" + id + ", usuario=" + usuario
                + ", equipo=" + equipo + ", estado=" + estado + "]";
    }
}

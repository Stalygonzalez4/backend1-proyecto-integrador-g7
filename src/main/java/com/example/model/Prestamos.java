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
    private Usuario usuario;
    private Equipo equipo;
    private Usuario administrador;
    private Date fechaPrestamo;
    private Date fechaDevolucionPrevista;
    private String estado;

    public Prestamo() {
    }

    public Prestamo(Usuario usuario, Equipo equipo, Usuario administrador,
                    Date fechaPrestamo, Date fechaDevolucionPrevista, String estado) {
        this(null, usuario, equipo, administrador, fechaPrestamo,
                fechaDevolucionPrevista, estado);
    }

    public Prestamo(Long id, Usuario usuario, Equipo equipo, Usuario administrador,
                    Date fechaPrestamo, Date fechaDevolucionPrevista, String estado) {
        this.id = id;
        this.usuario = usuario;
        this.equipo = equipo;
        this.administrador = administrador;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Usuario getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Usuario administrador) {
        this.administrador = administrador;
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

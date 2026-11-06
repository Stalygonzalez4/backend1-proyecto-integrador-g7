package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "equipos")
public class Equipos {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEquipos;
    private String codigoEquipos;
    private String nombreEquipos;
    private String categoria;
    private String Descripcion;
    private String Estado;
    private Double precio;
    private Integer stock;

    // ===================
    // Constructores
    // ===================
    public Equipos () {
    }

    public Equipos (String codigoEquipos, String nombreEquipos, String categoria, String descripcion, String estado, Double precio, Integer stock) {
        this(null, codigoEquipos, nombreEquipos, categoria, descripcion, estado, precio, stock);
    }

    public Equipos (Long idEquipos, String codigoEquipos, String nombreEquipos, String categoria, String descripcion, String estado, Double precio, Integer stock) {
        this.idEquipos = idEquipos;
        this.codigoEquipos = codigoEquipos;
        this.nombreEquipos = nombreEquipos;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.estado = estado;
        this.precio = precio;
        this.stock = stock;
    }

    // ===================
    // Getters y setters
    // ===================
    public Long getIdEquipos() {
        return idEquipos;
    }

    public void setIdEquipos(Long idEquipos) {
        this.idEquipos = id;
    }

    public String getCodigoEquipos() {
        return codigoEquipos;
    }

    public void setCodigoEquipos(String codigoEquipos) {
        this.codigoEquipos = codigoEquipos;
    }

    public String getNombreEquipos() {
        return nombreEquipos;
    }

    public void setNombreEquipos(String nombreEquipos) {
        this.nombreEquipos = nombreEquipos;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion () {
        return descripcion;
    }

    public void setDescripcion (String descripcion) {
        this.descripcion = descripcion;
    }

    public getEstado () {
        return estado;
    }

    public setEstado (String estado) {
        this.estado = estado;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

      @Override
    public String toString() {
        return "Producto [id=" + id + ", codigo=" + codigo + ", nombre=" + nombre + ",descripcion=" + descripcion + ",estado=" + estado + ", stock=" + stock + "]";
    }
}

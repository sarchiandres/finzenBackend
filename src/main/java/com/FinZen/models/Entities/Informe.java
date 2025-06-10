package com.FinZen.models.Entities;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="INFORNE")
public class Informe {
=======
import jakarta.persistence.*;

@Entity
@Table(name = "INFORME")
public class Informe {

>>>>>>> main
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_informe")
    private Long idInforme;
<<<<<<< HEAD
    

    @Column(name = "descripcion", length = 800)
    private String descripcion;


    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnore
    private Usuarios usuarios;
}
=======

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = true)
    private Usuarios usuario;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    // Constructores
    public Informe() {}

    public Informe(Usuarios usuario, String descripcion) {
        this.usuario = usuario;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Long getIdInforme() {
        return idInforme;
    }

    public void setIdInforme(Long idInforme) {
        this.idInforme = idInforme;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Informe{" +
                "idInforme=" + idInforme +
                ", usuario=" + (usuario != null ? usuario.getIdUsuario() : null) +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
>>>>>>> main

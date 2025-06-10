package com.FinZen.models.Entities;

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
@Table(name = "SOPORTE")
public class Soporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_soporte")    
    private Long idSoporte;

    @Column(name="pregunta")
    private String pregunta;

    @Column(name="respuesta")
    private String respuesta;

    @ManyToOne
    @JoinColumn(name="id_usuario")
    @JsonIgnore
    private Usuarios usuario;
    
}

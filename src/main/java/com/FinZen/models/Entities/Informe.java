package com.FinZen.models.Entities;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "INFORME")
@Data
public class Informe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_informe")
    private Long idInforme;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = true)
    private Usuarios usuario;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    

    
}


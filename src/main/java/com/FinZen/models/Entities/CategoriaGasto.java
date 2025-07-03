package com.FinZen.models.Entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "gastocategoria")
public class CategoriaGasto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria;


    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;


    
    @OneToMany(mappedBy = "categoria" ,cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Gastos> gastos = new ArrayList<>();
}

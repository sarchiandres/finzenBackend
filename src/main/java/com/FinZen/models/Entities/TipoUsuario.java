package com.FinZen.models.Entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "TIPOUSUARIO")
@Data
public class TipoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipousuario")
    private Long idTipoUsuario;

    @Column(name = "nombre", length = 40, nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "tipoUsuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Usuarios> usuarios = new ArrayList<>();
    
}

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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "USUARIOS")
@Data
public class Usuarios {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "correo", length = 150, nullable = false, unique = true)
    private String correo;

    @Column(name = "contrasena", length = 255, nullable = false)
    private String contrasena;

    @Column(name = "numero_documento", nullable = false)
    private Long numeroDocumento;

    @Column(name = "pais_residencia", length = 250)
    private String paisResidencia;

    @Column(name = "ingreso_mensual", nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long ingresoMensual;

    @Column(name = "meta_actual", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean metaActual;

    @Column(name = "nombre_usuario", length = 50)
    private String nombreUsuario;

 
    @Column(name = "tipo_documento", columnDefinition = "ENUM('CEDULA','PASAPORTE', 'TARJETA_DE_IDENTIDAD', 'CEDULA_EXTRANJERA') DEFAULT 'cedula'")
    private String tipoDocumento;

    @Column(name = "tipo_persona", columnDefinition = "ENUM('padre_de_familia', 'joven_profesional', 'jubilado', 'personalizado','emprendedor')")
    private String tipoPersona;

    @Column(name = "url_img", length = 200)
    private String urlImg;

    @ManyToOne
    @JoinColumn(name = "id_tipousuario", nullable = false)
    @JsonIgnore
    private TipoUsuario tipoUsuario;

    @OneToMany(mappedBy = "usuarios", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Cuenta> cuentas = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<PeriodoMesual> periodos = new ArrayList<>();

   @OneToMany(mappedBy = "usuario",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Soporte> soportes = new ArrayList<>();
}

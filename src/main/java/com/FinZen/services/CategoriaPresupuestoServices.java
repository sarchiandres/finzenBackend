package com.FinZen.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinZen.models.DTOS.CategoriaPresupuestoDto;
import com.FinZen.models.Entities.CategoriaPresupuesto;
import com.FinZen.repository.CategoriaPresupuestoRepository;

@Service
public class CategoriaPresupuestoServices {
    @Autowired
    private CategoriaPresupuestoRepository categoriaPresupuestoRepository;

    public CategoriaPresupuesto createCategoria(CategoriaPresupuestoDto categoria){
        if (categoriaPresupuestoRepository.findByNombre(categoria.getNombre()).isPresent()) {
            throw new RuntimeException("La categoria " + categoria.getNombre() + " ya existe");
        }
        CategoriaPresupuesto categoriaPresupuesto = new CategoriaPresupuesto();
        categoriaPresupuesto.setNombre(categoria.getNombre());


        return categoriaPresupuestoRepository.save(categoriaPresupuesto);

    }

    public CategoriaPresupuesto updateCategoria(Long idCategoria, CategoriaPresupuestoDto categoria){
        CategoriaPresupuesto categoriaPresupuesto = categoriaPresupuestoRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("No se encontró la categoria con ID: " + idCategoria));

        if (categoria.getNombre() != null) {
            categoriaPresupuesto.setNombre(categoria.getNombre());
        }

        return categoriaPresupuestoRepository.save(categoriaPresupuesto);
    }

    public String deleteCategoria(Long idCategoria){
        CategoriaPresupuesto categoriaPresupuesto = categoriaPresupuestoRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("No se encontró la categoria con ID: " + idCategoria));

        categoriaPresupuestoRepository.delete(categoriaPresupuesto);
        return "Categoria eliminada";
    }

    public List<CategoriaPresupuesto> getAllCategorias(){
        return categoriaPresupuestoRepository.findAll();
    }
    
    
}

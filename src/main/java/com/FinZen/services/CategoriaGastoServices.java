package com.FinZen.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinZen.models.Entities.CategoriaGasto;
import com.FinZen.repository.CategoriaGastoRepository;

@Service
public class CategoriaGastoServices {

    @Autowired
    private CategoriaGastoRepository categoriaGastoRepository;


    public CategoriaGasto createCategoriaGasto(CategoriaGasto categoriaGasto) {
        if (categoriaGastoRepository.findByNombre(categoriaGasto.getNombre()).isPresent()) {
            throw new RuntimeException("La categoria " + categoriaGasto.getNombre() + " ya existe");
        }
        return categoriaGastoRepository.save(categoriaGasto);
    }

    public CategoriaGasto updateCategoriaGasto(Long idCategoria, CategoriaGasto categoriaGasto) {
        CategoriaGasto existingCategoria = categoriaGastoRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("No se encontró la categoría de gasto con ID: " + idCategoria));
        existingCategoria.setNombre(categoriaGasto.getNombre());

        return categoriaGastoRepository.save(existingCategoria);
    }
    public String  deleteCategoriaGasto(Long idCategoria) {
        try {
            CategoriaGasto categoriaGasto = categoriaGastoRepository.findById(idCategoria)
                    .orElseThrow(() -> new RuntimeException("No se encontró la categoría de gasto con ID: " + idCategoria));
            categoriaGastoRepository.delete(categoriaGasto);
            return "La categoria de gasto fue eliminada correctamente";
        } catch (Exception e) {
            return "Error al eliminar la categoria de gasto: " + e.getMessage();
        }
    }

    public List<CategoriaGasto> getByIdusario() {
        return categoriaGastoRepository.findAll();

    }
}

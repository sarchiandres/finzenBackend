package com.FinZen.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.FinZen.models.DTOS.SoporteDto;
import com.FinZen.models.Entities.Soporte;
import com.FinZen.models.Entities.Usuarios;
import com.FinZen.repository.SoporteRepository;
import com.FinZen.repository.UsuariosRepository;

@Service
public class SoporteServices {
    @Autowired
    private SoporteRepository soporteRepository;
    @Autowired
    private UsuariosRepository usuarioRepository;


    public Soporte createSoporte(SoporteDto soporteDto) {
        if (soporteRepository.findByPregunta(soporteDto.getPregunta()).isPresent()) {
            throw new RuntimeException("Soporte con la misma pregunta ya existe");
        }
        Usuarios usuario = usuarioRepository.findById(soporteDto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Soporte soporte = new Soporte();
        soporte.setPregunta(soporteDto.getPregunta());
        soporte.setRespuesta(soporteDto.getRespuesta());
        soporte.setUsuario(usuario);

        return soporteRepository.save(soporte);
    }


    public Soporte updateSoporte(Long idSoporte, SoporteDto soporteDto) {
        Soporte soporte = soporteRepository.findById(idSoporte)
                .orElseThrow(() -> new RuntimeException("Soporte no encontrado"));

        soporte.setPregunta(soporteDto.getPregunta());
        soporte.setRespuesta(soporteDto.getRespuesta());

        return soporteRepository.save(soporte);
    }

    public List<Soporte> getSoportesByUsuario() {
        return soporteRepository.findAll();
    }

    public String deleteSoporte(Long idSoporte) {
        Soporte soporte = soporteRepository.findById(idSoporte)
                .orElseThrow(() -> new RuntimeException("Soporte no encontrado"));
        soporteRepository.delete(soporte);
        return "Soporte eliminado correctamente";
    }
}

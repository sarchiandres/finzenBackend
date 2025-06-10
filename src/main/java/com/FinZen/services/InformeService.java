package com.FinZen.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import com.FinZen.models.DTOS.InformeDto;
import com.FinZen.models.Entities.Informe;
import com.FinZen.models.Entities.Usuarios;
import com.FinZen.repository.InformeRepository;
import com.FinZen.repository.UsuariosRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.Transactional;

@Service
public class InformeService {

    @Autowired
    private InformeRepository informeRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public InformeDto generarInformeCompleto(Long usuarioId) {
        Usuarios usuario = usuariosRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("generar_informe_completo");
            query.registerStoredProcedureParameter("p_id_usuario", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_informe", Long.class, ParameterMode.OUT);
            query.setParameter("p_id_usuario", usuarioId);
            query.execute();

            Long idInforme = (Long) query.getOutputParameterValue("p_id_informe");
            Informe informe = informeRepository.findById(idInforme)
                    .orElseThrow(() -> new RuntimeException("No se pudo generar el informe"));
            return toInformeDto(informe);
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el informe: " + e.getMessage(), e);
        }
    }

    public List<InformeDto> getInformesByUsuarioId(Long usuarioId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long authUserId = getUserIdFromAuth(auth);
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMINISTRADOR"));

        if (!isAdmin && !authUserId.equals(usuarioId)) {
            throw new RuntimeException("Acceso denegado: No puedes ver informes de otros usuarios");
        }

        usuariosRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return informeRepository.findByUsuarioIdOrderByIdInformeDesc(usuarioId)
                .stream()
                .map(this::toInformeDto)
                .collect(Collectors.toList());
    }

    public InformeDto getInformeByIdAndUsuario(Long informeId, Long usuarioId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long authUserId = getUserIdFromAuth(auth);
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMINISTRADOR"));

        if (!isAdmin && !authUserId.equals(usuarioId)) {
            throw new RuntimeException("Acceso denegado: No puedes ver informes de otros usuarios");
        }

        Informe informe = informeRepository.findByIdInformeAndUsuarioId(informeId, usuarioId)
                .orElseThrow(() -> new RuntimeException("Informe no encontrado o no pertenece al usuario"));
        return toInformeDto(informe);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public InformeDto getInformeByIdForAdmin(Long informeId) {
        Informe informe = informeRepository.findById(informeId)
                .orElseThrow(() -> new RuntimeException("Informe no encontrado"));
        return toInformeDto(informe);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<InformeDto> getAllInformesForAdmin() {
        return informeRepository.findAll()
                .stream()
                .map(this::toInformeDto)
                .collect(Collectors.toList());
    }

    public String deleteInformeByIdAndUsuario(Long informeId, Long usuarioId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long authUserId = getUserIdFromAuth(auth);
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMINISTRADOR"));

        if (!isAdmin && !authUserId.equals(usuarioId)) {
            throw new RuntimeException("Acceso denegado: No puedes eliminar informes de otros usuarios");
        }

        Informe informe = informeRepository.findByIdInformeAndUsuarioId(informeId, usuarioId)
                .orElseThrow(() -> new RuntimeException("Informe no encontrado o no pertenece al usuario"));
        informeRepository.delete(informe);
        return "Informe eliminado correctamente";
    }

    public String deleteAllInformesByUsuario(Long usuarioId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long authUserId = getUserIdFromAuth(auth);
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMINISTRADOR"));

        if (!isAdmin && !authUserId.equals(usuarioId)) {
            throw new RuntimeException("Acceso denegado: No puedes eliminar informes de otros usuarios");
        }

        usuariosRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        List<Informe> informes = informeRepository.findByUsuarioId(usuarioId);
        if (informes.isEmpty()) {
            return "No hay informes para eliminar";
        }
        informeRepository.deleteAll(informes);
        return "Todos los informes del usuario han sido eliminados correctamente";
    }

    public InformeDto getInformeMasReciente(Long usuarioId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long authUserId = getUserIdFromAuth(auth);
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMINISTRADOR"));

        if (!isAdmin && !authUserId.equals(usuarioId)) {
            throw new RuntimeException("Acceso denegado: No puedes ver informes de otros usuarios");
        }

        List<InformeDto> informes = getInformesByUsuarioId(usuarioId);
        if (informes.isEmpty()) {
            throw new RuntimeException("No se encontraron informes para el usuario");
        }
        return informes.get(0);
    }

    private InformeDto toInformeDto(Informe informe) {
        InformeDto dto = new InformeDto();
        dto.setIdInforme(informe.getIdInforme());
        dto.setIdUsuario(informe.getUsuario() != null ? informe.getUsuario().getIdUsuario() : null);
        dto.setDescripcion(informe.getDescripcion());
        return dto;
    }

    private Long getUserIdFromAuth(Authentication auth) {
        String userIdStr = auth.getName();
        try {
            return Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            throw new RuntimeException("ID de usuario inválido en el token");
        }
    }
}
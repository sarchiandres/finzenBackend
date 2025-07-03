package com.FinZen.services; // Asegúrate de que el paquete sea 'service' si lo cambiaste

import com.FinZen.models.DTOS.InformeDto;
import com.FinZen.models.Entities.Informe;
import com.FinZen.models.Entities.Usuarios;
import com.FinZen.repository.InformeRepository;
import com.FinZen.repository.UsuariosRepository;
// Ya no necesitamos StructuredReportDataDto si no lo vamos a parsear
// import com.FinZen.models.DTOS.StructuredReportDataDto; 
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
// Ya no necesitamos BigDecimal ni List/Map si no parseamos a StructuredReportDataDto
// import java.math.BigDecimal;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.regex.Matcher;
// import java.util.regex.Pattern;
import java.time.LocalDate;
import java.util.Optional;

// Importaciones para Thymeleaf
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class InformeService {

    @Autowired
    private InformeRepository informeRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TemplateEngine templateEngine;

    // Puedes mantener convertToDto si lo usas en otros lugares, si no, puedes eliminarlo.
    private InformeDto convertToDto(Informe informe) {
        if (informe == null) {
            return null;
        }
        InformeDto dto = new InformeDto();
        dto.setIdInforme(informe.getIdInforme());
        if (informe.getUsuario() != null) {
            dto.setIdUsuario(informe.getUsuario().getIdUsuario());
            dto.setNombreUsuario(informe.getUsuario().getNombre());
        }
        dto.setFechaGeneracion(informe.getFechaGeneracion());
        dto.setDescripcion(informe.getDescripcion());
        dto.setTipoInforme(informe.getTipoInforme());
        return dto;
    }

    @Transactional
    public Optional<byte[]> generarYObtenerInformePdf(Long usuarioId) {
        Optional<Usuarios> usuarioOpt = usuariosRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            System.err.println("Usuario con ID " + usuarioId + " no encontrado. No se puede generar el informe.");
            return Optional.empty();
        }

        Long informeGeneradoId = null;
        String contenidoInformeRaw = null; // Para almacenar el contenido RAW del SP

        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("generar_informe_completo");

            query.registerStoredProcedureParameter("p_id_usuario", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_id_informe", Long.class, ParameterMode.OUT);

            query.setParameter("p_id_usuario", usuarioId);

            query.execute();

            informeGeneradoId = (Long) query.getOutputParameterValue("p_id_informe");

        } catch (PersistenceException e) {
            System.err.println("Error de persistencia al llamar al procedimiento almacenado: " + e.getMessage());
            e.printStackTrace(); // Imprimir stack trace para depuración
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error inesperado al generar el informe: " + e.getMessage());
            e.printStackTrace(); // Imprimir stack trace para depuración
            return Optional.empty();
        }

        if (informeGeneradoId != null) {
            Optional<Informe> informeEntityOpt = informeRepository.findById(informeGeneradoId);
            if (informeEntityOpt.isPresent()) {
                contenidoInformeRaw = informeEntityOpt.get().getDescripcion();
                
                // --- PROCESAMIENTO DE LA PLANTILLA HTML Y GENERACIÓN DE PDF ---
                try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                    // 1. Crear el contexto de Thymeleaf y añadir los datos
                    Context context = new Context();
                    
                    // Pasamos el contenido RAW directamente
                    context.setVariable("rawReportContent", contenidoInformeRaw);
                    
                    // También podemos pasar algunos datos del usuario si se necesitan para el encabezado/pie de página
                    context.setVariable("userName", usuarioOpt.get().getNombre());
                    context.setVariable("userEmail", usuarioOpt.get().getCorreo()); // Asumiendo que Usuarios tiene getCorreo()
                    context.setVariable("generationDate", LocalDate.now().toString());

                    // 2. Procesar la plantilla HTML con Thymeleaf
                    String renderedHtmlContent = templateEngine.process("informe_template", context);

                    // 3. Convertir el HTML renderizado a PDF usando OpenHtmlToPdf
                    PdfRendererBuilder builder = new PdfRendererBuilder();
                    builder.withHtmlContent(renderedHtmlContent, null);
                    builder.toStream(os);
                    builder.run();

                    return Optional.of(os.toByteArray());

                } catch (IOException e) {
                    System.err.println("Error de IO al generar el PDF: " + e.getMessage());
                    e.printStackTrace();
                    return Optional.empty();
                } catch (Exception e) {
                    System.err.println("Error al generar el PDF desde HTML: " + e.getMessage());
                    e.printStackTrace();
                    return Optional.empty();
                }
            }
        }
        System.err.println("Informe con ID " + informeGeneradoId + " no encontrado después de la generación del SP, o SP no devolvió ID.");
        return Optional.empty();
    }

    // Eliminamos el método parseContenidoInforme, ya no es necesario
    /*
    private StructuredReportDataDto parseContenidoInforme(String rawContent, Usuarios usuario) {
        // ... (lógica de parseo que ya no se usará)
        return null;
    }
    */
}
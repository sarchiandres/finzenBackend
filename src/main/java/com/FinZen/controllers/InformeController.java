package com.FinZen.controllers;

import java.time.LocalDate; // Importar LocalDate para el nombre del archivo PDF
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException; // Ya no necesario si no se usa directamente
import org.springframework.web.bind.annotation.GetMapping; // Cambiado a GetMapping
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinZen.security.Jwt.JwtUtils;
import com.FinZen.services.InformeService; // Asegúrate de que el paquete sea 'service' si lo cambiaste
                                         // En tu código anterior era 'services', lo he corregido a 'service'
                                         // para que coincida con el ejemplo anterior

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/finzen/informes")
public class InformeController {

    @Autowired
    private InformeService informeService;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Obtiene el ID del usuario autenticado a partir del token JWT en la solicitud.
     * Lanza AccessDeniedException si el token es inválido o no está presente.
     *
     * @param request La solicitud HTTP actual.
     * @return El ID (Long) del usuario autenticado.
     * @throws AccessDeniedException Si el token JWT no es válido o no se puede extraer el ID.
     */
    private Long getAuthenticatedUserId(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromRequest(request); // Asume que getJwtFromRequest extrae "Bearer token"
                                                        // o solo el token si lo envías directamente.
        if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
            throw new AccessDeniedException("Token inválido o no proporcionado.");
        }
        return jwtUtils.getUserIdFromJwtToken(jwt); // Asume que getUserIdFromJwtToken devuelve Long
    }

    // Cambiado a @GetMapping ya que generar un informe típicamente no tiene efectos secundarios
    // significativos para ser un POST y la obtención de datos suele ser GET.
    // También se eliminó @PreAuthorize según tu solicitud.
    @GetMapping("/generar")
    public ResponseEntity<?> generarInforme(HttpServletRequest request) {
        try {
            Long userId = getAuthenticatedUserId(request);

            Optional<byte[]> pdfBytesOpt = informeService.generarYObtenerInformePdf(userId);

            if (pdfBytesOpt.isPresent()) {
                byte[] pdfBytes = pdfBytesOpt.get();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                // Nombre de archivo más descriptivo, incluyendo la fecha y el ID del usuario
                String filename = "informe_financiero_" + LocalDate.now() + "_user_" + userId + ".pdf";
                headers.setContentDispositionFormData("attachment", filename);
                headers.setContentLength(pdfBytes.length);

                return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            } else {
                // Si el servicio devuelve Optional.empty(), significa que no se pudo generar por alguna razón.
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se pudo generar o encontrar el informe PDF para el usuario con ID: " + userId + ". Verifique si el usuario existe o si la operación fue exitosa.");
            }
        } catch (AccessDeniedException e) {
            // Captura la excepción de autenticación/autorización de getAuthenticatedUserId
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            // Log del error para depuración
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al generar el informe: " + e.getMessage());
        }
    }
}
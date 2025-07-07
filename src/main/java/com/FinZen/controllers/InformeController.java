package com.FinZen.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException; // Importar java.io.IOException
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinZen.security.Jwt.JwtUtils;
import com.FinZen.services.InformeService;
import com.FinZen.services.ReportExcelService; // Asegúrate de que el paquete sea 'services'

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/finzen/informes")
public class InformeController {

    @Autowired
    private InformeService informeService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ReportExcelService reportExcelService;

    /**
     * Obtiene el ID del usuario autenticado a partir del token JWT en la solicitud.
     * Lanza AccessDeniedException si el token es inválido o no está presente.
     *
     * @param request La solicitud HTTP actual.
     * @return El ID (Long) del usuario autenticado.
     * @throws AccessDeniedException Si el token JWT no es válido o no se puede extraer el ID.
     */
    private Long getAuthenticatedUserId(HttpServletRequest request) {
        String jwt = jwtUtils.getJwtFromRequest(request);
        if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
            throw new AccessDeniedException("Token inválido o no proporcionado.");
        }
        return jwtUtils.getUserIdFromJwtToken(jwt);
    }

    @GetMapping("/generar")
    public ResponseEntity<?> generarInforme(HttpServletRequest request) {
        try {
            Long userId = getAuthenticatedUserId(request);

            Optional<byte[]> pdfBytesOpt = informeService.generarYObtenerInformePdf(userId);

            if (pdfBytesOpt.isPresent()) {
                byte[] pdfBytes = pdfBytesOpt.get();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                String filename = "informe_financiero_" + LocalDate.now() + "_user_" + userId + ".pdf";
                headers.setContentDispositionFormData("attachment", filename);
                headers.setContentLength(pdfBytes.length);

                return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se pudo generar o encontrar el informe PDF para el usuario con ID: " + userId + ". Verifique si el usuario existe o si la operación fue exitosa.");
            }
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al generar el informe: " + e.getMessage());
        }
    }

    @GetMapping("/excel-informe-completo") // Cambiado el endpoint para reflejar el informe completo
    public ResponseEntity<?> downloadFullFinancialReportExcel(HttpServletRequest request) {

        String token = jwtUtils.getJwtFromRequest(request);

        if (token == null || !jwtUtils.validateJwtToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token inválido o no proporcionado."));
        }

        try {
            Long userId = jwtUtils.getUserIdFromJwtToken(token);
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No se pudo obtener el ID de usuario del token."));
            }

            // **CORRECCIÓN AQUÍ: Llamar al método correcto del servicio de Excel**
            // generateFullReportExcel es el nombre del método en ReportExcelService que genera el informe completo
            ByteArrayInputStream excelStream = reportExcelService.generateFullReportExcel(userId);

            String filename = String.format("informe_financiero_completo_usuario_%d_%s.xlsx", userId, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
            headers.add(HttpHeaders.PRAGMA, "no-cache");
            headers.add(HttpHeaders.EXPIRES, "0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(excelStream.available())
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(new ByteArrayResource(excelStream.readAllBytes()));

        } catch (AccessDeniedException e) { // Cambiado de SecurityException a AccessDeniedException para consistencia
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        } catch (IOException e) { // Cambiado de io.jsonwebtoken.io.IOException a java.io.IOException
            System.err.println("Error al generar el archivo Excel: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno al generar el archivo Excel: " + e.getMessage()));
        } catch (Exception e) {
            System.err.println("Error inesperado al generar el informe general de finanzas:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor al generar el informe."));
        }
    }
}
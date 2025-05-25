package com.FinZen.controllers;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FinZen.models.Entities.PasswordResetToken;
import com.FinZen.models.Entities.TipoUsuario;
import com.FinZen.models.Entities.Usuarios;
import com.FinZen.payload.ForgotPasswordRequest;
import com.FinZen.payload.LoginRequest;
import com.FinZen.payload.ResetPasswordRequest;
import com.FinZen.payload.SignupRequest;
import com.FinZen.payload.response.JwtResponse;
import com.FinZen.payload.response.MessageResponse;
import com.FinZen.repository.PasswordResetTokenRepository;
import com.FinZen.repository.TipoUsuarioRepository;
import com.FinZen.repository.UsuariosRepository;
import com.FinZen.security.Jwt.JwtUtils;
import com.FinZen.services.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/finzen/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private  UsuariosRepository   usuarioRepository;
    @Autowired
    private TipoUsuarioRepository tipoUsuarioRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private EmailService emailService;
  

     @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getCorreo(), loginRequest.getContrasena()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuarios usuario = usuarioRepository.findByCorreo(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String role = usuario.getTipoUsuario().getNombre();

        // Enviar correo de confirmación de inicio de sesión
        try {
            emailService.sendLoginNotification(usuario.getCorreo(), usuario.getNombre());


            System.out.println("Correo de notificación de inicio de sesión enviado a: " + usuario.getCorreo());
        } catch (Exception e) {
            System.err.println("Error al enviar correo de notificación: " + e.getMessage());
            // No detener el proceso de login si el correo falla
        }

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                usuario.getIdUsuario(),
                usuario.getNombreUsuario(),
                usuario.getCorreo(),
                role));
    }


     @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        // Depuración avanzada del objeto recibido
        System.out.println("Contenido completo de SignupRequest:");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println(objectMapper.writeValueAsString(signUpRequest));
        } catch (Exception e) {
            System.out.println("No se pudo serializar el objeto: " + e.getMessage());
        }

        // Depuración específica del campo role
        System.out.println("Valor de role: " + signUpRequest.getRole());

        // Log the incoming request
        System.out.println("Signup Request: " + signUpRequest.toString());

        // Validate existing user
        if (usuarioRepository.existsByNombreUsuario(signUpRequest.getNombreUsuario())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: El nombre de usuario ya está en uso!"));
        }
        if (usuarioRepository.existsByCorreo(signUpRequest.getCorreo())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: El correo ya está en uso!"));
        }
        if (usuarioRepository.findByNumeroDocumento(signUpRequest.getNumeroDocumento()).isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: El número de documento ya está en uso!"));
        }

        // Validate role (con mejor log para debugging)
        String role = signUpRequest.getRole() != null ? signUpRequest.getRole().toUpperCase() : "USUARIO";
        System.out.println("Requested role: " + role + " (original value: " + signUpRequest.getRole() + ")");

        if (!Arrays.asList("USUARIO", "ADMIN").contains(role)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Rol inválido. Debe ser 'USUARIO' o 'ADMIN'"));
        }

        // Verificar los roles existentes en la base de datos
        List<TipoUsuario> tiposExistentes = tipoUsuarioRepository.findAll();
        System.out.println("Roles existentes en la base de datos:");
        for (TipoUsuario tipo : tiposExistentes) {
            System.out.println("ID: " + tipo.getIdTipoUsuario() + ", Nombre: " + tipo.getNombre());
        }

        // Create user
        Usuarios usuario = new Usuarios();
        usuario.setNombre(signUpRequest.getNombre());
        usuario.setCorreo(signUpRequest.getCorreo());
        usuario.setContrasena(encoder.encode(signUpRequest.getContrasena()));
        usuario.setNumeroDocumento(signUpRequest.getNumeroDocumento());
        usuario.setNombreUsuario(signUpRequest.getNombreUsuario());
        usuario.setPaisResidencia(signUpRequest.getPaisResidencia());
        usuario.setIngresoMensual(signUpRequest.getIngresoMensual() != null ? signUpRequest.getIngresoMensual() : 0L);
        usuario.setMetaActual(signUpRequest.getMetaActual() != null ? signUpRequest.getMetaActual() : true);

        // Set tipoDocumento
        try {
            usuario.setTipoDocumento(signUpRequest.getTipoDocumento());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Tipo de documento inválido"));
        }

        // Set tipoPersona
        if (signUpRequest.getTipoPersona() != null) {
            try {
                usuario.setTipoPersona(signUpRequest.getTipoPersona());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Tipo de persona inválido"));
            }
        
        }
        // Set tipoUsuario based on role
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findByNombre(role)
                .orElseGet(() -> {
                    System.out.println("Role '" + role + "' not found, creating it");
                    TipoUsuario newRole = new TipoUsuario();
                    newRole.setNombre(role);
                    return tipoUsuarioRepository.save(newRole);
                });

        System.out.println("Assigned tipoUsuario: " + tipoUsuario.getNombre() + " (ID: " + tipoUsuario.getIdTipoUsuario() + ")");
        usuario.setTipoUsuario(tipoUsuario);

        Usuarios savedUsuario = usuarioRepository.save(usuario);
        System.out.println("Usuario guardado con ID: " + savedUsuario.getIdUsuario() + " y rol: " + savedUsuario.getTipoUsuario().getNombre());

        // Construir una respuesta más detallada para depuración
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("id", savedUsuario.getIdUsuario());
        responseData.put("nombre", savedUsuario.getNombre());
        responseData.put("correo", savedUsuario.getCorreo());
        responseData.put("nombreUsuario", savedUsuario.getNombreUsuario());
        responseData.put("tipoUsuarioId", savedUsuario.getTipoUsuario().getIdTipoUsuario());
        responseData.put("tipoUsuarioNombre", savedUsuario.getTipoUsuario().getNombre());

        // Devolver una respuesta más detallada
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Usuario registrado con éxito!");
        response.put("data", responseData);

        return ResponseEntity.ok(response);
         
    }


    // Método para comprobar los roles existentes - útil para debug
    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(tipoUsuarioRepository.findAll());
    }

    /**
     * Endpoint para solicitar restablecimiento de contraseña
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        Optional<Usuarios> userOptional = usuarioRepository.findByCorreo(request.getCorreo());

        if (!userOptional.isPresent()) {
            // No revelamos si el correo existe en la base de datos por seguridad
            return ResponseEntity.ok(new MessageResponse("Si el correo existe en nuestra base de datos, " +
                    "recibirás instrucciones para restablecer tu contraseña."));
        }

        Usuarios usuario = userOptional.get();

        // Eliminar tokens existentes para este usuario
        passwordResetTokenRepository.findByUsuario(usuario).ifPresent(token ->
                passwordResetTokenRepository.delete(token)
        );

        // Generar un token de 6 dígitos
        String token = String.format("%06d", new java.util.Random().nextInt(999999));

        // Guardar el token en la base de datos
        PasswordResetToken resetToken = new PasswordResetToken(token, usuario);
        passwordResetTokenRepository.save(resetToken);

        // Enviar correo con el token
        try {
            emailService.sendPasswordResetToken(usuario.getCorreo(), usuario.getNombre(), token);
            System.out.println("Correo de restablecimiento enviado a: " + usuario.getCorreo());
        } catch (Exception e) {
            System.err.println("Error al enviar correo de restablecimiento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error al enviar el correo. Por favor intente más tarde."));
        }

        return ResponseEntity.ok(new MessageResponse("Se ha enviado un correo con instrucciones para restablecer tu contraseña."));
    }

    /**
     * Endpoint para restablecer la contraseña usando el token
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByToken(request.getToken());

        if (!tokenOptional.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Token inválido."));
        }

        PasswordResetToken resetToken = tokenOptional.get();

        if (resetToken.isExpired()) {
            passwordResetTokenRepository.delete(resetToken);
            return ResponseEntity.badRequest().body(new MessageResponse("El token ha expirado."));
        }

        Usuarios usuario = resetToken.getUsuario();
        usuario.setContrasena(encoder.encode(request.getNuevaContrasena()));
        usuarioRepository.save(usuario);

        // Eliminar el token después de usarlo
        passwordResetTokenRepository.delete(resetToken);

        return ResponseEntity.ok(new MessageResponse("Contraseña restablecida con éxito."));
    }
    
}

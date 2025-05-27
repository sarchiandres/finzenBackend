package com.FinZen.controllers;



import java.util.HashMap;

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
import com.FinZen.services.UsuariosServices;
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
    @Autowired
    private UsuariosServices usuarioService;
  

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
        try {
            Map<String, Object> responseData = usuarioService.saveUsuario(signUpRequest);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Usuario registrado con éxito!");
            response.put("data", responseData);
            emailService.sendRegister(signUpRequest.getCorreo(), signUpRequest.getNombre());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error interno del servidor: " + e.getMessage()));
        }


        
         
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

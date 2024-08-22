package com.example.democrud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        ex.printStackTrace(); // Imprimer l'erreur dans la console

        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        // Gestion des exceptions courantes avec conditions précises
        if (ex instanceof BadCredentialsException) {
            response.put("message", "Identifiant ou mot de passe incorrect.");
            status = HttpStatus.UNAUTHORIZED; // 401
        } else if (ex instanceof AccessDeniedException) {
            // On peut préciser les différents types d'accès refusé
            if (ex.getMessage().contains("Insufficient scope")) {
                response.put("message", "Droits insuffisants pour accéder à cette ressource.");
            } else {
                response.put("message", "Accès refusé.");
            }
            status = HttpStatus.FORBIDDEN; // 403
        } else if (ex instanceof UsernameNotFoundException) {
            response.put("message", "Utilisateur non trouvé.");
            status = HttpStatus.NOT_FOUND; // 404
        } else if (ex instanceof MethodArgumentNotValidException) {
            response.put("message", "Erreur de validation des données.");
            status = HttpStatus.BAD_REQUEST; // 400

            // Détails des erreurs de validation
            Map<String, String> validationErrors = new HashMap<>();
            ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors()
                    .forEach(error -> validationErrors.put(error.getField(), error.getDefaultMessage()));
            response.put("erreurs", validationErrors);
        } else if (ex instanceof IllegalArgumentException) {
            response.put("message", "Paramètre illégal : " + ex.getMessage());
            status = HttpStatus.BAD_REQUEST; // 400
        } else if (ex.getMessage().contains("TokenExpiredException")) {
            response.put("message", "Votre session a expiré, veuillez vous reconnecter.");
            status = HttpStatus.FORBIDDEN; // 403
        } else if (ex.getMessage().contains("TokenSignatureException")) {
            response.put("message", "Signature de token invalide.");
            status = HttpStatus.UNAUTHORIZED; // 401
        } else {
            response.put("message", "Une erreur inattendue est survenue.");
        }

        response.put("status", status.value());
        response.put("erreur", status.getReasonPhrase());
        response.put("timestamp", System.currentTimeMillis());
        response.put("path", request.getDescription(false).substring(4));

        return new ResponseEntity<>(response, status);
    }
}

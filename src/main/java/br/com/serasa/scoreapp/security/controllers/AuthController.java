package br.com.serasa.scoreapp.security.controllers;

import br.com.serasa.scoreapp.security.domain.User;
import br.com.serasa.scoreapp.security.dto.LogonDto;
import br.com.serasa.scoreapp.security.dto.RegistroDto;
import br.com.serasa.scoreapp.security.jwt.JWTUtil;
import br.com.serasa.scoreapp.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/v1")
public class AuthController {


    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/logon")
    public ResponseEntity<?> logon(@Valid @RequestBody LogonDto loginDto){
        try {

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getSenha());
            authManager.authenticate(authenticationToken);
            String token = jwtUtil.generateToken(loginDto.getEmail());
            Map<String, String> response = Collections.singletonMap("jwt-token", token);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException ex){
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroDto registrarDto){
        User user = new User();
        user.setEmail(registrarDto.getEmail());
        user.setPassword(registrarDto.getSenha());
        try {
           User response = userRepository.save(user);
           return ResponseEntity.ok(response);
        } catch(ResponseStatusException ex){
            return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
        }
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}

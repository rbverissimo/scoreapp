package br.com.serasa.scoreapp.security.controllers;

import br.com.serasa.scoreapp.dto.MensagensResponseDto;
import br.com.serasa.scoreapp.security.domain.User;
import br.com.serasa.scoreapp.security.dto.LogonDto;
import br.com.serasa.scoreapp.security.dto.RegistroDto;
import br.com.serasa.scoreapp.security.jwt.JWTUtil;
import br.com.serasa.scoreapp.security.services.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/v1/logon")
    @ApiOperation(value = "Fazer o logon no sistema", notes = "Retorna um JWT ao autenticar o usuário",
            consumes = "application/json", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Acesso garantido. Token gerado"),
            @ApiResponse(code = 401, message = "Credenciais inválidas")
    })
    public ResponseEntity<?> logon(@Valid @RequestBody LogonDto loginDto){
        try {

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getSenha());
            authManager.authenticate(authenticationToken);
            String token = jwtUtil.generateToken(loginDto.getEmail());
            Map<String, String> response = Collections.singletonMap("jwt-token", token);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (BadCredentialsException ex){
            return ResponseEntity.status(401).body(new MensagensResponseDto(Arrays.asList("Credenciais inválidas")));
        }
    }

    @PostMapping("/v1/registrar")
    @ApiOperation(value = "Cadastrar usuário", produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuário cadastrado"),
            @ApiResponse(code = 400, message = "Requisição rejeitada"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroDto registrarDto){
        User user = new User();
        user.setName(registrarDto.getNomeUsuario());
        user.setEmail(registrarDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(registrarDto.getSenha()));
        user.setRoles(new HashSet<>(){{ add(userService.getRoleByNome("USER")
                .orElseThrow(() -> new RuntimeException("Role USER não encontrado.")));}});

        try {
           User response = userService.save(user);
           return ResponseEntity.status(HttpStatus.CREATED).body(response);
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

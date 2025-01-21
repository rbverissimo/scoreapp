package br.com.serasa.scoreapp.controllers;

import br.com.serasa.scoreapp.client.ViaCepWebService;
import br.com.serasa.scoreapp.domain.Pessoa;
import br.com.serasa.scoreapp.dto.EnderecoViaCepResponseDto;
import br.com.serasa.scoreapp.dto.PessoaDto;
import br.com.serasa.scoreapp.services.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/pessoas")
public class PessoaController {

    @Autowired
    private ViaCepWebService cepApi;

    @Autowired
    private PessoaService pessoaService;

    @PostMapping("/v1")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> cadastrarPessoa(@Valid @RequestBody PessoaDto pessoaDto){

        try{
            EnderecoViaCepResponseDto enderecoViaCepResponseDto = cepApi
                    .fetchEnderecoByCep(pessoaDto.getCep())
                    .orElseThrow(
                            () -> new IllegalArgumentException("CEP não encontrado")
                    );
            Pessoa response = pessoaService.save(pessoaDto, enderecoViaCepResponseDto);
            return ResponseEntity.ok(response);
        } catch (IOException | InterruptedException | IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensagem", ex.getMessage()));
        }
    }

    @GetMapping("/v1/{id}")
    public ResponseEntity<?> findPessoaById(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(pessoaService.findById(id).orElseThrow(() -> new RuntimeException("Pessoa não encontrada")));
        } catch(Exception ex){
            return ResponseEntity.status(404).body(Map.of("mensagem", ex.getMessage()));
        }
    }

}

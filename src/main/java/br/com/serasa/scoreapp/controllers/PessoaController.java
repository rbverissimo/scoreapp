package br.com.serasa.scoreapp.controllers;

import br.com.serasa.scoreapp.business.ScoreDescricaoHandler;
import br.com.serasa.scoreapp.client.ViaCepWebService;
import br.com.serasa.scoreapp.domain.Pessoa;
import br.com.serasa.scoreapp.dto.EnderecoViaCepResponseDto;
import br.com.serasa.scoreapp.dto.PessoaDto;
import br.com.serasa.scoreapp.exceptions.EnderecoException;
import br.com.serasa.scoreapp.services.PessoaService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("api/pessoas")
public class PessoaController {

    @Autowired
    private ViaCepWebService cepApi;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private ScoreDescricaoHandler scoreDescricaoHandler;

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
        } catch (IOException | InterruptedException | IllegalArgumentException | EnderecoException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensagem", ex.getMessage()));
        }
    }

    @GetMapping("/v1/{id}")
    public ResponseEntity<?> findPessoaById(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(pessoaService.findById(id).orElseThrow(() -> new Exception("Pessoa não encontrada")));
        } catch(Exception ex){
            return ResponseEntity.status(404).body(Map.of("mensagem", ex.getMessage()));
        }
    }

    @GetMapping("/v1/score/{id}")
    public ResponseEntity<?> getDescricaoScoreByPessoa(@PathVariable("id") Long id){
        return pessoaService.findById(id)
                .map(pessoa -> {
                    try{
                        String descricao = scoreDescricaoHandler.getDescricaoByScore(pessoa.getScore());
                        return ResponseEntity.ok(Map.of("descricao", descricao));
                    } catch (IllegalArgumentException ex){
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("mensagem", ex.getMessage()));
                    }
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));
    }

    @GetMapping("/v1/pessoas")
    public ResponseEntity<Page<Pessoa>> findAll(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer idade,
            @RequestParam(required = false) String cep,
            @PageableDefault(page = 0, size = 15) Pageable pageable
            ){
        try{
            return ResponseEntity.ok(pessoaService.findAllByFilters(nome, idade, cep, pageable));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/v1/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody PessoaDto pessoaDto){
        return pessoaService.findById(id).map(pessoa -> {
                    EnderecoViaCepResponseDto enderecoViaCepResponseDto = null;
                    Pessoa pessoaAtualizada = null;
                    if(!pessoa.getEndereco().getCep().equals(pessoaDto.getCep())){
                        try{
                            enderecoViaCepResponseDto =
                                   cepApi.fetchEnderecoByCep(pessoaDto.getCep())
                                           .orElseThrow(
                                                   () -> new IllegalArgumentException("CEP não encontrado")
                                           );
                            pessoaAtualizada = pessoaService.save(pessoaDto, enderecoViaCepResponseDto);
                        } catch (IOException | InterruptedException | IllegalArgumentException | EnderecoException ex){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensagem", ex.getMessage()));
                        }
                    }
                    return ResponseEntity.ok(pessoaAtualizada);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));
    }

    @DeleteMapping("/v1/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        return pessoaService.findById(id).map(pessoa -> {
            pessoaService.delete(pessoa);
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));
    }

}

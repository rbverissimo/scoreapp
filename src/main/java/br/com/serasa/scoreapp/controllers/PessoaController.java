package br.com.serasa.scoreapp.controllers;

import br.com.serasa.scoreapp.business.ScoreDescricaoHandler;
import br.com.serasa.scoreapp.client.ViaCepWebService;
import br.com.serasa.scoreapp.domain.Pessoa;
import br.com.serasa.scoreapp.dto.DescricaoScoreResponseDto;
import br.com.serasa.scoreapp.dto.EnderecoViaCepResponseDto;
import br.com.serasa.scoreapp.dto.PessoaDto;
import br.com.serasa.scoreapp.dto.MensagensResponseDto;
import br.com.serasa.scoreapp.exceptions.EnderecoException;
import br.com.serasa.scoreapp.exceptions.ViaCepException;
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
import java.util.Arrays;
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
    @ApiOperation(value = "Cadastrar uma nova pessoa", produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Pessoa cadastrada"),
            @ApiResponse(code = 400, message = "Requisição rejeitada")
    })
    public ResponseEntity<?> cadastrarPessoa(@Valid @RequestBody PessoaDto pessoaDto){

        try{
            EnderecoViaCepResponseDto enderecoViaCepResponseDto = cepApi
                    .fetchEnderecoByCep(pessoaDto.getCep())
                    .orElseThrow(
                            () -> new IllegalArgumentException("CEP não encontrado")
                    );
            Pessoa response = pessoaService.save(pessoaDto, enderecoViaCepResponseDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IOException | InterruptedException | IllegalArgumentException | EnderecoException | ViaCepException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MensagensResponseDto(Arrays.asList(ex.getMessage())));
        }
    }

    @GetMapping("/v1/{id}")
    @ApiOperation(value = "Encontrar pessoa pelo ID", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Pessoa não encontrada")
    })
    public ResponseEntity<?> findPessoaById(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(pessoaService.findById(id).orElseThrow(() -> new Exception("Pessoa não encontrada")));
        } catch(Exception ex){
            return ResponseEntity.status(404).body(new MensagensResponseDto(Arrays.asList(ex.getMessage())));
        }
    }

    @GetMapping("/v1/score/{id}")
    @ApiOperation(value = "Retornar a descrição do score de uma pessoa", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = DescricaoScoreResponseDto.class),
            @ApiResponse(code = 500, message = "Erro interno do servidor", response = MensagensResponseDto.class),
            @ApiResponse(code = 404, message = "Não encontrado")
    })
    public ResponseEntity<?> getDescricaoScoreByPessoa(@PathVariable("id") Long id){
        return pessoaService.findById(id)
                .map(pessoa -> {
                    try{
                        String descricao = scoreDescricaoHandler.getDescricaoByScore(pessoa.getScore());
                        return ResponseEntity.ok(new DescricaoScoreResponseDto(pessoa.getNome(), pessoa.getScore(), descricao));
                    } catch (IllegalArgumentException ex){
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MensagensResponseDto(Arrays.asList(ex.getMessage())));
                    }
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));
    }

    @GetMapping("/v1/pessoas")
    @ApiOperation(value = "Buscar lista paginada de pessoas", produces = "application/json")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "OK"),
                    @ApiResponse(code = 400, message = "Requisição rejeitada")
            })
    public ResponseEntity<Page<Pessoa>> findAll(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Integer idade,
            @RequestParam(required = false) String cep,
            @PageableDefault(page = 0, size = 15) Pageable pageable
            ){
        try{
            return ResponseEntity.ok(pessoaService.findAllByFilters(nome, idade, cep, pageable));
        } catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/v1/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Atualizar uma pessoa pelo ID", produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Pessoa atualizada"),
            @ApiResponse(code = 400, message = "Requisição rejeitada"),
            @ApiResponse(code = 404, message = "Pessoa não encontrada")
    })
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
                                                   () -> new IllegalArgumentException("Erro encontrado ao buscar o CEP")
                                           );
                            pessoaAtualizada = pessoaService.save(pessoaDto, enderecoViaCepResponseDto);
                        } catch (IOException | InterruptedException | IllegalArgumentException | EnderecoException |
                                 ViaCepException ex){
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MensagensResponseDto(Arrays.asList(ex.getMessage())));
                        }
                    }
                    return ResponseEntity.status(HttpStatus.CREATED).body(pessoaAtualizada);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));
    }

    @DeleteMapping("/v1/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiOperation(value = "Excluir uma pessoa pelo ID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Pessoa excluída"),
            @ApiResponse(code = 404, message = "Pessoa não encontrada"),
            @ApiResponse(code = 500, message = "Erro interno do servidor")
    })
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        return pessoaService.findById(id).map(pessoa -> {
            try{
                pessoaService.delete(pessoa);
                return ResponseEntity.noContent().build();
            } catch(Exception ex){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MensagensResponseDto(Arrays.asList(ex.getMessage())));
            }
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa não encontrada"));
    }

}

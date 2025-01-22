package br.com.serasa.scoreapp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.*;
import java.io.Serializable;

@ApiModel(value = "Campos necessários para o cadastro de pessoa")
public class PessoaDto implements Serializable {

    @ApiModelProperty(notes = "Nome da pessoa")
    @NotBlank(message = "O nome é obrigatório")
    @Size(min=3, max=64, message = "O nome deve possuir entre 3 e 64 caracteres")
    private String nome;

    @ApiModelProperty(notes = "Um CEP apenas números", example = "74230050")
    @NotBlank(message = "O cep é obrigatório")
    private String cep;

    @ApiModelProperty(notes = "A idade da pessoa")
    @Positive(message = "A idade deve ser um número positivo")
    private int idade;

    @ApiModelProperty(notes = "Um score entre 0 e 1000")
    @NotNull(message = "Informe o score da pessoa")
    @Min(value = 0, message = "O score mínimo é 0")
    @Max(value = 1000, message = "O score máximo é 1000")
    private int score;

    @ApiModelProperty(notes = "O telefone com DDD apenas números", example = "62980801212")
    @NotBlank(message = "O número de telefone é obrigatório")
    private String telefone;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}

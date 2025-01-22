package br.com.serasa.scoreapp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "Objeto de retorno da descrição do score", description = "Dados do score")
public class DescricaoScoreResponseDto implements Serializable {

    @ApiModelProperty(notes = "Nome da pessoa")
    private String nome;

    @ApiModelProperty(notes = "O número absoluto do score", example = "900, 705")
    private int score;

    @ApiModelProperty(notes = "Uma descrição válida", example = "Recomendável")
    private String descricao;

    public DescricaoScoreResponseDto(String nome, int score, String descricao) {
        this.nome = nome;
        this.score = score;
        this.descricao = descricao;
    }

    public DescricaoScoreResponseDto() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

package br.com.serasa.scoreapp.business;

public enum ScoreDescricaoEnum {

    INSUFICIENTE("Insuficiente"),
    INACEITAVEL("Inaceitável"),
    ACEITAVEL("Aceitável"),
    RECOMENDAVEL("Recomendável");

    public String descricao;

    ScoreDescricaoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

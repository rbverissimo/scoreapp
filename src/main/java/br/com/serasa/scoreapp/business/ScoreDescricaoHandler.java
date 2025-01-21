package br.com.serasa.scoreapp.business;

import org.springframework.stereotype.Component;

@Component
public class ScoreDescricaoHandler {

    public String getDescricaoByScore(Integer score) throws IllegalArgumentException{
        if(score == null || score < 0) throw new IllegalArgumentException("O score informado é inválido");

        if(score > 701) return ScoreDescricaoEnum.RECOMENDAVEL.getDescricao();
        else if(score > 501) return ScoreDescricaoEnum.ACEITAVEL.getDescricao();
        else if(score > 201) return ScoreDescricaoEnum.INACEITAVEL.getDescricao();
        else return ScoreDescricaoEnum.INSUFICIENTE.getDescricao();
    }
}

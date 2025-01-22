package br.com.serasa.scoreapp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "Objeto de retorno de mensagens", description = "Mensagens retornadas pela API")
public class MensagensResponseDto implements Serializable {

    @ApiModelProperty(notes = "Mensagens contendo erros ou alertas")
    private List<String> mensagens;

    public MensagensResponseDto(List<String> mensagens) {
        this.mensagens = mensagens;
    }

    public MensagensResponseDto() {
    }

    public List<String> getMensagens() {
        return mensagens;
    }

    public void setMensagens(List<String> mensagens) {
        this.mensagens = mensagens;
    }
}

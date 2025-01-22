package br.com.serasa.scoreapp.security.dto;

import io.swagger.annotations.ApiModel;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel(value = "Dados para o registro de um usuário no sistema", description = "Todos campos são necessários")
public class RegistroDto {

    @NotBlank(message = "O nome de usuário é obrigatório")
    @Size(min=3, max=32, message = "O nome do usuário deve conter entre 3 e 32 caracteres")
    private String nomeUsuario;

    @NotBlank(message = "O email do usuário é obrigatório")
    @Email(message = "O email fornecido é inválido")
    private String email;

    @NotBlank(message = "Informe uma senha válida")
    @Size(min = 6, message = "A senha informada deve ter ao menos 6 dígito alfanuméricos")
    private String senha;

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}

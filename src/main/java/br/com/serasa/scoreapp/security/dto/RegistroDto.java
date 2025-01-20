package br.com.serasa.scoreapp.security.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegistroDto {

    @NotBlank(message = "O nome de usuário é obrigatório")
    @Size(min=3, max=32, message = "O nome do usuário deve conter entre 3 e 32 caracteres")
    private String username;

    @NotBlank(message = "O email do usuário é obrigatório")
    @Email(message = "O email fornecido é inválido")
    private String email;

    @NotBlank(message = "Informe uma senha válida")
    @Size(min = 6, message = "A senha informada deve ter ao menos 6 dígito alfanuméricos")
    private String senha;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

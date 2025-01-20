package br.com.serasa.scoreapp.security.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class LogonDto {

    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    public @Email(message = "Email inválido") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Email inválido") String email) {
        this.email = email;
    }

    public @NotBlank(message = "A senha é obrigatória") String getSenha() {
        return senha;
    }

    public void setSenha(@NotBlank(message = "A senha é obrigatória") String senha) {
        this.senha = senha;
    }
}

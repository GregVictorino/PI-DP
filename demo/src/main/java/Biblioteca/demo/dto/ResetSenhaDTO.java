package Biblioteca.demo.dto;

public class ResetSenhaDTO {

    private String email;
    private String novaSenha;

    public ResetSenhaDTO() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNovaSenha() { return novaSenha; }
    public void setNovaSenha(String novaSenha) { this.novaSenha = novaSenha; }
}

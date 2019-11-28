package ritgames.chat.model;

import org.bson.Document;

import java.util.List;

public class Conta {
    private String nome;
    private String login;
    private String email;
    private String type;
    private Object __token__;

    public Conta(String nome, String login, String email, String type) {
        this.nome = nome;
        this.login = login;
        this.email = email;
        this.type = type;
    }

    public Conta(Document document) {
        this.nome = document.get("nome").toString();
        this.login = document.get("login").toString();
        this.email = document.get("email").toString();
        this.type = document.get("type").toString();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object get__token__() {
        return __token__;
    }

    public void set__token__(Object __token__) {
        this.__token__ = __token__;
    }

    @Override
    public String toString() {
        return "Conta{" +
                "nome='" + nome + '\'' +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", type='" + type + '\'' +
                ", __token__='" + __token__ + '\'' +
                '}';
    }

    public Document makeDocument() {
        return new Document()
                .append("nome",getNome())
                .append("login",getLogin())
                .append("email",getEmail())
                .append("type","user");
    }
}

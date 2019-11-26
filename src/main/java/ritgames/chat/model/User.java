package ritgames.chat.model;

import org.bson.Document;

public class User extends Conta {

    String senha;
    public User(String nome, String login, String email, String type, String senha) {
        super(nome, login, email, type);
        this.senha = senha;
    }

    public User(Document document) {
        super(document);
        this.senha = document.get("senha").toString();;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public Document getDocument() {
        return super.getDocument().append("senha",getSenha());
    }
}

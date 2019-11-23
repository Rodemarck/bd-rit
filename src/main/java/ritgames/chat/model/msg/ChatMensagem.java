package ritgames.chat.model.msg;

public class ChatMensagem {
    private String conteudo;
    private String remetente;
    private TipoMensagem tipo;
    private String nome;

    public TipoMensagem getTipo() {
        return tipo;
    }

    public void setTipo(TipoMensagem tipo) {
        this.tipo = tipo;
    }

    public enum TipoMensagem{
        CHAT,SAIDA,ENTRADA
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getRemetente() {
        return remetente;
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "ChatMensagem{" +
                "conteudo='" + conteudo + '\'' +
                ", remetente='" + remetente + '\'' +
                ", tipo=" + tipo +
                '}';
    }
}

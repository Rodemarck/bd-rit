package ritgames.chat.controller;

import org.springframework.web.bind.annotation.*;
import ritgames.chat.model.Conta;
import ritgames.chat.model.User;
import ritgames.chat.model.dao.UserDao;

import java.util.ArrayList;

@RestController
public class RitController {

    @GetMapping("/conta/logar/{login}/{senha}")
    public Conta logar(@PathVariable(value="login") String login, @PathVariable(value="senha") String senha) throws Exception {
        return UserDao.logar(login,senha);
    }

    @PostMapping("/conta/cadastrar/{nome}/{login}/{senha}/{email}/{type}")
    public void cadastrar(
                          @PathVariable(value="nome") String nome,
                          @PathVariable(value="login") String login,
                          @PathVariable(value="senha") String senha,
                          @PathVariable(value="email") String email,
                          @PathVariable(value="type") String type
    ) throws Exception {
        User user = new User(nome, login, email, type, senha);
        UserDao.cadastra(user);
    }

    @GetMapping("/conta/token/{tk}")
    public Conta logarPorToken(@PathVariable(value="tk") String tk) throws Exception{
        return UserDao.logarPorToken(tk);
    }

    @GetMapping("/conta/listar")
    public ArrayList<User> listarConta() throws InterruptedException{
        return UserDao.listar();
    }

    @PostMapping("/conta/atualizar/{login}/{senha}/{novoNome}/{novoLogin}/{novoSenha}/{novoEmail}/{novoType}")
    public void atualizaConta(
            @PathVariable(value="login") String login,
            @PathVariable(value="senha") String senha,
            @PathVariable(value="novoNome") String novoNome,
            @PathVariable(value="novoLogin") String novoLogin,
            @PathVariable(value="novoSenha") String novoSenha,
            @PathVariable(value="novoEmail") String novoEmail,
            @PathVariable(value="novoType") String novoType
    ) throws Exception {
        UserDao.atualizar(login,senha,new User(novoNome,novoLogin,novoEmail,novoType,novoSenha));
    }
}

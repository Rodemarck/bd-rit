package ritgames.chat.controller;

import org.springframework.web.bind.annotation.*;
import ritgames.chat.model.Conta;
import ritgames.chat.model.dao.UserDao;

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
    )throws InterruptedException {
        Conta c = new Conta(nome, login, email, type);
    }

    @GetMapping("/conta/token/{tk}")
    public Conta logarPorToken(@PathVariable(value="tk") String tk){
        return UserDao.logarPorToken(tk);
    }


}

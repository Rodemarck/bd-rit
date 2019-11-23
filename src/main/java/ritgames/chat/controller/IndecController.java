package ritgames.chat.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
/*
@CrossOrigin(origins = "http://domain2.com", maxAge = 3600)
@RestController
public class IndecController {
    @RequestMapping("/{token}")
    public ModelAndView index(@PathVariable(value="token") String token){
        if(token.equals("favicon"))
            return null;
        System.out.println("meu token é [" + token + "]");
        ModelAndView mv = new ModelAndView("indexLogado");
        mv.addObject("username",token);
        mv.addObject("nome","nenhum");
        return mv;
    }

    @RequestMapping("/")
    public ModelAndView indexAnonimo(){
        return new ModelAndView("index");
    }
}*/

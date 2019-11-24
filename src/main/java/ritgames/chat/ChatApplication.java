package ritgames.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import ritgames.chat.model.dao.UserDao;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class ChatApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ChatApplication.class, args);


        //System.out.println("\n\n\n\n\n------------------------------\n\n\n\n\n");
        //UserDao.listar();
        /*System.out.println("\n\n\n\n\n------------------------------\n\n\n\n\n");
        System.out.println(UserDao.logar("iury","123"));
        System.out.println("\n\n\n\n\n------------------------------\n\n\n\n\n");
        UserDao.listar();*/
    }

}

package ritgames.chat;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import ritgames.chat.model.dao.Exemplo;
import ritgames.chat.model.dao.MeuQueridoMongo;
import ritgames.chat.model.dao.UserDao;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class ChatApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ChatApplication.class, args);
    }

}

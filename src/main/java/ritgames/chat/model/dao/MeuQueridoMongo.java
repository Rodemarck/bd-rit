package ritgames.chat.model.dao;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import java.util.concurrent.Semaphore;

public class MeuQueridoMongo {
    public static MongoClient getClient() throws InterruptedException {
        return new MongoClient( new MongoClientURI("mongodb+srv://rit:U4sxzFfYM4xbFTwZ@cluster0-ltzau.gcp.mongodb.net/test?retryWrites=true&w=majority"));
    }
}

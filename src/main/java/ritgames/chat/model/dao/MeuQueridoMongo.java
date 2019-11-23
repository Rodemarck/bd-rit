package ritgames.chat.model.dao;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import java.util.concurrent.Semaphore;

public class MeuQueridoMongo {
    private static MeuQueridoMongo instance;
    private static Semaphore semaforo;
    private MongoClient client;
    private MongoDatabase db;

    public static MeuQueridoMongo getInstance(){
        if(instance == null){
            instance = new MeuQueridoMongo();
            if(semaforo == null){
                semaforo = new Semaphore(1);
            }
        }
        return instance;
    }

    private MeuQueridoMongo(){
        client = new MongoClient( new MongoClientURI("mongodb+srv://rit:U4sxzFfYM4xbFTwZ@cluster0-ltzau.gcp.mongodb.net/test?retryWrites=true&w=majority"));
        db = client.getDatabase("rit-games");
    }

    public MongoClient getClient() throws InterruptedException {
        return new MongoClient( new MongoClientURI("mongodb+srv://rit:U4sxzFfYM4xbFTwZ@cluster0-ltzau.gcp.mongodb.net/test?retryWrites=true&w=majority"));
    }


    public MongoDatabase getDb() throws InterruptedException {
        semaforo.acquire(1);
        return db;
    }

    public void release(){
        semaforo.release();
    }

    public void close() {
    }
}

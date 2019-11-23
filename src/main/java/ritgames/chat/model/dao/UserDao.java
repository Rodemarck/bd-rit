package ritgames.chat.model.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ritgames.chat.model.Conta;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Random;

public class UserDao {
    private static final char[] ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final  int LEN = ALPHA.length;
    private static final Random rng = new Random();
    public static void listar() throws InterruptedException {
        MongoClient client = MeuQueridoMongo.getInstance().getClient();
        MongoDatabase db = client.getDatabase("rit-games");
        MongoCollection<Document> collection = db.getCollection("user");

        FindIterable<Document> documents = collection.find();
        for(Document doc : documents){
            System.out.println(doc.toJson());
        }
        client.close();
        /*
        System.out.println(collection.find().filter(query).first().toJson());*/
    }

    public static void getConta(String token)throws InterruptedException{
        MongoClient client = MeuQueridoMongo.getInstance().getClient();
        BasicDBObject query = new BasicDBObject();
        query.put("tk", token);
        MongoDatabase db = client.getDatabase("rit-games");
        MongoCollection<Document> collection = db.getCollection("token");
        Object contaIdUser = collection.find().filter(query).first().get("contaIdUser");

        System.out.println(contaIdUser);
        query.clear();
        query.put("_id",contaIdUser);
        collection = db.getCollection("user");
        ArrayList a = collection.find().filter(query).into(new ArrayList<>());
        System.out.println(collection.find().filter(query).first().toJson());
        client.close();
    }

    public static Conta logar(String login, String senha) throws InterruptedException {
        MongoClient client = MeuQueridoMongo.getInstance().getClient();
        BasicDBObject query = new BasicDBObject();
        query.put("login", login);
        query.put("senha",senha);
        MongoDatabase db = client.getDatabase("rit-games");
        MongoCollection<Document> collection = db.getCollection("user");
        ArrayList<Document> docs =collection.find().filter(query).into(new ArrayList<>());
        if(docs.size() == 0){
            return null;
        }
        Conta c = new Conta(docs.get(0));
        System.out.println("\n\n\n\n\t" + docs.get(0).toJson() + "\n\n\n\n\t");
        Object token = docs.get(0).get("_id");

        query.clear();
        query.put("contaIdUser",token);
        collection = db.getCollection("token");
        collection.deleteMany(query);
        int num = 1;
        while(num !=0 ){
            query.clear();
            query.put("tk",geraString());
            num = collection.find(query).into(new ArrayList<>()).size();
        }
        c.set__token__(query.get("tk"));

        Document doc = new Document()
                        .append("tk",c.get__token__())
                        .append("contaIdUser",token);
        collection.insertOne(doc);

        client.close();
        return c;
    }

    private static String geraString(){
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<24;i++)
            sb.append(ALPHA[rng.nextInt(LEN)]);
        return sb.toString();
    }

    public static void cadastra(Conta conta, String senha) throws InterruptedException {
        MongoClient client = MeuQueridoMongo.getInstance().getClient();
        MongoDatabase db = client.getDatabase("rit-games");
        MongoCollection<Document> collection = db.getCollection("user");
        Document doc = new Document()
                        .append("nome",conta.getNome())
                        .append("login",conta.getLogin())
                        .append("senha",senha)
                        .append("email",conta.getEmail())
                        .append("type",conta.getType());
        collection.insertOne(doc);
        client.close();
    }

}

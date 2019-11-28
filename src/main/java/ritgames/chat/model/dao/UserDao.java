package ritgames.chat.model.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ritgames.chat.model.Conta;
import ritgames.chat.model.User;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class UserDao {

    private static final char[] ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final  int LEN = ALPHA.length;
    private static final Random rng = new Random();

    public static ArrayList<User> listar() throws InterruptedException {
        MongoClient client = MeuQueridoMongo.getClient();
        MongoDatabase db = client.getDatabase("rit-games");
        MongoCollection<Document> collection = db.getCollection("user");

        FindIterable<Document> documents = collection.find();
        ArrayList<User> users = new ArrayList<>();
        for(Document doc : documents){
            users.add(new User(doc));
        }
        client.close();
       return users;
    }

    public static void getConta(String token)throws InterruptedException{
        MongoClient client = MeuQueridoMongo.getClient();
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
        MongoClient client = MeuQueridoMongo.getClient();
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

    public static void cadastra(User conta) throws Exception {
        MongoClient client = MeuQueridoMongo.getClient();
        MongoDatabase db = client.getDatabase("rit-games");
        MongoCollection<Document> collection = db.getCollection("user");


        verificaCampo(collection,"nome",conta.getNome());
        verificaCampo(collection,"login",conta.getLogin());
        verificaCampo(collection,"email",conta.getEmail());

        Document doc = new Document()
                        .append("nome",conta.getNome())
                        .append("login",conta.getLogin())
                        .append("senha",conta.getSenha())
                        .append("email",conta.getEmail())
                        .append("type",conta.getType());
        collection.insertOne(doc);
        client.close();
    }

    public static Conta logarPorToken(String token) throws Exception {
        MongoClient client = MeuQueridoMongo.getClient();
        MongoDatabase db = client.getDatabase("rit-games");
        MongoCollection<Document> collectionTk = db.getCollection("token");
        BasicDBObject filtro = new BasicDBObject();
        filtro.put("tk", token);

        ArrayList<Document> docs = collectionTk.find().filter(filtro).into(new ArrayList<>());
        if(docs.size() == 0){
            throw new Exception("erros no sistema");
        }

        filtro.clear();
        filtro.put("_id",docs.get(0).get("contaIdUser"));
        MongoCollection<Document> collectionUser = db.getCollection("user");
        ArrayList<Document> docs1 = collectionUser.find(filtro).into(new ArrayList<>());
        Conta c = new Conta(docs1.get(0));

        //todo
        client.close();
        return c;
    }

    public static void atualizar(Conta contaAntiga, User user) throws Exception {
        MongoClient client = MeuQueridoMongo.getClient();
        MongoDatabase db = client.getDatabase("rit-games");
        MongoCollection<Document> collectionUser = db.getCollection("user");
        BasicDBObject filtro = new BasicDBObject();
        filtro.put("login", contaAntiga.getNome());
        filtro.put("senha", user.getSenha());

        ArrayList<Document> docs = collectionUser.find().filter(filtro).into(new ArrayList<>());
        if(docs.size() == 0){
            throw new Exception();
        }

        User c = new User(docs.get(0));

        collectionUser.deleteMany(filtro);
        c.setNome(user.getNome());
        c.setSenha(user.getSenha());
        c.setLogin(user.getLogin());
        c.setEmail(user.getEmail());
        c.set__token__(contaAntiga.get__token__());

        collectionUser.insertOne(c.getDocument());
    }

    public static void verificaCampo(MongoCollection<Document> collection, String campo, Object item) throws Exception{
        if(collection.find(new BasicDBObject().append(campo, item)).into(new ArrayList<>()).size() != 0)
            throw new Exception("campo " + campo + " [" + item + "] já está em uso");
    }
}

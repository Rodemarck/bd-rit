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

    private static final char[] ALPHA = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final  int LEN = ALPHA.length;
    private static final Random rng = new Random();

    synchronized public static ArrayList<User> listar() throws InterruptedException {
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

    synchronized public static void getConta(String token)throws InterruptedException{
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

    synchronized public static Conta logar(String login, String senha) throws Exception {
        MongoClient client = MeuQueridoMongo.getClient();
        BasicDBObject query = new BasicDBObject();
        query.put("login", login);
        query.put("senha",senha);
        MongoDatabase db = client.getDatabase("rit-games");
        MongoCollection<Document> collection = db.getCollection("user");
        ArrayList<Document> docs =collection.find().filter(query).into(new ArrayList<>());
        if(docs.size() == 0){
            throw new Exception("login ou senha invalidos");
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

    synchronized private static String geraString(){
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<24;i++)
            sb.append(ALPHA[rng.nextInt(LEN)]);
        return sb.toString();
    }

    synchronized public static void cadastra(User conta) throws Exception {
        MongoClient client = MeuQueridoMongo.getClient();
        MongoDatabase db = client.getDatabase("rit-games");
        MongoCollection<Document> collection = db.getCollection("user");

        testarConta(collection, conta);

        Document doc = new Document()
                        .append("nome",conta.getNome())
                        .append("login",conta.getLogin())
                        .append("senha",conta.getSenha())
                        .append("email",conta.getEmail())
                        .append("type",conta.getType());
        collection.insertOne(doc);
        client.close();
    }

    synchronized private static void testarConta(MongoCollection<Document> collection, Conta conta) throws Exception {
        testaLogin(conta.getLogin());
        verificaCampo(collection,"nome",conta.getNome());
        verificaCampo(collection,"login",conta.getLogin());
        verificaCampo(collection,"email",conta.getEmail());
    }

    synchronized private static void testaLogin(String login) throws Exception {
        if(login.equals("get-login") || login.equals("atualizar") ||
                login.equals("listar") || login.equals("token") || login.equals("cadastrar") || login.equals("logar"))
            throw  new Exception("login invalido");
    }

    synchronized public static Conta logarPorToken(String token) throws Exception {
        MongoClient client = MeuQueridoMongo.getClient();
        MongoDatabase db = client.getDatabase("rit-games");
        MongoCollection<Document> collectionTk = db.getCollection("token");
        BasicDBObject filtro = new BasicDBObject();
        filtro.put("tk", token);

        ArrayList<Document> docs = collectionTk.find().filter(filtro).into(new ArrayList<>());
        if(docs.size() == 0){
            throw new Exception("token não encontrado");
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

    synchronized public static void atualizar(String login, String senha, User novaConta) throws Exception {
        MongoClient client = MeuQueridoMongo.getClient();
        MongoDatabase db = client.getDatabase("rit-games");
        MongoCollection<Document> collectionUser = db.getCollection("user");
        BasicDBObject filtro = new BasicDBObject();
        filtro.put("login", login);
        filtro.put("senha", senha);

        ArrayList<Document> docs = collectionUser.find().filter(filtro).into(new ArrayList<>());
        if(docs.size() == 0){
            throw new Exception("login ou senha invalidos");
        }
        collectionUser.deleteMany(filtro);

        User contaAntiga = new User(docs.get(0));

        if(!contaAntiga.getNome().equals(novaConta.getNome()))
            verificaCampo(collectionUser,"nome",novaConta.getNome());
        if(!contaAntiga.getEmail().equals(novaConta.getEmail()))
            verificaCampo(collectionUser,"email",novaConta.getEmail());
        if(!contaAntiga.getLogin().equals(novaConta.getLogin())) {
            testaLogin(novaConta.getLogin());
            verificaCampo(collectionUser, "login", novaConta.getLogin());
        }

        collectionUser.insertOne(novaConta.makeDocument());
    }

    synchronized public static void verificaCampo(MongoCollection<Document> collection, String campo, Object item) throws Exception{
        if(collection.find(new BasicDBObject().append(campo, item)).into(new ArrayList<>()).size() != 0)
            throw new Exception("campo " + campo + " [" + item + "] já está em uso");
    }

    synchronized public static Conta getContaByLogin(String login) throws Exception {
        MongoClient client = MeuQueridoMongo.getClient();
        MongoDatabase db = client.getDatabase("rit-games");
        MongoCollection<Document> collection = db.getCollection("user");
        BasicDBObject filtro = new BasicDBObject()
                .append("login", login);
        ArrayList<Document> docs = collection.find(filtro).into(new ArrayList<>());
        if(docs.size() == 0){
            throw new Exception("login invalido");
        }
        client.close();
        return new Conta(docs.get(0));
    }
}

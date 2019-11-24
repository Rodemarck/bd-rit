package ritgames.chat.model.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;

public class Exemplo {

    public static void exemplo() throws InterruptedException {
        //antes de mais nada é necessario conseguir um cliente para isso criei a classe
        // "MeuQueridoMongo" só para evitar de digitar a msm coisa varias vezes
        MongoClient client = MeuQueridoMongo.getClient();

        //para conseguir o database apartir do cliente
        MongoDatabase db = client.getDatabase("rit-games");

        //para conseguir uma coleção do database
        MongoCollection<Document> collection = db.getCollection("user");

        //na coleção podemos usar alguns comandos como find, delete.. etc..
        collection.find();

        //mas para ficar útil vc tem q transforma em ArrayList
        ArrayList<Document> docs = collection.find().into(new ArrayList<>());

        //para fazer alguma buscar especifica é necessario criar um filto e passar
        BasicDBObject filtro = new BasicDBObject();
        filtro.put("login", "iury");
        filtro.put("senha","123");
        collection.find(filtro);

        //criar documentos em java é meio zoado
        Document doc = new Document()
                .append("nome","Meu nome")
                .append("login","meu login")
                .append("senha","minha senha")
                .append("email","meu email")
                .append("type","user");

        //inserir
        collection.insertOne(doc);

        //deletar, usando o filto
        collection.deleteMany(filtro);

        //fechar, sempre feche no fim da função ou vai dar merda
        client.close();
    }
}

package ui;

import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import control.CRUDZip;
import entity.Zip;

public class MainTest {
	public static void main(String[] args) {
		
		MongoClient client = new MongoClient();
		MongoDatabase db = client.getDatabase("zipdb");
		MongoCollection<Document> collection = db.getCollection("zips");
		
		CRUDZip crudZip = new CRUDZip(collection);
		
		List<Document> list = crudZip.listState();
		list.forEach(doc -> System.out.println(doc));
		
//		Zip zip = crudZip.getZip("943sgdf53453");
//		System.out.println(zip);
		
//		Zip zip = new Zip("94353453", "abc", new double[] {34.4343, 46546.343}, 10000, "xyz");
//		
//		boolean result = crudZip.addZip(zip);
//		System.out.println(result);
		
		
//		List<Zip> list = crudZip.listZip(5);
//		list.forEach(z -> System.out.println(z));
		
		client.close();
	}
}

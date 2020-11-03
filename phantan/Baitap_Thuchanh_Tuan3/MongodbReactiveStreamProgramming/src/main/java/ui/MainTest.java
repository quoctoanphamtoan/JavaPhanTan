package ui;

import java.util.List;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import control.CRUDZip;
import entity.Zip;

public class MainTest {
	public static void main(String[] args) {
		
		MongoClient client = MongoClients.create();
		MongoDatabase db = client.getDatabase("zipdb");
		MongoCollection<Document> collection = db.getCollection("zips");
		
		CRUDZip crudZip = new CRUDZip(collection);
		
		
		try {
			List<Zip> list = crudZip.listZip(5);
			list.forEach(z -> System.out.println(z));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
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

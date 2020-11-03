package control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.InsertOneResult;

import entity.Zip;

public class CRUDZip {
	private static final Gson gson = new Gson();
	private MongoCollection<Document> collection;

	public CRUDZip(MongoCollection<Document> collection) {
		super();
		this.collection = collection;
	}
	
	
	public List<Zip> listZip(int n) {
		List<Zip> list = new ArrayList<Zip>();
		
		MongoCursor<Document> it = collection.find().limit(n).cursor();
		while(it.hasNext()) {
			Document doc = it.next();
			String json = doc.toJson();
			Zip  zip = gson.fromJson(json, Zip.class);
			list.add(zip);
		}
		
		return list;
	}
	
	public boolean addZip(Zip zip) {
		
		String json = gson.toJson(zip);
		Document document = Document.parse(json);
		
		InsertOneResult result = collection.insertOne(document);
		
		return result.getInsertedId() != null ? true : false;
	}
	
	/**
	 *  db.zips.find({_id:'94353453'})
	 * @param id
	 * @return
	 */
	public Zip getZip(String id) {
		Zip zip = null;
		
		Document doc = collection.find(Document.parse("{_id:'" + id + "'}")).first();
		if(doc != null) {
			String json = doc.toJson();
			zip = gson.fromJson(json, Zip.class);
		}
		
		return zip;
	}
	
	/**
	 * db.zips.aggregate([{$group:{_id:{state:"$state",city:"$city"}}},
	 * {$replaceRoot:{newRoot:"$_id"}},{$group:{_id:"$state", num:{$sum:1}}}])
	 * @return
	 */
	public List<Document> listState() {
		List<Document> list = new ArrayList<Document>();
		
		MongoCursor<Document> it = collection.aggregate(Arrays.asList(
				Document.parse("{$group:{_id:{state:\"$state\",city:\"$city\"}}}"),
				Document.parse("{$replaceRoot:{newRoot:\"$_id\"}}"),
				Document.parse("{$group:{_id:\"$state\", num:{$sum:1}}}")
				)).iterator();
		
		while(it.hasNext()) {
			Document doc = it.next();
			list.add(doc);
		}
		
		return list;
	}
	
	
}

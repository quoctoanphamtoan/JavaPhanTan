package control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.bson.Document;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import com.google.gson.Gson;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoCollection;

import entity.Zip;

public class CRUDZip {
	private static final Gson gson = new Gson();
	protected static final CountDownLatch latch = new CountDownLatch(1);
	private MongoCollection<Document> collection;

	public CRUDZip(MongoCollection<Document> collection) {
		super();
		this.collection = collection;
	}
	
	private List<Zip> list = new ArrayList<Zip>();
	
	public List<Zip> listZip(int n) throws InterruptedException {
		
		FindPublisher<Document> pub = collection.find().limit(n);
		Subscriber<Document> sub = new Subscriber<Document>() {
			
			private Subscription s;

			public void onSubscribe(Subscription s) {
				this.s = s;
				this.s.request(1);
				
			}

			public void onNext(Document doc) {
				
				String json = doc.toJson();
				Zip  zip = gson.fromJson(json, Zip.class);
				list.add(zip);
				
				this.s.request(1);
			}

			public void onError(Throwable t) {
				t.printStackTrace();
			}

			public void onComplete() {
				latch.countDown();
			}
		};
		
		pub.subscribe(sub);
		
		latch.await();
		
		return list;
	}
	
//	public boolean addZip(Zip zip) {
//		
//		String json = gson.toJson(zip);
//		Document document = Document.parse(json);
//		
//		InsertOneResult result = collection.insertOne(document);
//		
//		return result.getInsertedId() != null ? true : false;
//	}
//	
//	/**
//	 *  db.zips.find({_id:'94353453'})
//	 * @param id
//	 * @return
//	 */
//	public Zip getZip(String id) {
//		Zip zip = null;
//		
//		Document doc = collection.find(Document.parse("{_id:'" + id + "'}")).first();
//		if(doc != null) {
//			String json = doc.toJson();
//			zip = gson.fromJson(json, Zip.class);
//		}
//		
//		return zip;
//	}
//	
//	/**
//	 * db.zips.aggregate([{$group:{_id:{state:"$state",city:"$city"}}},
//	 * {$replaceRoot:{newRoot:"$_id"}},{$group:{_id:"$state", num:{$sum:1}}}])
//	 * @return
//	 */
//	public List<Document> listState() {
//		List<Document> list = new ArrayList<Document>();
//		
//		MongoCursor<Document> it = collection.aggregate(Arrays.asList(
//				Document.parse("{$group:{_id:{state:\"$state\",city:\"$city\"}}}"),
//				Document.parse("{$replaceRoot:{newRoot:\"$_id\"}}"),
//				Document.parse("{$group:{_id:\"$state\", num:{$sum:1}}}")
//				)).iterator();
//		
//		while(it.hasNext()) {
//			Document doc = it.next();
//			list.add(doc);
//		}
//		
//		return list;
//	}
//	
	
}

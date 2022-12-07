package io.peerislands.hackathon.coldstorage.context;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

public class MongoDBContext {

	private static MongoCollection<Document> collection;

	public static void setCollection(MongoCollection<Document> collection) {
		if (MongoDBContext.collection == null) {
			MongoDBContext.collection = collection;
		}
	}

	public static MongoCollection<Document> getCollection() {
		return collection;
	}
}

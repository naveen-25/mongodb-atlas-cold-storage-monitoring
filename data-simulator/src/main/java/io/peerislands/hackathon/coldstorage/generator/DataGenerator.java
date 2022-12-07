package io.peerislands.hackathon.coldstorage.generator;

import java.util.Date;
import java.util.Random;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.peerislands.hackathon.coldstorage.context.MongoDBContext;

public class DataGenerator implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataGenerator.class);

	private String assetId;
	private Long sleepInterval;
	private static final Random RANDOM = new Random();

	public DataGenerator(String assetId, Long sleepInterval) {
		this.assetId = assetId;
		this.sleepInterval = sleepInterval;
	}

	@Override
	public void run() {
		while (true) {
			if (Thread.interrupted()) {
				break;
			}
			Document document = new Document("assetId", assetId).append("timestamp", new Date()).append("data",
					new Document("temp", RANDOM.nextInt(400, 900) / 100.0).append("humidity", RANDOM.nextInt(90, 100))
							.append("doorOpened", getWeightedRandomBoolean(0.9)));
			LOGGER.info("Simulating message: "+document);
			MongoDBContext.getCollection().insertOne(document);
			try {
				Thread.sleep(sleepInterval);
			} catch (InterruptedException e) {
				LOGGER.error("Thread interrupted", e);
				break;
			}
		}
	}

	private boolean getWeightedRandomBoolean(Double weight) {
		return RANDOM.nextDouble() > weight;
	}

}

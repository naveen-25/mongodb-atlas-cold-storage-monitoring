package io.peerislands.hackathon.coldstorage.generator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

@Service
public class DataGeneratorService {

	private ExecutorService executor;

	public void startSimulation(int numDevices, Long sleepInterval) {
		executor = Executors.newFixedThreadPool(numDevices);
		for (int i = 1; i <= numDevices; i++) {
			executor.execute(new DataGenerator("asset-00" + i, sleepInterval));
		}
	}

}

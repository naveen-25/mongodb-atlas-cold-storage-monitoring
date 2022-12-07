package io.peerislands.hackathon.coldstorage;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mongodb.client.MongoClients;

import io.peerislands.hackathon.coldstorage.context.MongoDBContext;
import io.peerislands.hackathon.coldstorage.generator.DataGeneratorService;

@SpringBootApplication
public class DataSimulatorApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DataSimulatorApplication.class, args);
	}

	@Autowired
	private DataGeneratorService service;

	@Override
	public void run(String... args) throws Exception {
		Option help = Option.builder("h").required(false).hasArg(false).longOpt("help").desc("Help for the loadgen cli")
				.build();
		Option connectionString = Option.builder("conn").required(true).hasArg(true).argName("conn")
				.longOpt("connectionString")
				.desc("Connection string of the target mongodb cluster in which the data is to be simulated").build();
		Option database = Option.builder("db").required(true).hasArg(true).argName("database").longOpt("database")
				.desc("Database name of the target collection").build();
		Option collection = Option.builder("coll").required(true).hasArg(true).argName("collection")
				.longOpt("collection").desc("Target collection to write the data").build();
		Option numDevices = Option.builder("n").required(true).hasArg(true).argName("numDevices").longOpt("numDevices")
				.desc("Number of devices to simulate").build();

		Option sleepInterval = Option.builder("s").required(false).hasArg(true).argName("sleep").longOpt("sleep")
				.desc("Sleep interval(ms) between each data simulation. Defaults to 10000").build();

		Options options = new Options();
		options.addOption(help);
		options.addOption(connectionString);
		options.addOption(database);
		options.addOption(collection);
		options.addOption(numDevices);
		options.addOption(sleepInterval);

		CommandLineParser parser = new DefaultParser();

		if (args.length == 0) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar data-simulator.jar", options, true);
			return;
		}

		CommandLine line = parser.parse(options, args);

		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar data-simulator.jar", options, true);
			return;
		} else {
			MongoDBContext.setCollection(MongoClients.create(line.getOptionValue("conn"))
					.getDatabase(line.getOptionValue("db")).getCollection(line.getOptionValue("coll")));
		}

		Long sleep = line.hasOption("s") ? Long.valueOf(line.getOptionValue("s")) : 10000L;

		service.startSimulation(Integer.valueOf(line.getOptionValue("numDevices")), sleep);

	}

}

package jvminternals.labs.benchmark;

import java.util.ArrayList;
import java.util.List;

import jvminternals.labs.JsonConverter;
import jvminternals.labs.JsonConverterException;
import jvminternals.labs.JsonConverterInterface;
import jvminternals.labs.fixtures.JsonTestClass;

public class ToJsonBenchmark {

	//TODO: why static?
	private static JsonConverterInterface converter = new JsonConverter();
	private static List<Long> perfData = new ArrayList<>();
	private static final int ROUNDS = 3000;
	
	public static void main(String[] args) {
		warmup();
		measure();
		dump();
	}

	private static void warmup() {
		for(int i=0; i<ROUNDS; i++){
			JsonTestClass dummy = new JsonTestClass();
			try {
				String json = converter.toJson(dummy);
			} catch (JsonConverterException e) { }
		}
	}

	private static void measure() {
		for(int i=0; i<ROUNDS; i++){
			JsonTestClass dummy = new JsonTestClass();
			long startTime = System.nanoTime();
			try {
				String json = converter.toJson(dummy);
				perfData.add( System.nanoTime() - startTime );
			} catch (JsonConverterException e) { }
		}
	}

	private static void dump() {
		for(Long l : perfData){
			System.out.println("" + l);
		}
	}
}

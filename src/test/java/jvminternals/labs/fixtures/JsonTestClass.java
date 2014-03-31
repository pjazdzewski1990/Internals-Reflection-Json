package jvminternals.labs.fixtures;

import java.util.ArrayList;
import java.util.List;

public class JsonTestClass {
	public String string;
	public int integer;
	public Dummy object;
	public List<Integer> array;
	public boolean bool;
	
	public JsonTestClass() { }

	public static JsonTestClass generateFixtures() {
		JsonTestClass fixtures = new JsonTestClass();
		
		fixtures.string = "str";
		fixtures.integer = 11;
		fixtures.object = new Dummy();
		fixtures.array = new ArrayList<>();
		fixtures.array.add(1);
		fixtures.array.add(2);
		fixtures.array.add(3);
		fixtures.bool = false;

		return fixtures;
	}
	
	public String makeTestString() {
		
		return "{\"string\": \"" + string + "\", \"integer\": " + integer 
				+ ", \"object\": " + "{}" + ", \"array\": "+ array +", \"bool\": " + bool 
				+ "}";
	}
}

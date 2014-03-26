package jvminternals.labs;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

class TestClass {
	public String string;
	public int integer;
	public Dummy object;
	public List<Integer> array;
	public boolean bool;
	
	public TestClass() { }
}
class Dummy{
	//TODO: fix this hack
	@Override
	public String toString(){
		return "{}";
	}
}

public class JsonConverterTests {

	TestClass fixtures;
	
	public JsonConverterTests(){
		fixtures = new TestClass();
		fixtures.string = "str";
		fixtures.integer = 11;
		fixtures.object = new Dummy();
		fixtures.array = new ArrayList<>();
		fixtures.array.add(1);
		fixtures.array.add(2);
		fixtures.array.add(3);
		fixtures.bool = false;
	}

	@Ignore // pending until fromJson is implemented
	@Test
	public void testFromJson() throws JsonConverterException {
		
		String testString = makeTestString(fixtures);
		System.out.println("TestString is " + testString);
		
		JsonConverterInterface json = new JsonConverter();
		TestClass tc = json.fromJson(testString, TestClass.class);
		assertNotNull(tc);
		
		assertEquals(tc.string, fixtures.string);
		assertEquals(tc.integer, fixtures.integer);
		assertEquals(tc.array, fixtures.array);
		assertEquals(tc.bool, fixtures.bool);
	}

	private String makeTestString(TestClass target) {
		
		return "{string: \"" + target.string + "\", integer: " + target.integer + 
				", object: {}, array: "+ target.array +", bool: " + target.bool + "}";
	}

	@Test
	public void testToJson() throws JsonConverterException {
		
		JsonConverterInterface json = new JsonConverter();
		String stringified = json.toJson(fixtures);
		System.out.println("ToJson  is " + stringified);
		System.out.println("Fixture is " + makeTestString(fixtures));
		assertNotNull(stringified);
		assertNotEquals(stringified,"");
		
		assertEquals(makeTestString(fixtures), stringified);
	}
}

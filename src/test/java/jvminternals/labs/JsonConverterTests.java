package jvminternals.labs;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
class Dummy { }

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

	@Test
	public void testToJson() throws JsonConverterException {
		
		JsonConverterInterface json = new JsonConverter();
		String stringified = json.toJson(fixtures);
		assertNotNull(stringified);
		assertNotEquals(stringified,"");
		
		assertEquals(makeTestString(fixtures), stringified);
	}
	
	@Test
	public void privateFieldToJson() throws JsonConverterException {
		
		class Private{
			private int field1 = 1;
			private int field2 = 2;
		}
		
		Private privateFieldsObject = new Private();
		
		JsonConverterInterface json = new JsonConverter();
		String stringified = json.toJson(privateFieldsObject);
		
		assertNotNull(stringified);
		assertEquals("{}", stringified);
	}
	
	@Test
	public void missingFieldToJson() throws JsonConverterException {
		
		class Nullable{
			public Integer field1 = null;
			public List field2 = null;
			public int field3 = 3;
		}
		
		Nullable nullableFieldsObject = new Nullable();
		
		JsonConverterInterface json = new JsonConverter();
		String stringified = json.toJson(nullableFieldsObject);
		
		assertNotNull(stringified);
		assertEquals("{field1: null, field2: null, field3: 3}", stringified);
	}
	
	@Test
	public void arrayFieldToJson() throws JsonConverterException {
		//TODO: works only with Integer[] not int[]. Reason -> cast issues
		class WithArrayField{
			public Integer[] field = new Integer[]{1,2,3};
		}
		
		WithArrayField nullableFieldsObject = new WithArrayField();
		
		JsonConverterInterface json = new JsonConverter();
		String stringified = json.toJson(nullableFieldsObject);
		
		assertNotNull(stringified);
		assertEquals("{field: [1, 2, 3]}", stringified);
	}
	
	@Test
	public void mapFieldToJson() throws JsonConverterException {
		class WithMapField{
			public Map<Integer, String> field = new HashMap<>();
			WithMapField(){
				field.put(1, "jeden");
				field.put(2, "dwa");
			}
		}
		
		WithMapField nullableFieldsObject = new WithMapField();
		
		JsonConverterInterface json = new JsonConverter();
		String stringified = json.toJson(nullableFieldsObject);
		
		assertNotNull(stringified);
		assertEquals("{field: {1: \"jeden\", 2: \"dwa\"}}", stringified);
	}
	
	/// --- helpers
	
	private String makeTestString(TestClass target) {
		
		return "{string: \"" + target.string + "\", integer: " + target.integer + 
				", object: " + "{}" + ", array: "+ target.array +", bool: " + target.bool + "}";
	}
}

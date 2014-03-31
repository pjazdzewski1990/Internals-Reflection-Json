package jvminternals.labs;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import jvminternals.labs.fixtures.JsonTestClass;

public class ToJsonConverterTests {

	JsonTestClass fixtures = JsonTestClass.generateFixtures();

	@Test
	public void testToJson() throws JsonConverterException {
		
		JsonConverterInterface json = new JsonConverter();
		String stringified = json.toJson(fixtures);
		assertNotNull(stringified);
		assertNotEquals(stringified,"");
		
		assertEquals(fixtures.makeTestString(), stringified);
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
		assertEquals("{\"field1\": null, \"field2\": null, \"field3\": 3}", stringified);
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
		assertEquals("{\"field\": [1, 2, 3]}", stringified);
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
		
		WithMapField mapFieldObject = new WithMapField();
		
		JsonConverterInterface json = new JsonConverter();
		String stringified = json.toJson(mapFieldObject);
		
		assertNotNull(stringified);
		assertEquals("{\"field\": {1: \"jeden\", 2: \"dwa\"}}", stringified);
	}
	
	@Test
	public void inheritedClassToJson() throws JsonConverterException {
		class Base {
			public int baseInt = 1;
		}
		class Foo extends Base {
			public int fooInt = 2;
		}
		
		Base baseObject = new Base();
		Foo fooObject = new Foo();
		Base fooAsBaseObject = new Foo();
		
		JsonConverterInterface json = new JsonConverter();
		String stringified = json.toJson(baseObject);
		
		assertNotNull(stringified);
		assertEquals("{\"baseInt\": 1}", stringified);
		
		stringified = json.toJson(fooObject);
		
		assertNotNull(stringified);
		assertEquals("{\"fooInt\": 2, \"baseInt\": 1}", stringified);
		
		stringified = json.toJson(fooAsBaseObject);
		
		assertNotNull(stringified);
		///TODO: what a retarded way of comparing json 
		assertEquals("{\"fooInt\": 2, \"baseInt\": 1}", stringified);
	}
}

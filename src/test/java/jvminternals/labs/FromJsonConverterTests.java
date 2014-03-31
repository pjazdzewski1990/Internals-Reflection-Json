package jvminternals.labs;

import static org.junit.Assert.*;
import jvminternals.labs.fixtures.JsonTestClass;

import org.junit.Ignore;
import org.junit.Test;

public class FromJsonConverterTests {
	
	JsonTestClass fixtures = JsonTestClass.generateFixtures();
	
	@Ignore // pending until fromJson is implemented
	@Test
	public void testFromJson() throws JsonConverterException {
		
		String testString = fixtures.makeTestString();
		System.out.println("TestString is " + testString);
		
		JsonConverterInterface json = new JsonConverter();
		JsonTestClass tc = json.fromJson(testString, JsonTestClass.class);
		assertNotNull(tc);
		
		assertEquals(tc.string, fixtures.string);
		assertEquals(tc.integer, fixtures.integer);
		assertEquals(tc.array, fixtures.array);
		assertEquals(tc.bool, fixtures.bool);
	}
}

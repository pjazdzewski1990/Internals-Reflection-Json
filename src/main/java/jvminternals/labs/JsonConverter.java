package jvminternals.labs;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class JsonConverter implements JsonConverterInterface {

	@Override
	public <T> String toJson(T obj) throws JsonConverterException {
		if (obj == null) {
			throw new JsonConverterException("null object converion");
		}

		StringBuilder jsonBuilder = new StringBuilder("{");
		Field[] allFields = obj.getClass().getFields();
		
		for (int i=0; i<allFields.length; i++) {
			Field field = allFields[i]; 
			try {
				jsonBuilder.append(field.getName());
				jsonBuilder.append(": ");
				jsonBuilder.append(toWritableForm(field.get(obj)));
				
				if(i!=allFields.length-1){
					jsonBuilder.append(", ");
				}
			} catch (IllegalArgumentException e) { //ignore errors
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		jsonBuilder.append("}");
		
		return jsonBuilder.toString();
	}


	private String toWritableForm(Object toWrite) throws JsonConverterException {
		if(toWrite instanceof String){
			return "\"" + toWrite.toString() + "\"";
		}
		return toWrite.toString();
		//throw new JsonConverterException("No writable form for " + toWrite);
	}

	@Override
	public <T> T fromJson(String json, Class<T> cls)
			throws JsonConverterException {
		try {
			return (T) cls.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new JsonConverterException(e);
		}
	}

}

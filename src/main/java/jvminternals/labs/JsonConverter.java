package jvminternals.labs;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		if(toWrite == null){
			return "null";
		}
		if(toWrite instanceof Integer || toWrite instanceof Double || toWrite instanceof Boolean){
			return toWrite.toString();
		}
		if(toWrite instanceof String){
			return "\"" + toWrite.toString() + "\"";
		}
		//TODO:
		if(toWrite.getClass().isArray()){
			//let combineList handle this
			toWrite = Arrays.asList(toWrite);
		}
		if(toWrite instanceof List){
			return combineList((List)toWrite);
		}
		if(toWrite instanceof Object){
			return toJson(toWrite);
		}
		System.out.println("No writable form for " + toWrite);
		throw new JsonConverterException("No writable form for " + toWrite);
	}

	private String combineList(List toWrite) throws JsonConverterException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("[");
		for(int i=0; i<toWrite.size(); i++){
			Object obj = toWrite.get(i);
			sb.append(toWritableForm(obj));
			
			if(i!=toWrite.size()-1){
				sb.append(", ");
			}
		}
		sb.append("]");
		
		return sb.toString();
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

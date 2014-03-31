package jvminternals.labs;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

public class JsonConverter implements JsonConverterInterface {

	@Override
	public <T> String toJson(T obj) throws JsonConverterException {
		if (obj == null) {
			throw new JsonConverterException("null object converion");
		}
		
		StringBuilder jsonBuilder = new StringBuilder("{");
		jsonBuilder.append( extractObjectFields(obj, obj.getClass()) );
		jsonBuilder.append(", ");
		jsonBuilder.delete(jsonBuilder.length()-2, jsonBuilder.length());
		jsonBuilder.append("}");
		
		return jsonBuilder.toString();
	}


	private <T> StringBuilder extractObjectFields(T obj, Class objClass)
			throws JsonConverterException {
		
		Field[] allFields = objClass.getFields();

		StringBuilder jsonBuilder = new StringBuilder();
		for (int i=0; i<allFields.length; i++) {
			Field field = allFields[i]; 
			try {
				jsonBuilder.append("\"");
				jsonBuilder.append(field.getName());
				jsonBuilder.append("\"");
				jsonBuilder.append(": ");
				jsonBuilder.append(toWritableForm(field.get(obj)));
				
				if(i != allFields.length - 1){
					jsonBuilder.append(", ");
				}
			} catch (IllegalArgumentException e) { //ignore errors
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return jsonBuilder;
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
		//TODO: objects vs primitives
		if(toWrite instanceof Collection){
			//let combineList handle this
			Collection toWriteList = (Collection) toWrite;
			toWrite = toWriteList.toArray();
		}
		if(toWrite.getClass().isArray()){
			return combineArray((Object[])toWrite);
		}
		if(toWrite instanceof Map){
			return combineMap((Map)toWrite);
		}
		if(toWrite instanceof Object){
			return toJson(toWrite);
		}
		System.out.println("No writable form for " + toWrite);
		throw new JsonConverterException("No writable form for " + toWrite);
	}

	private String combineMap(Map map) throws JsonConverterException {
		StringBuilder sb = new StringBuilder("{");
		for (Object keyAndValue : map.entrySet()) {
			Map.Entry entry = (Map.Entry) keyAndValue;
			sb.append(toWritableForm(entry.getKey()));
			sb.append(": ");
			sb.append(toWritableForm(entry.getValue()));
			sb.append(", ");
		}
		sb.delete(sb.length()-2, sb.length());
		sb.append("}");
		return sb.toString();
	}


	private String combineArray(Object[] toWrite) throws JsonConverterException {
		StringBuilder sb = new StringBuilder();
		
		sb.append("[");
		for(int i=0; i<toWrite.length; i++){
			Object obj = toWrite[i];
			sb.append(toWritableForm(obj));
			
			if(i!=toWrite.length-1){
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

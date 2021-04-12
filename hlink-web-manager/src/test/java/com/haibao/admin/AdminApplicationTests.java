package com.haibao.admin;

import cn.hutool.core.util.StrUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haibao.flink.utils.GsonUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdminApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminApplicationTests {

	@Test
	public void contextLoads() {

	}


	@Test
	public void testJsonSchema() {

		InputStream inputStream = getClass().getResourceAsStream("/templates/ddl/Schema.json");

		JSONObject Schema = new JSONObject(new JSONTokener(inputStream));

//		org.everit.json.schema.Schema schema = SchemaLoader.load(Schema);

		SchemaLoader loader = SchemaLoader.builder()
				.schemaJson(Schema)
				.draftV7Support()
				.build();
		Schema schema = loader.load().build();

		JSONObject data = new JSONObject("{\n" +
				"\t\"rectangle\" : {\n" +
				"\t\t\"a\" : -5,\n" +
				"\t\t\"b\" : 5\n" +
				"\t}\n" +
				"}");
		try {
			schema.validate(data);
		} catch (ValidationException e) {
			System.out.println(e.getAllMessages());
		}
	}

	@Test
	public void testParsejson(){
		String json = "{\n" +
				"  \"type\": \"object\",\n" +
				"  \"properties\": {\n" +
				"    \"user_id\": {\n" +
				"      \"type\": \"string\"\n" +
				"    },\n" +
				"    \"item_id\": {\n" +
				"      \"type\": \"string\"\n" +
				"    },\n" +
				"    \"category_id\": {\n" +
				"      \"type\": \"string\"\n" +
				"    },\n" +
				"    \"behavior\": {\n" +
				"      \"type\": \"string\"\n" +
				"    },\n" +
				"    \"ts\": {\n" +
				"      \"type\": \"string\",\n" +
				"      \"format\": \"date-time\"\n" +
				"    }\n" +
				"  }\n" +
				"}";

		 JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
		 Object type = jsonObject.get("type");
		 JsonObject properties = jsonObject.get("properties").getAsJsonObject();

		LinkedHashMap filedsMap = new  LinkedHashMap();
		for (Map.Entry<String, JsonElement> entry : properties.entrySet()){
//			System.out.println(entry.getKey() + ":" + entry.getValue());

			String FSType = JSONSchemaTypes2FlinkSQLTypes(GsonUtils.gsonString(entry.getValue()));
			filedsMap.put(entry.getKey(),FSType);
		}

		//输出转换结果
		for (Object entry2 : filedsMap.entrySet()){
			System.out.println(entry2.toString());
		}

	}

	/**
	 * JSONSchemaTypes2FlinkSQLTypes
	 * @param jsonStr
	 * @return
	 */
	public static String JSONSchemaTypes2FlinkSQLTypes(String jsonStr){
		JsonObject typesJsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();

		String result = null;
//		for (Map.Entry<String, Object> typeEntry : typesJsonObject.entrySet()){
//			System.out.println(typeEntry.getKey() + ":" + typeEntry.getValue());
//			String jsonSchemaType = typeEntry.getKey();
		    String jsonSchemaType = typesJsonObject.get("type").getAsString();
			if(StrUtil.isEmpty(jsonSchemaType) || "null".equals(jsonSchemaType)){
//				result = "NULL";
//				break;
			    return "NULL";
			}
			switch (jsonSchemaType.toLowerCase()){
				case "object":
					result = "ROW";
					break;
				case "boolean":
					result = "BOOLEAN";
					break;
				case "array":
					result = "ARRAY[_]";
					break;
				case "number":
					result = "DECIMAL";
					break;
				case "integer":
					result = "DECIMAL";
					break;
				case "string":

					result = "VARCHAR";

					if(typesJsonObject.has("format")){
						//结合format
						String format = typesJsonObject.get("format").toString();

						if("date-time".equals(format)){ //string with format: date-time	TIMESTAMP
							result = "TIMESTAMP";
						}else if("date".equals(format)){ //string with format: date	DATE
							result = "DATE";
						}else if("time".equals(format)){ //string with format: time	TIME
							result = "TIME";
						}
					}else if(typesJsonObject.has("encoding")){
						String encoding = typesJsonObject.get("encoding").toString();
						if("base64".equals(encoding)){ //string with encoding: base64	ARRAY[TINYINT]
							result = "ARRAY[TINYINT]";
						}
					}
					break;
				default:
					break;
			}

//			if(null != result){
//				break;
//			}
//		}

		return result;
	}


}

package BP.Tools;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Iterator;

import org.springframework.util.StringUtils;

import BP.DA.DBType;
import BP.DA.DataColumn;
import BP.DA.DataRow;
import BP.DA.DataRowCollection;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.Sys.SystemConfig;
//import BP.Tools.JsonTest.NullStringAdapter;
import BP.WF.DotNetToJavaStringHelper;
import BP.WF.Comm.JsonResultInnerData;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class Json
{
	/**
	 * 对象转换为Json字符串
	 */
	public static String ToJson(Object jsonObject)
	{
//		JSONObject object = JSONObject.fromObject(jsonObject);
//		System.out.println(object.toString());
//		return object.toString();

		Gson gson = new GsonBuilder().registerTypeAdapterFactory(new TypeAdapterFactory() {

			public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typetoken) {
				Class<T> rawType = (Class<T>) typetoken.getRawType();
				if (rawType != String.class) {
					return null;
				}
				return (TypeAdapter<T>) new TypeAdapter<String>() {

					@Override
					public String read(JsonReader reader) throws IOException {
						if (reader.peek() == JsonToken.NULL) {
							reader.nextNull();
							return "";
						}
						return reader.nextString();
					}

					@Override
					public void write(JsonWriter writer, String value) throws IOException {
						if (value == null) {
							// writer.nullValue();
							writer.value("");
							return;
						}
						writer.value(value);
					}

				};
			}

		}).create();

//		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
//		System.err.println(gson.toJson(jsonObject));
		
		return gson.toJson(jsonObject);
	}
	
	/**
	 * 对象集合转换Json
	 */
	@SuppressWarnings("rawtypes")
	public static String ToJson(Iterable array)
	{
//		JSONArray jsonArray = JSONArray.fromObject(array);
//		return jsonArray.toString();
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(new TypeAdapterFactory() {

			public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typetoken) {
				Class<T> rawType = (Class<T>) typetoken.getRawType();
				if (rawType != String.class) {
					return null;
				}
				return (TypeAdapter<T>) new TypeAdapter<String>() {

					@Override
					public String read(JsonReader reader) throws IOException {
						if (reader.peek() == JsonToken.NULL) {
							reader.nextNull();
							return "";
						}
						return reader.nextString();
					}

					@Override
					public void write(JsonWriter writer, String value) throws IOException {
						if (value == null) {
							// writer.nullValue();
							writer.value("");
							return;
						}
						writer.value(value);
					}

				};
			}

		}).create();
		return gson.toJson(array);
	}
	
	/**
	 * 普通集合转换Json
	 */
	@SuppressWarnings("rawtypes")
	public static String ToArrayString(Iterable array)
	{
		JSONObject object = JSONObject.fromObject(array);
		return object.toString();
	}
	
	////一些三个注释掉的方法：尝试使用手写+gson的方式书写，但是方法不能覆盖所有object转json
	/**
	 * 对象转换为Json字符串
	 *//*
	public static String ToJson(Object jsonObject)
	{
		JSONObject object = JSONObject.fromObject(jsonObject);
		Gson gson =  new GsonBuilder().serializeNulls().create();
		System.out.println(object.toString());
		System.out.println(gson.toJson(jsonObject).replaceAll(":null",":\"\""));
		return gson.toJson(jsonObject).replaceAll(":null",":\"\"");
		try {
			Class cla = Class.forName(jsonObject.getClass().getName());

			String separate = "";
			StringBuilder build = new StringBuilder("{");
			StringBuffer methodname = new StringBuffer();

			// 获取java类的变量
			Field[] fields = cla.getDeclaredFields();
			for (Field temp : fields) {
				build.append(separate);
				build.append("\"");
				build.append(temp.getName());
				build.append("\":");

				methodname.append("get");
				methodname.append(temp.getName().substring(0, 1).toUpperCase());
				methodname.append(temp.getName().substring(1));
				// 获取java的get方法
				Method method = cla.getMethod(methodname.toString());
				Object objectValue = method.invoke(jsonObject);
				String value = "";
				if (null != objectValue && !"".equals(objectValue)) {
					if (objectValue instanceof Iterable) {
						value = ToJson((Iterable) objectValue);
					} else if(objectValue.toString().contains("BP.") || objectValue instanceof Class){
						//value = ToJson((Class) objectValue);
						Gson gson =  new GsonBuilder().serializeNulls().create();
						value = gson.toJson(value).replaceAll(":null",":\"\"");
					}
					else{
						value = "\"" + ToJson(objectValue.toString()) + "\"";
					}
				} else {
					value = "\"\"";
				}
				build.append(value);
				methodname.setLength(0);
				separate = ",";
			}
			build.append("}");
			return build.toString();
		} catch (Exception e) {
			return null;
		}
	}
	
	*//**
	 * 对象集合转换Json
	 * @param <E>
	 *//*
	@SuppressWarnings("rawtypes")
	public static <E> String ToJson(Iterable<E> array)
	{
		JSONArray jsonArray = JSONArray.fromObject(array);
		return jsonArray.toString();
		//Gson gson =  new GsonBuilder().serializeNulls().create();
		//return gson.toJson(array).replaceAll(":null",":\"\"");
		String separate = "";
		StringBuilder build = new StringBuilder("[");
		for (Object item : array) {
			if(!StringUtils.isEmpty(item.getClass().getName()) && item.getClass().getName().contains("BP.")){
				build.append(separate);
				Gson gson =  new GsonBuilder().serializeNulls().create();
				build.append(gson.toJson(item).replaceAll(":null",":\"\""));
				separate = ",";
			}else{
				build.append(separate);
				build.append(ToJson(item.toString()));
				separate = ",";
			}
			
		}
		build.append("]");
		return build.toString();
	}
	
	*//**
	 * 普通集合转换Json
	 *//*
	@SuppressWarnings("rawtypes")
	public static String ToArrayString(Iterable array)
	{
		//JSONObject object = JSONObject.fromObject(jsonObject);
		//Gson gson = new Gson();
		//return gson.toJson(array);
		String separate = "";
		StringBuilder build = new StringBuilder("[");
		for (Object item : array) {
			build.append(separate);
			build.append(ToJson(item.toString()));
			separate = ",";
		}
		build.append("]");
		return build.toString();
	}*/
	
	/**
	 * 删除结尾字符
	 * 
	 * @param str
	 *            需要删除的字符
	 */
	private static String DeleteLast(String str)
	{
		if (str.length() > 1)
		{
			return str.substring(0, str.length() - 1);
		}
		return str;
	}
	
	   
	/**
	 * 把Ht转换成Entity模式.
	 * @param ht
	 * @return
	 */
    public static String ToJsonEntityModel(Hashtable ht)
    {
        String strs = "{";
        for(Object key : ht.keySet())
        {
            strs += "\"" + key + "\":\"" + ht.get(key) + "\",";
        }
        strs += "\"OutEnd\":\"无效参数请忽略\"";
        strs += "}";
        strs = TranJsonStr(strs);
        return strs;
    }
    public static String ToJsonEntitiesNoNameModel(Hashtable ht)
    {
    	DataTable dt = new DataTable();
		dt.Columns.Add("No");
		dt.Columns.Add("Name");

		for (Object key : ht.keySet())
		{
			DataRow dr = dt.NewRow();
			dr.setValueStr("No", key+"");
			dr.setValueStr("Name",ht.get(key)+"");
			dt.Rows.add(dr);

		}

		return BP.Tools.Json.DataTableToJson(dt, false);
    }
	
	/**
	 * Datatable转换为Json
	 * 
	 * @param Datatable对象
	 */
	public static String ToJson(DataTable table)
	{
		String jsonString = "[";
		DataRowCollection drc = table.Rows;
		for (int i = 0; i < drc.size(); i++)
		{
			jsonString += "{";
			for (DataColumn column : table.Columns)
			{
				jsonString += "\"" + ToJson(column.ColumnName) + "\":";
				Object obj = drc.get(i).getValue(column.ColumnName);
				if (column.DataType == java.util.Date.class || column.DataType == String.class)
				{
					if (null != obj)
					{
						jsonString += "\"" + ToJson(obj.toString()) + "\",";
					} else
					{
						jsonString += "\"\",";
					}
				} else
				{
					if (null != obj && !"".equals(obj))
					{
						jsonString += ToJson(obj.toString()) + ",";
					} else
					{
						jsonString += "0,";	// 老周改为0
					}
				}
			}
			jsonString = DeleteLast(jsonString) + "},";
		}
		return DeleteLast(jsonString) + "]";
	}
	
	/** 区分大小写
	 * Datatable转换为Json
	 * 
	 * @param Datatable对象
	 *//*
	public static String ToJson_2017(DataTable table)
	{
		String jsonString = "[";
		DataRowCollection drc = table.Rows;
		for (int i = 0; i < drc.size(); i++)
		{
			jsonString += "{";
			for (DataColumn column : table.Columns)
			{
				jsonString += "\"" + ToJson(column.ColumnName) + "\":";
				Object obj = drc.get(i).getValue_2017(column.ColumnName);
				if (column.DataType == java.util.Date.class
						|| column.DataType == String.class)
				{
					if (null != obj)
					{
						jsonString += "\"" + ToJson(obj.toString()) + "\",";
					} else
					{
						jsonString += "\"\",";
					}
				} else
				{
					if (null != obj && !"".equals(obj))
					{
						jsonString += ToJson(obj.toString()) + ",";
					} else
					{
						jsonString += "\"\",";
					}
				}
			}
			jsonString = DeleteLast(jsonString) + "},";
		}
		return DeleteLast(jsonString) + "]";
	}*/
	/**
	 * Datatable转换为Json  upper
	 * 
	 * @param Datatable对象
	 */
	public static String ToJsonUpper(DataTable table)
	{
		String jsonString = "[";
		DataRowCollection drc = table.Rows;
		for (int i = 0; i < drc.size(); i++)
		{
			jsonString += "{";
			for (DataColumn column : table.Columns)
			{
				jsonString += "\"" + ToJson(column.ColumnName.toUpperCase()) + "\":";
				Object obj = drc.get(i).getValue_2017(column.ColumnName);
				if (column.DataType == java.util.Date.class
						|| column.DataType == String.class)
				{
					if (null != obj)
					{
						jsonString += "\"" + ToJson(obj.toString()) + "\",";
					} else
					{
						jsonString += "\"\",";
					}
				} else
				{
					if (null != obj)
					{
						jsonString += ToJson(obj.toString()) + ",";
					} else
					{
						jsonString += ",";
					}
				}
			}
			jsonString = DeleteLast(jsonString) + "},";
		}
		return DeleteLast(jsonString) + "]";
	}
	
	/**
	 * DataSet转换为Json
	 * 
	 * @param DataSet对象
	 */
	public static String ToJson(DataSet dataSet)
	{
		String jsonString = "{";
		for (DataTable table : dataSet.Tables)
		{
			if(null==table)
				continue;
			jsonString += "\"" + ToJson(table.TableName) + "\":"
					+ ToJson(table.Rows) + ",";
		}
		return jsonString = DeleteLast(jsonString) + "}";
	}
	
	/**
	 * DataSet转换为Json   区分大小写
	 * 
	 * @param DataSet对象
	 *//*
	public static String ToJson_2017(DataSet dataSet)
	{
		String jsonString = "{";
		for (DataTable table : dataSet.Tables)
		{
			if(null==table)
				continue;
			jsonString += "\"" + ToJson(table.TableName) + "\":"
					+ ToJson_2017(table) + ",";
		}
		return jsonString = DeleteLast(jsonString) + "}";
	}*/
	
	/**
	 * String转换为Json
	 * 
	 * @param value
	 *            String对象
	 * @return Json字符串
	 */
	public static String ToJson(String value)
	{
		if (StringHelper.isNullOrEmpty(value))
		{
			return "";
		}
		
		String temstr;
		temstr = value;
		temstr = temstr.replace("{", "｛").replace("}", "｝").replace(":", "：")
				.replace(",", "，").replace("[", "【").replace("]", "】")
				.replace(";", "；").replace("\n", "<br/>").replace("\r", "");
		
		temstr = temstr.replace("\t", "   ");
		temstr = temstr.replace("'", "\'");
		temstr = temstr.replace("\\", "\\\\");
		temstr = temstr.replace("\"", "\"\"");
		return temstr;
	}
	
	 /** 
	  * 把一个json转化一个datatable
	  * 
	  * @param json 一个json字符串
	  * @return 序列化的datatable
	  */
	public static DataTable ToDataTable(String strJson)
	{
		DataTable tb = new DataTable();
		DataRow row = null;
		DataColumn dc = null;
		String key = "";
		String value  = "";
		//转换json格式
		JSONArray json = JSONArray.fromObject(strJson);
		tb = new DataTable();
        tb.TableName = "";
		for(int i = 0;i < json.size();i++) {
			JSONObject pjson = (JSONObject)json.get(i);
			@SuppressWarnings("unchecked")
			Iterator<String> iterator = pjson.keys();
			row = tb.NewRow();
			dc = new DataColumn();
			while(iterator.hasNext())
			{
				key = (String) iterator.next();
				value = pjson.getString(key);
				dc.ColumnName = key;
				tb.Columns.Add(dc.ColumnName);
				row.setValue(dc.ColumnName, value);
			}
			tb.Rows.add(i, row);
		}
		
		return tb;
	}

	/** 
	 * 转化成Json.
	 * @param ht Hashtable
	 * @param isNoNameFormat 是否编号名称格式
	 * @return 
	 */
	public static String ToJson(Hashtable ht, boolean isNoNameFormat)
	{
		if (isNoNameFormat)
		{
			/*如果是datatable 模式. */
			DataTable dt = new DataTable();
			dt.TableName = "HT"; //此表名不能修改.
			dt.Columns.Add(new DataColumn("No", String.class));
			dt.Columns.Add(new DataColumn("Name", String.class));
			for (Object key : ht.keySet())
			{
				if (key == null || "".equals(key.toString()))
				{
					continue;
				}

				DataRow dr = dt.NewRow();
				dr.setValue("No", key);

				String v = (String)((ht.get(key) instanceof String) ? ht.get(key) : null);
				if (v == null)
				{
					v = "";
				}
				dr.setValue("Name", v);
				dt.Rows.Add(dr);
			}
			return ToJson(dt);
		}

		String strs = "{";
		for (Object key : ht.keySet())
		{
			strs += "\"" + key.toString() + "\":\"" + ht.get(key.toString()) + "\",";
		}
		strs += "\"OutEnd\":\"1\"";
		strs += "}";
		strs = TranJsonStr(strs);
		return strs;
	}
	
	/** 
	 * JSON字符串的转义
	 * @param jsonStr
	 * @return 
	 */
	private static String TranJsonStr(String jsonStr)
	{
		String strs = jsonStr;
		strs = strs.replace("\\", "\\\\");
		strs = strs.replace("\n", "\\n");
		strs = strs.replace("\b", "\\b");
		strs = strs.replace("\t", "\\t");
		strs = strs.replace("\f", "\\f");
		strs = strs.replace("/", "\\/");
		return strs;
	}
	
	/**add by dgq**/
	public final static JSONObject GetObjectFromArrary_ByKeyValue(JSONArray jsOb,String Key, String value)
	{
		if (jsOb.isArray() == true)
		{
			for (int i = 0, j = jsOb.size(); i < j; i++)
			{
				if (!jsOb.getJSONObject(i).containsKey(Key))
				{
					continue;
				}

				if (!jsOb.getJSONObject(i).isNullObject()&& jsOb.getJSONObject(i).get(Key).equals(value))
				{
					return jsOb.getJSONObject(i);
				}
			}
		}
		return null;
	}

	/** 把dataset转成json 不区分大小写.
	 
	 @param dataSet
	 @return 
*/
//ORIGINAL LINE: public static string DataSetToJson(DataSet dataSet, bool isUpperColumn = true)
	public static String DataSetToJson(DataSet dataSet, boolean isUpperColumn)
	{
		String jsonString = "{";
		for (DataTable table : dataSet.Tables)
		{
			if (isUpperColumn == true)
			{
				jsonString += "\"" + table.TableName.toUpperCase() + "\":" + DataTableToJson(table, true) + ",";
			}
			else
			{
				jsonString += "\"" + table.TableName + "\":" + DataTableToJson(table, false) + ",";
			}
		}
		jsonString = DotNetToJavaStringHelper.trimEnd(jsonString, ',');
		return jsonString + "}";
	}
	/**  
	 Datatable转换为Json 
	  
	 @param table Datatable对象 
	 @return Json字符串 
*/
//ORIGINAL LINE: public static string DataTableToJson(DataTable dt, bool isUpperColumn = true)
	public static String DataTableToJson(DataTable dt, boolean isUpperColumn)
	{
		StringBuilder jsonString = new StringBuilder();
		if (dt.Rows.size() == 0)
		{
			jsonString.append("[]");
			return jsonString.toString();
		}
		
		boolean isOracel=false;
		if (SystemConfig.getAppCenterDBType() ==  DBType.Oracle)
			isOracel=true;
			
				

		jsonString.append("[");
		DataRowCollection drc = dt.Rows;
		for (int i = 0; i < drc.size(); i++)
		{
			jsonString.append("{");
			for (int j = 0; j < dt.Columns.size(); j++)
			{
				String strKey = null;
				String strValue=null;

				if (isUpperColumn == true)
				{
					strKey = dt.Columns.get(j).ColumnName.toUpperCase();
					String strOld = dt.Columns.get(j).ColumnName;
					strValue = drc.get(i).get(strKey) == null ? (drc.get(i).get(strOld) == null ?"":drc.get(i).get(strOld).toString()) : drc.get(i).get(strKey).toString();
				}
				else
				{
					
					strKey = dt.Columns.get(j).ColumnName;
					
					if (isOracel==false)
						strValue = drc.get(i).get(strKey) == null ? "" : drc.get(i).get(strKey).toString();
						
					else
					{			
						strValue = drc.get(i).get(strKey) == null ? "" : drc.get(i).get(strKey).toString();
						
						if (StringUtils.isEmpty(strValue))
							strValue=drc.get(i).get(strKey.toUpperCase()) == null ? "" : drc.get(i).get(strKey.toUpperCase()).toString();
					}
					 
				}

				 
				
				Object type = dt.Columns.get(j).getDataType();
				jsonString.append("\"" + strKey + "\":");
				strValue = StringFormat(strValue, type);
				if (j < dt.Columns.size() - 1)
				{
					jsonString.append( strValue + ",");
				}
				else
				{
					jsonString.append(strValue);
				}
			}
			jsonString.append("},");
		}
		jsonString.deleteCharAt(jsonString.length() - 1);
		jsonString.append("]");
		return jsonString.toString();
	}
	
	/**  
	 格式化字符型、日期型、布尔型 
	  
	 @param str 
	 @param type 
	 @return  
*/
	private static String StringFormat(String str, Object type)
	{
		if (type == String.class)
		{
			str = String2Json(str);
			str = "\"" + str + "\"";
		}
		else if (type == java.util.Date.class)
		{
			str = "\"" + str + "\"";//Convert.ToDateTime(str).ToShortDateString()
		}
		else if (type == Boolean.class)
		{
			str = str.toLowerCase();
		}
		else if (type == byte[].class)
		{
			//数字字段需转string后进行拼接 @于庆海 需要翻译
			str = "\"" + str + "\"";
		}
		
		
		if (str.length() == 0)
		{
			str = "\"\"";
		}

		return str;
	}
	
	/**  
	 过滤特殊字符 
	  
	 @param s 
	 @return  
*/
	private static String String2Json(String s)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.toCharArray()[i];

			switch (c)
			{
				case '\"':
					sb.append("\\\"");
					break;
				case '\\':
					sb.append("\\\\");
					break;
				case '/':
					sb.append("\\/");
					break;
				case '\b':
					sb.append("\\b");
					break;
				case '\f':
					sb.append("\\f");
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '\r':
					sb.append("\\r");
					break;
				case '\t':
					sb.append("\\t");
					break;
				default:
					sb.append(c);
					break;
			}
		}
		return sb.toString();
	}

	/** 
		 把一个json转化一个datatable 杨玉慧
		 
		 @param json 一个json字符串
		 @return 序列化的datatable
*/
		public static DataTable ToDataTableOneRow(String strJson)
		{
			////杨玉慧  写  用这个写
			if (strJson.trim().indexOf('[') != 0)
			{
				strJson = "[" + strJson + "]";
			}
			//DataTable dtt = (DataTable)JsonConvert.<DataTable>DeserializeObject(strJson);
			DataTable dtt = ToDataTable(strJson);

			return dtt;
		}
		/**
		 * 适用于oracle
		 * @param dataSet
		 * @param isUpperColumn
		 * @param isRowUper
		 * @return
		 */
		public static String DataSetToJson(DataSet dataSet, boolean isUpperColumn,boolean isRowUper)
		{
			String jsonString = "{";
			for (DataTable table : dataSet.Tables)
			{
				if (isUpperColumn == true)
				{
					jsonString += "\"" + table.TableName.toUpperCase() + "\":" + DataTableToJson(table, true,isRowUper) + ",";
				}
				else
				{
					jsonString += "\"" + table.TableName + "\":" + DataTableToJson(table, false,isRowUper) + ",";
				}
			}
			jsonString = DotNetToJavaStringHelper.trimEnd(jsonString, ',');
			return jsonString + "}";
		}
		
		//cols是大小写区分  row是大小写区分/或者大写    返回类型为大小写区分
		public static String DataSetToJson(DataSet dataSet, boolean isUpperColumn,boolean isRowUper,Boolean appace)
		{
			String jsonString = "{";
			for (DataTable table : dataSet.Tables)
			{
				if (isUpperColumn == true)
				{
					jsonString += "\"" + table.TableName.toUpperCase() + "\":" + DataTableToJson(table, true, isRowUper, appace) + ",";
				}
				else
				{
					jsonString += "\"" + table.TableName + "\":" + DataTableToJson(table, false, isRowUper, appace) + ",";
				}
			}
			jsonString = DotNetToJavaStringHelper.trimEnd(jsonString, ',');
			return jsonString + "}";
		}
		/**  
		 Datatable转换为Json 
		  
		 @param table Datatable对象 
		 @return Json字符串 
	*/
	//ORIGINAL LINE: public static string DataTableToJson(DataTable dt, bool isUpperColumn = true)
		public static String DataTableToJson(DataTable dt, boolean isUpperColumn, boolean isRowUper)
		{
			StringBuilder jsonString = new StringBuilder();
			if (dt.Rows.size() == 0)
			{
				jsonString.append("[]");
				return jsonString.toString();
			}

			jsonString.append("[");
			DataRowCollection drc = dt.Rows;
			for (int i = 0; i < drc.size(); i++)
			{
				jsonString.append("{");
				for (int j = 0; j < dt.Columns.size(); j++)
				{
					String strKey = null;

					if (isUpperColumn == true)
					{
						strKey = dt.Columns.get(j).ColumnName.toUpperCase();
					}
					else
					{
						strKey = dt.Columns.get(j).ColumnName;
					}
					String strValue = "";
					if(!isRowUper){
						strValue = drc.get(i).get(dt.Columns.get(j).ColumnName) == null ? "" : drc.get(i).get(dt.Columns.get(j).ColumnName).toString();
					}else{
						strValue = drc.get(i).get(strKey) == null ? "" : drc.get(i).get(strKey).toString();
					}
					Object type = dt.Columns.get(j).getDataType();
					jsonString.append("\"" + strKey + "\":");
					strValue = StringFormat(strValue, type);
					if (j < dt.Columns.size() - 1)
					{
						jsonString.append(strValue + ",");
					}
					else
					{
						jsonString.append(strValue);
					}
				}
				jsonString.append("},");
			}
			jsonString.deleteCharAt(jsonString.length() - 1);
			jsonString.append("]");
			return jsonString.toString();
		}
		
		//appAce 传入大小写区分的cols，输出区分大消息的数据，当时oracle时，取值按照大写取值
		public static String DataTableToJson(DataTable dt, boolean isUpperColumn, boolean isRowUper,boolean appace)
		{
			StringBuilder jsonString = new StringBuilder();
			if (dt.Rows.size() == 0)
			{
				jsonString.append("[]");
				return jsonString.toString();
			}

			jsonString.append("[");
			DataRowCollection drc = dt.Rows;
			for (int i = 0; i < drc.size(); i++)
			{
				jsonString.append("{");
				for (int j = 0; j < dt.Columns.size(); j++)
				{
					String strKey = null;

					if (isUpperColumn == true)
					{
						strKey = dt.Columns.get(j).ColumnName.toUpperCase();
					}
					else
					{
						strKey = dt.Columns.get(j).ColumnName;
					}
					String strValue = "";
					if(!isRowUper){
						if (SystemConfig.getAppCenterDBType() == DBType.Oracle){//按照大写取值
							strValue = drc.get(i).get(strKey.toUpperCase()) == null ? "" : drc.get(i).get(strKey.toUpperCase()).toString();
						}else{
							strValue = drc.get(i).get(dt.Columns.get(j).ColumnName) == null ? "" : drc.get(i).get(dt.Columns.get(j).ColumnName).toString();
						}
					}else{
						strValue = drc.get(i).get(strKey) == null ? "" : drc.get(i).get(strKey).toString();
					}
					Object type = dt.Columns.get(j).getDataType();
					jsonString.append("\"" + strKey + "\":");
					strValue = StringFormat(strValue, type);
					if (j < dt.Columns.size() - 1)
					{
						jsonString.append(strValue + ",");
					}
					else
					{
						jsonString.append(strValue);
					}
				}
				jsonString.append("},");
			}
			jsonString.deleteCharAt(jsonString.length() - 1);
			jsonString.append("]");
			return jsonString.toString();
		}
		
		public static String ToJsonEntitiesNoNameMode(Hashtable ht)
	    {
	    	DataTable dt = new DataTable();
			dt.Columns.Add("No");
			dt.Columns.Add("Name");

			for (Object key : ht.keySet())
			{
				DataRow dr = dt.NewRow();
				dr.setValueStr("No", key+"");
				dr.setValueStr("Name",ht.get(key)+"");
				dt.Rows.add(dr);

			}

			return BP.Tools.Json.DataTableToJson(dt, false);
	    }
}
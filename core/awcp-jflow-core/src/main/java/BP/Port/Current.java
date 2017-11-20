package BP.Port;

import java.util.concurrent.ConcurrentHashMap;

public class Current {
	public static ConcurrentHashMap<String, String> Session;
	static {
		Session = new ConcurrentHashMap<>();
	}

	public static void SetSession(String key, String Value) {
		if (Value != null) {
			Session.put(key, Value);
		}
	}

	public static String GetSessionStr(String key, String isNullAsValue) {
		Object val = Session.get(key);
		if (val == null) {
			return isNullAsValue;
		}
		return (String) ((val instanceof String) ? val : null);
		// if (Session.ContainsKey(key))

		// {

		// Session.remove(key);

		// }

		// Session.Add(key, Value);

	}
}
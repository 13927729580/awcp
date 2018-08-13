package cn.org.awcp.venson.common;

import java.util.Arrays;
import java.util.List;

public final class SC {
	public static final String DATA = "data";
	public static final String TOTAL = "count";
	public static final String QUERY_LIST = "queryList";
	public static final String COUNT_ALL = "countAll";
	public static final String USER_ID = "userId";
	public static final String VERIFY_CODE = "verifyCode";
	public static final String USER_NAME = "userName";
	public static final String USER_ROLE = "userRole";
	public static final String USER_SEX = "userSex";
	public static final String USER_ACCOUNT = "userAccount";
	public static final String SECRET_KEY = "secretKey";
	public static final String PRIVILEGES_VECTOR = "privilegesVector";
	public static final String USER_INFO = "userInfo";
	public static final String USER_IP_ADDRESS = "userIpAddress";
	public static final String PRIVILEGES_ID = "privilegesID";
	public static final String DEFAULT_PWD = "791013";
	public static final String USER_GROUP = "user_group";
	public static final String SALT = "awcp";
	public static final String USER_STATUS_AUDIT = "0";
	public static final String USER_STATUS_DISABLED = "2";
	public static final String[] TARGET_URL = new String[] { "manage/index.html", "devAdmin/list.jsp" };
	public static final List<String> ENCRYPT_PASSWORD_KEY = Arrays.asList("password","smsPassword", "emailPassword","idcard_AppSecret","redis.password");
	public static final String CURRENT_USER_DATA_SOURCE = "current_user_data_source";
	public static final String DATA_SOURCE_COOKIE_KEY = "group_name";
	public static final String SMS_CODE_SALT="sljksdf@34#s51";

}

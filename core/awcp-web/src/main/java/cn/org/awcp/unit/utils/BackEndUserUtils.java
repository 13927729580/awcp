package cn.org.awcp.unit.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * 
 * @author allen
 *	后台用户类
 */
public abstract class BackEndUserUtils {
	/**
	 * 系统管理员
	 */
	private static String BACK_END_USER_ADMIN = "admin";
	/**
	 * 超级用户
	 */
	private static String BACK_END_USER_SUPERUSER = "superUser";
	/**
	 * 开发者
	 */
	private static String BACK_END_USER_DEVELOPER = "developer";
	
	/**
	 * 开发管理员
	 */
	private static String DEVELOPER_ADMIN = "developerAdmin";
	
	/**
	 * 开发人员
	 */
	private static String DEVELOPER = "developer";
	
	/**
	 * 是否为后台用户
	 * @return
	 */
	public static boolean isBackEndRole(){
		Subject s = SecurityUtils.getSubject();
		if (s.hasRole(BACK_END_USER_ADMIN)
				|| s.hasRole(BACK_END_USER_SUPERUSER)
				|| s.hasRole(BACK_END_USER_DEVELOPER)) {
			return true;//是后台用户
		}
		return false;
	}
	
	/**
	 * 判断是否为管理员
	 * @return
	 */
	public static boolean isAdmin(){
		Subject s = SecurityUtils.getSubject();
		if(s.hasRole(BACK_END_USER_ADMIN)){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否为开发人员
	 * @return
	 */
	public static boolean isDeveloper(){
		Subject s = SecurityUtils.getSubject();
		if(s.hasRole(BACK_END_USER_DEVELOPER)){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否为超级用户
	 * @return
	 */
	public static boolean isSuperuser(){
		Subject s = SecurityUtils.getSubject();
		if(s.hasRole(BACK_END_USER_SUPERUSER)){
			return true;
		}
		return false;
	}
	
	public static boolean isSuperuserOrDeveloper() {
		Subject s = SecurityUtils.getSubject();
		if (s.hasRole(BACK_END_USER_SUPERUSER)
				|| s.hasRole(BACK_END_USER_DEVELOPER)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否是超级用户、开发人员与管理员之一
	 */
	public static boolean[] isBackEndUser() {
		Subject subject = SecurityUtils.getSubject();
		List<String> backEndUsers = new ArrayList<String>();
		backEndUsers.add(BACK_END_USER_ADMIN);
		backEndUsers.add(BACK_END_USER_DEVELOPER);
		backEndUsers.add(BACK_END_USER_SUPERUSER);
		boolean[] result = subject.hasRoles(backEndUsers);
		return result;
	}
	
	/**
	 */
	
}

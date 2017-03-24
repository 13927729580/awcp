package org.szcloud.framework.unit.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 
 * @author allen
 *	自定义token
 */
public class PunUserToken extends UsernamePasswordToken {
	private static final long serialVersionUID = 3930380687789117867L;
	
	private String orgcode;//组织机构代码
	
	private String captcha;//验证码
	
	public PunUserToken(String username, char[] password, 
			 boolean rememberMe, String host,String captcha,String orgcode){
		super(username, password, rememberMe, host); 
		this.captcha = captcha;
		this.orgcode = orgcode;
	}

	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	
	
}

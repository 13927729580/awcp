package org.jflow.framework.designer.model;


import BP.Port.Emp;
import BP.Port.WebUser;
import BP.Sys.SystemConfig;

public class FrmsModel{
	public void init(){
		 Emp emp = new Emp(SystemConfig.getAppSettings().get("Admin").toString());
         WebUser.SignInOfGener(emp);
	}

}

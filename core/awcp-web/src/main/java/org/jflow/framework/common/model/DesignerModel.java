package org.jflow.framework.common.model;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.jdbc.core.JdbcTemplate;
import cn.org.awcp.core.utils.Springfactory;

import BP.DA.DBAccess;
import BP.Port.Emp;
import BP.Port.WebUser;
import BP.Sys.SystemConfig;

public class DesignerModel extends BaseModel{

	
	private HttpServletRequest resquest;
	
	private HttpServletResponse response;

	private String basePath;
	
	public DesignerModel(HttpServletRequest request,
			HttpServletResponse response,String basePath) {
		super(request, response);
		this.response=response;
		this.resquest=resquest;
		this.basePath=basePath;
	}

	public void init() throws Exception{
		
//		//#region 检查是否是安装了ccflow如果没有就让其安装.
//        try
//        {
//            //如果没有Port_Dept 表就可能没有安装ccflow.
//            DBAccess.RunSQL("SELECT count(*) FROM Port_Dept WHERE 1=2");
//            JdbcTemplate jdbcTemplate=Springfactory.getBean("jdbcTemplate");
//            jdbcTemplate.queryForInt("SELECT count(*) FROM Port_Dept WHERE 1=2");
//        }
//        catch(EnumConstantNotPresentException e)
//        {
//            /*数据库链接不通或者有异常，说明没有安装.*/
//            this.get_response().sendRedirect("../DBInstall.jsp");
//            return;
//        }
        //#endregion 检查是否是安装了ccflow如果没有就让其安装.
        
        //#region 执行admin登陆.
        Emp emp = new Emp();
        emp.setNo(SystemConfig.getAppSettings().get("Admin").toString());
        if (emp.RetrieveFromDBSources() == 1)
        {
            WebUser.SignInOfGener(emp, true);
        }
        else
        {
//            emp.setNo(SystemConfig.getAppSettings().get("Admin").toString());
//            emp.setName("admin");
//            emp.setFK_Dept("01");
//            emp.setPass("pub");
//            emp.Insert();
//            WebUser.SignInOfGener(emp, true);
            throw new Exception("admin 用户丢失，请注意大小写。");
        }
        //#endregion 执行admin登陆.

        // 执行升级, 现在升级代码移动到 Glo 里面了。
//        String str = BP.WF.Glo.UpdataCCFlowVer(); //执行升级.
//        if (str != null)
//        {
//            //   this.Response.Write(str);
//            BP.Sys.PubClass.Alert("系统成功升级到:" + str,this.get_response());
//        }
	
	}

}

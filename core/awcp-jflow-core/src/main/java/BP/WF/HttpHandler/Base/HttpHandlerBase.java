package BP.WF.HttpHandler.Base;

import BP.DA.Log;
import cn.jflow.controller.wf.workopt.BaseController;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public abstract class HttpHandlerBase extends BaseController
{
	/** 
	 * 获取 "Handler业务处理类"的Type
	 * 注意： "Handler业务处理类"必须继承自BP.WF.HttpHandler.WebContralBase</p>
	*/
	public abstract java.lang.Class getCtrlType();

	public final boolean getIsReusable()
	{
		return false;
	}
	
	public void ProcessRequest(Object mycontext)
	{	
		PrintWriter out =null;
		//创建 ctrl 对象.
		Object tempVar = mycontext;

		WebContralBase ctrl = (WebContralBase)((tempVar instanceof WebContralBase) ? tempVar : null);
		Log.DebugWriteInfo("执行类[" + this.getCtrlType().toString() + "]，方法[" + ctrl.getDoType() + "]");
		try
		{
			HttpServletResponse response = this.getResponse();
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			//执行方法返回json.
			String data = ctrl.DoMethod(ctrl, ctrl.getDoType());
			//返回执行的结果.
			//ctrl.context.Response.Write(data);
			out.write(data);
		}
		catch (Exception ex)
		{
			String err=ex.getMessage();
			
			//返回执行错误的结果.
			if (ex.getCause() != null)
			{
				err+="err@在执行类[" + this.getCtrlType().toString() + "]，方法[" + ctrl.getDoType() + "]错误 \t\n @" + ex.getCause().getMessage() + " \t\n @技术信息:" + ex.getStackTrace();
			}
			else
			{
				err+="err@在执行类[" + this.getCtrlType().toString() + "]，方法[" + ctrl.getDoType() + "]错误 \t\n @" + ex.getMessage() + " \t\n @技术信息:" + ex.getStackTrace();
			}
			
			BP.DA.Log.DebugWriteError(err);
			if(null !=out) {
				out.write(err);
			}
			
		}finally{
			if(null !=out){
				out.close();
			}
		}
	}

}

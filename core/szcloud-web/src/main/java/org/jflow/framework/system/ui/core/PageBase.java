package org.jflow.framework.system.ui.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PageBase {

	protected StringBuilder _content = null;
	protected List<String> _include = null;
	protected List<String> _script = null;
	protected HttpServletRequest _request;
	protected HttpServletResponse _response;

	public PageBase(HttpServletRequest request, HttpServletResponse response) {
		this._response = response;
		this._request = request;
		this._content = new StringBuilder();
		this._include = new ArrayList<String>();
		this._script = new ArrayList<String>();
	}

	public StringBuilder get_content() {
		return _content;
	}

	public void set_content(StringBuilder _content) {
		this._content = _content;
	}

	public List<String> get_include() {
		return _include;
	}

	public void set_include(List<String> _include) {
		this._include = _include;
	}

	public List<String> get_script() {
		return _script;
	}

	public void set_script(List<String> _script) {
		this._script = _script;
	}

	public HttpServletRequest get_request() {
		return _request;
	}

	public void set_request(HttpServletRequest _request) {
		this._request = _request;
	}

	public HttpServletResponse get_response() {
		return _response;
	}

	public void set_response(HttpServletResponse _response) {
		this._response = _response;
	}


	public void AddBR() {
		_content.append("<BR/>");
	}

	public void AddBtn() {
		_content.append("<input type='button'></input>");
	}

	public void AddBtn(String id, String name) {
		_content.append("<input type='button' id='" + id + "' name='" + name
				+ "'></input>");
	}

	public void Add(String text) {
		_content.append(text);
	}

	public void AddTable() {
		_content.append("<TABLE>");
	}

	public void AddTableEnd() {
		_content.append("</TABLE>");
	}

	public void AddTDTitle(String title) {
		_content.append("<TD>" + title + "</TD>");
	}
	
    public void AddFieldSet(String title) {
       Add("<fieldset width='100%' ><legend>&nbsp;" + title + "&nbsp;</legend>");
    }
   
    public void AddFieldSetEnd(){
       Add("</fieldset>");
    }
    
    public void AddTDTitleExt(String str){
    	Add("\n<TD class='TitleExt' nowrap=true >" + str + "</TD>");
	}
    
	public void AddTR(){
		Add("\n<TR>");
	}
	public void AddTR(String attr){
	    this.Add("\n<TR " + attr + " >");
	}
	
	public void AddTREnd(){
		Add("\n</TR>");
	}
	
	public void AddTDDesc(String str) {
        this.Add("\n<TD class='FDesc' nowrap=true >" + str + "</TD>");
    }
	
	public void AddFieldSetRed(String title, String msg)
    {
		Add("<fieldset class=FieldSetRed ><legend>&nbsp;" + title + "&nbsp;</legend>");
		Add(msg);
		Add("</fieldset>");
    }
	
	public void AddTD()
    {
		Add("\n<TD >&nbsp;</TD>");
    }
    public void AddTD(String str){
        if (str == null || str == "")
        	Add("\n<TD  nowrap >&nbsp;</TD>");
        else
        	Add("\n<TD  nowrap >" + str + "</TD>");
   	}
    public void AddTDEnd()
    {
        this.Add("\n</TD>");
    }
    public void AddTD(String attr, String str)
    {
    	Add( "\n<TD " + attr + " >" + str + "</TD>");
    }
    
    public void AddTDIdx(int idx)
    {
    	Add("\n<TD class='Idx' nowrap>" + idx + "</TD>");
    }
    
    public void AddTDCenter(String str)
    {
    	Add("\n<TD align=center nowrap >" + str + "</TD>");
    }
    
    public void AddTRSum()
    {
    	Add("\n<TR class='TRSum' >");
    }
    
    public void addTableEnd()
    {
    	Add("</Table>");
    }
   
    public void AddMsgOfInfo(String title, String doc) {
       AddFieldSet(title);
       if (doc != null){
    	   Add(doc.replace("@", "<BR>@"));
       }
       AddFieldSetEnd();
    }
   
	/**
	 * 关闭窗口
	 */
	public void WinClose(){
		try {
			_response.getWriter().write("<script language='JavaScript'> window.close();</script>");
		} catch (IOException e) {
			e.printStackTrace();
		}
     }
	
	public void WinClose(String val){
		try {
			 //经测试谷歌,IE都走window.top.returnValue 方法
			String clientscript = "<script language='javascript'> if(window.opener != undefined){window.top.returnValue = '" + val + "';} else { window.returnValue = '" + val + "';} window.close(); </script>";
			_response.getWriter().write(clientscript);
		} catch (IOException e) {
			e.printStackTrace();
		}
     }
	
	public void WinCloseWithMsg(String mess) {
       mess = mess.replace("'", "＇");

       mess = mess.replace("\"", "＂");

       mess = mess.replace(";", "；");
       mess = mess.replace(")", "）");
       mess = mess.replace("(", "（");

       mess = mess.replace(",", "，");
       mess = mess.replace(":", "：");


       mess = mess.replace("<", "［");
       mess = mess.replace(">", "］");

       mess = mess.replace("[", "［");
       mess = mess.replace("]", "］");


       mess = mess.replace("@", "\\n@");

       mess = mess.replace("\r\n", "");

       try {
			_response.getWriter().write("<script language='JavaScript'>alert('" + mess + "'); window.close()</script>");
		} catch (IOException e) {
			e.printStackTrace();
		}
   }
	
	/**
	 * 切换到信息也面。
	 * @param mess
	 * @param context 
	 */
	public void ToWFMsgPage(String mess, ServletContext context) {
		mess = mess.replace("@", "<BR>@");
		mess = mess.replace("~", "@");
		HttpSession session = _request.getSession();
		session.setAttribute("info", mess);
		
		try {
			if (context.getAttribute("PageMsg") == null) {
				_response.sendRedirect(BP.WF.Glo.getCCFlowAppPath() + "WF/Comm/Port/InfoPage.jsp?d=" + new Date());
			} else {
				_response.sendRedirect(context.getAttribute("PageMsg") + "WF/Comm/Port/InfoPage.jsp?d=" + new Date());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 切换到错误信息页面。
	 * @param mess
	 */
    public void ToErrorPage(String mess) {
    	HttpSession session = _request.getSession();
		session.setAttribute("info", mess);
		try {
			_response.sendRedirect(BP.WF.Glo.getCCFlowAppPath() + "WF/Comm/Port/ToErrorPage.jsp?d=" + new Date());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}

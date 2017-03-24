package org.jflow.framework.designer.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jflow.framework.system.ui.core.DDL;
import org.jflow.framework.system.ui.core.ListItem;

import BP.Tools.StringHelper;
import BP.WF.Template.Node;
import BP.WF.Template.Nodes;

public class FlowSkipModel {

	//private String basePath;
	private HttpServletRequest _request = null;
	//private HttpServletResponse _response = null;
	
	public StringBuilder Pub = null;
	public FlowSkipModel(HttpServletRequest request, HttpServletResponse response, String basePath) {
		//this.basePath = basePath; 
		this._request = request;
		Pub = new StringBuilder();
		//this._response = response;
	}

	
	public String getParameter(String key){
		String value = _request.getParameter(key);
		if(StringHelper.isNullOrEmpty(value))
			return "";
		return value;
	}
	public final String getFK_Flow(){
		return getParameter("FK_Flow");
	}
	public final int getFK_Node(){
		String fk_node = getParameter("FK_Node");
		if("".equals(fk_node)){
			return 0;
		}else{
			return Integer.parseInt(fk_node);
		}
	}
	public final Long getFID(){
		String fid = getParameter("FID"); 
		if("".equals(fid)){
			return 0l;
		}else{
			return Long.valueOf(fid);
		}
	}
	public final Long getWorkID(){
		String workid = getParameter("WorkID"); 
		if("".equals(workid)){
			return 0l;
		}else{
			return Long.valueOf(workid);
		}
	}
	
	public void init(){
		
		 Nodes nds = new Nodes(this.getFK_Flow());
		 DDL SkipToNode = new DDL();
		 SkipToNode.setId("DDL_SkipToNode");
		 SkipToNode.setName("DDL_SkipToNode");
		 for (Node nd : Nodes.convertNodes(nds)){
			 SkipToNode.Items.add(new ListItem("步骤:" + nd.getStep() + "名称:" + nd.getName(), String.valueOf(nd.getNodeID())));
		 }
		 this.Pub.append(SkipToNode);
	}
	
}

package org.jflow.framework.common.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WF_WorkOpt_OneWork_ChartTrack extends BaseModel {
	/**
	 * 日志对象
	 */
	private static Log logger = LogFactory.getLog(WF_WorkOpt_OneWork_ChartTrack.class);
	private int workID;
	private int fk_flow;
	private int fid;
	private String basePath;

	public WF_WorkOpt_OneWork_ChartTrack(HttpServletRequest request, HttpServletResponse response, int workID,
			int fk_flow, int fid, String basePath) {
		super(request, response);
		this.workID = workID;
		this.fk_flow = fk_flow;
		this.fid = fid;
		this.basePath = basePath;
	}

	public void init() {
		try {
			String url = "";
			url = basePath + "ChartTrack.jsp?FID=" + fid + "&FK_Flow=" + fk_flow + "&WorkID=" + workID;
			// content.Attributes.Add("src", url);
		} catch (Exception ee) {
			logger.info("ERROR", ee);
		}
	}
}

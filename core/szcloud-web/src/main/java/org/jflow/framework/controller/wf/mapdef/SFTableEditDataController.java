package org.jflow.framework.controller.wf.mapdef;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jflow.framework.common.model.TempObject;
import org.jflow.framework.controller.wf.workopt.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import BP.En.QueryObject;
import BP.Sys.GENoName;
import BP.Sys.GENoNames;

@Controller
@RequestMapping("/WF/MapDef")
public class SFTableEditDataController extends BaseController {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());

	@RequestMapping(value = "/btn_Click3", method = RequestMethod.POST)
	public void btn_Click(TempObject object, HttpServletRequest request, HttpServletResponse response) {
		// 批量保存数据。
		GENoNames ens = new GENoNames(this.getRefNo(), "sdsd");
		QueryObject qo = new QueryObject(ens);
		qo.DoQuery("No", 10, this.getPageIdx(), false);
		for (GENoName myen : GENoNames.convertGENoNames(ens)) {
			String no = myen.getNo();
			String name1 = request.getParameter("TB_" + myen.getNo());
			if (name1.equals("")) {
				continue;
			}
			BP.DA.DBAccess.RunSQL("update " + this.getRefNo() + " set Name='" + name1 + "' WHERE no='" + no + "'");
		}

		GENoName en = new GENoName(this.getRefNo(), "sd");
		String name = request.getParameter("TB_Name");
		if (name.length() > 0) {
			en.setName(name);
			en.setNo(en.getGenerNewNo());
			en.Insert();
			try {
				response.sendRedirect("SFTableEditData.jsp?RefNo=" + this.getRefNo() + "&PageIdx=" + this.getPageIdx());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.info("ERROR", e);
			}
		}

	}

}

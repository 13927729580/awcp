package org.jflow.framework.designer.model;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jflow.framework.common.model.BaseModel;

import BP.DA.DBAccess;
import BP.DA.DataType;
import BP.En.AttrOfOneVSM;
import BP.En.AttrsOfOneVSM;
import BP.En.ClassFactory;
import BP.En.EnDtl;
import BP.En.EnDtls;
import BP.En.Entities;
import BP.En.Entity;
import BP.En.RefMethod;
import BP.En.RefMethodType;
import BP.En.RefMethods;
import BP.Tools.StringHelper;

public class RefLeftModel {
	/**
	 * 日志对象
	 */
	protected final Log logger = LogFactory.getLog(getClass());

	private String basePath;
	private int ItemCount = 0;
	private HttpServletRequest _request = null;
	/// <summary>
	/// 结点属性左侧功能菜单第一项的默认图标
	/// </summary>
	private String IconFirstDefault = "WF/Img/Home.gif";

	/// <summary>
	/// 结点属性左侧功能菜单多对多的默认图标
	/// </summary>
	private String IconM2MDefault = "WF/Img/M2M.png";

	/// <summary>
	/// 结点属性左侧功能菜单明细的默认图标
	/// </summary>
	private String IconDtlDefault = "WF/Img/Btn/Dtl.gif";

	/// <summary>
	/// 是否显示结点属性左侧功能菜单默认图标
	/// </summary>
	private boolean ShowIconDefault = true;

	public RefLeftModel(HttpServletRequest request, String basePath) {
		this.basePath = basePath;
		this._request = request;
	}

	public final String getEnName() throws Exception {
		String enName = this._request.getParameter("EnName");
		String ensName = this._request.getParameter("EnsName");
		if (StringHelper.isNullOrEmpty(enName) && StringHelper.isNullOrEmpty(ensName))
			throw new Exception("@缺少参数");
		if (StringHelper.isNullOrEmpty(enName)) {
			Entities ens = ClassFactory.GetEns(this.getEnsName());
			enName = ens.getGetNewEntity().toString();
		}
		return enName;
	}

	public final String getEnsName() throws Exception {
		String enName = this._request.getParameter("EnName");
		String ensName = this._request.getParameter("EnsName");
		if (StringHelper.isNullOrEmpty(enName) && StringHelper.isNullOrEmpty(ensName))
			throw new Exception("@缺少参数");

		if (StringHelper.isNullOrEmpty(ensName)) {
			Entity en = ClassFactory.GetEn(this.getEnName());
			ensName = en.getGetNewEntities().toString();
		}
		return ensName;
	}

	public final String getPK() throws Exception {
		String pk = this._request.getParameter("PK");
		if (StringHelper.isNullOrEmpty(pk)) {
			pk = this._request.getParameter("No");
		}
		if (StringHelper.isNullOrEmpty(pk)) {
			pk = this._request.getParameter("RefNo");
		}
		if (StringHelper.isNullOrEmpty(pk)) {
			pk = this._request.getParameter("OID");
		}
		if (StringHelper.isNullOrEmpty(pk)) {
			pk = this._request.getParameter("MyPK");
		}
		if (StringHelper.isNullOrEmpty(pk)) {
			Entity mainEn = ClassFactory.GetEn(this.getEnName());
			pk = this._request.getParameter(mainEn.getPK());
		}
		return pk;
	}

	public final String getAttrKey() {
		if (_request.getParameter("AttrKey") == null)
			return "";
		return _request.getParameter("AttrKey");
	}

	public StringBuilder Pub1 = null;

	public void init() {
		this.Pub1 = new StringBuilder();
		try {
			Entity en = BP.En.ClassFactory.GetEn(this.getEnName());
			if (StringHelper.isNullOrEmpty(getPK()))
				return;
			if (en == null)
				throw new Exception(this.getEnsName() + " " + this.getEnName());
			if (en.getEnMap().getAttrsOfOneVSM().size() + en.getEnMap().getDtls().size()
					+ en.getEnMap().getHisRefMethods().size() == 0)
				return;

			en.setPKVal(this.getPK());
			String keys = "&" + en.getPK() + "=" + this.getPK() + "&r=" + DataType.dateToStr(new Date(), "MMddhhmmss");

			String titleKey = "";
			if (en.getEnMap().getAttrs().Contains("Name"))
				titleKey = "Name";
			else if (en.getEnMap().getAttrs().Contains("Title"))
				titleKey = "Title";
			String desc = en.getEnDesc();
			if (!"".equals(titleKey)) {
				en.RetrieveFromDBSources();
				desc = en.GetValStrByKey(titleKey);
				if (desc.length() > 30)
					desc = en.getEnDesc();
			}
			this.Pub1.append(BaseModel.AddUL("class='navlist'"));
			this.Pub1.append(BaseModel.AddLi(String.format(
					"<div><a href='" + basePath
							+ "WF/Comm/RefFunc/UIEn.jsp?EnName=%1$s&PK=%2$s'>%5$s<span class='nav'>%3$s</span></a></div>%4$s",
					this.getEnName(), this.getPK(), "Title".equals(titleKey) ? "主页" : desc, "\r\n",
					GetIcon(IconFirstDefault))));

			// #region 加入一对多的实体编辑
			AttrsOfOneVSM oneVsM = en.getEnMap().getAttrsOfOneVSM();
			String sql = "";
			int i = 0;

			if (oneVsM.size() > 0) {
				for (AttrOfOneVSM vsM : oneVsM) {
					String url = basePath + "WF/Comm/RefFunc/Dot2Dot.jsp?EnsName="
							+ en.getGetNewEntities().getClass().getName() + "&EnName=" + this.getEnName() + "&AttrKey="
							+ vsM.getEnsOfMM().getClass().getName() + keys;
					try {
						sql = "SELECT COUNT(*) as NUM FROM "
								+ vsM.getEnsOfMM().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE "
								+ vsM.getAttrOfOneInMM() + "='" + en.getPKVal() + "'";
						i = DBAccess.RunSQLReturnValInt(sql);
					} catch (Exception e) {
						sql = "SELECT COUNT(*) as NUM FROM "
								+ vsM.getEnsOfMM().getGetNewEntity().getEnMap().getPhysicsTable() + " WHERE "
								+ vsM.getAttrOfOneInMM() + "=" + en.getPKVal();
						try {
							i = DBAccess.RunSQLReturnValInt(sql);
						} catch (Exception ee) {
							vsM.getEnsOfMM().getGetNewEntity().CheckPhysicsTable();
						}
					}
					if (i == 0) {
						if ((this.getAttrKey()).equals(vsM.getEnsOfMM().getClass().getName())) {
							this.Pub1.append(BaseModel.AddLi(String.format(
									"<div style='font-weight:bold'><a href='%1$s'>%4$s<span class='nav'>%2$s</span></a></div>%3$s",
									url, vsM.getDesc(), "\r\n", GetIcon(IconM2MDefault))));
							this.ItemCount++;
						} else {
							this.Pub1.append(BaseModel.AddLi(
									String.format("<div><a href='%1$s'>%4$s<span class='nav'>%2$s</span></a></div>%3$s",
											url, vsM.getDesc(), "\r\n", GetIcon(IconM2MDefault))));
							this.ItemCount++;
						}
					} else {
						if ((this.getAttrKey()).equals(vsM.getEnsOfMM().getClass().getName())) {
							this.Pub1.append(BaseModel.AddLi(String.format(
									"<div style='font-weight:bold'><a href='%1$s'>%5$s<span class='nav'>%2$s[%3$s]</span></a></div>%4$s",
									url, vsM.getDesc(), i, "\r\n", GetIcon(IconM2MDefault))));
							this.ItemCount++;
						} else {
							this.Pub1.append(BaseModel.AddLi(String.format(
									"<div><a href='%1$s'>%5$s<span class='nav'>%2$s[%3$s]</span></a></div>%4$s", url,
									vsM.getDesc(), i, "\r\n", GetIcon(IconM2MDefault))));
							this.ItemCount++;
						}
					}
				}
			}
			// #endregion

			// #region 加入它们的方法
			RefMethods myreffuncs = en.getEnMap().getHisRefMethods();
			for (RefMethod func : myreffuncs) {
				if (!func.Visable || null != func.RefAttrKey)
					continue;

				if (func.refMethodType != RefMethodType.Func) {
					String myurl = func.Do(null).toString();
					// int h = func.Height;

					if (func.refMethodType == RefMethodType.RightFrameOpen) {
						this.Pub1.append(BaseModel.AddLi(String.format(
								"<div><a href='javascript:void(0)' onclick=\"javascript:OpenUrlInRightFrame(this,'%1$s')\" title='%2$s'>%5$s<span class='nav'>%3$s</span></a></div>%4$s",
								myurl, StringHelper.isEmpty(func.ToolTip, ""), func.Title, "\r\n",
								GetBasePathIcon(func.Icon))));
						ItemCount++;
						continue;
					}
					if (func.Target == null || "".equals(func.Target)) {
						// this.AddLi(func.GetIcon(path) + "<a href='" + myurl +
						// "' ToolTip='" + func.ToolTip + "' >" + func.Title +
						// "</a>");
						this.Pub1.append(BaseModel.AddLi(String.format(
								"<div><a href='%1$s' title='%2$s'>%5$s<span class='nav'>%3$s</span></a></div>%4$s",
								myurl, StringHelper.isEmpty(func.ToolTip, ""), func.Title, "\r\n",
								GetBasePathIcon(func.Icon))));
						ItemCount++;
					} else {
						// this.AddLi(func.GetIcon(path) + "<a
						// href=\"javascript:WinOpen('" + myurl + "','" +
						// func.Target + "')\" ToolTip='" + func.ToolTip + "' >"
						// + func.Title + "</a>");
						this.Pub1.append(BaseModel.AddLi(String.format(
								"<div><a href=\"javascript:WinOpen('%1$s','%2$s')\" title='%3$s'>%6$s<span class='nav'>%4$s</span></a></div>%5$s",
								myurl, func.Target, StringHelper.isEmpty(func.ToolTip, ""), func.Title, "\r\n",
								GetBasePathIcon(func.Icon))));
						ItemCount++;
					}
					continue;
				}

				// string url = path + "/Comm/RefMethod.aspx?Index=" +
				// func.Index + "&EnsName=" + hisens.ToString() + keys;
				String url = basePath + "WF/Comm/RefMethod.jsp?Index=" + func.Index + "&EnsName="
						+ en.getGetNewEntities().getClass().getName() + keys;

				// string urlRefFunc = "RefMethod.aspx?Index=" + func.Index +
				// "&EnsName=" + en.GetNewEntities.ToString() + keys;
				if (func.Warning == null || "".equals(func.Warning)) {
					if (func.Target == null || "".equals(func.Target)) {
						// this.AddLi(func.GetIcon(path) + "<a href='" + url +
						// "' ToolTip='" + func.ToolTip + "' >" + func.Title +
						// "</a>");
						this.Pub1.append(BaseModel.AddLi(String.format(
								"<div><a href='%1$s' title='%2$s'>%5$s<span class='nav'>%3$s</span></a></div>%4$s", url,
								StringHelper.isEmpty(func.ToolTip, ""), func.Title, "\r\n",
								GetBasePathIcon(func.Icon))));
						ItemCount++;
					} else {
						// this.AddLi(func.GetIcon(path) + "<a
						// href=\"javascript:WinOpen('" + url + "','" +
						// func.Target + "')\" ToolTip='" + func.ToolTip + "' >"
						// + func.Title + "</a>");
						this.Pub1.append(BaseModel.AddLi(String.format(
								"<div><a href=\"javascript:WinOpen('%1$s','%2$s')\" title='%3$s'>%6$s<span class='nav'>%4$s</span></a></div>%5$s",
								url, func.Target, StringHelper.isEmpty(func.ToolTip, ""), func.Title, "\r\n",
								GetBasePathIcon(func.Icon))));
						ItemCount++;
					}
				} else {
					if (func.Target == null || "".equals(func.Target)) {
						// this.AddLi(func.GetIcon(path) + "<a
						// href=\"javascript: if ( confirm('" + func.Warning +
						// "') ) { window.location.href='" + url + "' }\"
						// ToolTip='" + func.ToolTip + "' >" + func.Title +
						// "</a>");
						this.Pub1.append(BaseModel.AddLi(String.format(
								"<div><a href=\"javascript: if ( confirm('%1$s')){{ window.location.href='%2$s' }}\" title='%3$s'>%6$s<span class='nav'>%4$s</span></a></div>%5$s",
								func.Warning, url, StringHelper.isEmpty(func.ToolTip, ""), func.Title, "\r\n",
								GetBasePathIcon(func.Icon))));
						ItemCount++;
					} else {
						// this.AddLi(func.GetIcon(path) + "<a
						// href=\"javascript: if ( confirm('" + func.Warning +
						// "') ) { WinOpen('" + url + "','" + func.Target + "')
						// }\" ToolTip='" + func.ToolTip + "' >" + func.Title +
						// "</a>");
						this.Pub1.append(BaseModel.AddLi(String.format(
								"<div><a href=\"javascript: if ( confirm('%1$s')){{ WinOpen('%2$s','%3$s') }}\" title='%4$s'>%7$s<span class='nav'>%5$s</span></a></div>%6$s",
								func.Warning, url, func.Target, StringHelper.isEmpty(func.ToolTip, ""), func.Title,
								"\r\n", GetBasePathIcon(func.Icon))));
						ItemCount++;
					}
				}
			}
			// #endregion

			// #region 加入他的明细
			EnDtls enDtls = en.getEnMap().getDtls();
			for (EnDtl enDtl : enDtls) {
				String url = basePath + "WF/Comm/RefFunc/Dtl.jsp?EnName=" + this.getEnName() + "&PK=" + this.getPK()
						+ "&EnsName=" + enDtl.getEns().getClass().getName() + "&RefKey=" + enDtl.getRefKey()
						+ "&RefVal=" + en.getPKVal().toString() + "&MainEnsName=" + en.getClass().getName() + keys;

				try {
					i = DBAccess.RunSQLReturnValInt(
							"SELECT COUNT(*) FROM " + enDtl.getEns().getGetNewEntity().getEnMap().getPhysicsTable()
									+ " WHERE " + enDtl.getRefKey() + "='" + en.getPKVal() + "'");
				} catch (Exception e) {
					try {
						i = DBAccess.RunSQLReturnValInt(
								"SELECT COUNT(*) FROM " + enDtl.getEns().getGetNewEntity().getEnMap().getPhysicsTable()
										+ " WHERE " + enDtl.getRefKey() + "=" + en.getPKVal());
					} catch (Exception ee) {
						enDtl.getEns().getGetNewEntity().CheckPhysicsTable();
					}
				}

				if (i == 0) {
					// this.AddLi("<a href=\"" + url + "\" >" + enDtl.Desc +
					// "</a>");
					this.Pub1.append(BaseModel
							.AddLi(String.format("<div><a href='%1$s'>%4$s<span class='nav'>%2$s</span></a></div>%3$s",
									url, enDtl.getDesc(), "\r\n", GetIcon(IconDtlDefault))));
					ItemCount++;
				} else {
					// this.AddLi("<a href=\"" + url + "\" >" + enDtl.Desc + "-"
					// + i + "</a>");
					this.Pub1.append(BaseModel.AddLi(
							String.format("<div><a href='%1$s'>%5$s<span class='nav'>%2$s [%3$s]</span></a></div>%4$s",
									url, enDtl.getDesc(), i, "\r\n", GetIcon(IconDtlDefault))));
					ItemCount++;
				}
			}
			// #endregion
			this.Pub1.append(BaseModel.AddULEnd());
		} catch (Exception e) {
			logger.info("ERROR", e);
			this.Pub1.append(BaseModel.AddMsgOfWarning("错误", e.getMessage()));
		}

	}

	/// <summary>
	/// 获取结点属性左侧功能菜单项默认前置图标
	/// <para></para>
	/// <para>根据本页中设置的ShowIconDefault与IconXXXDefault来生成</para>
	/// </summary>
	/// <param name="imgPath">图标的相对路径，空则为默认明细的图标</param>
	/// <returns></returns>
	private String GetIcon(String imgPath) {
		if (!this.ShowIconDefault)
			return "";

		imgPath = StringHelper.isNullOrEmpty(imgPath) ? IconDtlDefault : imgPath;
		if (!imgPath.contains("http")) {
			return "<img src='" + this.basePath + imgPath + "' width='16' border='0' />";
		} else {
			return "<img src='" + imgPath + "' width='16' border='0' />";
		}
	}

	private String GetBasePathIcon(String imgPath) {
		if (!this.ShowIconDefault)
			return "";

		imgPath = StringHelper.isNullOrEmpty(imgPath) ? IconDtlDefault : imgPath;
		if (!imgPath.contains("http")) {
			return "<img src='" + this.basePath + imgPath + "' width='16' border='0' />";
		} else {
			return "<img src='" + imgPath + "' width='16' border='0' />";
		}
	}

	public int getItemCount() {
		return ItemCount;
	}

	public void setItemCount(int itemCount) {
		ItemCount = itemCount;
	}

}

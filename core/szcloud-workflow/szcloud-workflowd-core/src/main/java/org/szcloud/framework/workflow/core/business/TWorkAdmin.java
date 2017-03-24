package org.szcloud.framework.workflow.core.business;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.workflow.core.base.CDept;
import org.szcloud.framework.workflow.core.base.CLogon;
import org.szcloud.framework.workflow.core.base.CSqlHandle;
import org.szcloud.framework.workflow.core.base.DeptManage;
import org.szcloud.framework.workflow.core.emun.EFileSaveType;
import org.szcloud.framework.workflow.core.emun.ELockType;
import org.szcloud.framework.workflow.core.emun.EUserType;
import org.szcloud.framework.workflow.core.emun.EWorkItemStatus;
import org.szcloud.framework.workflow.core.entity.CEntry;
import org.szcloud.framework.workflow.core.entity.CWorkItem;
import org.szcloud.framework.workflow.core.module.MFile;
import org.szcloud.framework.workflow.core.module.MGlobal;
import org.szcloud.framework.workflow.core.module.MXmlHandle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TWorkAdmin {

	/**
	 * 初始化
	 * 
	 * @param ao_Logon
	 *            登录对象
	 */
	public TWorkAdmin(CLogon ao_Logon) {
		this.Logon = ao_Logon;
	}

	// #==========================================================================#
	// 对象引用变量定义
	// #==========================================================================#
	/**
	 * 所属的环境对象
	 */
	public CLogon Logon = null;

	/**
	 * 部门管理目录结构树
	 */
	private static DeptManage mo_DeptManage = null;

	/**
	 * 获取部门目录树
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @return
	 */
	public static DeptManage getDeptManage(CLogon ao_Logon) {
		try {
			if (mo_DeptManage == null)
				mo_DeptManage = new DeptManage(ao_Logon);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return mo_DeptManage;
	}

	// #==========================================================================#
	// 常用过程或函数定义
	// #==========================================================================#

	/**
	 * 清除释放对象的内存数据
	 */
	public void ClearUp() {
		// 所属的环境对象
		this.Logon = null;

		// 部门目录树
		// mo_DeptManage = null;
	}

	/**
	 * 删除临时目录下的某些临时文件[不包含文件路径]
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_FileNames
	 *            文件名称
	 */
	public static void deleleTempFileName(CLogon ao_Logon, String as_FileNames) {
		if (MGlobal.isEmpty(as_FileNames))
			return;

		String[] ls_Array = as_FileNames.split("|");
		for (int i = 0; i < ls_Array.length; i++) {
			if (MGlobal.isExist(ls_Array[i])) {
				MFile.killFile(ao_Logon.GlobalPara.TempDataPath + ls_Array[i]);
			}
		}

	}

	/**
	 * 删除数据目录下的某些临时文件[不包含文件路径]
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_FileNames
	 *            文件名称
	 */
	public static void deleleDataFileName(CLogon ao_Logon, String as_FileNames) {
		if (MGlobal.isEmpty(as_FileNames))
			return;

		String[] ls_Array = as_FileNames.split("|");
		for (int i = 0; i < ls_Array.length; i++) {
			if (MGlobal.isExist(ls_Array[i])) {
				MFile.killFile(ao_Logon.GlobalPara.SaveDataPath + ls_Array[i]);
			}
		}
	}

	/**
	 * 获取公文文件存储路径
	 * 
	 * @param ao_Logon
	 * @param al_TempID
	 *            公文模板标识
	 * @param ai_Type
	 *            文件存储类型
	 * @param al_SaveId
	 *            文件存储位置标识
	 * @param ad_Date
	 *            日期
	 * @return
	 */
	public static String getFileSavePath(CLogon ao_Logon, int al_TempID,
			int ai_Type, int al_SaveId, Date ad_Date) {
		String ls_Path;
		if (ai_Type == EFileSaveType.RuleSaveType) {
			String ls_Sql = "SELECT FilePath FROM FileDataSavePath WHERE PathID = "
					+ String.valueOf(al_SaveId);
			ls_Path = CSqlHandle.getValueBySql(ls_Sql);
			if (MGlobal.isEmpty(ls_Path)) {
				ls_Path = ao_Logon.GlobalPara.SaveDataPath + "WorkItem\\Data"
						+ String.valueOf(al_TempID) + "\\";
			} else {
				ls_Path += "WorkFlowData\\WorkItem\\Data"
						+ String.valueOf(al_TempID) + "\\";
			}
		} else {
			ls_Path = ao_Logon.GlobalPara.SaveDataPath + "WorkItem\\Data"
					+ String.valueOf(al_TempID) + "\\";
			if (ai_Type == EFileSaveType.MonthSaveType) {
				ls_Path += MGlobal.dateToString(ad_Date, "yyyyMM\\");
			} else if (ai_Type == EFileSaveType.YearSaveType) {
				ls_Path += MGlobal.dateToString(ad_Date, "yyyyMM\\");
			} else {// DefaultSaveType
			}
		}
		return ls_Path;
	}

	/**
	 * 根据公文实例模板标识获取公文文件存储路径(不可用)
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param al_TemplateID
	 *            公文实例模板标识
	 * @param al_Index
	 *            公文历史标识
	 * @return
	 */
	public static String getFileSavePathByTempId(CLogon ao_Logon,
			int al_TemplateID, int al_Index) {
		try {
			String ls_Sql = "SELECT TemplateID, FileSaveType, FileSaveID, FileSaveDate FROM TemplateDefine";
			if (al_Index > 0)
				ls_Sql += String.valueOf(al_Index);
			ls_Sql += " WHERE TemplateID = " + String.valueOf(al_TemplateID);
			List<Map<String,Object>> lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			ls_Sql = "";
			if (lo_Set.size()>0) {
				if (MGlobal.readObject(lo_Set.get(0),"FileSaveType") == null) {
					ls_Sql = getFileSavePath(ao_Logon, al_TemplateID,
							EFileSaveType.DefaultSaveType, 0, MGlobal.getNow());
				} else {
					ls_Sql = getFileSavePath(ao_Logon, al_TemplateID,
							MGlobal.readInt(lo_Set.get(0),"FileSaveType"),
							MGlobal.readInt(lo_Set.get(0),"FileSaveID"),
							MGlobal.readDate(lo_Set.get(0),"FileSaveDate"));
				}
			}
			lo_Set = null;
			return ls_Sql;
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	/**
	 * 根据公文实例标识获取公文文件存储路径
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param al_WorkItemID
	 *            公文实例标识
	 * @param al_Index
	 *            公文历史标识
	 * @return
	 */
	public static String getFileSavePathByItemId(CLogon ao_Logon,
			int al_WorkItemID, int al_Index) {
		try {
			String ls_Sql = "SELECT TemplateID, FileSaveType, FileSaveID, FileSaveDate FROM TemplateInst";
			if (al_Index > 0)
				ls_Sql += String.valueOf(al_Index);
			ls_Sql += " WHERE WorkItemID = " + String.valueOf(al_WorkItemID);
			List<Map<String,Object>> lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			ls_Sql = "";
			if (lo_Set.size()>0) {
				if (MGlobal.readObject(lo_Set.get(0),"FileSaveType") == null) {
					ls_Sql = getFileSavePath(ao_Logon,
							MGlobal.readInt(lo_Set.get(0),"TemplateID"),
							EFileSaveType.DefaultSaveType, 0, MGlobal.getNow());
				} else {
					ls_Sql = getFileSavePath(ao_Logon,
							MGlobal.readInt(lo_Set.get(0),"TemplateID"),
							MGlobal.readInt(lo_Set.get(0),"FileSaveType"),
							MGlobal.readInt(lo_Set.get(0),"FileSaveID"),
							MGlobal.readDate(lo_Set.get(0),"FileSaveDate"));
				}
			}
			lo_Set = null;
			return ls_Sql;
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	/**
	 * 读取某一个附件内容
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param ai_Type
	 *            附件类型
	 * @param al_ItemID
	 *            公文标识
	 * @param al_DocumentID
	 *            附件标识
	 * @return
	 */
	public static byte[] readDocumentContent(CLogon ao_Logon, int ai_Type,
			int al_ItemID, int al_DocumentID) {
		try {
			String ls_Sql = "";

			if (ai_Type == 0) {
				ls_Sql = "SELECT * FROM DocumentList WHERE TemplateID = "
						+ String.valueOf(al_ItemID) + " AND DocumentID = "
						+ String.valueOf(al_DocumentID);
			} else {
				ls_Sql = "SELECT DocumentInst.*, TemplateInst.TemplateID FROM DocumentInst, TemplateInst WHERE DocumentInst.WorkItemID = "
						+ String.valueOf(al_ItemID)
						+ " AND DocumentInst.DocumentID = "
						+ String.valueOf(al_DocumentID)
						+ " AND DocumentInst.WorkItemID = TemplateInst.WorkItemID";
			}
			List<Map<String,Object>> lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			int ll_ErrNumber = 0;
			String ls_FileName = "";
			if (lo_Set.size()>0) {
				// 错误：您所要读取的文件已被删除
				if (MGlobal.readInt(lo_Set.get(0),"DocumentType") == -2)
					ll_ErrNumber = 1104;
				// 错误：您所要读取的附件为非文件型附件
				if (MGlobal.readInt(lo_Set.get(0),"DocumentType") == 7
						|| MGlobal.readInt(lo_Set.get(0),"DocumentType") == 8)
					ll_ErrNumber = 1105;
			} else {
				// 错误：您所要读取的文件不存在
				ll_ErrNumber = 1103;
			}

			if (ll_ErrNumber > 0) {
				lo_Set = null;
				ao_Logon.Raise(
						ll_ErrNumber,
						"TWorkAdmin->ReadDocumentToTemp",
						String.valueOf(al_ItemID) + "."
								+ String.valueOf(al_DocumentID));
				return null;
			}

			if (ai_Type == 0) {
				ls_FileName = ao_Logon.GlobalPara.SaveDataPath
						+ "Template\\Data" + MGlobal.readString(lo_Set.get(0),"TemplateID")
						+ "\\File" + MGlobal.readString(lo_Set.get(0),"TemplateID") + "_"
						+ MGlobal.readString(lo_Set.get(0),"DocumentID") + "."
						+ MFile.getFileExt(MGlobal.readString(lo_Set.get(0),"FileName"));
			} else {
				ls_FileName = "WorkItem\\Data" + MGlobal.readString(lo_Set.get(0),"TemplateID")
						+ "\\File" + MGlobal.readString(lo_Set.get(0),"TemplateID") + "_"
						+ MGlobal.readString(lo_Set.get(0),"DocumentID") + "."
						+ MFile.getFileExt(MGlobal.readString(lo_Set.get(0),"FileName"));

				ls_FileName = getFileSavePathByItemId(ao_Logon, al_ItemID, -1)
						+ "File" + MGlobal.readString(lo_Set.get(0),"WorkItemID") + "_"
						+ MGlobal.readString(lo_Set.get(0),"DocumentID") + "."
						+ MFile.getFileExt(MGlobal.readString(lo_Set.get(0),"FileName"));
			}
			lo_Set = null;

			return MFile.readFile(ls_FileName);
		} catch (Exception ex) {
			ao_Logon.Raise(
					ex,
					"TWorkAdmin->ReadDocumentContent",
					String.valueOf(al_ItemID) + "."
							+ String.valueOf(al_DocumentID));
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 读取某一个附件到临时文件夹
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param ai_Type
	 *            类型，=0为公文模板附件；=1为公文实例附件
	 * @param al_ItemID
	 *            公文模板或公文实例标识
	 * @param al_DocumentID
	 *            附件标识
	 * @return
	 */
	public static String readDocumentToTemp(CLogon ao_Logon, int ai_Type,
			int al_ItemID, int al_DocumentID) {
		return readDocumentToTempFile(ao_Logon, ai_Type, al_ItemID,
				al_DocumentID, "");
	}

	/**
	 * 读取某一个附件到临时文件夹
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param ai_Type
	 *            类型，=0为公文模板附件；=1为公文实例附件
	 * @param al_ItemID
	 *            公文模板或公文实例标识
	 * @param al_DocumentID
	 *            附件标识
	 * @param as_TempFile
	 *            临时文件
	 * @return
	 */
	public static String readDocumentToTempFile(CLogon ao_Logon, int ai_Type,
			int al_ItemID, int al_DocumentID, String as_TempFile) {
		try {
			String ls_Sql = "";
			int ll_ErrNumber = 0;
			String ls_FileName = "";
			String ls_Prop = "0";

			if (ai_Type == 0) {
				ls_Sql = "SELECT * FROM DocumentList WHERE TemplateID = "
						+ String.valueOf(al_ItemID) + " AND DocumentID = "
						+ String.valueOf(al_DocumentID);
			} else {
				ls_Sql = "SELECT WorkItemProp FROM WorkItemInst WHERE WorkItemID = "
						+ String.valueOf(al_ItemID);
				ls_Sql = CSqlHandle.getValueBySql(ls_Sql);
				if (MGlobal.isExist(ls_Sql))
					ls_Prop = ls_Sql;

				ls_Sql = "SELECT DocumentInst.*, TemplateInst.TemplateID FROM DocumentInst, TemplateInst WHERE DocumentInst.WorkItemID = "
						+ String.valueOf(al_ItemID)
						+ " AND DocumentInst.DocumentID = "
						+ String.valueOf(al_DocumentID)
						+ " AND DocumentInst.WorkItemID = TemplateInst.WorkItemID";
			}

			List<Map<String,Object>> lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			if (lo_Set.size()>0) {
				// 错误：您所要读取的文件已被删除
				if (MGlobal.readInt(lo_Set.get(0),"DocumentType") == -2)
					ll_ErrNumber = 1104;

				// 错误：您所要读取的附件为非文件型附件
				if (MGlobal.readInt(lo_Set.get(0),"DocumentType") == 7
						|| MGlobal.readInt(lo_Set.get(0),"DocumentType") == 8)
					ll_ErrNumber = 1105;
			} else {
				// 错误：您所要读取的文件不存在
				ll_ErrNumber = 1103;
			}

			if (ll_ErrNumber > 0) {
				lo_Set = null;
				ao_Logon.Raise(ll_ErrNumber, "TWorkAdmin->readDocumentToTemp",
						"");
				return "";
			}

			if (ai_Type == 0) {
				ls_FileName = "Template\\Data" + MGlobal.readString(lo_Set.get(0),"TemplateID")
						+ "\\File" + MGlobal.readString(lo_Set.get(0),"TemplateID") + "_"
						+ MGlobal.readString(lo_Set.get(0),"DocumentID") + "."
						+ MFile.getFileExt(MGlobal.readString(lo_Set.get(0),"FileName"));
				if (MGlobal.readInt(lo_Set.get(0),"DocumentType") == 10) { // JPG扫描文件[不存在该种情况]
					ls_Sql = readFileDataToTemp(ao_Logon, ls_FileName,
							MGlobal.readInt(lo_Set.get(0),"FilePages"), "", as_TempFile);
				} else {
					ls_Sql = readFileDataToTemp(ao_Logon, ls_FileName, 0, "",
							as_TempFile);
				}
			} else {
				if (ls_Prop.equals("1") && MGlobal.readObject(lo_Set.get(0),"FilePath") != null) {
					ls_FileName = MGlobal.readString(lo_Set.get(0),"FilePath");
					if (MGlobal.readInt(lo_Set.get(0),"DocumentType") == 10) {// JPG扫描文件
						ls_Sql = readFileDataToTemp(ao_Logon, ls_FileName,
								MGlobal.readInt(lo_Set.get(0),"FilePages"),
								ao_Logon.getParaValue("EXFLOW_DATA_PATH"),
								as_TempFile);
					} else {
						ls_Sql = readFileDataToTemp(ao_Logon, ls_FileName, 0,
								ao_Logon.getParaValue("EXFLOW_DATA_PATH"),
								as_TempFile);
					}
				} else {
					ls_FileName = "WorkItem\\Data"
							+ MGlobal.readString(lo_Set.get(0),"TemplateID") + "\\File";
					ls_FileName = getFileSavePathByItemId(ao_Logon,
							MGlobal.readInt(lo_Set.get(0),"WorkItemID"), -1)
							+ "File";
					ls_FileName = MGlobal
							.right(ls_FileName, ls_FileName.length()
									- ao_Logon.GlobalPara.SaveDataPath.length())
							+ MGlobal.readString(lo_Set.get(0),"WorkItemID")
							+ "_"
							+ MGlobal.readString(lo_Set.get(0),"DocumentID")
							+ "."
							+ MFile.getFileExt(MGlobal.readString(lo_Set.get(0),"FileName"));
					if (MGlobal.readInt(lo_Set.get(0),"DocumentType") == 10) {// JPG扫描文件
						ls_Sql = readFileDataToTemp(ao_Logon, ls_FileName,
								MGlobal.readInt(lo_Set.get(0),"FilePages"), "", as_TempFile);
					} else {
						ls_Sql = readFileDataToTemp(ao_Logon, ls_FileName, 0,
								"", as_TempFile);
					}
				}
			}
			lo_Set = null;
			return ls_Sql;
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWordAdmin->readDocumentToTempFile", "");
			ex.printStackTrace();
			return "";
		}
	}

	/**
	 * 读取服务器端一个附件到临时文件夹
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_FileName
	 *            实际文件名称[不包括总路径，但包括相对路径，如：WorkItem\File12_3.doc]
	 * @param ai_Count
	 *            数量
	 * @param as_Path
	 *            文件路径
	 * @param as_TempFile
	 *            临时文件
	 * @return 返回一个临时文件，位于临时文件夹目录下
	 */
	private static String readFileDataToTemp(CLogon ao_Logon,
			String as_FileName, int ai_Count, String as_Path, String as_TempFile) {
		try {

			String ls_File = (MGlobal.isEmpty(as_Path) ? ao_Logon.GlobalPara.SaveDataPath
					: as_Path)
					+ as_FileName;
			if (!MFile.existFile(ls_File)) {
				// 错误：不存在所要的文件，可能该文件已被删除或移动，请与系统管理员联系
				ao_Logon.Raise(1047, "TWorkAdmin->readFileDataToTemp", "");
				return "";
			}

			String ls_Temp = "";
			if (MGlobal.isEmpty(as_TempFile))
				ls_Temp = MFile.getTempFile(ls_File);

			if (MGlobal.isEmpty(ls_Temp)) {
				// 错误：取临时文件失败，请重新操作，如还存在问题，请与系统管理员联系
				ao_Logon.Raise(1048, "TWorkAdmin->readFileDataToTemp", "");
				return "";
			}

			MFile.copyFile(ls_File, ao_Logon.GlobalPara.TempDataPath + ls_Temp);

			String ls_Name = "";
			if (ai_Count > 1) {
				String ls_Ext = "." + MFile.getFileExt(ls_File);
				ls_Name = ls_File.substring(0,
						ls_File.length() - ls_Ext.length());
				for (int i = 2; i <= ai_Count; i++) {
					MFile.copyFile(
							ls_Name + "_" + String.valueOf(i) + ls_Ext,
							ao_Logon.GlobalPara.TempDataPath
									+ ls_Temp.replace(".",
											"_" + String.valueOf(i) + "."));
				}
			}

			return ls_Temp;
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWordAdmin->readFileDataToTemp", "");
			ex.printStackTrace();
			return "";
		}
	}

	/**
	 * 读取某一个文件模板的文件到一个临时文件目录，并返回临时文件名称
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_FileName
	 *            文件模板名称（包括相对路径）
	 * @return
	 */
	public static String readFileTemplateToTemp(CLogon ao_Logon,
			String as_FileName) {
		try {
			String ls_Temp = MFile.getTempFile(MFile.getFileExt(as_FileName),
					ao_Logon.GlobalPara.TempDataPath);
			MFile.copyFile(ao_Logon.GlobalPara.SaveDataPath + "Template\\"
					+ as_FileName, ls_Temp);

			return MGlobal.right(ls_Temp, ls_Temp.length()
					- ao_Logon.GlobalPara.TempDataPath.length());
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWordAdmin->readFileTemplateToTemp",
					as_FileName);
			ex.printStackTrace();
			return "";
		}
	}

	/**
	 * 读取某一组文件模板的文件到一个临时文件目录，并返回临时文件名称
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_FileName
	 *            文件模板名称（包括相对路径）
	 * @param ai_Count
	 *            数量
	 * @return
	 */
	public static String readFilesTemplateToTemp(CLogon ao_Logon,
			String as_FileName, int ai_Count) {
		try {
			String ls_Temp = MFile.getTempFile(MFile.getFileExt(as_FileName),
					ao_Logon.GlobalPara.TempDataPath);
			String ls_TempName = MGlobal.right(ls_Temp, ls_Temp.length()
					- ao_Logon.GlobalPara.TempDataPath.length());
			MFile.copyFile(ao_Logon.GlobalPara.SaveDataPath + "Template\\"
					+ as_FileName, ls_Temp);
			for (int i = 2; i <= ai_Count; i++) {
				MFile.copyFile(
						ao_Logon.GlobalPara.SaveDataPath
								+ "Template\\"
								+ as_FileName.replace(".",
										"_" + String.valueOf(i) + "."),
						ao_Logon.GlobalPara.TempDataPath
								+ ls_TempName.replace(".",
										"_" + String.valueOf(i) + "."));
			}

			return ls_TempName;
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWordAdmin->readFilesTemplateToTemp",
					as_FileName);
			ex.printStackTrace();
			return "";
		}
	}

	/**
	 * 取公文使用权限
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_TypeIDs
	 *            分类标识连接串，使用“;”分隔
	 * @param as_TemplateIDs
	 *            模板标识连接串，使用“;”分隔
	 * @param ai_GetType
	 *            取权限类型方式：=0-使用Or方式，=1-使用And方式
	 * @return 返回权限定义的XML字符串
	 */
	public static String getUsingRight(CLogon ao_Logon, String as_TypeIDs,
			String as_TemplateIDs, int ai_GetType) {
		try {
			if ((ao_Logon.GlobalPara.UserType & EUserType.SystemAdministrator) == EUserType.SystemAdministrator)
				return "<Right All=\"1\" />";

			int[] li_Rights = new int[7];
			for (int i = 0; i < 7; i++) {
				li_Rights[i] = 0;
			}

			String ls_Sql = "";
			String ls_Cond = "";
			if (MGlobal.isExist(as_TypeIDs)) {
				ls_Cond = as_TypeIDs.replace(";", ",");
				if (ls_Cond.substring(ls_Cond.length() - 1).equals(",")) {
					ls_Cond = ls_Cond.substring(0, ls_Cond.length() - 1);
				}
				ls_Cond = " AND (ModuleID IN (" + ls_Cond
						+ ", 0) AND AdminType = 0)";
				ls_Sql = ls_Cond;
			}

			if (MGlobal.isExist(as_TemplateIDs)) {
				ls_Cond = as_TemplateIDs.replace(";", ",");
				if (ls_Cond.substring(ls_Cond.length() - 1).equals(",")) {
					ls_Cond = ls_Cond.substring(0, ls_Cond.length() - 1);
				}
				ls_Cond = " AND (ModuleID IN (" + ls_Cond
						+ ") AND AdminType = 1)";
				ls_Sql = ls_Sql + ls_Cond;
			}

			ls_Sql = "SELECT * FROM WorkRight WHERE (UserID = "
					+ ao_Logon.getUserID()
					+ " OR UserID IN (SELECT GroupID FROM UserGroup WHERE UserID = "
					+ ao_Logon.getUserID() + "))" + ls_Cond;
			List<Map<String,Object>> lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			for(Map<String,Object> row : lo_Set) {
				if (MGlobal.readInt(row,"ReadRight") == 1)
					li_Rights[0] = 1;

				if (MGlobal.readInt(row,"CreateRight") == 1)
					li_Rights[1] = 1;

				if (MGlobal.readInt(row,"DeleteRight") == 1)
					li_Rights[2] = 1;

				if (MGlobal.readInt(row,"QueryRight") == 1)
					li_Rights[3] = 1;

				if (MGlobal.readInt(row,"FileRight") == 1)
					li_Rights[4] = 1;

				if (MGlobal.readInt(row,"TailRight") == 1)
					li_Rights[5] = 1;

				if (MGlobal.readInt(row,"AllRight") == 1)
					li_Rights[6] = 1;
			}
			lo_Set = null;

			return "<Right All=\"" + String.valueOf(li_Rights[6]) + "\""
					+ " Read=\"" + String.valueOf(li_Rights[0]) + "\""
					+ " Create=\"" + String.valueOf(li_Rights[1]) + "\""
					+ " Delete=\"" + (li_Rights[2]) + "\"" + " Query=\""
					+ String.valueOf(li_Rights[3]) + "\"" + " File=\""
					+ String.valueOf(li_Rights[4]) + "\"" + " Tail=\""
					+ String.valueOf(li_Rights[5]) + "\" />";
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWordAdmin->getUsingRight", "");
			ex.printStackTrace();
			return "";
		}
	}

	//
	// as_WorkItemIDs
	/**
	 * 修改公文的流转状态
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_WorkItemIds
	 *            公文实例标识的连接串，使用“;”分隔
	 * @param ai_Status
	 *            状态
	 * @return
	 */
	public static boolean editWorkItemStatus(CLogon ao_Logon,
			String as_WorkItemIds, int ai_Status) {
		try {
			if (MGlobal.isEmpty(as_WorkItemIds))
				return false;

			// 判断是否具有权限
			String ls_Sql = "";
			String ls_TempIDs = "";
			String ls_TypeIDs = "";
			String ls_Cond = as_WorkItemIds.replace(";", ",");
			if (ls_Cond.substring(ls_Cond.length() - 1).equals(","))
				ls_Cond = ls_Cond.substring(0, ls_Cond.length() - 1);

			List<Map<String,Object>> lo_Set;
			if ((ao_Logon.GlobalPara.UserType & EUserType.SystemAdministrator) != EUserType.SystemAdministrator) {
				ls_Sql = "SELECT ModuleID, AdminType FROM WorkRight WHERE AllRight = 1 AND ((ModuleID IN ("
						+ "SELECT TemplateID FROM TemplateInst WHERE WorkItemID IN ("
						+ ls_Cond
						+ ")) AND AdminType = 1) OR (ModuleID IN (SELECT TypeID FROM TemplateInst"
						+ " WHERE WorkItemID IN ("
						+ ls_Cond
						+ ")) AND AdminType = 0) OR (AdminType = 0 AND ModuleID = 0))";
			}
			lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			//boolean lb_Boolean = lo_Set.size()>0;
			if (lo_Set == null || lo_Set.size()<=0) {
				lo_Set = null;
				// 错误：没有权限修改公文实例状态
				ao_Logon.Raise(1049, "TWorkAdmin->EditWorkItemStatus", "");
				return false;
			}
			for(Map<String,Object> row : lo_Set) {
				if (MGlobal.readInt(row,"AdminType") == 1)
					ls_TempIDs += MGlobal.readString(row,"ModuleID") + ",";
				if (MGlobal.readInt(row,"AdminType") == 0)
					ls_TypeIDs += MGlobal.readString(row,"ModuleID") + ",";

			}
			lo_Set = null;

			// 状态修改
			ls_Sql = "UPDATE WorkItemInst SET WorkItemStatus = "
					+ String.valueOf(ai_Status);
			if (ai_Status == EWorkItemStatus.HaveFinished) {
				ls_Sql += ", IsFinished = 1, FinishedDate = GETDATE()";
			} else {
				ls_Sql += ", IsFinished = 0, FinishedDate = NULL";
			}
			ls_Sql += " WHERE WorkItemID IN (" + ls_Cond + ")";
			if ((ao_Logon.GlobalPara.UserType & EUserType.SystemAdministrator) != EUserType.SystemAdministrator) {
				if (("," + ls_TypeIDs).indexOf(",0,") == -1) {
					ls_Cond = "SELECT WorkItemID FROM TemplateInst WHERE TemplateID IN ("
							+ ls_TempIDs
							+ "-1)"
							+ " OR TypeID IN ("
							+ ls_TypeIDs
							+ "-1) AND WorkItemID IN ("
							+ ls_Cond
							+ ")";
				}
			}
			CSqlHandle.execSql(ls_Sql);
			ls_Sql = "UPDATE WorkItemInst SET LastUpdateType = (LastUpdateType | 1), LastUpdateDate = GETDATE() WHERE WorkItemID IN ("
					+ ls_Cond + ")";
			CSqlHandle.execSql(ls_Sql);

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 取公文代理[授权]内容
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param ai_Type
	 *            类型：=1-取本人授权给别人的内容，=2-取别人授权给本人的内容
	 * @param as_Condition
	 *            限制条件
	 * @return
	 */
	public static List<Map<String,Object>> getProxyInfo(CLogon ao_Logon, int ai_Type,
			String as_Condition) {
		try {
			String ls_Sql = "";
			switch (ai_Type) {
			case 1:
				ls_Sql = "SELECT * FROM ProxyInfo WHERE UserID = "
						+ ao_Logon.getUserID();
				break;
			case 2:
				ls_Sql = "SELECT * FROM ProxyInfo WHERE ProxyID = "
						+ ao_Logon.getUserID();
				break;
			default:
				return null;
			}
			if (MGlobal.isExist(as_Condition))
				ls_Sql += " AND (" + as_Condition + ")";

			List<Map<String,Object>> lo_Set = CSqlHandle.getSetBySql(ls_Sql, false);
			return lo_Set;
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWorkAdmin->GetProxyInfo", "");
			return null;
		}
	}

	/**
	 * 取公文代理[授权]内容XML
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param ai_Type
	 *            类型：=1-取本人授权给别人的内容，=2-取别人授权给本人的内容
	 * @param as_Condition
	 *            限制条件
	 * @return
	 */
	public static String getProxyInfoXml(CLogon ao_Logon, int ai_Type,
			String as_Condition) {
		try {
			String ls_Sql = "";
			switch (ai_Type) {
			case 1:
				ls_Sql = "SELECT * FROM WorkItemProxyDefine WHERE UserID = "
						+ ao_Logon.getUserID();
				break;
			case 2:
				ls_Sql = "SELECT * FROM WorkItemProxyDefine WHERE ProxyID = "
						+ ao_Logon.getUserID();
				break;
			default:
				return "";
			}
			if (MGlobal.isExist(as_Condition))
				ls_Sql += " AND (" + as_Condition + ")";

			return CSqlHandle.getXmlBySql(ls_Sql);
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWorkAdmin->getProxyInfoXml", "");
			ex.printStackTrace();
			return "";
		}
	}

	//
	// as_ProxyXML的”
	/**
	 * 插入公文代理[授权]
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_ProxyXml
	 *            更新的Xml内容，格式：<Proxy ModuleID='*' ProxyType='*'
	 *            StartDate='2002-2-13' StopDate='2002-3-21' ProxyID='*' />...
	 * @return
	 */
	public static boolean insertProxyInfo(CLogon ao_Logon, String as_ProxyXml) {
		try {
			// 记录日志
			ao_Logon.Record("TWorkAdmin->InsertProxyInfo", "");

			Document lo_Xml = MXmlHandle.LoadXml(as_ProxyXml);
			if (lo_Xml == null)
				return false;
			Element lo_Node = lo_Xml.getDocumentElement();

			String ls_Sql = "INSERT INTO ProxyInfo (ModuleID, ProxyType, UserID, StartDate, StopDate, ProxyID, ProxyStatus, ProxyDate, ConfirmDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

			List parasList = new ArrayList();
			parasList.add(Integer.parseInt(lo_Node.getAttribute("ModuleID")));
			parasList.add(Integer.parseInt(lo_Node.getAttribute("ProxyType")));
			parasList.add(ao_Logon.GlobalPara.UserID);
			parasList.add(MGlobal.stringToDate(lo_Node
					.getAttribute("StartDate")));
			parasList.add(MGlobal.stringToDate(lo_Node.getAttribute("StopDate")
					+ " 23:59:59"));
			parasList.add(Integer.parseInt(lo_Node.getAttribute("ProxyID")));
			parasList.add(0);
			parasList.add(MGlobal.getNow());
			parasList.add(null);

			return CSqlHandle.getJdbcTemplate().update(ls_Sql,
					parasList.toArray()) > 0;
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWorkAdmin->InsertProxyInfo", "");
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除公文代理[授权]
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_Condition
	 *            删除条件
	 * @return
	 */
	public static boolean deleteProxyInfo(CLogon ao_Logon, String as_Condition) {
		try {
			// 记录日志
			ao_Logon.Record("TWorkAdmin->InsertProxyInfo", as_Condition);

			String ls_Sql = "DELETE FROM ProxyInfo WHERE UserID = "
					+ ao_Logon.getUserID();
			if (MGlobal.isExist(as_Condition))
				ls_Sql += " AND (" + as_Condition + ")";

			return CSqlHandle.execSql(ls_Sql);
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWorkAdmin->deleteProxyInfo", "");
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 接受或拒绝公文代理
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_ProxyXml
	 *            授权数据，格式：<Proxy ModuleID='*' ProxyType='*' UserID='*' />
	 * @param ai_HandleType
	 *            处理类型，=0-接受；=1-拒绝；
	 * @return
	 */
	public static boolean transactProxyInfo(CLogon ao_Logon,
			String as_ProxyXml, int ai_HandleType) {
		try {
			// 记录日志
			ao_Logon.Record("TWorkAdmin->TransactProxyInfo",
					String.valueOf(ai_HandleType));

			Document lo_Xml = MXmlHandle.LoadXml(as_ProxyXml);
			if (lo_Xml == null)
				return false;

			Element lo_Node = lo_Xml.getDocumentElement();
			String ls_Sql = "UPDATE ProxyInfo SET ProxyStatus = "
					+ (ai_HandleType == 0 ? "1" : "2")
					+ " WHERE ProxyStatus = 0" + "UserID = "
					+ lo_Node.getAttribute("UserID") + " AND ModuleID = "
					+ lo_Node.getAttribute("ModuleID") + " AND ProxyType = "
					+ lo_Node.getAttribute("ProxyType");

			return CSqlHandle.execSql(ls_Sql);
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWorkAdmin->transactProxyInfo", "");
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 读取公文模板或分类的使用权限结果集
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_Condition
	 *            查询条件
	 * @return
	 */
	public static List<Map<String,Object>> getWorkRight(CLogon ao_Logon, String as_Condition) {
		try {
			String ls_Sql = "SELECT * FROM WorkRight";
			if (MGlobal.isExist(as_Condition))
				ls_Sql += " WHERE " + as_Condition;

			return CSqlHandle.getSetBySql(ls_Sql, false);
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWorkAdmin->getWorkRight", as_Condition);
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 读取公文模板或分类的使用权限的XML字符串
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param ai_AdminType
	 *            读取类型，=0-分类，=1-模板
	 * @param al_ItemID
	 *            分类或模板标识
	 * @return
	 */
	public static String getWorkRightXml(CLogon ao_Logon, int ai_AdminType,
			int al_ItemID) {
		try {
			String ls_Sql = "SELECT WorkRight.*, UserInfo.UserName FROM WorkRight, UserInfo "
					+ "WHERE WorkRight.UserID = UserInfo.UserID AND WorkRight.ModuleID = "
					+ String.valueOf(al_ItemID)
					+ " AND WorkRight.AdminType = "
					+ String.valueOf(ai_AdminType);
			return CSqlHandle.getXmlBySql(ls_Sql);
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWorkAdmin->getWorkRightXml", "");
			ex.printStackTrace();
			return "";
		}
	}

	/**
	 * 插入或更新公文模板或分类的使用权限
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_WorkRightXml
	 *            权限定义，格式：<Right UserIDs='*1;*2;...' ModuleID='*'
	 *            AdminType='0/1' ReadRight='0/1' CreateRight='0/1'
	 *            DeleteRight='0/1' QueryRight='0/1' FileRight='0/1'
	 *            TailRight='0/1' OtherRight='***' AllRight='0/1' />
	 * @return
	 */
	public static boolean updateWorkRight(CLogon ao_Logon,
			String as_WorkRightXml) {
		try {
			// 记录日志
			ao_Logon.Record("TWorkAdmin->UpdateWorkRight", as_WorkRightXml);

			Document lo_Xml = MXmlHandle.LoadXml(as_WorkRightXml);
			if (lo_Xml == null)
				return false;

			Element lo_Node = lo_Xml.getDocumentElement();
			String[] ls_UserID = lo_Node.getAttribute("UserIDs").split(";");
			String ls_Sql1 = "DELETE FROM WorkRight WHERE UserID = {0} AND ModuleID = {1} AND AdminType = {2};";
			String ls_Sql2 = "INSERT INTO WorkRight (UserID, ModuleID, AdminType, ReadRight, CreateRight, DeleteRight, "
					+ "QueryRight, FileRight, TailRight, AppRight, OtherRight, AllRight, DeptIDS, UserIDS, FlagID) VALUES "
					+ "({0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}, {9}, {10}, {11}, {12}, {13}, {14});";
			String ls_Sqls = "";
			for (int i = 0; i < ls_UserID.length; i++) {
				if (MGlobal.isExist(ls_UserID[i])) {
					ls_Sqls += MessageFormat.format(ls_Sql1, ls_UserID[i],
							lo_Node.getAttribute("ModuleID"),
							lo_Node.getAttribute("AdminType"));
					ls_Sqls += MessageFormat.format(ls_Sql2, ls_UserID[i],
							lo_Node.getAttribute("ModuleID"), lo_Node
									.getAttribute("AdminType"), lo_Node
									.getAttribute("ReadRight"), lo_Node
									.getAttribute("CreateRight"), lo_Node
									.getAttribute("DeleteRight"), lo_Node
									.getAttribute("QueryRight"), lo_Node
									.getAttribute("FileRight"), lo_Node
									.getAttribute("TailRight"), lo_Node
									.getAttribute("AppRight"), lo_Node
									.getAttribute("OtherRight"), lo_Node
									.getAttribute("AllRight"), (lo_Node
									.getAttribute("DeptIDS").equals("") ? null
									: "'" + lo_Node.getAttribute("DeptIDS")
											+ "'"),
							(lo_Node.getAttribute("UserIDS").equals("") ? null
									: "'" + lo_Node.getAttribute("UserIDS")
											+ "'"), lo_Node
									.getAttribute("FlagID"));
				}
			}
			lo_Xml = null;

			return CSqlHandle.execSql(ls_Sqls, ";");
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWorkAdmin->getWorkRightXml", "");
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除公文模板或分类的使用权限
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_WorkRightXml
	 *            权限定义Xml，格式：<Right UserIDs='*1;*2;...' ModuleID='*'
	 *            AdminType='0/1' />
	 * @return
	 */
	public static boolean deleteWorkRight(CLogon ao_Logon,
			String as_WorkRightXml) {
		try {
			// 记录日志
			ao_Logon.Record("TWorkAdmin->DeleteWorkRight", as_WorkRightXml);

			Document lo_Xml = MXmlHandle.LoadXml(as_WorkRightXml);
			if (lo_Xml == null)
				return false;

			Element lo_Node = lo_Xml.getDocumentElement();
			String ls_Value = lo_Node.getAttribute("UserIDs").replaceAll(";",
					",");
			if (MGlobal.right(ls_Value, 1).equals(","))
				ls_Value = ls_Value.substring(0, ls_Value.length() - 1);

			ls_Value = "DELETE FROM WorkRight WHERE UserID IN (" + ls_Value
					+ ") AND ModuleID = " + lo_Node.getAttribute("ModuleID")
					+ " AND AdminType = " + lo_Node.getAttribute("AdminType");

			return CSqlHandle.execSql(ls_Value, ";");
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWorkAdmin->deleteWorkRight", "");
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 读取系统角色定义结果集
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_Condition
	 *            限制条件
	 * @return
	 */
	public static List<Map<String,Object>> getSystemRole(CLogon ao_Logon, String as_Condition) {
		try {
			String ls_Sql = "SELECT * FROM UserPosition";
			if (MGlobal.isExist(as_Condition))
				ls_Sql += " WHERE " + as_Condition;

			return CSqlHandle.getSetBySql(ls_Sql);
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWorkAdmin->getSystemRole", as_Condition);
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 读取系统角色定义Xml
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_Condition
	 *            限制条件
	 * @return
	 */
	public static String getSystemRoleXml(CLogon ao_Logon, String as_Condition) {
		try {
			String ls_Sql = "SELECT * FROM UserPosition";
			if (MGlobal.isExist(as_Condition))
				ls_Sql += " WHERE " + as_Condition;

			return CSqlHandle.getXmlBySql(ls_Sql);
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWorkAdmin->getSystemRoleXml", as_Condition);
			ex.printStackTrace();
			return "";
		}
	}

	/**
	 * 插入系统角色
	 * 
	 * @param ao_Logon
	 * @param as_XmlData
	 * @return
	 */
	public static boolean insertSystemRole(CLogon ao_Logon, String as_XmlData) {
		try {
			// 记录日志
			ao_Logon.Record("TWorkAdmin->InsertSystemRole", "");

			Document lo_Xml = MXmlHandle.LoadXml(as_XmlData);
			if (lo_Xml == null)
				return false;

			// int ll_Value = ao_Logon.getPublicFunc().getSeqValue("PositionID",
			// "系统角色标识流水号", true, "UserPosition", "PositionID",
			// lo_Xml.getDocumentElement().getChildNodes().getLength());

			return false;
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWorkAdmin->insertSystemRole", as_XmlData);
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 取流转实例中流程跟踪信息的字符串连接<br>
	 * 查看公文时对批示意见的查看设置，参数值：1-1-1-1<br>
	 * 第一个：1——系统管理员具有权限<br>
	 * 第二个：1——应用管理员具有权限<br>
	 * 第三个：1——特殊用户具有权限<br>
	 * 第四个：0——普通用户没有权限<br>
	 * 第四个：1——普通用户有查看以前流转过的权限<br>
	 * 第四个：2——普通用户有查看以后流转过的权限<br>
	 * 第四个：3——普通用户有查看所有流转过的权限
	 * 
	 * @param ao_Logon
	 * @param al_WorkItemID
	 * @param al_EntryID
	 * @return
	 */
	public static CWorkItem workItemTrackInfo(CLogon ao_Logon,
			int al_WorkItemID, int al_EntryID) {
		try {
			String ls_Right = ao_Logon.getParaValue("LOOK_UP_INFORMATION");
			if (MGlobal.isEmpty(ls_Right))
				ls_Right = "1111";

			int li_Right = 0;
			String ls_Sql;
			List<Map<String,Object>> lo_Set;
			String ls_UserId = ao_Logon.getUserID();

			if (ls_Right.substring(0, 1).equals("1")) { // 判断是否是系统管理员
				if ((ao_Logon.GlobalPara.UserType & EUserType.SystemAdministrator) == EUserType.SystemAdministrator) {
					li_Right = 3;
				}
			}

			if (li_Right == 0) { // 判断是否是应用管理员
				if (ls_Right.substring(1, 2).equals("1")) {
					if ((ao_Logon.GlobalPara.UserType & EUserType.ApplicationAdmin) == EUserType.ApplicationAdmin) {
						ls_Sql = "SELECT 1 FROM TemplateDefine WHERE CreateID = "
								+ ls_UserId
								+ " AND TemplateID IN (SELECT TemplateID FROM TemplateInst WHERE WorkItemID = "
								+ String.valueOf(al_WorkItemID) + ")";
						lo_Set = CSqlHandle.getSetBySql(ls_Sql);
						if (lo_Set!=null && lo_Set.size()>0) {
							li_Right = 3;
						}
						lo_Set = null;
					}
				}
			}

			if (li_Right == 0) { // 判断是否是特殊用户具有权限
				if (ls_Right.substring(2, 3).equals("1")) {
					ls_Sql = "SELECT 1 FROM WorkRight WHERE (UserID = "
							+ ls_UserId
							+ " OR UserID IN (SELECT GroupID FROM UserGroup WHERE UserID = "
							+ ls_UserId
							+ ")) AND ((ModuleID IN (SELECT TemplateID FROM TemplateInst WHERE "
							+ "WorkItemID = "
							+ String.valueOf(al_WorkItemID)
							+ ") AND AdminType = 1) OR (ModuleID IN (SELECT TypeID FROM TemplateInst WHERE "
							+ "WorkItemID = " + String.valueOf(al_WorkItemID)
							+ ") AND AdminType = 0))";
					lo_Set = CSqlHandle.getSetBySql(ls_Sql);
					if (lo_Set!=null && lo_Set.size()>0) {
						li_Right = 3;
					}
					lo_Set = null;
				}
			}

			if (li_Right == 0) {
				li_Right = Integer.parseInt(ls_Right.substring(ls_Right
						.length() - 1));
			}

			CWorkItem lo_Item = new CWorkItem(ao_Logon);
			ls_Sql = "SELECT WorkItemID, WorkItemName, CreateID, Creator, DeptID, DeptName, CreateDate, COMP_ID, "
					+ "EditID, Editor, EditDate, WorkItemStatus, OrignStatus, FinishedDate FROM WorkItemInst WHERE WorkItemID = "
					+ String.valueOf(al_WorkItemID);
			lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			if (lo_Set.size()>0) {
				lo_Item.Open(lo_Set.get(0), 9);
				lo_Set = null;
			} else {
				lo_Set = null;
				// 错误：不存在所要跟踪的公文实例
				ao_Logon.Raise(1044,
						"TWorkAdmin->WorkItemTrackInfo() Function", "");
				return null;
			}

			ls_Sql = "SELECT WEID, WorkItemID, EntryID, StateID, EntryType, ReciType, RecipientID, Recipients, ProxyID, Proxyor, "
					+ "RUserID, RUserName, DeptID, DeptName, InceptDate, ReadDate, FinishedDate, OverDate, AlterDate, "
					+ "CallDate, ParentID, OriginalID, ActivityID, ActivityName, ChoiceContent, EntryContent "
					+ "FROM EntryInst WHERE WorkItemID = "
					+ String.valueOf(al_WorkItemID);
			lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			for(Map<String,Object> row : lo_Set) {
				CEntry lo_Entry = new CEntry(ao_Logon);
				lo_Entry.Open(row);
				lo_Entry.WorkItem = lo_Item;
				lo_Item.Entries.put(lo_Entry.EntryID, lo_Entry);
			}

			return lo_Item;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 取流转实例中流程跟踪信息的字符串连接
	 * 
	 * @param al_WorkItemID
	 * @param al_EntryID
	 * @return
	 * @throws SQLException
	 */
	public String WorkItemTrackInfo2(int al_WorkItemID, int al_EntryID)
			throws SQLException {
		return WorkItemTrackInfo3(al_WorkItemID, al_EntryID, 0);
	}

	/**
	 * 取流转实例中流程跟踪信息的字符串连接
	 * 
	 * @param al_WorkItemID
	 * @param al_EntryID
	 * @param ai_Index
	 * @return
	 * @throws SQLException
	 */
	public String WorkItemTrackInfo3(int al_WorkItemID, int al_EntryID,
			int ai_Index) throws SQLException {
		// LOOK_UP_INFORMATION
		// 查看公文时对批示意见的查看设置
		// 0
		// 参数值：1-1-1-1
		// 第一个：1——系统管理员具有权限
		// 第二个：1——应用管理员具有权限
		// 第三个：1——特殊用户具有权限
		// 第四个：0——普通用户没有权限
		// 第四个：1——普通用户有查看以前流转过的权限
		// 第四个：2——普通用户有查看以后流转过的权限
		// 第四个：3——普通用户有查看所有流转过的权限
		List<Map<String,Object>> lo_Rec = null;
		String ls_Sql = "";
		String ls_Str;
		int j;
		int i;
		int k;
		String ls_Value;
		String ls_Text = "";
		String ls_Sign;
		int ll_EntryID = 0;
		String ls_Index = "";
		ls_Index = (ai_Index == 0 ? "" : ai_Index + "");

		String ls_Right = "";
		int li_Right = 0;
		li_Right = 0;
		Document lo_Xml = MXmlHandle.LoadXml("<Activity />");
		MXmlHandle.getNodeByPath(lo_Xml.getDocumentElement(),
				"<WorkItemInst WorkItemID=\'" + al_WorkItemID + "\' />");
		Element lo_Node = lo_Xml.getDocumentElement();
		Element lo_Parent = lo_Xml.getDocumentElement();
		Document lo_EntryXML = MXmlHandle.LoadXml("<Activity />");
		ls_Sql = "SELECT * FROM SysParameter WHERE ParameterCode = \'LOOK_UP_INFORMATION\'";
		lo_Rec = CSqlHandle.getSetBySql(ls_Sql);
		// lo_Rec.CursorLocation = ADODB.CursorLocationEnum.adUseClient;
		// lo_Rec.Open(ls_Sql, GlobalPara.objConnection,
		// ADODB.CursorTypeEnum.adOpenKeyset, ADODB.LockTypeEnum.adLockReadOnly,
		// -1);
		if (lo_Rec.size()>0) {
			ls_Right = "1111";
		} else {
			ls_Right = (String) MGlobal.readObject(lo_Rec.get(0),"ParameterValue");
		}

		if (ls_Right.substring(0, 1) == "1") {
			// 判断是否是系统管理员
			if ((this.Logon.GlobalPara.UserType & EUserType.SystemAdministrator) == EUserType.SystemAdministrator) {
				li_Right = 3;
			}
		}

		if (li_Right == 0) {
			// 判断是否是应用管理员
			if (ls_Right.substring(1, 1) == "1") {
				if ((this.Logon.GlobalPara.UserType & EUserType.ApplicationAdmin) == EUserType.ApplicationAdmin) {
					ls_Sql = "SELECT 1 FROM TemplateDefine WHERE CreateID = "
							+ (this.Logon.GlobalPara.UserID)
							+ " AND TemplateID IN (SELECT TemplateID FROM TemplateInst WHERE "
							+ "WorkItemID = " + al_WorkItemID + ")";
					lo_Rec = CSqlHandle.getSetBySql(ls_Sql);
					// lo_Rec.CursorLocation =
					// ADODB.CursorLocationEnum.adUseClient;
					// lo_Rec.Open(ls_Sql, GlobalPara.objConnection,
					// ADODB.CursorTypeEnum.adOpenKeyset,
					// ADODB.LockTypeEnum.adLockReadOnly, -1);
					if (lo_Rec.size()>0) {
						li_Right = 3;
					}
				}
			}
		}

		if (li_Right == 0) {
			// 判断是否是特殊用户具有权限
			if (ls_Right.substring(2, 1) == "1") {
				ls_Sql = "SELECT 1 FROM WorkRight WHERE (UserID = "
						+ this.Logon.GlobalPara.UserID + " OR UserID";
				ls_Sql = ls_Sql
						+ " IN (SELECT GroupID FROM UserGroup WHERE UserID = "
						+ this.Logon.GlobalPara.UserID + "))";
				ls_Sql = ls_Sql
						+ " AND ((ModuleID IN (SELECT TemplateID FROM TemplateInst WHERE "
						+ "WorkItemID = ";
				ls_Sql = ls_Sql + (al_WorkItemID) + ") AND AdminType = 1) OR";
				ls_Sql = ls_Sql
						+ " (ModuleID IN (SELECT TypeID FROM TemplateInst WHERE "
						+ "WorkItemID = ";
				ls_Sql = ls_Sql + (al_WorkItemID) + ") AND AdminType = 0))";
				lo_Rec = CSqlHandle.getSetBySql(ls_Sql);
				// lo_Rec.CursorLocation = ADODB.CursorLocationEnum.adUseClient;
				// lo_Rec.Open(ls_Sql, GlobalPara.objConnection,
				// ADODB.CursorTypeEnum.adOpenKeyset,
				// ADODB.LockTypeEnum.adLockReadOnly, -1);
				if (lo_Rec.size()>0) {
					li_Right = 3;
				}
			}
		}

		if (li_Right == 0) {
			li_Right = Integer.parseInt(ls_Right.substring(
					ls_Right.length() - 1, 1));
		}

		ls_Sql = "SELECT WorkItemID, WorkItemName, WorkItemStatus FROM WorkItemInst"
				+ ls_Index + " WHERE WorkItemID = " + (al_WorkItemID);
		lo_Rec = CSqlHandle.getSetBySql(ls_Sql);
		// lo_Rec.CursorLocation = ADODB.CursorLocationEnum.adUseClient;
		// lo_Rec.Open(ls_Sql, GlobalPara.objConnection,
		// ADODB.CursorTypeEnum.adOpenKeyset, ADODB.LockTypeEnum.adLockReadOnly,
		// -1);
		if (lo_Rec.size()>0) {
			lo_Rec = null;
			// GlobalPara.RaiseError(1044, Information.TypeName(this) +
			// "::WorkItemTrackInfo() Function", "");
			return "";
		}
		lo_Xml.getDocumentElement().setAttribute("WorkItemName",
				MGlobal.readObject(lo_Rec.get(0),"WorkItemName").toString());
		lo_Xml.getDocumentElement().setAttribute("WorkItemStatus",
				(MGlobal.readObject(lo_Rec.get(0),"WorkItemStatus").toString()));

		lo_Xml.getDocumentElement().setAttribute("RightType",
				Integer.toString(li_Right));

		ls_Sql = "SELECT Max(EntryID) AS MaxEntryID, ParentID FROM EntryInst"
				+ ls_Index + " WHERE WorkItemID = ";
		ls_Sql = ls_Sql + (al_WorkItemID) + " AND (RecipientID = "
				+ this.Logon.GlobalPara.UserID;
		ls_Sql = ls_Sql + " OR ProxyID = " + this.Logon.GlobalPara.UserID
				+ ") GROUP BY ParentID";
		lo_Rec = CSqlHandle.getSetBySql(ls_Sql);
		// lo_Rec.CursorLocation = ADODB.CursorLocationEnum.adUseClient;
		// lo_Rec.Open(ls_Sql, GlobalPara.objConnection,
		// ADODB.CursorTypeEnum.adOpenKeyset, ADODB.LockTypeEnum.adLockReadOnly,
		// -1);
		ll_EntryID = al_EntryID;
		// lo_Rec.Sort = "ParentID DESC";
		if (!(lo_Rec.size()>0)) {
			if (MGlobal.readObject(lo_Rec.get(0),"ParentID") != null) {
				ll_EntryID = (Integer) (MGlobal.readObject(lo_Rec.get(0),"ParentID"));
			}
		}

		lo_Xml.getDocumentElement().setAttribute("MaxEntryID",
				Integer.toString(ll_EntryID));

		if (HaveCommentRight(al_WorkItemID, ls_Index)) {
			ls_Sql = "SELECT EntryID, StateID, RecipientID, Recipients, ProxyID, Proxyor, InceptDate, ";
			ls_Sql = ls_Sql
					+ "ReadDate, FinishedDate, OverDate, CallDate, ActivityID, ActivityName,";
			ls_Sql = ls_Sql
					+ "AlterNumber, ChoiceContent, ParentID, EntryType, DueTransactType ";
		} else // 没有权限，隐藏一些内容
		{
			ls_Sql = "SELECT EntryID, StateID, RecipientID, \'XXXXXX\' AS Recipients, ProxyID, \'XXXXXX\' AS Proxyor, InceptDate, ";
			ls_Sql = ls_Sql
					+ "ReadDate, FinishedDate, OverDate, CallDate, ActivityID, ActivityName,";
			ls_Sql = ls_Sql
					+ "AlterNumber, NULL AS ChoiceContent, ParentID, EntryType, DueTransactType ";
		}
		ls_Sql = ls_Sql + "FROM EntryInst" + ls_Index + " WHERE WorkItemID = "
				+ (al_WorkItemID); // FOR
									// XML
									// AUTO"

		ls_Text = CSqlHandle.getXmlBySql(ls_Sql, "EntryInst");

		if (MXmlHandle.getNodeByPath(lo_EntryXML.getDocumentElement(), "<Root>"
				+ ls_Text + "</Root>") != null) {
			while (lo_EntryXML.getDocumentElement().hasChildNodes()) {
				lo_Node = (Element) lo_EntryXML.getDocumentElement()
						.getFirstChild();
				if (lo_Node.getAttribute("ParentID") == null) {
					lo_EntryXML.getDocumentElement().removeChild(lo_Node);
				} else {
					lo_Parent = (Element) lo_Xml
							.getDocumentElement()
							.getElementsByTagName(
									"//EntryInst[@EntryID=\'"
											+ (lo_Node.getAttribute("ParentID"))
											+ "\']");
					if (lo_Parent == null) {
						lo_Xml.getDocumentElement().appendChild(lo_Node);
					} else {
						lo_Parent.appendChild(lo_Node);
					}
				}
			}
		}
		lo_EntryXML = null;
		lo_Rec = null;
		String returnValue = lo_Xml.getTextContent().replace("\'", "&apos;")
				.replace("\"", "\'");
		lo_Xml = null;
		lo_Rec = null;
		// GlobalPara.RaiseError(Information.Err().Number,
		// Information.TypeName(this) + "::WorkItemTrackInfo3() Function",
		// Information.Err().Description);
		return returnValue;
	}

	public boolean IsValidPassword(int al_StamperID, String as_Password) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 判断是否有查看意见的权限
	 * 
	 * @param al_WorkItemID
	 * @param as_Index
	 * @return
	 */
	public boolean HaveCommentRight(int al_WorkItemID, String as_Index) {
		try {
			String ls_Sql = "SELECT * FROM WorkItemCommentRight WHERE WorkItemID = "
					+ (al_WorkItemID);
			List<Map<String,Object>> lo_Rec = CSqlHandle.getSetBySql(ls_Sql, true);
			boolean lb_Result = true;
			if (lo_Rec.size()>0) {
				for(Map<String,Object> row : lo_Rec) {
					if (Integer.parseInt(MGlobal.readObject(row,"RightType")
							.toString()) == 0) {
						lb_Result = false;
					} else if (Integer.parseInt(MGlobal.readObject(row,"RightType")
							.toString()) == 1) {
						if (lb_Result) {
							ls_Sql = MGlobal.readObject(row,"RightContent")
									.toString().replace(";", ",");
							if (("," + ls_Sql + ",")
									.indexOf(","
											+ String.valueOf(this.Logon.GlobalPara.UserID)
											+ ",") + 1 == 0) {
								if (ls_Sql.substring(ls_Sql.length() - 1, 1) == ",") {
									ls_Sql = ls_Sql.substring(0,
											ls_Sql.length() - 1);
								}
								ls_Sql = "SELECT \'1\' FROM UserGroup WHERE UserID = "
										+ String.valueOf(this.Logon.GlobalPara.UserID)
										+ " AND GroupID IN (" + ls_Sql + ")";
								if (CSqlHandle.getValueBySql(ls_Sql) == "1") {
									lb_Result = false;
								}
							} else {
								lb_Result = false;
							}
						}
					} else if (Integer.parseInt(MGlobal.readObject(row,"RightType")
							.toString()) == 2) {
						if (lb_Result == false) {
							ls_Sql = MGlobal.readObject(row,"RightContent")
									.toString().replace(";", ",");
							if (("," + ls_Sql + ",").indexOf(","
									+ (this.Logon.GlobalPara.UserID) + ",") + 1 == 0) {
								if (ls_Sql.substring(ls_Sql.length() - 1, 1) == ",") {
									ls_Sql = ls_Sql.substring(0,
											ls_Sql.length() - 1);
								}
								ls_Sql = "SELECT \'1\' FROM UserGroup WHERE UserID = "
										+ (this.Logon.GlobalPara.UserID)
										+ " AND GroupID IN (" + ls_Sql + ")";
								if (CSqlHandle.getValueBySql(ls_Sql) == "1") {
									lb_Result = true;
								}
							} else {
								lb_Result = true;
							}
						}
					} else if (Integer.parseInt(MGlobal.readObject(row,"RightType")
							.toString()) == 3) {
						if (lb_Result) {
							ls_Sql = MGlobal.readObject(row,"RightContent")
									.toString().replace(";", ",");
							if (ls_Sql.substring(ls_Sql.length() - 1, 1) == ",") {
								ls_Sql = ls_Sql.substring(0,
										ls_Sql.length() - 1);
							}
							ls_Sql = "SELECT \'1\' FROM EntryInst" + as_Index
									+ " WHERE EntryID IN (" + ls_Sql + ")";
							ls_Sql = ls_Sql + " AND WorkItemID = "
									+ (al_WorkItemID) + " AND (RecipientID = ";
							ls_Sql = ls_Sql + (this.Logon.GlobalPara.UserID)
									+ " OR ProxyID = "
									+ (this.Logon.GlobalPara.UserID) + ")";
							if (CSqlHandle.getValueBySql(ls_Sql) == "1") {
								lb_Result = false;
							}
						}
					} else if (Integer.parseInt(MGlobal.readObject(row,"RightType")
							.toString()) == 4) {
						if (lb_Result == false) {
							ls_Sql = MGlobal.readObject(row,"RightContent")
									.toString().replace(";", ",");
							if (ls_Sql.substring(ls_Sql.length() - 1, 1) == ",") {
								ls_Sql = ls_Sql.substring(0,
										ls_Sql.length() - 1);
							}
							ls_Sql = "SELECT \'1\' FROM EntryInst" + as_Index
									+ " WHERE EntryID IN (" + ls_Sql + ")";
							ls_Sql = ls_Sql + " AND WorkItemID = "
									+ (al_WorkItemID) + " AND (RecipientID = ";
							ls_Sql = ls_Sql + (this.Logon.GlobalPara.UserID)
									+ " OR ProxyID = "
									+ (this.Logon.GlobalPara.UserID) + ")";
							if (CSqlHandle.getValueBySql(ls_Sql) == "1") {
								lb_Result = true;
							}
						}
					}
				}
			}
			lo_Rec = null;
			return lb_Result;
		} catch (Exception e) {
			return true;
		}
	}

	public String getCyclostyleNameByID(int al_Id) {
		// TODO Auto-generated method stub
		return "";
	}

	public int getCyclostyleIDByName(String as_Name) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 根据标识取得签名设计的XML对象
	 * 
	 * @param as_SignIDs
	 * @return
	 * @throws Exception
	 */
	public Document getSignDesignObject(String as_SignIDs) throws Exception {
		String ls_Sql = as_SignIDs.replaceAll(";", ",").trim();
		if (MGlobal.right(ls_Sql, 1).equals(","))
			ls_Sql = MGlobal.left(ls_Sql, ls_Sql.length() - 1);
		ls_Sql = "SELECT SignID, SignContent FROM UserSignDesign WHERE SignID IN ("
				+ ls_Sql + ")";
		List<Map<String,Object>> lo_Set = CSqlHandle.getSetBySql(ls_Sql);
		boolean lb_Boolean = lo_Set.size()>0;
		if (!lb_Boolean) {
			lo_Set = null;
			return null;
		}

		byte[] ly_Bytes;

		Document lo_Xml = MXmlHandle
				.LoadXml("<?xml version=\"1.0\"?><Root xmlns:dt=\"urn:schemas-microsoft-com:datatypes\" />");
		for(Map<String,Object> row : lo_Set) {
			ly_Bytes = MGlobal.readBytes(row,"SignContent");
			Element lo_Node = lo_Xml.createElement("Sign");
			lo_Node.setTextContent(ly_Bytes.toString());
			lo_Node.setAttribute("SignID",
					String.valueOf(MGlobal.readInt(row,"SignID")));
			lo_Xml.getDocumentElement().appendChild(lo_Node);
		}
		lo_Set = null;

		return lo_Xml;
	}

	/**
	 * 获取用户所拥有的用户角色标识连接串，使用“,”分隔
	 * 
	 * @param al_UserID
	 * @return
	 */
	public static String getUserRoleIDs(CLogon ao_Logon, int al_UserID) {
		// 获取岗位、岗位组、部门组（部门组可以作为一个特殊的岗位组来使用）
		String ls_Sql = "SELECT UserID FROM UserInfo WHERE (UserID1 = {0} Or UserID2 = {0}"
				+ " OR UserIDS LIKE ''{0};%\'' OR UserIDS LIKE \''%;{0};%\'') AND UserType IN (2, 3, 4)";
		String ls_UserIDs = CSqlHandle.getValuesBySql(
				MessageFormat.format(ls_Sql, String.valueOf(al_UserID)),
				"UserID", ";");

		// 获取部门组
		int ll_DeptID = ao_Logon.GlobalPara.DeptID;
		if (ll_DeptID == 0) {// 无部门用户
			ls_Sql = "SELECT UserID FROM UserInfo WHERE UserType = 4 AND DeptID IS NULL";
			ls_UserIDs += CSqlHandle
					.getValuesBySql(ls_Sql, "UserID", ";");
		} else {
			// 0 - 包括本部门用户
			ls_Sql = "SELECT UserID FROM UserInfo WHERE UserType = 4 AND DeptID = "
					+ String.valueOf(ll_DeptID) + " AND UserProp = 0";
			ls_UserIDs += CSqlHandle
					.getValuesBySql(ls_Sql, "UserID", ";");
			DeptManage lo_Manage = TWorkAdmin.getDeptManage(ao_Logon);
			// 1 - 包括本部门和下级部门用户；
			String ls_DeptIDs = lo_Manage.getChildIds(ll_DeptID, true);
			if (MGlobal.isExist(ls_DeptIDs)) {
				ls_Sql = "SELECT UserID FROM UserInfo WHERE UserType = 4 AND DeptID IN ("
						+ ls_DeptIDs + ") AND UserProp = 1";
				ls_UserIDs += CSqlHandle.getValuesBySql(ls_Sql, "UserID",
						";");
			}
			// 2 - 包括本部门和上级部门用户；
			ls_DeptIDs = lo_Manage.getParentIds(ll_DeptID, true);
			if (MGlobal.isExist(ls_DeptIDs)) {
				ls_Sql = "SELECT UserID FROM UserInfo WHERE UserType = 4 AND DeptID IN ("
						+ ls_DeptIDs + ") AND UserProp = 2";
				ls_UserIDs += CSqlHandle.getValuesBySql(ls_Sql, "UserID",
						";");
			}
			// 3 - 包括本部门、上级部门和下级部门用户；
			ls_DeptIDs = lo_Manage.getAllIds(ll_DeptID);
			if (MGlobal.isExist(ls_DeptIDs)) {
				ls_Sql = "SELECT UserID FROM UserInfo WHERE UserType = 4 AND DeptID IN ("
						+ ls_DeptIDs + ") AND UserProp = 3";
				ls_UserIDs += CSqlHandle.getValuesBySql(ls_Sql, "UserID",
						";");
			}
		}

		ls_UserIDs = ls_UserIDs.replaceAll(";", ",");
		if (MGlobal.right(ls_UserIDs, 1).equals(",")) {
			ls_UserIDs = ls_UserIDs.substring(0, ls_UserIDs.length() - 1);
		}

		return ls_UserIDs;
	}

	/**
	 * 取得XML目录树结构的某一个域的内容
	 * 
	 * @param ao_Node
	 *            XML数据节点
	 * @param as_AttrName
	 *            属性名称
	 * @param as_Split
	 *            分隔符号
	 * @param ab_Include
	 *            是否包含本节点
	 * @return
	 */
	public static String getXmlNodeAttrValues(Element ao_Node,
			String as_AttrName, String as_Split, boolean ab_Include) {
		String ls_Value = "";
		if (ab_Include)
			ls_Value = ao_Node.getAttribute(as_AttrName) + as_Split;

		NodeList lo_List = ao_Node.getChildNodes();
		for (int i = 0; i < lo_List.getLength(); i++) {
			Element lo_Node = (Element) lo_List.item(i);
			ls_Value += getXmlNodeAttrValues(lo_Node, as_AttrName, as_Split,
					ab_Include);
		}

		return ls_Value;
	}

	/**
	 * 获取部门节点
	 * 
	 * @param al_DeptID
	 *            部门标识
	 * @return
	 */
	public static CDept getDept(CLogon ao_Logon, int al_DeptID) {
		DeptManage lo_Manage = getDeptManage(ao_Logon);
		if (lo_Manage == null)
			return null;
		return lo_Manage.Depts.get(al_DeptID);
	}

	/**
	 * 公文加锁
	 * 
	 * @param ao_Item
	 *            公文对象
	 * @param ai_LockType
	 *            锁类型
	 * @return
	 * @throws SQLException
	 */
	public static boolean addLock(CWorkItem ao_Item, int ai_LockType)
			throws SQLException {
		return addLock(ao_Item.Logon, ao_Item.WorkItemID,
				ao_Item.CurEntry.EntryID, 0, ai_LockType);
	}

	/**
	 * 公文加锁
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param al_WorkItemID
	 *            公文标识
	 * @param al_EntryID
	 *            状态标识
	 * @param al_AddLockUserID
	 *            加锁用户标识
	 * @param ai_LockType
	 *            锁类型
	 * @return
	 * @throws SQLException
	 */
	public static boolean addLock(CLogon ao_Logon, int al_WorkItemID,
			int al_EntryID, int al_AddLockUserID, int ai_LockType)
			throws SQLException {
		return true;
	}

	/**
	 * 公文加锁
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param al_WorkItemID
	 *            公文标识
	 * @param al_EntryID
	 *            状态标识
	 * @param al_AddLockUserID
	 *            加锁用户标识
	 * @param ai_LockType
	 *            锁类型
	 * @return
	 * @throws SQLException
	 */
	public static boolean addLock_back(CLogon ao_Logon, int al_WorkItemID,
			int al_EntryID, int al_AddLockUserID, int ai_LockType)
			throws SQLException {
		ao_Logon.Record(
				"addLock",
				"(" + String.valueOf(al_WorkItemID) + ","
						+ String.valueOf(al_EntryID) + ","
						+ String.valueOf(al_AddLockUserID) + ")");

		int ll_UserID = (al_AddLockUserID == 0 ? ao_Logon.GlobalPara.UserID
				: al_AddLockUserID);
		String ls_Name = ao_Logon.GlobalPara.UserName;
		if (al_AddLockUserID > 0) {
			ls_Name = CSqlHandle
					.getValueBySql("SELECT UserName FROM UserInfo WHERE UserID = "
							+ String.valueOf(al_AddLockUserID));
			if (ls_Name.equals("")) {
				ls_Name = ao_Logon.GlobalPara.UserName;
				ll_UserID = ao_Logon.GlobalPara.UserID;
			}
		}

		String ls_Sql = "";
		// 以下修改保证同一篇公文自己不能打开两次
		if (ai_LockType == ELockType.RepellentLock) {
			ls_Sql = "SELECT '1' FROM WorkItemLock WHERE WorkItemID = "
					+ String.valueOf(al_WorkItemID);
		} else {
			ls_Sql = "SELECT '1' FROM WorkItemLock WHERE WorkItemID = "
					+ String.valueOf(al_WorkItemID)
					+ " AND (LockType = 0 OR UserID = "
					+ String.valueOf(ll_UserID) + ")";
		}
		if (CSqlHandle.getValueBySql(ls_Sql).equals("1"))
			return false;

		ls_Sql = "INSERT INTO WorkItemLock (WorkItemID, EntryID, UserID, UserName, LockType, LockDate) VALUES (?, ?, ?, ?, ?, ?)";

		List parasList = new ArrayList();
		parasList.add(al_WorkItemID);
		parasList.add(al_EntryID);
		parasList.add(ll_UserID);
		parasList.add(ls_Name);
		parasList.add(ai_LockType);
		parasList.add(MGlobal.getSqlTimeNow());

		return (CSqlHandle.getJdbcTemplate().update(ls_Sql,
				parasList.toArray()) > 0);

	}

	/**
	 * 公文实例解锁
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param al_WorkItemID
	 *            清除所有锁
	 * @param al_EntryID
	 *            清除所有al_WorkItemID的锁
	 * @return
	 */
	public static boolean releaseLock(CLogon ao_Logon, int al_WorkItemID,
			int al_EntryID) {
		ao_Logon.Record(
				"TWorkAdmin->ReleaseLock",
				"(" + String.valueOf(al_WorkItemID) + ","
						+ String.valueOf(al_EntryID) + ")");

		String ls_Sql = "";

		if (al_WorkItemID == -1) {
			ls_Sql = "DELETE FROM WorkItemLock";
		} else if (al_EntryID == -1) {
			ls_Sql = "DELETE FROM WorkItemLock WHERE WorkItemID = "
					+ String.valueOf(al_WorkItemID);
		} else {
			ls_Sql = "DELETE FROM WorkItemLock WHERE WorkItemID = "
					+ String.valueOf(al_WorkItemID) + " AND EntryID = "
					+ String.valueOf(al_EntryID);
		}

		return CSqlHandle.execSql(ls_Sql);
	}

	/**
	 * 公文实例自动解锁
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @return
	 */
	public static boolean autoReleaseLock(CLogon ao_Logon) {
		int ll_ReleaseTime = 360; // 6个小时
		String ls_Sql = ao_Logon.getParaValue("AUTOMATISM_RELEASE_LOCK");
		if (MGlobal.isInt(ls_Sql))
			ll_ReleaseTime = Integer.parseInt(ls_Sql);

		if (ll_ReleaseTime < 30)
			ll_ReleaseTime = 30; // 半个小时

		if (ll_ReleaseTime > 1440)
			ll_ReleaseTime = 1440; // 一天

		ls_Sql = "DELETE FROM WorkItemLock WHERE DATEDIFF(mi, LockDate, GETDATE()) > "
				+ String.valueOf(ll_ReleaseTime);
		ao_Logon.Record("TWorkAdmin->autoReleaseLock", "");

		return CSqlHandle.execSql(ls_Sql);
	}

	/**
	 * 设置服务器端临时文件夹到附件文件中
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_FilePath
	 *            文件路径
	 * @param as_TempFiles
	 *            临时文件
	 * @param as_Files
	 *            文件
	 * @param ab_DeleteTemp
	 *            是否删除临时文件
	 * @return
	 */
	public static boolean writeFileTempToData(CLogon ao_Logon,
			String as_FilePath, String as_TempFiles, String as_Files,
			boolean ab_DeleteTemp) {
		if (MGlobal.isEmpty(as_TempFiles) || MGlobal.isEmpty(as_Files))
			return false;
		String[] ls_ArrayTemp = as_TempFiles.split("|");
		String[] ls_ArrayFile = as_Files.split("|");
		if (ls_ArrayTemp.length != ls_ArrayFile.length)
			return false;
		String ls_Path = ao_Logon.SaveDataPath;
		String ls_TempPath = ao_Logon.TempPath;
		// 创建文件夹
		MFile.createFolder(ls_Path + as_FilePath);

		// 迁移临时文件
		for (int i = 0; i < ls_ArrayTemp.length; i++) {
			if (MGlobal.isExist(ls_ArrayTemp[i])) {
				MFile.copyFile(ls_TempPath + ls_ArrayTemp[i], ls_Path
						+ as_FilePath + ls_ArrayTemp[i]);
			}
		}

		// 删除已有文件
		for (int i = 0; i < ls_ArrayFile.length; i++) {
			if (MGlobal.isExist(ls_ArrayFile[i])) {
				String ls_File = ls_Path + as_FilePath + ls_ArrayFile[i];
				MFile.deleteFile(ls_File);
			}
		}

		// 修改文件名称
		for (int i = 0; i < ls_ArrayTemp.length; i++) {
			if (MGlobal.isExist(ls_ArrayTemp[i])) {
				MFile.rename(ls_Path + as_FilePath, ls_ArrayTemp[i],
						ls_ArrayFile[i]);
			}
		}

		// 删除临时文件
		if (ab_DeleteTemp) {
			for (int i = 0; i < ls_ArrayTemp.length; i++) {
				if (MGlobal.isExist(ls_ArrayTemp[i])) {
					MFile.deleteFile(ls_TempPath + ls_ArrayTemp[i]);
				}
			}
		}

		return true;
	}

	/**
	 * 删除公文实例(把公文放入回收站)
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_WorkItemIds
	 *            公文实例标识连接串，使用【;】分开
	 * @return
	 */
	public static boolean workItemToRelyed(CLogon ao_Logon,
			String as_WorkItemIds) {
		try {
			// 记录日志
			ao_Logon.Record("TWorkAdmin->workItemToRelyed", as_WorkItemIds);

			if (MGlobal.isEmpty(as_WorkItemIds))
				return false;

			String ls_Sql = "";
			String ls_TempIDs = "";
			String ls_TypeIDs = "";
			if ((ao_Logon.GlobalPara.UserType & EUserType.SystemAdministrator) == EUserType.SystemAdministrator) {
				ls_Sql = as_WorkItemIds;
			} else {
				String ls_UserId = ao_Logon.getUserID();
				ls_Sql = "SELECT ModuleID, AdminType FROM WorkRight WHERE DeleteRight = 1 And (UserID = {0}"
						+ " OR UserID IN (SELECT GroupID FROM UserGroup WHERE UserID = {0}))";
				List<Map<String,Object>> lo_Set = CSqlHandle.getSetBySql(MessageFormat
						.format(ls_Sql, ls_UserId));
				boolean lb_Boolean = lo_Set.size()>0;
				if (!lb_Boolean) {
					lo_Set = null;
					return false;
				}
				for(Map<String,Object> row : lo_Set) {
					if (MGlobal.readInt(row,"AdminType") == 0) {
						ls_TypeIDs += MGlobal.readString(row,"ModuleID") + ",";
					} else {
						ls_TempIDs += MGlobal.readString(row,"ModuleID") + ",";
					}
				}
				lo_Set = null;

				if (("," + ls_TypeIDs).indexOf(",0,") > -1) {
					ls_Sql = as_WorkItemIds;
				} else {
					ls_Sql = as_WorkItemIds.replace(";", ",");
					if (ls_Sql.substring(ls_Sql.length() - 1).equals(","))
						ls_Sql = ls_Sql.substring(0, ls_Sql.length() - 1);
					ls_Sql = "SELECT WorkItemID FROM TemplateInst WHERE WorkItemID IN ("
							+ ls_Sql + ")";
					ls_TypeIDs += "-1";
					ls_TempIDs += "-1";
					ls_Sql += " AND TemplateID IN (SELECT TemplateID FROM TemplateDefine WHERE TemplateID IN ("
							+ ls_TempIDs
							+ ") OR TypeID IN ("
							+ ls_TypeIDs
							+ "))";
					ls_Sql = CSqlHandle.getValuesBySql(ls_Sql);
					if (MGlobal.isEmpty(ls_Sql))
						return false;
				}
			}

			ls_Sql = ls_Sql.replaceAll(";", ",");
			if (ls_Sql.substring(ls_Sql.length() - 1).equals(","))
				ls_Sql = ls_Sql.substring(0, ls_Sql.length() - 1);
			ls_Sql = "WorkItemID IN (" + ls_Sql + ")";

			String ls_Sqls = "UPDATE WorkItemInst SET OrignStatus = WorkItemStatus WHERE (OrignStatus IS NULL) AND "
					+ ls_Sql;
			ls_Sqls += ";UPDATE WorkItemInst SET WorkItemStatus = 3 WHERE "
					+ ls_Sql;

			return CSqlHandle.execSql(ls_Sqls, ";");
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWorkAdmin->WorkItemToRelyed", as_WorkItemIds);
			return false;
		}
	}

	/**
	 * 删除公文实例
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_WorkItemIds
	 *            公文实例标识连接串，使用“;”分开
	 * @return
	 */
	public static boolean deleteWorkItem(CLogon ao_Logon, String as_WorkItemIds) {
		try {
			// 记录日志
			ao_Logon.Record("TWorkAdmin->DeleteWorkItem", as_WorkItemIds);

			String ls_Cond = as_WorkItemIds.replace(";", ",");
			if (MGlobal.right(ls_Cond, 1).equals(","))
				ls_Cond = ls_Cond.substring(0, ls_Cond.length() - 1);

			String ls_Sql = "SELECT WorkItemName FROM WorkItemInst WHERE WorkItemID IN ("
					+ ls_Cond + ")";
			ls_Sql = CSqlHandle.getValuesBySql(ls_Sql);
			ao_Logon.recordKeyLog("TWorkAdmin->DeleteWorkItem", "删除公文数据",
					ls_Sql);

			// 读取正文附件文件名称
			ls_Sql = "SELECT dt.WorkItemID, dt.DocumentID, dt.FileName, dt.DocumentType, dt.FilePages, tt.TemplateID FROM "
					+ "DocumentInst dt, TemplateInst tt WHERE dt.WorkItemID = tt.WorkItemID AND dt.WorkItemID IN "
					+ "(SELECT dt.WorkItemID FROM DocumentInst dt, TemplateInst tt WHERE dt.WorkItemID = tt.WorkItemID) AND "
					+ "(dt.DocumentType < 7 OR dt.DocumentType IN (9, 10)) AND dt.WorkItemID IN ("
					+ ls_Cond + ")";
			List<Map<String,Object>> lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			ArrayList<String> lo_FileNames = new ArrayList<String>();
			for(Map<String,Object> row : lo_Set) {
				if (MGlobal.readObject(row,"FileSaveType") != null) {
					ls_Sql = ao_Logon.getFileSavePath(
							MGlobal.readInt(row,"TemplateID"),
							EFileSaveType.DefaultSaveType, 0, MGlobal.getNow());
				} else {
					ls_Sql = ao_Logon.getFileSavePath(
							MGlobal.readInt(row,"TemplateID"),
							MGlobal.readInt(row,"FileSaveType"),
							MGlobal.readInt(row,"FileSaveID"),
							MGlobal.readTime(row, "FileSaveDate"));
				}
				lo_FileNames.add(ao_Logon.SaveDataPath + "WorkItem\\Data"
						+ MGlobal.readString(row,"TemplateID") + "\\File"
						+ MGlobal.readString(row,"WorkItemID") + "_"
						+ MGlobal.readString(row,"DocumentID") + "."
						+ MFile.getFileExt(MGlobal.readString(row,"FileName")));
				lo_FileNames.add(ls_Sql + "File"
						+ MGlobal.readString(row,"WorkItemID") + "_"
						+ MGlobal.readString(row,"DocumentID") + "."
						+ MFile.getFileExt(MGlobal.readString(row,"FileName")));
				if (MGlobal.readInt(row,"DocumentType") == 10) {
					if (MGlobal.readObject(row,"FilePages") != null) {
						if (MGlobal.readInt(row,"FilePages") > 1)
							lo_FileNames.add(ao_Logon.SaveDataPath
									+ "WorkItem\\Data"
									+ MGlobal.readString(row,"TemplateID")
									+ "\\File"
									+ MGlobal.readString(row,"WorkItemID")
									+ "_"
									+ MGlobal.readString(row,"DocumentID")
									+ "_*."
									+ MFile.getFileExt(MGlobal.readString(row,"FileName")));
					}
				}
			}
			lo_Set = null;

			ls_Sql = " WHERE WorkItemID IN (" + ls_Cond + ")";
			String ls_Sqls = getAddDelSqls(ao_Logon, ls_Sql);

			List<String> batchSqlList = new ArrayList<String>();
			// 删除公文流转实例锁
			batchSqlList.add("DELETE FROM WorkItemLock" + ls_Sql);
			// 删除公文实例内容存储表格
			batchSqlList.add("DELETE FROM WorkItemContent" + ls_Sql);
			// 删除公文状态内容存储表格
			batchSqlList.add("DELETE FROM WorkEntryContent" + ls_Sql);
			// 删除意见签名权限设置
			batchSqlList.add("DELETE FROM ResponseLookupRight" + ls_Sql);
			// 公文转发登记表
			batchSqlList.add("DELETE FROM WorkItemTransfer" + ls_Sql);
			// 删除流转状态
			batchSqlList.add("DELETE FROM EntryInst" + ls_Sql);
			// 删除公文被监督人描述
			batchSqlList.add("DELETE FROM SuperviseUser" + ls_Sql);
			// 删除人工监督
			batchSqlList.add("DELETE FROM UserSupervise" + ls_Sql);
			// 删除二次开发函数实例
			batchSqlList.add("DELETE FROM ScriptInst" + ls_Sql);
			// 删除角色实例
			batchSqlList.add("DELETE FROM RoleInst" + ls_Sql);
			// 删除表格定义实例
			batchSqlList.add("DELETE FROM FormInst" + ls_Sql);
			// 删除表头属性实例
			batchSqlList.add("DELETE FROM PropInst" + ls_Sql);
			// 删除附件实例
			batchSqlList.add("DELETE FROM DocumentInst" + ls_Sql);
			// 删除流程步骤实例
			batchSqlList.add("DELETE FROM ActivityInst" + ls_Sql);
			// 删除流程实例
			batchSqlList.add("DELETE FROM FlowInst" + ls_Sql);
			// 删除权限实例
			batchSqlList.add("DELETE FROM RightInst" + ls_Sql);
			// 删除数据存储表格实例
			batchSqlList.add("DELETE FROM TableInst" + ls_Sql);
			// 删除公文发布模板实例
			batchSqlList.add("DELETE FROM PublicTemplateInst" + ls_Sql);
			// 删除公文定义实例
			batchSqlList.add("DELETE FROM TemplateInst" + ls_Sql);
			// 删除公文远程启动信息表
			batchSqlList.add("DELETE FROM LaunchInfo" + ls_Sql);
			// 删除流转系统远程启动回传信息
			batchSqlList.add("DELETE FROM ReturnInfo" + ls_Sql);
			// 公文实例备份记录管理
			batchSqlList.add("DELETE FROM WorkItemInstBack" + ls_Sql);
			batchSqlList
					.add("INSERT INTO WorkItemInstBack SELECT *, 2 AS BackType FROM WorkItemInst"
							+ ls_Sql);
			// 删除公文实例
			batchSqlList.add("DELETE FROM WorkItemInst" + ls_Sql);
			// 更新流水号-不执行
			// "UPDATE SysSequence SET SequenceValue = 1 WHERE SequenceCode = 'WorkItemID'";
			// "UPDATE SysSequence SET SequenceValue = (SELECT MAX(WorkItemID) + 1 FROM WorkItemInst) WHERE SequenceCode = 'WorkItemID' AND Exists(SELECT 1 FROM WorkItemInst)";

			// 删除业务数据表
			if (MGlobal.isExist(ls_Sqls)) {
				String[] ls_Array = ls_Sqls.split(";");
				for (int i = 0; i < ls_Array.length; i++) {
					if (MGlobal.isExist(ls_Array[i])
							&& ls_Array[i].substring(0, 1).equals("[") == false) {
						batchSqlList.add(ls_Array[i]);
					}
				}
			}

			// 删除文件
			for (int i = 0; i < lo_FileNames.size(); i++) {
				MFile.killFile(lo_FileNames.get(i));
			}

			CSqlHandle.getJdbcTemplate().batchUpdate(
					(String[]) batchSqlList.toArray(new String[batchSqlList
							.size()]));

			// 删除业务数据表
			if (MGlobal.isExist(ls_Sqls)) {
				String[] ls_Array = ls_Sqls.split(";");
				for (int i = 0; i < ls_Array.length; i++) {
					if (MGlobal.isExist(ls_Array[i])
							&& ls_Array[i].substring(0, 1).equals("[")) {
						int j = ls_Array[i].indexOf("]");
						String ls_ConnCode = ls_Array[i].substring(1, j - 1);
						CSqlHandle.execSql(ls_Array[i]);
					}
				}
			}
			lo_FileNames.clear();
			lo_FileNames = null;

			return true;
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWorkAdmin->deleteWorkItem", as_WorkItemIds);
			return false;
		}
	}

	/**
	 * 读取附加删除的SQL语句
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_Condition
	 *            限制条件
	 * @return
	 * @throws SQLException
	 */
	private static String getAddDelSqls(CLogon ao_Logon, String as_Condition)
			throws SQLException {
		String ls_Sql = "SELECT WorkItemID, ExtendFields FROM TemplateInst"
				+ as_Condition;
		List<Map<String,Object>> lo_Set = CSqlHandle.getSetBySql(ls_Sql);
		ls_Sql = "";
		for(Map<String,Object> row : lo_Set) {
			if (MGlobal.readObject(row,"ExtendFields") != null) {
				ls_Sql += getAddDelSql(ao_Logon, MGlobal.readInt(row,"WorkItemID"),
						MGlobal.readString(row,"ExtendFields"));
			}
		}
		lo_Set = null;
		return ls_Sql;
	}

	/**
	 * 读取附加删除的SQL语句
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param al_WorkItemID
	 *            公文标识
	 * @param as_Xml
	 *            定义的Xml内容
	 * @return
	 */
	private static String getAddDelSql(CLogon ao_Logon, int al_WorkItemID,
			String as_Xml) {
		Document lo_Data = MXmlHandle.LoadXml(as_Xml);
		if (lo_Data == null)
			return "";
		if (lo_Data.getDocumentElement().getAttribute("DeleteHandle")
				.equals("")) {
			lo_Data = null;
			return "";
		}
		Document lo_Xml = MXmlHandle.LoadXml(lo_Data.getDocumentElement()
				.getAttribute("DeleteHandle"));
		lo_Data = null;
		if (lo_Xml == null)
			return "";

		String ls_Conn = lo_Xml.getDocumentElement().getAttribute("ConnCode");
		String ls_Sql = "", ls_Value = "";
		for (int i = 0; i < lo_Xml.getDocumentElement().getChildNodes()
				.getLength(); i++) {
			Element lo_Node = (Element) lo_Xml.getDocumentElement()
					.getChildNodes().item(i);
			ls_Value = lo_Node.getAttribute("ConnCode");

			if (MGlobal.isEmpty(ls_Value))
				ls_Value = ls_Conn;

			if (MGlobal.isExist(ls_Value))
				ls_Sql += "[" + ls_Value + "]";
			ls_Sql += "DELETE FROM " + lo_Node.getAttribute("Table")
					+ " WHERE " + lo_Node.getAttribute("ItemId") + " = "
					+ String.valueOf(al_WorkItemID);
			if (MGlobal.isExist(lo_Node.getAttribute("Condition"))) {
				if (MGlobal.isExist(lo_Node.getAttribute("Condition"))) {
					ls_Sql += " AND (" + lo_Node.getAttribute("Condition")
							+ ")";
				}
			}
			ls_Sql += ";";
		}
		lo_Xml = null;

		return ls_Sql;
	}

	/**
	 * 获取公文模板列表
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_Condition
	 *            限制条件
	 * @param as_Order
	 *            排列顺序
	 * @param al_CurPage
	 *            当前页数
	 * @param ai_PageRows
	 *            每页数量
	 * @return 返回结果集
	 */
	public static List<Map<String,Object>> getCyclostyleListAll(CLogon ao_Logon,
			String as_Condition, String as_Order, int al_CurPage,
			int ai_PageRows) {
		String ls_Fields = "TemplateID,TemplateName,TemplateCode,TypeID,CreateID,Creator,CreateDate,"
				+ "EditID,Editor,EditDate,HaveBody,HaveDocument, HaveForm,FirstPage,FlowFlag,TemplateDescribe,"
				+ "TemplateIsValid,BindImage,IsPublicUsing,OrderID,COMP_ID,TypeName";
		return CSqlHandle.getSetAsPage("V_TemplateDefine", "TemplateID",
				ls_Fields, as_Condition, as_Order, al_CurPage, ai_PageRows);
	}

	/**
	 * 删除公文模板
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_CyclostyleIDs
	 *            流程模板标识连接串，使用“;”分开
	 * @return
	 */
	public static boolean deleteCyclostyles(CLogon ao_Logon,
			String as_CyclostyleIDs) {
		try {
			if (MGlobal.isEmpty(as_CyclostyleIDs))
				return false;

			// 记录日志
			ao_Logon.Record("TWorkAdmin->deleteCyclostyles", as_CyclostyleIDs);

			String ls_Sql = "";
			String ls_Cond = as_CyclostyleIDs.replaceAll(";", ",");
			if (MGlobal.right(ls_Cond, 1).equals(","))
				ls_Cond = ls_Cond.substring(0, ls_Cond.length() - 1);
			ls_Cond = "WHERE TemplateID IN (" + ls_Cond + ")";

			List<String> batchSqlList = new ArrayList<String>();

			// 模板使用权限：WorkRight
			batchSqlList.add("DELETE FROM WorkRight "
					+ ls_Cond.replace("TemplateID", "ModuleID")
					+ " AND AdminType = 1");
			// 流程步骤：FlowActivity
			batchSqlList
					.add("DELETE FROM FlowActivity WHERE FlowID IN (SELECT FlowID FROM FlowDefine "
							+ ls_Cond + ")");
			// 流程定义：FlowDefine
			batchSqlList.add("DELETE FROM FlowDefine " + ls_Cond);
			// 权限列表：RightList
			batchSqlList.add("DELETE FROM RightList " + ls_Cond);
			// 表头属性：PropList
			batchSqlList.add("DELETE FROM PropList " + ls_Cond);
			// 附件模板使用列表
			batchSqlList.add("DELETE FROM DocumentFileList " + ls_Cond);
			// 附件列表：DocumentList
			batchSqlList.add("DELETE FROM DocumentList " + ls_Cond);
			// 数据存储表格定义：TableList
			batchSqlList.add("DELETE FROM TableList " + ls_Cond);
			// #表格定义列表：FormList
			batchSqlList.add("DELETE FROM FormList " + ls_Cond);
			// #角色列表：RoleList
			batchSqlList.add("DELETE FROM RoleList " + ls_Cond);
			// #模板专用二次开发函数：ScriptList
			batchSqlList.add("DELETE FROM ScriptList " + ls_Cond);
			// 删除公文发布模板定义：PublicTemplate
			batchSqlList.add("DELETE FROM PublicTemplate" + ls_Sql);
			// #公文模板定义：TemplateDefine
			batchSqlList.add("DELETE FROM TemplateDefine " + ls_Cond);

			CSqlHandle.getJdbcTemplate().batchUpdate(
					(String[]) batchSqlList.toArray(new String[batchSqlList
							.size()]));

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 导出公文到Xml文件
	 * 
	 * @param ao_Item
	 * @return
	 */
	public static String workItemToXmlFile(CWorkItem ao_Item) {
		try {
			String ls_Xml = ao_Item.ExportToXml("WorkItem", 0);
			String ls_Path = ao_Item.Logon.getParaValue("JAVA_TEMP_PATH") + "U"
					+ ao_Item.Logon.getUserID();
			MFile.createFolder(ls_Path);
			String ls_File = MFile.getTempFile("xml", ls_Path);
			ls_Xml = MFile.writeTxtFile(ls_Path + "\\" + ls_File, ls_Xml);
			if (MGlobal.isEmpty(ls_Xml))
				return ls_Path + "\\" + ls_File;
			return "";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	/**
	 * 从Xml文件导入到公文
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param as_File
	 *            临时文件
	 * @return
	 */
	public static CWorkItem xmlFileToWorkItem(CLogon ao_Logon, String as_File) {
		try {
			String ls_Xml = MFile.readTxtFile(as_File);
			CWorkItem lo_Item = new CWorkItem(ao_Logon);
			lo_Item.ImportFormXml(ls_Xml, 0);
			ls_Xml = null;
			return lo_Item;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据公文标识获取模板标识
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param al_WorkItemId
	 *            公文标识
	 * @return
	 */
	public static int getTemplateIdByWorkItem(CLogon ao_Logon, int al_WorkItemId) {
		String ls_Sql = "SELECT TemplateID FROM TemplateInst WHERE WorkItemID = "
				+ String.valueOf(al_WorkItemId);
		ls_Sql = CSqlHandle.getValueBySql(ls_Sql);
		if (MGlobal.isEmpty(ls_Sql))
			return -1;
		return Integer.parseInt(ls_Sql);
	}

	/**
	 * 根据公文标识获取模板代码
	 * 
	 * @param ao_Logon
	 *            登录对象
	 * @param al_WorkItemId
	 *            公文标识
	 * @return
	 */
	public static String getTemplateCodeByWorkItem(CLogon ao_Logon,
			int al_WorkItemId) {
		String ls_Sql = "SELECT TemplateID FROM TemplateInst WHERE WorkItemID = "
				+ String.valueOf(al_WorkItemId);
		return CSqlHandle.getValueBySql(ls_Sql);
	}

	/**
	 * 
	 * @param al_Count
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<HashMap<String, Object>> getRemindMessage(
			int al_Count) throws SQLException {
		String ls_MaxId = CSqlHandle
				.getValueBySql(
						"SELECT ParameterValue FROM SysParameter WHERE ParameterCode = 'MAX_SERVICE_ID'",
						"ParameterValue");
		if (MGlobal.isEmpty(ls_MaxId))
			ls_MaxId = "0";
		String ls_Sql = "SELECT TOP "
				+ String.valueOf(al_Count)
				+ " * FROM RemindMessage WHERE ItemID IN (11, 12) AND RemindStatus = 0 AND MessageID > "
				+ ls_MaxId;// +
							// " order by UpdateDate";
		List<Map<String,Object>> lo_Set = CSqlHandle.getSetBySql(ls_Sql, 0);
		int ll_Id = 0;
		ArrayList<HashMap<String, Object>> lo_List = new ArrayList<HashMap<String, Object>>();
		String ls_Fields = "MessageID,ItemID,UserID,RemindContent,RemindStatus,UpdateDate,Parameter1,";
		ls_Fields += "Parameter2,Parameter3,RemindParameter,RemindLink,RemindAddFlag,RemindRunFlag,";
		ls_Fields += "MutiRemind,RemindMaxNum,RemindNum,RemingMinute,LastRemind,OperatorID,Operator";
		String[] ls_ArrField = ls_Fields.split(",");
		for(Map<String,Object> row : lo_Set) {
			HashMap<String, Object> lo_Map = new HashMap<String, Object>();
			for (String ls_Field : ls_ArrField) {
				if (ls_Field.equals("RemindLink")) {
					String ls_Url = "workflow/wf/openTask.do?WorkItemID={0}&EntryID={1}&flowTaskType=1";
					lo_Map.put(
							ls_Field,
							MessageFormat.format(ls_Url,
									MGlobal.readString(row,"Parameter1"),
									MGlobal.readString(row,"Parameter2")));
				} else {
					lo_Map.put(ls_Field, MGlobal.readObject(row,ls_Field));
				}
			}
			if (ll_Id < MGlobal.readInt(row,"MessageID"))
				ll_Id = MGlobal.readInt(row,"MessageID");
			lo_List.add(lo_Map);
		}
		if (ll_Id > 0) {
			CSqlHandle.execSql("UPDATE SysParameter SET ParameterValue = '"
					+ String.valueOf(ll_Id)
					+ "' WHERE ParameterCode = 'MAX_SERVICE_ID'", "", false);
		}
		return lo_List;
	}

	/**
	 * 插入人工监督
	 * 
	 * @param ao_Logon
	 * @param as_XmlData
	 * @return
	 */
	public static boolean insertUserSupervise(CLogon ao_Logon, String as_XmlData) {
		try {
			Document lo_Xml = MXmlHandle.LoadXml(as_XmlData);
			Element lo_Node = lo_Xml.getDocumentElement();
			String ls_Sql = "SELECT EntryID, StateID, RecipientID, Recipients, InceptDate, ActivityName FROM EntryInst";
			ls_Sql += " WHERE WorkItemID = {0} AND EntryType = 2 AND StateID IN (0, 1, 2) AND WorkItemID IN";
			ls_Sql += " (SELECT WorkItemID FROM WorkItemInst WHERE WorkItemStatus = 0)";
			ls_Sql = MessageFormat.format(ls_Sql,
					lo_Node.getAttribute("WorkItemID"));
			if (MGlobal.isExist(lo_Node.getAttribute("EntryIDS"))) {
				String ls_IDs = lo_Node.getAttribute("EntryIDS").replace(";",
						",");
				if (MGlobal.right(ls_IDs, 1).equals(","))
					ls_IDs = MGlobal.left(ls_IDs, ls_IDs.length() - 1);
				ls_Sql += MessageFormat.format(" AND EntryID IN ({0})", ls_IDs);
			}

			List<Map<String,Object>> lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			if (!(lo_Set.size()>0)) {
				lo_Set = null;
				// 错误：当前公文不可设置人工监督
				ao_Logon.Raise(1123, "TWorkAdmin->insertUserSupervise",
						as_XmlData);
				return false;
			}

			ls_Sql = "SELECT MAX(SuperviseID) FROM UserSupervise WHERE WorkItemID = "
					+ lo_Node.getAttribute("WorkItemID");
			String ls_SuperviseID = CSqlHandle.getValueBySql(ls_Sql);

			String lo_Supervise = "INSERT INTO UserSupervise (WorkItemID, SuperviseID, UserID, Superintendent, EntryID, StartDate, StopDate, SupervisedType, SuperviseContent, AddMsgType) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			String lo_User = "INSERT INTO SuperviseUser (SuperviseID, WorkItemID, EntryID, SuperviseStatus, ReadDate, ResponseDate, ResponseContent) VALUES (?, ?, ?, ?, ?, ?, ?)";

			List lo_SuperviseList = new ArrayList();
			lo_SuperviseList.add(Integer.parseInt(lo_Node
					.getAttribute("WorkItemID"))); // WorkItemID
			if (MGlobal.isInt(ls_SuperviseID)) { // SuperviseID
				lo_SuperviseList.add(Integer.parseInt(ls_SuperviseID) + 1);
			} else {
				lo_SuperviseList.add(1);
			}
			lo_SuperviseList.add(ao_Logon.GlobalPara.UserID);// UserID
			lo_SuperviseList.add(ao_Logon.GlobalPara.UserName);// Superintendent
			lo_SuperviseList.add(0);// EntryID
			lo_SuperviseList.add(MGlobal.stringToDate(lo_Node
					.getAttribute("StartDate"))); // StartDate
			lo_SuperviseList.add(MGlobal.stringToDate(lo_Node
					.getAttribute("StopDate"))); // StopDate
			lo_SuperviseList.add(1);// SupervisedType
			lo_SuperviseList.add(lo_Node.getAttribute("SuperviseContent"));// SuperviseContent
			lo_SuperviseList.add(Integer.parseInt(lo_Node
					.getAttribute("SuperviseContent")));// AddMsgType
			CSqlHandle.getJdbcTemplate().update(lo_Supervise,
					lo_SuperviseList.toArray());

			String ls_UserIDs = "";
			boolean lb_Boolean = true;

			for(Map<String,Object> row : lo_Set) {
				List lo_UserList = new ArrayList();
				lo_UserList.add(MGlobal.readInt(row,"SuperviseID"));
				lo_UserList.add(MGlobal.readInt(row,"WorkItemID"));
				lo_UserList.add(MGlobal.readInt(row,"EntryID"));
				lo_UserList.add(0);// SuperviseStatus
				lo_UserList.add(null);// ReadDate
				lo_UserList.add(null);// ResponseDate
				lo_UserList.add(null);// ResponseContent
				CSqlHandle.getJdbcTemplate().update(lo_User,
						lo_UserList.toArray());
			}
			lo_Set = null;

			int li_Type = Integer.parseInt(lo_Node.getAttribute("AddMsgType"));
			if (li_Type > 0) {
				String ls_Msg = "流转系统督办通知：您收到的文件[{0}]请尽快处理，谢谢！\n督办人：{1}";
				ls_Msg = MessageFormat.format(ls_Msg,
						lo_Node.getAttribute("WorkItemName"),
						ao_Logon.GlobalPara.UserName);
				if (MGlobal.isExist(lo_Node.getAttribute("SuperviseContent")))
					ls_Msg += "\n督办信息："
							+ lo_Node.getAttribute("SuperviseContent");
				if ((li_Type & 1) == 1) {// 手机短信通知
					// 增加手机短信通知
				}

				if ((li_Type & 2) == 2) {// 内部即时消息通知
					// 增加内部即时消息通知
				}
			}
			lo_Xml = null;

			return true;
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWorkAdmin->insertUserSupervise", as_XmlData);
			return false;
		}
	}

	/**
	 * 删除人工监督
	 * 
	 * @param ao_Logon
	 * @param as_ItemSupervise
	 * @return
	 */
	public static boolean deleteUserSupervise(CLogon ao_Logon,
			String as_ItemSupervise) {
		try {
			String ls_Sql = "";
			String ls_Array[] = as_ItemSupervise.split(",");
			for (int i = 0; i < ls_Array.length; i++) {
				if (MGlobal.isExist(ls_Array[i])) {
					String ls_ID[] = ls_Array[i].split("_");
					ls_Sql += MessageFormat
							.format("DELETE FROM SuperviseUser WHERE WorkItemID = {0} AND SuperviseID = {1};",
									ls_ID[0], ls_ID[2]);
					ls_Sql += MessageFormat
							.format("DELETE FROM UserSupervise WHERE WorkItemID = {0} AND SuperviseID = {1};",
									ls_ID[0], ls_ID[2]);
				}
			}

			return CSqlHandle.execSql(ls_Sql, ";");
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWorkAdmin->deleteUserSupervise",
					as_ItemSupervise);
			return false;
		}
	}

	/**
	 * 更新公文被监督人描述
	 * 
	 * @param as_XmlData
	 * @return
	 */
	public static boolean updateSuperviseUser(CLogon ao_Logon, String as_XmlData) {
		try {
			Document lo_Xml = MXmlHandle.LoadXml(as_XmlData);
			String ls_Value = lo_Xml.getDocumentElement().getAttribute("ID");
			String ls_ID[] = ls_Value.split("_");
			String ls_Sql = "SELECT * FROM SuperviseUser WHERE WorkItemID = {0} AND SuperviseID = {1} AND EntryID = {2}";
			ls_Sql = MessageFormat.format(ls_Sql, ls_ID[0], ls_ID[1], ls_ID[2]);
			List<Map<String,Object>> lo_Set = CSqlHandle.getSetBySql(ls_Sql);
			for(Map<String,Object> row : lo_Set) {
				if (lo_Xml.getDocumentElement().getAttribute("SuperviseStatus")
						.equals("1")) {
					if (MGlobal.readInt(row,"SuperviseStatus") > 0) {
						lo_Set = null;
						return false;
					}
					ls_Sql = "UPDATE SuperviseUser SET ReadDate = GETDATE() WHERE WorkItemID = {0} AND SuperviseID = {1} AND EntryID = {2}";
					ls_Sql = MessageFormat.format(ls_Sql, ls_ID[0], ls_ID[1],
							ls_ID[2]);
				} else {
					if (MGlobal.readInt(row,"SuperviseStatus") > 1) {
						lo_Set = null;
						return false;
					}
					ls_Sql = "UPDATE SuperviseUser SET ResponseDate = GETDATE(), ResponseContent = '{0}', SuperviseStatus = {1} WHERE WorkItemID = {2} AND SuperviseID = {3} AND EntryID = {4}";
					ls_Sql = MessageFormat.format(
							ls_Sql,
							lo_Xml.getDocumentElement()
									.getAttribute("ResponseContent")
									.replace("'", "''"),
							lo_Xml.getDocumentElement().getAttribute(
									"SuperviseStatus"), ls_ID[0], ls_ID[1],
							ls_ID[2]);
				}
			}
			lo_Set = null;

			return CSqlHandle.execSql(ls_Sql);
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWorkAdmin->updateSuperviseUser", as_XmlData);
			return false;
		}
	}

	/**
	 * 删除公文被监督人描述
	 * 
	 * @param as_ItemSupervise
	 * @return
	 */
	public static boolean deleteSuperviseUser(CLogon ao_Logon,
			String as_ItemSupervise) {
		try {
			String ls_Array[] = as_ItemSupervise.split(";");
			String ls_Sql = "";

			for (int i = 0; i < ls_Array.length; i++) {
				if (MGlobal.isExist(ls_Array[i])) {
					String ls_ID[] = ls_Array[i].split("_");
					ls_Sql += MessageFormat
							.format("DELETE FROM SuperviseUser WHERE WorkItemID = {0} AND SuperviseID = {1} AND EntryID = {2};",
									ls_ID[0], ls_ID[1], ls_ID[2]);
				}
			}

			return CSqlHandle.execSql(ls_Sql, ";");
		} catch (Exception ex) {
			ao_Logon.Raise(ex, "TWorkAdmin->deleteSuperviseUser",
					as_ItemSupervise);
			return false;
		}
	}
}

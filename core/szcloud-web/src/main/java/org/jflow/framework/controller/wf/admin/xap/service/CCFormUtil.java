package org.jflow.framework.controller.wf.admin.xap.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import BP.DA.DataColumn;
import BP.DA.DataColumnCollection;
import BP.DA.DataRow;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.DA.Para;

public class CCFormUtil {
	/**
	 * 日志对象
	 */
	private static final Log logger = LogFactory.getLog(CCFormUtil.class);

	public static String SaveDT(DataTable dt) throws Exception {
		if (dt.Rows.size() == 0)
			return "";

		String tableName = dt.TableName.replace("CopyOf", "");
		if (tableName.equals("Sys_MapData".toUpperCase())) {
			int i = 0;
		}

		if (dt.Columns.size() <= 0)
			return null;

		TableSQL sqls = getTableSql(tableName, dt.Columns);
		String updataSQL = sqls.UPDATED;
		String pk = sqls.PK;
		String insertSQL = sqls.INSERTED;

		for (DataRow dr : dt.Rows) {
			BP.DA.Paras ps = new BP.DA.Paras();
			for (DataColumn dc : dt.Columns) {
				if (dc.ColumnName.toUpperCase() == pk)
					continue;

				if (tableName == "Sys_MapAttr".toUpperCase() && dc.ColumnName.trim() == "UIBindKey".toUpperCase())
					continue;

				if (updataSQL.contains(BP.Sys.SystemConfig.getAppCenterDBVarStr() + dc.ColumnName.trim())) {
					try {
						ps.Add(dc.ColumnName.trim().toUpperCase(), dr.get(dc.ColumnName.trim()));
					} catch (Exception e) {
						ps.Add(dc.ColumnName.trim(), null);
					}
				}
			}

			ps.Add(pk.toUpperCase(), dr.get(pk.toUpperCase()));
			ps.SQL = updataSQL;
			try {
				if (BP.DA.DBAccess.RunSQL(ps) == 0) {
					ps.clear();
					for (DataColumn dc : dt.Columns) {
						if (tableName == "Sys_MapAttr".toUpperCase() && dc.ColumnName == "UIBindKey".toUpperCase())
							continue;

						if (updataSQL.contains(BP.Sys.SystemConfig.getAppCenterDBVarStr() + dc.ColumnName.trim()))
							ps.Add(dc.ColumnName.trim().toUpperCase(), dr.get(dc.ColumnName.trim()));
					}
					ps.SQL = insertSQL;
					BP.DA.DBAccess.RunSQL(ps);
					continue;
				}
			} catch (Exception ex) {
				String pastrs = "";
				for (Para p : ps) {
					pastrs += "\t\n@" + p.ParaName + "=" + p.val;
				}
				throw new Exception("@执行sql=" + ps.SQL + "失败." + ex.getMessage() + "\t\n@paras=" + pastrs);
			}
		}
		return null;
	}

	/// <summary>
	/// 保存文件
	/// </summary>
	/// <param name="FileByte">文件大小</param>
	/// <param name="fileName">文件名</param>
	/// <param name="saveTo">保存路径</param>
	/// <returns></returns>
	public static String UploadFile(byte[] FileByte, String fileName, String saveTo) throws IOException {
		// 创建目录.
		String pathSave = BP.Sys.SystemConfig.getPathOfTemp() + File.separator + saveTo;
		File file1 = new File(pathSave);
		if (file1.exists())
			file1.mkdirs();

		String filePath = pathSave + fileName;

		File file2 = new File(filePath);
		if (file2.exists())
			file2.delete();

		// 创建文件流实例，用于写入文件
		FileOutputStream stream = new FileOutputStream(filePath);

		// 写入文件
		stream.write(FileByte, 0, FileByte.length);
		stream.close();

		DataSet ds = new DataSet();
		ds.readXml(filePath);

		return DataSet.ConvertDataSetToXml(ds);
	}

	class TableSQL {
		public TableSQL() {

		}

		public String PK;
		public String INSERTED;
		public String UPDATED;
	}

	static Hashtable<String, TableSQL> dicTableSql = new Hashtable<String, TableSQL>();

	public static TableSQL getTableSql(String tableName, DataColumnCollection columns) {
		TableSQL tableSql = null;
		if (dicTableSql.containsKey(tableName)) {
			tableSql = dicTableSql.get(tableName);
		} else {
			if (columns != null && columns.size() > 0) {
				String igF = "@RowIndex@RowState@";

				// 生成sqlUpdate
				String sqlUpdate = "UPDATE " + tableName + " SET ";
				for (DataColumn dc : columns) {
					if (igF.contains(":" + dc.ColumnName + ":"))
						continue;

					String str = dc.ColumnName;
					if (str.equals("MyPK".toUpperCase())) {

					} else if (str.equals("OID")) {

					} else if (str.equals("No".toUpperCase())) {
						logger.debug("welcome");
						continue;
					} else {
						// logger.debug("wwww");
						// break;
					}

					if (tableName == "Sys_MapAttr".toUpperCase() && dc.ColumnName == "UIBindKey")
						continue;
					try {
						sqlUpdate += dc.ColumnName + "=" + BP.Sys.SystemConfig.getAppCenterDBVarStr()
								+ dc.ColumnName.trim() + ",";
					} catch (Exception e) {
					}
				}
				sqlUpdate = sqlUpdate.substring(0, sqlUpdate.length() - 1);
				String pk = "";
				for (DataColumn c : columns) {
					if (c.ColumnName.equals("NodeID".toUpperCase())) {
						pk = "NODEID";
						break;
					}
					if (c.ColumnName.equals("MyPK".toUpperCase())) {
						pk = "MYPK";
						break;
					}
					if (c.ColumnName.equals("OID")) {
						pk = "OID";
						break;
					}
					if (c.ColumnName.equals("No".toUpperCase())) {
						pk = "NO";
						break;
					}
					if (c.ColumnName.equals("NodeID".toUpperCase())) {
						break;
					}
					pk = "NODEID";
				}

				sqlUpdate += " WHERE " + pk + "=" + BP.Sys.SystemConfig.getAppCenterDBVarStr() + pk;

				// 生成sqlInsert
				String sqlInsert = "INSERT INTO " + tableName + "(";
				for (DataColumn dc : columns) {
					if (igF.contains("@" + dc.ColumnName.trim() + "@"))
						continue;

					if (tableName == "Sys_MapAttr".toUpperCase() && dc.ColumnName.trim() == "UIBindKey".toUpperCase())
						continue;

					sqlInsert += dc.ColumnName.trim() + ",";
				}
				sqlInsert = sqlInsert.substring(0, sqlInsert.length() - 1);
				sqlInsert += ") VALUES (";
				for (DataColumn dc : columns) {
					if (igF.contains("@" + dc.ColumnName + "@"))
						continue;

					if (tableName == "Sys_MapAttr" && dc.ColumnName.trim() == "UIBindKey")
						continue;

					sqlInsert += BP.Sys.SystemConfig.getAppCenterDBVarStr() + dc.ColumnName.trim() + ",";
				}
				sqlInsert = sqlInsert.substring(0, sqlInsert.length() - 1);
				sqlInsert += ")";
				CCFormUtil ccfu = new CCFormUtil();
				tableSql = ccfu.new TableSQL();
				tableSql.UPDATED = sqlUpdate;
				tableSql.INSERTED = sqlInsert;
				tableSql.PK = pk;

				dicTableSql.put(tableName, tableSql);
			}
		}

		return tableSql;
	}

}

package BP.WF.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import BP.DA.DBType;
import BP.DA.DataSet;
import BP.DA.DataTable;
import BP.Sys.MapData;
import BP.Sys.SystemConfig;
import BP.Tools.StringHelper;
import BP.WF.Node;
import BP.WF.HttpHandler.Base.WebContralBase;

public class WF_Admin_FoolFormDesigner_ImpExp extends WebContralBase {
	/**
	 * 初始化 导入的界面 .
	 * @return
	 */
    public String Imp_Init()
    {
        DataSet ds = new DataSet();
        String sql = "";
        DataTable dt;
        if(!StringHelper.isNullOrEmpty(getFK_Flow()))
        {
            //加入节点表单. 如果没有流程参数.
        	sql = "SELECT NodeID, Name  FROM WF_Node WHERE FK_Flow='" + this.getFK_Flow() + "' ORDER BY NODEID ";
            dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
            dt.TableName = "WF_Node";
            if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
            {
                dt.Columns.get("NODEID").ColumnName = "NodeID";
                dt.Columns.get("NAME").ColumnName = "Name";
            }
            ds.Tables.add(dt);
        }
        //加入表单库目录.
        if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
            sql = "SELECT NO as No ,Name,ParentNo FROM Sys_FormTree ORDER BY  PARENTNO, IDX ";
        else
            sql = "SELECT No,Name,ParentNo FROM Sys_FormTree ORDER BY  PARENTNO, IDX ";
        dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
        if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
        {
            dt.Columns.get("NO").ColumnName = "No";
            dt.Columns.get("NAME").ColumnName = "Name";
            dt.Columns.get("PARENTNO").ColumnName = "ParentNo";
        }
        dt.TableName = "Sys_FormTree";
        ds.Tables.add(dt);
        //加入表单
        sql = "SELECT A.No, A.Name, A.FK_FormTree  FROM Sys_MapData A, Sys_FormTree B WHERE A.FK_FormTree=B.No";
        dt = BP.DA.DBAccess.RunSQLReturnTable(sql);
        dt.TableName = "Sys_MapData";
        ds.Tables.add(dt);
        if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
        {
            dt.Columns.get("NO").ColumnName = "No";
            dt.Columns.get("NAME").ColumnName = "Name";
            dt.Columns.get("FK_FORMTREE").ColumnName = "FK_FormTree";
        }
        BP.Sys.SFDBSrcs ens = new BP.Sys.SFDBSrcs();
        ens.RetrieveAll();
        ds.Tables.add(ens.ToDataTableField("SFDBSrcs"));
        if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
        	return BP.Tools.Json.DataSetToJson(ds,false,false,true);
        else
        	return BP.Tools.Json.ToJson(ds);
    }
   
	public void setMultipartRequest(DefaultMultipartHttpServletRequest request) {
		this.request = request;
	}

	private DefaultMultipartHttpServletRequest request;

    /**
     * 从本机装载表单模版
     * <param name="fileByte">文件流</param>
     * <param name="fk_mapData">表单模版ID</param>
     * <param name="isClear">是否清空？</param>
     * @return
     */
    public String Imp_LoadFrmTempleteFromLocalFile()
    {
		File xmlFile = null;
		String fileName = UUID.randomUUID().toString();
		try {
			xmlFile = File.createTempFile(fileName, ".xml");
		} catch (IOException e1) {
			xmlFile = new File(System.getProperty("java.io.tmpdir"), fileName + ".xml");
		}
		xmlFile.deleteOnExit();
		String contentType = getRequest().getContentType();
		if (contentType != null && contentType.indexOf("multipart/form-data") != -1) {
			MultipartFile multipartFile = request.getFile("file");
			try {
				multipartFile.transferTo(xmlFile);
			} catch (Exception e) {
				return "执行失败";
			}
		}

//    	MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) getRequest();
//		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");

//    	if(getRequest().getParameter("file")==null || getRequest().getParameter("file").length()==0)
//    	{
//            return "请上传导入的模板文件.";
//    	}
    	Object a  = getRequest().getParameter("file");
        String fk_mapData = this.getFK_MapData();
        //读取上传的XML 文件.
        DataSet ds = new DataSet();
//        ds.readXml(getRequest().getParameter("file"));
		ds.readXml(xmlFile.getAbsolutePath());
        //ds.ReadXml(this.context.Request.Files[0].InputStream);
        //执行装载.
        MapData.ImpMapData(fk_mapData, ds, true);

        if (fk_mapData.contains("ND"))
        {
             //判断是否是节点表单 
            int nodeID = 0;
            try
            {
                nodeID = Integer.parseInt(fk_mapData.replace("ND", ""));
            }
            catch(Exception e)
            {
                return "执行成功.";
            }

            Node nd = new Node(nodeID);
            nd.RepareMap();
        }
        return "执行成功.";
    }
    
    /**
     * 从节点上Copy
     *  <param name="fromMapData">从表单ID</param>
     *  <param name="fk_mapdata">到表单ID</param>
     *  <param name="isClear">是否清楚现有的元素？@param
     *  <param name="isSetReadonly">是否设置为只读？</param>
     *  <returns>执行结果</returns>
     */
    public String Imp_CopyFrm()
    {
    	String fromMapData = this.getFromMapData();
        boolean isClear = this.getIsClear();
        boolean isSetReadonly = this.getIsSetReadonly();

        MapData md = new MapData(fromMapData);

        MapData.ImpMapData(this.getFK_MapData(), BP.Sys.CCFormAPI.GenerHisDataSet(md.getNo(),isSetReadonly),true);
        //设置为只读模式.
		if (this.getIsSetReadonly() == true)
		{
			MapData.SetFrmIsReadonly(this.getFK_MapData());
		}
        // 如果是节点表单，就要执行一次修复，以免漏掉应该有的系统字段。
        if (this.getFK_MapData().contains("ND") == true)
        {
            String fk_node = this.getFK_MapData().replace("ND", "");
            Node nd = new Node(Integer.parseInt(fk_node));
            nd.RepareMap();
        }
        return "执行成功.";
    }
    
    private String getFromMapData() {
    	String str = this.GetRequestVal("FromMapData");
		if (str == null || str.equals("") || str.equals("null"))
		{
			return null;
		}
		return str;
	}
	public boolean getIsClear()
    {
    	String isClearStr = getRequest().getParameter("IsClear");
        boolean isClear = false;
        if (!StringHelper.isNullOrEmpty(isClearStr) && isClearStr.toString().toLowerCase().equals("on"))
        {
            isClear = true;
        }
            return isClear;
    }
    
    public boolean getIsSetReadonly()
    {
            String isSetReadonlyStr = getRequest().getParameter("IsSetReadonly");
            boolean isSetReadonly = false;
            if (!StringHelper.isNullOrEmpty(isSetReadonlyStr) && isSetReadonlyStr.toString().toLowerCase().equals("on"))
            {
                isSetReadonly = true;
            }
            return isSetReadonly;
    }
    
  /// 从表单库导入
    /// 从节点导入
    /// </summary>
    /// <returns></returns>
    public String Imp_FromsCopyFrm()
    {
        return Imp_CopyFrm();
    }
}

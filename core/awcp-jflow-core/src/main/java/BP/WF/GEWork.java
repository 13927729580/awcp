package BP.WF;

import BP.En.Entities;
import BP.En.Map;
import BP.En.SQLCash;

/** 
 普通工作
*/
public class GEWork extends Work
{
	private SQLCash _SQLCash = null;
	@Override
	public SQLCash getSQLCash()
	{
		if (_SQLCash == null)
		{
			_SQLCash = BP.DA.Cash.GetSQL(this.NodeFrmID.toString());
			if (_SQLCash == null)
			{
				_SQLCash = new SQLCash(this);
				BP.DA.Cash.SetSQL(this.NodeFrmID.toString(), _SQLCash);
			}
		}
		return _SQLCash;
	}
	@Override
	public void setSQLCash(SQLCash value)
	{
		_SQLCash = value;
	}
	/** 
	 普通工作
	*/
	public GEWork()
	{
	}
	/** 
	 普通工作
	 @param nodeid 节点ID
	*/
	public GEWork(int nodeid, String nodeFrmID)
	{
		this.NodeFrmID = nodeFrmID;
		this.setNodeID(nodeid);
		this.setSQLCash(null);
	}
	/** 
	 普通工作
	 @param nodeid 节点ID
	 @param _oid OID
	*/
	public GEWork(int nodeid, String nodeFrmID, long _oid)
	{
		this.NodeFrmID = nodeFrmID;
		this.setNodeID(nodeid);
		this.setOID(_oid);
		this.setSQLCash(null);
	}
	/** 
	 重写基类方法
	*/
	@Override
	public Map getEnMap()
	{
		this.set_enMap(BP.Sys.MapData.GenerHisMap(this.NodeFrmID));
		return this.get_enMap();
	}
	/** 
	 GEWorks
	*/
	@Override
	public Entities getGetNewEntities()
	{
		if (this.getNodeID() == 0)
		{
			return new GEWorks();
		}
		return new GEWorks(this.getNodeID(), this.NodeFrmID);
	}
}
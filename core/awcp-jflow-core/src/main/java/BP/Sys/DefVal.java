package BP.Sys;

import BP.DA.Depositary;
import BP.En.EnType;
import BP.En.EntitiesOID;
import BP.En.Entity;
import BP.En.EntityOID;
import BP.En.EntityTree;
import BP.En.Map;

public class DefVal extends EntityOID {
		
	    /**
	     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	     */
	    
		private static final long serialVersionUID = 1L;
		/** 
		 父节点编号
		 
		*/
		public final String getParentNo() {
			return this.GetValStringByKey(DefValAttr.ParentNo);
		}
		public final void setParentNo(String value) {
			this.SetValByKey(DefValAttr.ParentNo, value);
		}
		/** 
		 是否父节点
		 
		*/
		public final String getIsParent() {
			return this.GetValStringByKey(DefValAttr.IsParent);
		}
		public final void setIsParent(String value) {
			this.SetValByKey(DefValAttr.IsParent, value);
		}
		/** 
		 词汇类别
		 
		*/
		public final String getWordsSort() {
			return this.GetValStringByKey(DefValAttr.WordsSort);
		}
		public final void setWordsSort(String value) {
			this.SetValByKey(DefValAttr.WordsSort, value);
		}
		/** 
		 节点编号
		 
		*/
		public final String getFK_MapData() {
			return this.GetValStringByKey(DefValAttr.FK_MapData);
		}
		public final void setFK_MapData(String value) {
			this.SetValByKey(DefValAttr.FK_MapData, value);
		}
		/** 
		 节点对应字段
		 
		*/
		public final String getAttrKey() {
			return this.GetValStringByKey(DefValAttr.AttrKey);
		}
		public final void setAttrKey(String value) {
			this.SetValByKey(DefValAttr.AttrKey, value);
		}
		/** 
		 是否历史词汇
		 
		*/
		public final String getLB() {
			return this.GetValStringByKey(DefValAttr.LB);
		}
		public final void setLB(String value) {
			this.SetValByKey(DefValAttr.LB, value);
		}
		/** 
		 人员编号
		 
		*/
		public final String getFK_Emp() {
			return this.GetValStringByKey(DefValAttr.FK_Emp);
		}
		public final void setFK_Emp(String value) {
			this.SetValByKey(DefValAttr.FK_Emp, value);
		}
		/** 
		 节点文本
		 
		*/
		public final String getCurValue() {
			return this.GetValStringByKey(DefValAttr.CurValue);
		}
		public final void setCurValue(String value) {
			this.SetValByKey(DefValAttr.CurValue, value);
		}

		/** 
		 默认值
		 
		*/
		public DefVal() {
		}
		/** 
		 map
		 
		*/
		@Override
		public Map getEnMap() {
			if (this.get_enMap() != null)
			{
				return this.get_enMap();
			}
			Map map = new Map("Sys_DefVal");
			map.setEnType( EnType.Sys);
			map.setEnDesc("选择词汇");
			map.setCodeStruct( "2");
			map.AddTBIntPKOID();

				//秦2015-1-10   根据公司需求改动   以下是源码
				//map.AddTBStringPK(DefValAttr.No, null, "编号", true, true, 1, 50, 20);
				//map.AddTBString(DefValAttr.EnsName, null, "类名称", false, true, 0, 100, 10);
				//map.AddTBString(DefValAttr.EnsDesc, null, "类描述", false, true, 0, 100, 10);
				//map.AddTBString(DefValAttr.AttrKey, null, "属性", false, true, 0, 100, 10);
				//map.AddTBString(DefValAttr.AttrDesc, null, "属性描述", false, false, 0, 100, 10);
				//map.AddTBString(DefValAttr.FK_Emp, null, "人员", false, true, 0, 100, 10);
				//map.AddTBString(DefValAttr.Val, null, "值", true, false, 0, 1000, 10);
				//map.AddTBString(DefValAttr.ParentNo, null, "父节点编号", false, false, 0, 50, 20);
				//map.AddTBInt(DefValAttr.IsParent, 0, "是否父节点", false, false);
				//map.AddTBString(DefValAttr.HistoryWords, null, "历史词汇", false, false, 0, 2000, 20);

			map.AddTBString(DefValAttr.FK_MapData, null, "实体", false, false, 0, 100, 20);
			map.AddTBString(DefValAttr.AttrKey, null, "节点对应字段", false, false, 0, 50, 20);
				//map.AddTBInt(DefValAttr.WordsSort, 0, "词汇类型", false, false);//1,2,3... 退回-移交-表单...(暂时)
			map.AddTBInt(DefValAttr.LB, 0, "类别", false, false); //我的，历史,系统，
			map.AddTBString(DefValAttr.FK_Emp, null, "人员", false, true, 0, 100, 10);
			map.AddTBString(DefValAttr.CurValue, null, "文本", false, true, 0, 4000, 10);

			this.set_enMap(map);
			return this.get_enMap();
		}


}
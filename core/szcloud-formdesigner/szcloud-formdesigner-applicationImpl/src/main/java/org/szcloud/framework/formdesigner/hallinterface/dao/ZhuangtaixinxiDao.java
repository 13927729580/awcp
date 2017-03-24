package org.szcloud.framework.formdesigner.hallinterface.dao;

import org.szcloud.framework.formdesigner.hallinterface.bean.Zhuangtai;
import org.szcloud.framework.formdesigner.hallinterface.util.DBUtil;

public class ZhuangtaixinxiDao {
	
	/**
	 * 新增受理信息
	 * @param zhuangtai
	 * @return
	 */
	public long saveZhuangtaixinxi(Zhuangtai zhuangtai){
		/*String sql="INSERT INTO zhuangtaixinxi(tongyibianhao,dangqianweizhi,songdashijian,zhuangtaileixing,zhuangtaimiaoshu," +
				"banjieriqi,banjiewenjianshu,banjiewenjian,status,shangbaoshijian,transmissionID) VALUES('"+zhuangtai.getTongyibianhao()+
				"','"+zhuangtai.getDangqianweizhi()+"',GetDate(),'"+zhuangtai.getZhuangtaileixing()+
				"','"+zhuangtai.getZhuangtaimiaoshu()+"',GetDate(),'"+zhuangtai.getBanjiewenjianshu()+
				"','"+zhuangtai.getBanjiewenjian()+"','"+zhuangtai.getStatus()+"',GetDate(),'"+zhuangtai.getTransmissionID()+"')";*/
		
		String sql="INSERT INTO zhuangtaixinxi(tongyibianhao,dangqianweizhi,songdashijian,zhuangtaileixing,zhuangtaimiaoshu," +
				"banjieriqi,banjiewenjianshu,banjiewenjian,status,shangbaoshijian,transmissionID) VALUES('"+zhuangtai.getTongyibianhao()+
				"','"+zhuangtai.getDangqianweizhi()+"',GetDate(),'"+zhuangtai.getZhuangtaileixing()+
				"','"+zhuangtai.getZhuangtaimiaoshu()+"',GetDate(),'"+zhuangtai.getBanjiewenjianshu()+
				"','"+zhuangtai.getBanjiewenjian()+"','"+zhuangtai.getStatus()+"',GetDate(),'"+zhuangtai.getTransmissionID()+"')";
		return DBUtil.insert(sql);
	}
	
}

package org.szcloud.framework.formdesigner.hallinterface.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.formdesigner.hallinterface.bean.Huzxx;
import org.szcloud.framework.formdesigner.hallinterface.util.DBUtil;

public class HuizhixinxiDao {

	/**
	 * 根据审批系统原受理编号和业务类型返回大厅统一受理编号
	 * 
	 * @param shoulibianhao
	 * @param yewuleixing
	 * @return
	 */
	public List<Map<String, Object>> getTongyibianhaos(List<String> shoulibianhaos, String yewuleixing) {
		StringBuffer buffer = new StringBuffer();
		for (Iterator<String> it = shoulibianhaos.iterator(); it.hasNext();) {
			buffer.append(it.next());
			if(it.hasNext()){
				buffer.append("','");
			}
		}
		String sql = "SELECT SHOULIBIANHAO, TONGYIBIANHAO FROM HUIZHIXINXI WHERE SHOULIBIANHAO in ('"
				+ buffer.toString() + "') AND YEWULEIXING='" + yewuleixing + "'";
		return DBUtil.getMapList(sql);
	}
	
	/**
	 * 根据审批系统原受理编号和业务类型返回大厅统一受理编号
	 * 
	 * @param shoulibianhao
	 * @param yewuleixing
	 * @return
	 */
	public String getTongyibianhao(String shoulibianhao, String yewuleixing) {
		String sql = "SELECT TONGYIBIANHAO FROM HUIZHIXINXI WHERE SHOULIBIANHAO='"
				+ shoulibianhao + "' AND YEWULEIXING='" + yewuleixing + "'";
		String tongyibianhao = DBUtil.getString(sql);
		return tongyibianhao;
	}

	/**
	 * 根据审批系统原受理编号和业务类型查询回执信息
	 * 
	 * @return
	 */
	public Huzxx getHuzxx(String shoulibianhao, String yewuleixing) {
		String sql = "SELECT BEIZHUXINXI,CUOWUBIANHAO,CUOWULEIXING,CUOWUXINXIBIAOTI,CUOWUXINXIMIAOSHU,HUIZHILEIXING,HUIZHISHIJIAN"
				+ ",SHIXIANGBIANMA,SHOUDAOHUIZHISHIJIAN,SHOULIBIANHAO,SHOULIRENBIANHAO,TONGYIBIANHAO,TRANSMISSIONID,XITONGBIANMA,"
				+ "YEWULEIXING FROM HUIZHIXINXI WHERE SHOULIBIANHAO='"
				+ shoulibianhao + "' AND YEWULEIXING='" + yewuleixing + "'";
		Map<String, Object> huzxxMap = DBUtil.getOneRow(sql);
		Huzxx huzxx = new Huzxx();
		huzxx.setBeizhuxinxi(huzxxMap.get("BEIZHUXINXI").toString());
		huzxx.setCuowubianhao(huzxxMap.get("CUOWUBIANHAO").toString());
		huzxx.setCuowuleixing(huzxxMap.get("CUOWULEIXING").toString());
		huzxx.setCuowuxinxibiaoti(huzxxMap.get("CUOWUXINXIBIAOTI").toString());
		huzxx.setCuowuxinximiaoshu(huzxxMap.get("CUOWUXINXIMIAOSHU").toString());
		huzxx.setHuizhileixing(huzxxMap.get("HUIZHILEIXING").toString());
		Timestamp hzsj = (Timestamp) huzxxMap.get("HUIZHISHIJIAN");
		huzxx.setHuizhishijian(new Date(hzsj.getTime()));
		huzxx.setShixiangbianma(huzxxMap.get("SHIXIANGBIANMA").toString());
		Timestamp sdhzsj = (Timestamp) huzxxMap.get("SHOUDAOHUIZHISHIJIAN");
		huzxx.setShoudaohuizhishijian(new Date(sdhzsj.getTime()));
		huzxx.setShoulibianhao(huzxxMap.get("SHOULIBIANHAO").toString());
		huzxx.setShoulirenbianhao(huzxxMap.get("SHOULIRENBIANHAO").toString());
		huzxx.setTongyibianhao(huzxxMap.get("TONGYIBIANHAO").toString());
		huzxx.setTransmissionID(huzxxMap.get("TRANSMISSIONID").toString());
		huzxx.setXitongbianma(huzxxMap.get("XITONGBIANMA").toString());
		huzxx.setYewuleixing(huzxxMap.get("YEWULEIXING").toString());
		return huzxx;
	}

	/**
	 * 根据审批系统原受理编号查询回执信息集合
	 * 
	 * @param shoulibianhao
	 * @return
	 */
	public List<Huzxx> queryHuzxxByShoulibianhao(String shoulibianhao) {
		String sql = "SELECT BEIZHUXINXI,CUOWUBIANHAO,CUOWULEIXING,CUOWUXINXIBIAOTI,CUOWUXINXIMIAOSHU,HUIZHILEIXING,HUIZHISHIJIAN"
				+ ",SHIXIANGBIANMA,SHOUDAOHUIZHISHIJIAN,SHOULIBIANHAO,SHOULIRENBIANHAO,TONGYIBIANHAO,TRANSMISSIONID,XITONGBIANMA,"
				+ "YEWULEIXING FROM HUIZHIXINXI WHERE SHOULIBIANHAO='"
				+ shoulibianhao + "' ";
		List<Map<String, Object>> huzxxMaps = DBUtil.getMapList(sql);
		List<Huzxx> huzxxs = new ArrayList<Huzxx>();
		for (Map<String, Object> huzxxMap : huzxxMaps) {
			Huzxx huzxx = new Huzxx();
			huzxx.setBeizhuxinxi(huzxxMap.get("BEIZHUXINXI").toString());
			huzxx.setCuowubianhao(huzxxMap.get("CUOWUBIANHAO").toString());
			huzxx.setCuowuleixing(huzxxMap.get("CUOWULEIXING").toString());
			huzxx.setCuowuxinxibiaoti(huzxxMap.get("CUOWUXINXIBIAOTI")
					.toString());
			huzxx.setCuowuxinximiaoshu(huzxxMap.get("CUOWUXINXIMIAOSHU")
					.toString());
			huzxx.setHuizhileixing(huzxxMap.get("HUIZHILEIXING").toString());
			Timestamp hzsj = (Timestamp) huzxxMap.get("HUIZHISHIJIAN");
			huzxx.setHuizhishijian(new Date(hzsj.getTime()));
			huzxx.setShixiangbianma(huzxxMap.get("SHIXIANGBIANMA").toString());
			Timestamp sdhzsj = (Timestamp) huzxxMap.get("SHOUDAOHUIZHISHIJIAN");
			huzxx.setShoudaohuizhishijian(new Date(sdhzsj.getTime()));
			huzxx.setShoulibianhao(huzxxMap.get("SHOULIBIANHAO").toString());
			huzxx.setShoulirenbianhao(huzxxMap.get("SHOULIRENBIANHAO")
					.toString());
			huzxx.setTongyibianhao(huzxxMap.get("TONGYIBIANHAO").toString());
			huzxx.setTransmissionID(huzxxMap.get("TRANSMISSIONID").toString());
			huzxx.setXitongbianma(huzxxMap.get("XITONGBIANMA").toString());
			huzxx.setYewuleixing(huzxxMap.get("YEWULEIXING").toString());
			huzxxs.add(huzxx);
		}
		return huzxxs;
	}

	/**
	 * 根据申请事项编码查询回执信息集合
	 * 
	 * @param shixiangbianma
	 * @return
	 */
	public List<Huzxx> queryHuzxxByShixiangbianma(String shixiangbianma) {
		String sql = "SELECT BEIZHUXINXI,CUOWUBIANHAO,CUOWULEIXING,CUOWUXINXIBIAOTI,CUOWUXINXIMIAOSHU,HUIZHILEIXING,HUIZHISHIJIAN"
				+ ",SHIXIANGBIANMA,SHOUDAOHUIZHISHIJIAN,SHOULIBIANHAO,SHOULIRENBIANHAO,TONGYIBIANHAO,TRANSMISSIONID,XITONGBIANMA,"
				+ "YEWULEIXING FROM HUIZHIXINXI WHERE SHIXIANGBIANMA='"
				+ shixiangbianma + "'";
		List<Map<String, Object>> huzxxMaps = DBUtil.getMapList(sql);
		List<Huzxx> huzxxs = new ArrayList<Huzxx>();
		for (Map<String, Object> huzxxMap : huzxxMaps) {
			Huzxx huzxx = new Huzxx();
			huzxx.setBeizhuxinxi(huzxxMap.get("BEIZHUXINXI").toString());
			huzxx.setCuowubianhao(huzxxMap.get("CUOWUBIANHAO").toString());
			huzxx.setCuowuleixing(huzxxMap.get("CUOWULEIXING").toString());
			huzxx.setCuowuxinxibiaoti(huzxxMap.get("CUOWUXINXIBIAOTI")
					.toString());
			huzxx.setCuowuxinximiaoshu(huzxxMap.get("CUOWUXINXIMIAOSHU")
					.toString());
			huzxx.setHuizhileixing(huzxxMap.get("HUIZHILEIXING").toString());
			Timestamp hzsj = (Timestamp) huzxxMap.get("HUIZHISHIJIAN");
			huzxx.setHuizhishijian(new Date(hzsj.getTime()));
			huzxx.setShixiangbianma(huzxxMap.get("SHIXIANGBIANMA").toString());
			Timestamp sdhzsj = (Timestamp) huzxxMap.get("SHOUDAOHUIZHISHIJIAN");
			huzxx.setShoudaohuizhishijian(new Date(sdhzsj.getTime()));
			huzxx.setShoulibianhao(huzxxMap.get("SHOULIBIANHAO").toString());
			huzxx.setShoulirenbianhao(huzxxMap.get("SHOULIRENBIANHAO")
					.toString());
			huzxx.setTongyibianhao(huzxxMap.get("TONGYIBIANHAO").toString());
			huzxx.setTransmissionID(huzxxMap.get("TRANSMISSIONID").toString());
			huzxx.setXitongbianma(huzxxMap.get("XITONGBIANMA").toString());
			huzxx.setYewuleixing(huzxxMap.get("YEWULEIXING").toString());
			huzxxs.add(huzxx);
		}
		return huzxxs;
	}
}

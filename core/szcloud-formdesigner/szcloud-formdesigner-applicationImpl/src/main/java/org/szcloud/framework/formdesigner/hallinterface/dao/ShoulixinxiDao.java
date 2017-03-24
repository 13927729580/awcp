package org.szcloud.framework.formdesigner.hallinterface.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.formdesigner.hallinterface.bean.Shlixx;
import org.szcloud.framework.formdesigner.hallinterface.util.BaseDao;
import org.szcloud.framework.formdesigner.hallinterface.util.DBUtil;

public class ShoulixinxiDao {

	/**
	 * 批量插入受理信息
	 * 
	 * @param users
	 * @throws SQLException
	 * @author
	 */
	public void batchSave(List<Shlixx> shlixxs) {
		Connection conn = null;
		try {
			conn = BaseDao.getconn();
			conn.setAutoCommit(false);
			String sql = "INSERT INTO SHOULIXINXI(XITONGBIANMA,SHIXIANGBIANMA,SHENQINGSHIXIANG,SHOULIBIANHAO,JIAOFEIJINE,SHOULIRIQI,SHENPISHIXIAN,"
					+ "SHOULIDANWEIDAIMA,SHOUJIEDANWEIDAIMA,SHOULIRENBIANHAO,SHOULIRENXINGMING,ZHENGJIANLEIBIE,SHENQINGDANWEIDAIMA,SHENQINGDANWEIMINGCHENG,"
					+ "LIANXIRENXINGMING,LIANXIDIANHUA,LIANXISHOUJIHAO,LIANXICHUANZHENHAO,EMAIL,LIANXIRENDIZHI,LIANXIRENYOUBIAN,SHOUJIEZILIAOXIANGSHU,"
					+ "SHOUJIEZILIAO,STATUS,SHANGBAOSHIJIAN,TRANSMISSIONID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			for (Shlixx shlixx : shlixxs) {
				try {
					preparedStatement.setString(1, shlixx.getXitongbianma());
					preparedStatement.setString(2, shlixx.getShixiangbianma());
					preparedStatement
							.setString(3, shlixx.getShenqingshixiang());
					preparedStatement.setString(4, shlixx.getShoulibianhao());
					preparedStatement.setDouble(5, shlixx.getJiaofeijine());
					Date shouliriqi = new Date(shlixx.getShouliriqi().getTime());
					preparedStatement.setDate(6, shouliriqi);
					preparedStatement.setString(7, shlixx.getShenpishixian());
					preparedStatement.setString(8,
							shlixx.getShoulidanweidaima());
					preparedStatement.setString(9,
							shlixx.getShoujiedanweidaima());
					preparedStatement.setString(10,
							shlixx.getShoulirenbianhao());
					preparedStatement.setString(11,
							shlixx.getShoulirenxingming());
					preparedStatement
							.setString(12, shlixx.getZhengjianleibie());
					preparedStatement.setString(13,
							shlixx.getShenqingdanweidaima());
					preparedStatement.setString(14,
							shlixx.getShenqingdanweimingcheng());
					preparedStatement.setString(15,
							shlixx.getLianxirenxingming());
					preparedStatement.setString(16, shlixx.getLianxidianhua());
					preparedStatement
							.setString(17, shlixx.getLianxishoujihao());
					preparedStatement.setString(18,
							shlixx.getLianxichuanzhenhao());
					preparedStatement.setString(19, shlixx.getEmail());
					preparedStatement.setString(20, shlixx.getLianxirendizhi());
					preparedStatement.setString(21,
							shlixx.getLianxirenyoubian());
					preparedStatement.setInt(22,
							shlixx.getShoujieziliaoxiangshu());
					preparedStatement.setString(23, shlixx.getShoujieziliao());
					preparedStatement.setString(24, shlixx.getStatus());
					Date shangbaoshiji = new Date(shlixx.getShangbaoshijian()
							.getTime());
					preparedStatement.setDate(25, shangbaoshiji);
					preparedStatement.setString(26, shlixx.getTransmissionID());
					preparedStatement.addBatch();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			preparedStatement.executeBatch();
			conn.commit();
		} catch (Exception e) {
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	 /**
	  * 查询受理信息
	  * @param shoulibianhaos
	  * @return
	  */
	public List<Map<String, Object>> getShlixxes(List<String> shoulibianhaos) {
		StringBuffer buffer = new StringBuffer();
		for (Iterator<String> it = shoulibianhaos.iterator(); it.hasNext();) {
			buffer.append(it.next());
			if(it.hasNext()){
				buffer.append("','");
			}
		}
		
		String sql = "SELECT XITONGBIANMA,SHIXIANGBIANMA,SHENQINGSHIXIANG,SHOULIBIANHAO,JIAOFEIJINE,SHOULIRIQI,SHENPISHIXIAN,"
				+ "SHOULIDANWEIDAIMA,SHOUJIEDANWEIDAIMA,SHOULIRENBIANHAO,SHOULIRENXINGMING,ZHENGJIANLEIBIE,SHENQINGDANWEIDAIMA,SHENQINGDANWEIMINGCHENG,"
				+ "LIANXIRENXINGMING,LIANXIDIANHUA,LIANXISHOUJIHAO,LIANXICHUANZHENHAO,EMAIL,LIANXIRENDIZHI,LIANXIRENYOUBIAN,SHOUJIEZILIAOXIANGSHU,"
				+ "SHOUJIEZILIAO,STATUS,SHANGBAOSHIJIAN,TRANSMISSIONID FROM SHOULIXINXI WHERE SHOULIBIANHAO in ('"
				+ buffer.toString() + "')";
		
		return DBUtil.getMapList(sql);
	}

}

package org.szcloud.framework.formdesigner.hallinterface.service;

import java.util.List;
import java.util.Map;

import org.szcloud.framework.formdesigner.hallinterface.bean.Huzxx;
import org.szcloud.framework.formdesigner.hallinterface.dao.HuizhixinxiDao;

public class HuizhixinxiService {
	
	private HuizhixinxiDao huizhixinxiDao=new HuizhixinxiDao();
	
	/**
	 * 根据审批系统原受理编号和业务类型返回大厅统一受理编号
	 * @param shoulibianhao
	 * @param yewuleixing
	 * @return
	 */
	public String getTongyibianhao(String shoulibianhao,String yewuleixing){
		return huizhixinxiDao.getTongyibianhao(shoulibianhao, yewuleixing);
	}
	
	/**
	 * 根据审批系统原受理编号和业务类型返回大厅统一受理编号
	 * @param shoulibianhao
	 * @param yewuleixing
	 * @return
	 */
	public List<Map<String, Object>> getTongyibianhaos(List<String> shoulibianhaos,String yewuleixing){
		return huizhixinxiDao.getTongyibianhaos(shoulibianhaos, yewuleixing);
	}
	
	/**
	 * 根据审批系统原受理编号和业务类型查询回执信息
	 * @return
	 */
	public Huzxx getHuzxx(String shoulibianhao,String yewuleixing){
		return huizhixinxiDao.getHuzxx(shoulibianhao, yewuleixing);
	}
	
	/**
	 * 根据审批系统原受理编号查询回执信息集合
	 * @param shoulibianhao
	 * @return
	 */
	public List<Huzxx> queryHuzxxByShoulibianhao(String shoulibianhao){
		return huizhixinxiDao.queryHuzxxByShoulibianhao(shoulibianhao);
	}
	
	/**
	 * 根据申请事项编码查询回执信息集合
	 * @param shixiangbianma
	 * @return
	 */
	public List<Huzxx> queryHuzxxByShixiangbianma(String shixiangbianma){
		return huizhixinxiDao.queryHuzxxByShixiangbianma(shixiangbianma);
	}

}

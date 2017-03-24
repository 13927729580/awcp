package org.szcloud.framework.formdesigner.hallinterface.service;


import java.util.List;
import java.util.Map;

import org.szcloud.framework.formdesigner.hallinterface.bean.Shlixx;
import org.szcloud.framework.formdesigner.hallinterface.dao.ShoulixinxiDao;

public class ShoulixinxiService {

	private ShoulixinxiDao shoulixinxiDao = new ShoulixinxiDao();

	/**
	 * 批量新增受理信息
	 * 
	 * @param shlixx
	 * @return
	 */
	public void batchSave(List<Shlixx> shlixxs) {
		shoulixinxiDao.batchSave(shlixxs);
	}

	/**
	 * 批量查询受理信息
	 * @param shoulibianhaos 受理编号列表 
	 * @return
	 */
	public List<Map<String, Object>> getShlixxes(List<String> shoulibianhaos){
		return shoulixinxiDao.getShlixxes(shoulibianhaos);
	}
}

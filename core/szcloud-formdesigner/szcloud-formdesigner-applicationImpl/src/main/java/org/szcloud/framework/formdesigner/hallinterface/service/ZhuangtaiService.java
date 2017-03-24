package org.szcloud.framework.formdesigner.hallinterface.service;

import org.szcloud.framework.formdesigner.hallinterface.bean.Zhuangtai;
import org.szcloud.framework.formdesigner.hallinterface.dao.ZhuangtaixinxiDao;

public class ZhuangtaiService {
	
	private ZhuangtaixinxiDao zhuangtaixinxiDao=new ZhuangtaixinxiDao();
	
	/**
	 * 新增状态信息
	 * @param zhuangtai
	 * @return
	 */
	public long saveZhuangtaixinxi(Zhuangtai zhuangtai){
		return zhuangtaixinxiDao.saveZhuangtaixinxi(zhuangtai);
	}

}

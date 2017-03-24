package org.szcloud.framework.unit.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.szcloud.framework.core.domain.QueryChannelService;
import org.szcloud.framework.core.utils.BeanUtils;
import org.szcloud.framework.unit.core.domain.PunUserOrg;
import org.szcloud.framework.unit.vo.PunUserOrgVO;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

@Service("punUserOrgServiceImpl")
@Transactional
public class PunUserOrgServiceImpl implements PunUserOrgService {

	@Resource(name="queryChannel")
	private QueryChannelService queryChannel;
	
	@Override
	public List<PunUserOrgVO> findAll() {
		List<PunUserOrg> list = PunUserOrg.findAll(PunUserOrg.class);
		List<PunUserOrgVO> ls = new ArrayList<PunUserOrgVO>();
		for (PunUserOrg mm : list) {
			ls.add(BeanUtils.getNewInstance(mm, PunUserOrgVO.class));
		}
		return ls;
	}

	@Override
	public void remove(PunUserOrgVO vo) {
		PunUserOrg mm = BeanUtils.getNewInstance(vo, PunUserOrg.class);
		PunUserOrg.getRepository().remove(mm);
	}

	@Override
	public void update(PunUserOrgVO vo) {
		PunUserOrg mm = BeanUtils.getNewInstance(vo, PunUserOrg.class);
		PunUserOrg.getRepository().save(mm);
	}

	@Override
	public void save(PunUserOrgVO vo) {
		PunUserOrg mm = BeanUtils.getNewInstance(vo, PunUserOrg.class);
		PunUserOrg.getRepository().save(mm);
	}

	@Override
	public PageList<PunUserOrgVO> selectPagedByExample(String queryStr, Map<String, Object> params, int currentPage,
			int pageSize, String sortString) {
		PageList<PunUserOrgVO> resultVO = new PageList<PunUserOrgVO>();
		PageList result = queryChannel.queryPagedResult(PunUserOrg.class, queryStr,params, currentPage, pageSize, sortString);
		for (Object dd : result) {
			resultVO.add(BeanUtils.getNewInstance(dd, PunUserOrgVO.class));			
		}
		result.clear();
		return resultVO;
	}

	@Override
	public PunUserOrgVO get(Long id) {
		try {
			PunUserOrg model =  PunUserOrg.get(PunUserOrg.class, id);
			return BeanUtils.getNewInstance(model, PunUserOrgVO.class);
		} catch (Exception e) {
			throw new RuntimeException("错误信息", e);
		}
	}


//	private Map<String, Object> getValueMap(Object obj) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		// 获取f对象对应类中的所有属性域
//		try {
//			for (Field fidld : obj.getClass().getDeclaredFields()) {
//				String varName = fidld.getName();
//				// 获取原来的访问控制权限
//				boolean accessFlag = fidld.isAccessible();
//				// 修改访问控制权限
//				fidld.setAccessible(true);
//				// 获取在对象f中属性fields[i]对应的对象中的变量
//				Object o = fidld.get(obj);
//				if (o != null)
//					map.put(varName, o);
//				// 恢复访问控制权限
//				fidld.setAccessible(accessFlag);
//			}
//		} catch (IllegalArgumentException ex) {
//			ex.printStackTrace();
//		} catch (IllegalAccessException ex) {
//			ex.printStackTrace();
//		}
//		return map;
//
//	}

}

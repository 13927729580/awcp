package org.szcloud.framework.unit.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.szcloud.framework.core.common.exception.MRTException;
import org.szcloud.framework.core.domain.BaseExample;
import org.szcloud.framework.core.domain.QueryChannelService;
import org.szcloud.framework.core.utils.BeanUtils;
import org.szcloud.framework.unit.core.domain.PunPosition;
import org.szcloud.framework.unit.core.domain.PunSystem;
import org.szcloud.framework.unit.core.domain.SysDataSource;
import org.szcloud.framework.unit.vo.PunSystemVO;
import org.szcloud.framework.unit.vo.SysDataSourceVO;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

@Transactional
@Service("punSystemServiceImpl")
public class PunSystemServiceImpl implements PunSystemService {

	@Autowired
	private QueryChannelService queryChannel;

	/**
	 * 
	 * @Title: addOrUpdate @Description: 新增或修改 @author ljw @param @param
	 * vo @param @throws MRTException @return void @throws
	 */
	public void addOrUpdate(PunSystemVO vo) throws MRTException {
		PunSystem sys = BeanUtils.getNewInstance(vo, PunSystem.class);
		if (null != vo.getSysId()) {
			sys.setId(vo.getSysId());
		}
		sys.save();
		vo.setSysId(sys.getSysId());
	}

	/**
	 * 
	 * @Title: findById @Description: 根据ID查找 @author ljw @param @param
	 * id @param @return @param @throws MRTException @return PunSystemVO @throws
	 */
	public PunSystemVO findById(Long id) throws MRTException {
		PunSystem system = PunSystem.get(PunSystem.class, id);
		return BeanUtils.getNewInstance(system, PunSystemVO.class);
	}

	/**
	 * 
	 * @Title: findAll @Description: 查询全部记录 @author
	 * ljw @param @return @param @throws MRTException @return
	 * List<PunSystemVO> @throws
	 */
	public List<PunSystemVO> findAll() throws MRTException {
		List<PunSystem> result = PunSystem.findAll();
		List<PunSystemVO> resultVo = new ArrayList<PunSystemVO>();
		for (PunSystem mm : result) {
			resultVo.add(BeanUtils.getNewInstance(mm, PunSystemVO.class));
		}
		result.clear();
		return resultVo;

	}

	/**
	 * 
	 * @Title: queryResult @Description: 分页查询 @author ljw @param @param
	 * queryStr @param @param params @param @param currentPage @param @param
	 * pageSize @param @param sortString @param @return @return
	 * PageList<T> @throws
	 */
	@SuppressWarnings("unchecked")
	public PageList<PunSystemVO> queryPagedResult(String queryStr, Map<String, Object> params, int currentPage,
			int pageSize, String sortString) {
		PageList<PunSystem> systems = queryChannel.queryPagedResult(PunSystem.class, queryStr, params, currentPage,
				pageSize, sortString);
		List<PunSystemVO> tmp = new ArrayList<PunSystemVO>();
		for (PunSystem system : systems) {
			tmp.add(BeanUtils.getNewInstance(system, PunSystemVO.class));
		}
		PageList<PunSystemVO> vos = new PageList<PunSystemVO>(tmp, systems.getPaginator());
		systems.clear();
		return vos;
	}

	/**
	 * 
	 * @Title: queryResult @Description: 查询 @author ljw @param @param
	 * queryStr @param @param params 查询条件 @param @return @return
	 * List<PunSystemVO> @throws
	 */
	public List<PunSystemVO> queryResult(String queryStr, Map<String, Object> params) {
		List<PunSystem> systems = queryChannel.queryResult(PunSystem.class, queryStr, params);
		List<PunSystemVO> vos = new ArrayList<PunSystemVO>();
		for (PunSystem system : systems) {
			vos.add(BeanUtils.getNewInstance(system, PunSystemVO.class));
		}
		systems.clear();
		return vos;
	}

	/**
	 * 
	 * @Title: delete @Description: 删除，根据ID @author ljw @param @param
	 * id @param @return @param @throws MRTException @return String @throws
	 */
	public String delete(Long id) throws MRTException {
		PunSystem sys = PunSystem.get(PunSystem.class, id);
		if (sys == null) {
			return null;
		}
		// String sysName = sys.getSysName();
		removeRelations(sys);
		sys.delete();
		return null;
	}

	/**
	 * 删除应用系统与其它表的关联关系； auth:huangqr
	 * 
	 * @param user
	 * @throws MRTException
	 */
	private void removeRelations(PunSystem system) throws MRTException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sysId", system.getSysId());
		queryChannel.excuteMethod(PunPosition.class, "removeGroupSys", params);
		queryChannel.excuteMethod(PunPosition.class, "removeRoleAccessByRole", params);
		queryChannel.excuteMethod(PunPosition.class, "removeRoleAccessByResource", params);
		queryChannel.excuteMethod(PunPosition.class, "removeRoleInfo", params);
		queryChannel.excuteMethod(PunPosition.class, "removeResource", params);
	}

	/*
	 * public List<PunSystemVO> selectByExample(BaseExample example) throws
	 * MRTException{ List<PunSystem> result =
	 * PunPosition.selectByExample(PunSystem.class, example); List<PunSystemVO>
	 * resultVo = new ArrayList<PunSystemVO>(); for(PunSystem mm : result) {
	 * resultVo.add(BeanUtils.getNewInstance(mm, PunSystemVO.class)); }
	 * result.clear(); return resultVo;
	 * 
	 * }
	 */
	/**
	 * @Description 根据example模糊查询数据
	 */
	@SuppressWarnings("unchecked")
	@Override
	public PageList<PunSystemVO> selectPagedByExample(BaseExample example, int currentPage, int pageSize,
			String sortString) {
		PageList<PunSystem> list = queryChannel.selectPagedByExample(PunSystem.class, example, currentPage, pageSize,
				sortString);
		PageList<PunSystemVO> vos = new PageList<PunSystemVO>(list.getPaginator());
		for (PunSystem dp : list) {
			vos.add(BeanUtils.getNewInstance(dp, PunSystemVO.class));
		}
		list.clear();
		return vos;
	}

	/**
	 * 系统级联删除
	 * 
	 * @param id
	 * @return
	 * @throws MRTException
	 */
	/*
	 * public String sysCascadeDelete(Long id) throws MRTException{ return null;
	 * }
	 */

	@Override
	public List<SysDataSourceVO> getSystemDataSource(Long systemId) {
		BaseExample example = new BaseExample();
		example.createCriteria().andEqualTo("SYSTEM_ID", systemId);
		List<SysDataSourceVO> vos = new ArrayList<SysDataSourceVO>();
		List<SysDataSource> systemDataSource = queryChannel.selectPagedByExample(SysDataSource.class, example, 1,
				Integer.MAX_VALUE, null);
		if (systemDataSource != null && systemDataSource.size() > 0) {
			for (SysDataSource data : systemDataSource) {
				vos.add(BeanUtils.getNewInstance(data, SysDataSourceVO.class));
			}
		}
		return vos;

	}

}
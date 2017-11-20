package cn.org.awcp.metadesigner.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.core.domain.QueryChannelService;
import cn.org.awcp.core.utils.BeanUtils;
import cn.org.awcp.metadesigner.application.SysSourceRelationService;
import cn.org.awcp.metadesigner.core.domain.SysDataSource;
import cn.org.awcp.metadesigner.vo.SysDataSourceVO;

@Service("sysSourceRelationServiceImpl")
public class SysSourceRelationServiceImpl implements SysSourceRelationService {
	@Autowired
	private QueryChannelService queryChannel;

	@Override
	public SysDataSourceVO get(String id) {
		SysDataSource system = SysDataSource.get(SysDataSource.class, id);
		return BeanUtils.getNewInstance(system, SysDataSourceVO.class);
	}

	@Override
	public List<SysDataSourceVO> findAll() {
		List<SysDataSource> list = SysDataSource.findAll(SysDataSource.class);
		List<SysDataSourceVO> ls = new ArrayList<SysDataSourceVO>();
		for (SysDataSource mm : list) {
			ls.add(BeanUtils.getNewInstance(mm, SysDataSourceVO.class));
		}
		return ls;
	}

	@Override
	public void remove(SysDataSourceVO vo) {
		try {
			SysDataSource mm = BeanUtils.getNewInstance(vo, SysDataSource.class);
			SysDataSource.getRepository().remove(mm);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public SysDataSourceVO saveOrUpdate(SysDataSourceVO vo) {
		SysDataSource sys = BeanUtils.getNewInstance(vo, SysDataSource.class);
		sys.save();
		vo.setId(sys.getId());
		return vo;
	}

	public PageList<SysDataSourceVO> selectPagedByExample(BaseExample baseExample, int currentPage, int pageSize,
			String sortString) {
		PageList<SysDataSource> result = queryChannel.selectPagedByExample(SysDataSource.class, baseExample,
				currentPage, pageSize, sortString);
		List<SysDataSourceVO> resultVO = new ArrayList<SysDataSourceVO>();
		for (Object dd : result) {
			resultVO.add(BeanUtils.getNewInstance(dd, SysDataSourceVO.class));
		}
		PageList<SysDataSourceVO> rv = new PageList<SysDataSourceVO>(resultVO, result.getPaginator());
		result.clear();
		return rv;
	}

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

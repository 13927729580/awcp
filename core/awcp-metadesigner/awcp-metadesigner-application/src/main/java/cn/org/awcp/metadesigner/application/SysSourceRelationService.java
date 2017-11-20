package cn.org.awcp.metadesigner.application;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.metadesigner.vo.SysDataSourceVO;

public interface SysSourceRelationService {

	SysDataSourceVO get(String id);

	List<SysDataSourceVO> findAll();

	void remove(SysDataSourceVO vo);

	SysDataSourceVO saveOrUpdate(SysDataSourceVO vo);

	PageList<SysDataSourceVO> selectPagedByExample(BaseExample baseExample, int currentPage, int pageSize,
			String sortString);

	List<SysDataSourceVO> getSystemDataSource(Long systemId);
}

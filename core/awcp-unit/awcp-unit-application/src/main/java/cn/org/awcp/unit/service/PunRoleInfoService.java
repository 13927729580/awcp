package cn.org.awcp.unit.service;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.unit.vo.PunRoleInfoVO;

public interface PunRoleInfoService {

	public void addOrUpdateRole(PunRoleInfoVO vo);

	public PunRoleInfoVO findById(Long id);

	public List<PunRoleInfoVO> findAll();

	public String delete(Long id);

	public List<PunRoleInfoVO> queryResult(String queryStr, Map<String, Object> params);

	public PageList<PunRoleInfoVO> queryPagedResult(String queryStr, Map<String, Object> params, int currentPage,
			int pageSize, String sortString);

	public List<PunRoleInfoVO> selectByExample(BaseExample example);

	public PageList<PunRoleInfoVO> selectPagedByExample(BaseExample example, int currentPage, int pageSize,
			String sortString);

	/**
	 * 根据用户ID获取角色
	 * 
	 * @param userId
	 *            用户Id
	 * @return
	 */
	public List<String> queryByUser(Long userId);
}

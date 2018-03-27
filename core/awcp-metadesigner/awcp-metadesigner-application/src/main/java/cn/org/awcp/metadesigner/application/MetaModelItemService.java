package cn.org.awcp.metadesigner.application;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageList;

import cn.org.awcp.core.domain.BaseExample;
import cn.org.awcp.metadesigner.vo.MetaModelItemsVO;

public interface MetaModelItemService {

	/**
	 * 增加
	 *
	 * @param vo
	 */
	 String save(MetaModelItemsVO vo);

	/**
	 * 删除
	 *
	 * @param vo
	 */
	 boolean remove(MetaModelItemsVO vo);

	/**
	 * 查询一条数据
	 *
	 * @param id
	 * @return
	 */
	 MetaModelItemsVO get(String id);

	/**
	 * 分页查询
	 *
	 * @param queryStr
	 * @param params
	 * @param currentPage
	 * @param pageSize
	 * @param sortString
	 * @return
	 */
	 PageList<MetaModelItemsVO> queryResult(String queryStr, Map<String, Object> params, int currentPage,
			int pageSize, String sortString);

	/**
	 * 根据模型ID查询元数据模型属性
	 *
	 * @param queryStr
	 * @param modelId
	 * @return
	 */
	 List<MetaModelItemsVO> queryResult(String queryStr, String modelId);

	/**
	 * 修改
	 *
	 * @param queryStr
	 * @param vo
	 */
	 boolean update(String queryStr, MetaModelItemsVO vo);

	/**
	 * 判断列是否已经在数据存在
	 *
	 * @param queryStr
	 * @param tableName
	 * @param itemName
	 * @return
	 */
	 boolean columnIsExist(String queryStr, String tableName, String itemName);

	/**
	 * 根据属性的状态查找
	 *
	 * @param queryStr
	 * @param modelId
	 * @return
	 */
	 List<MetaModelItemsVO> queryByState(String queryStr, String modelId);

	/**
	 * 根据模型书名称查找
	 *
	 * @param queryStr
	 * @param tableName
	 * @return
	 */
	 List<MetaModelItemsVO> queryTableName(String queryStr, String tableName);

	/**
	 * 删除外键
	 *
	 * @param modelId
	 */
	 boolean removeByFk(String modelId);

	/**
	 * @Title: selectPagedByExample @Description: 复杂条件的查询 @param baseExample
	 *         条件 @param currentPage 当前页 @param pageSize 每页数 @param sortString
	 *         排序字段 @return PageList<MetaModelVO> @throws
	 */
	 PageList<MetaModelItemsVO> selectPagedByExample(BaseExample baseExample, int currentPage, int pageSize,
			String sortString);

	/**
	 * 根据modelId和ItemCode查找
	 *
	 * @param queryStr
	 * @param modelId
	 * @param itemCode
	 * @return
	 */
	 List<MetaModelItemsVO> queryColumn(String queryStr, String modelId, String itemCode);
}

package org.szcloud.framework.core.domain;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;


@Service(value="queryChannel")
public class QueryChannelServiceMybatis implements QueryChannelService {

	private static final long serialVersionUID = -7717195132438268067L;
	
    @Autowired
	private SqlSessionFactory sqlSessionFactory;

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@SuppressWarnings("rawtypes")
	public long queryResultSize(Class clazz,String queryStr, Map<String, Object> params) {
		SqlSession session = sqlSessionFactory.openSession();
		long size = 0;
		try {
			size = session.selectList(clazz.getName()+"."+queryStr, params).size();
		} finally {
		    session.close();
		}
		return size;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T querySingleResult(Class clazz,String queryStr, Map<String, Object> params) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			T t = (T)session.selectOne(clazz.getName()+"."+queryStr, params);
			return t;
		} finally {
		  session.close();
		}
	}
	

	public <T> List<T> queryResult(Class<T> clazz,String queryStr, Map<String, Object> params)
	{
		//PageList pageList = null;
		SqlSession session = sqlSessionFactory.openSession();

		try
		{
			return session.selectList(clazz.getName()+"."+queryStr, params);

		}finally
		{
			session.close();
		}
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public  PageList selectPagedByExample(Class clazz,
			BaseExample example,int currentPage, int pageSize,String sortString) {
		SqlSession session = sqlSessionFactory.openSession();
		example.orderByClause = sortString;
		example.start = (currentPage-1) * pageSize;
		example.limit = pageSize;
		try {
			Integer count = session.selectOne(clazz.getName() + ".queryCountByExample",
					example);
			List list = session.selectList(clazz.getName() + ".selectByExample",
					example);
			Paginator paginator = new Paginator(currentPage,pageSize,count);
			PageList page = new PageList(list,paginator);
			return page;
		} finally {
			session.close();
		}

	}	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public  PageList selectPagedByExample(String countId, String selectId, Class clazz,
			BaseExample example,int currentPage, int pageSize,String sortString) {
		SqlSession session = sqlSessionFactory.openSession();
		example.orderByClause = sortString;
		example.start = (currentPage-1) * pageSize;
		example.limit = pageSize;
		try {
			Integer count = session.selectOne(clazz.getName() + "." + countId,
					example);
			List list = session.selectList(clazz.getName() + "." + selectId,
					example);
			Paginator paginator = new Paginator(currentPage,pageSize,count);
			PageList page = new PageList(list,paginator);
			return page;
		} finally {
			session.close();
		}

	}	
	
	@SuppressWarnings("rawtypes")
	public PageList selectMapByPage(Class clazz,String queryStr,Map<String,Object> params,int currentPage,int pageSize,String sortString){
		SqlSession session=sqlSessionFactory.openSession();
		PageList pl=null;
		try {
			PageBounds pageBounds = new PageBounds(currentPage, pageSize , Order.formString(sortString));
			pl=(PageList) session.selectList(clazz.getName()+"."+queryStr, params, pageBounds);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			session.close();
		}
		return pl;
	}
	

	@SuppressWarnings("rawtypes")
	public PageList queryPagedResult(Class clazz,String queryStr, Map<String, Object> params, int currentPage, int pageSize,String sortString)
	{
		SqlSession session = sqlSessionFactory.openSession();
		//String sortString = "age.asc,gender.desc";//如果你想排序的话逗号分隔可以排序多列
		PageBounds pageBounds = new PageBounds(currentPage, pageSize , Order.formString(sortString));  
		try
		{
			return (PageList)session.selectList(clazz.getName()+"."+queryStr, params, pageBounds);

		}finally
		{
			session.close();
		}
	}
	@SuppressWarnings("rawtypes")
	public PageList eqQueryPagedResult(Class clazz,Object entity, int currentPage, int pageSize,String sortString)
	{
		SqlSession session = sqlSessionFactory.openSession();
		PageBounds pageBounds = new PageBounds(currentPage, pageSize , Order.formString(sortString));  
		try
		{
			return (PageList)session.selectList(clazz.getName()+".eqQueryList", entity,pageBounds );

		}finally
		{
			session.close();
		}
	}
	
	public boolean excuteMethod(Class clazz,String method,Map<String, Object> params)
	{
		SqlSession session = sqlSessionFactory.openSession();
		try {
			session.update(clazz.getName()+"."+method,params); 
			session.commit(); 
			session.close(); 
			return true;
		}catch(Exception e){ 
			return false;
		}finally {
		  session.close();
		}
	}

	public List<Map<String, Object>> selectMap(Class clazz, String queryStr,
			Map<String, Object> params) {
		SqlSession session=sqlSessionFactory.openSession();
		List<Map<String,Object>> m=null;
		try {
			m=session.selectList(clazz.getName()+"."+queryStr, params);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			session.close();
		}
		return m;
	}
	
	@SuppressWarnings("rawtypes")
	public List queryPagedResult(Class clazz,String queryStr, Map<String, Object> params)
	{
		SqlSession session = sqlSessionFactory.openSession();
		PageBounds pageBounds = new PageBounds(1, Integer.MAX_VALUE , Order.formString(""));  
		try
		{
			return session.selectList(clazz.getName()+"."+queryStr, params, pageBounds);

		}finally
		{
			session.close();
		}
	}

}

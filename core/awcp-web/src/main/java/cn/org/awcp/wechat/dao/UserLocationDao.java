package cn.org.awcp.wechat.dao;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserLocationDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void save(String Longitude,String Latitude,String from){
		String sql = "select count(*) from  wechat_user_location where openId=?";
		int count = jdbcTemplate.queryForObject(sql, Integer.class,from);
		if(count==0){
			sql = "insert into wechat_user_location(ID,Longitude,Latitude,openId) values(?,?,?,?)";
			jdbcTemplate.update(sql, UUID.randomUUID().toString(),Longitude,Latitude,from);
		}
		else{
			sql = "update wechat_user_location set Longitude=?,Latitude=? where openId=?";
			jdbcTemplate.update(sql, Longitude,Latitude,from);
		}
	}
}

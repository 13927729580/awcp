package org.szcloud.framework.formdesigner.hallinterface.util;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * 事项信息工具类
 * @author ljw
 * 2015-2-5
 */
@Transactional
public class MatterInfoUtil {
	
	@Resource
	private JdbcTemplate template;
	
	
}

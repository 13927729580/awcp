package cn.org.awcp.venson.tile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.core.utils.Springfactory;
import cn.org.awcp.formdesigner.utils.DocumentUtils;
import cn.org.awcp.venson.exception.PlatformException;
import cn.org.awcp.venson.util.ExcelUtil;

public class TileImportExcel {
	/**
	 * 日志对象
	 */
	private static final Log logger = LogFactory.getLog(TileImportExcel.class);
	private static SzcloudJdbcTemplate jdbcTemplate = Springfactory.getBean("jdbcTemplate");

	private static final String TYPE = "类别";
	private static final String NAME = "品名";
	private static final String MODEL = "型 号";
	private static final String SPEC = "规 格";
	private static final String SELL = "经销价(单位:元/片)";
	private static final String PRICE = "销售价格";
	private static final String WEIGHT = "重量（单位：千克/片）";
	private static final String COMPANY = "整装公司";

	/**
	 * 导入产品，公司，关联产品
	 * 
	 * @param input
	 *            数据
	 */
	public static boolean importData(InputStream input) {
		List<Map<String, String>> data = ExcelUtil.getDataFromExcel(input, "xlsx");
		if (data == null || data.isEmpty()) {
			return false;
		}
		try {
			jdbcTemplate.beginTranstaion();
			importProductFormExcel(data);
			jdbcTemplate.commit();
			return true;
		} catch (Exception e) {
			logger.debug("ERROR", e);
			jdbcTemplate.rollback();
			return false;
		}
	}

	/**
	 * 
	 * @param tableName
	 *            导入的表名
	 * @param map
	 *            导入的字段（要和excel表头名称一一对应）
	 * @param input
	 *            文件流
	 * @return
	 */
	public static boolean commonImport(String tableName, Map<String, String> map, InputStream input) {
		List<Map<String, String>> data = ExcelUtil.getDataFromExcel(input, "xlsx");
		if (data == null || data.isEmpty()) {
			return false;
		}
		int size = data.size();
		String[] sqls = new String[size];
		StringBuilder builder = new StringBuilder();
		StringBuilder values = new StringBuilder();
		for (int i = 0; i < size; i++) {
			builder.append(" INSERT INTO " + tableName + "(ID,");
			values.append(" VALUES ('" + UUID.randomUUID().toString() + "',");
			Map<String, String> d = data.get(i);
			Set<String> keys = map.keySet();
			for (String key : keys) {
				String colName = map.get(key);
				String value = d.get(key);
				builder.append(colName + ",");
				values.append("'" + value + "',");
			}
			builder.delete(builder.length() - 1, builder.length());
			builder.append(")");
			values.delete(values.length() - 1, values.length());
			values.append(")");
			builder.append(values);
			sqls[i] = builder.toString();
			builder.delete(0, builder.length());
			values.delete(0, values.length());
		}
		try {
			jdbcTemplate.beginTranstaion();
			jdbcTemplate.batchUpdate(sqls);
			jdbcTemplate.commit();
			return true;
		} catch (Exception e) {
			logger.debug("ERROR", e);
			jdbcTemplate.rollback();
			return false;
		}

	}

	@Transactional(rollbackFor = { Exception.class })
	public static void importProductFormExcel(List<Map<String, String>> data) {
		List<Map<String, String>> half = data.stream().filter(map -> "半包".equals(map.get(TYPE)))
				.collect(Collectors.toList());
		importHalf(half);
		List<Map<String, String>> all = data.stream().filter(map -> "整装".equals(map.get(TYPE)))
				.collect(Collectors.toList());
		importAll(all);
	}

	/**
	 * 导入整装
	 * 
	 * @param alls
	 *            数据
	 */
	public static void importAll(List<Map<String, String>> alls) {
		List<String> sqls = new ArrayList<>();
		for (Map<String, String> map : alls) {
			String companyId = getCompanyIdByName(map.get(COMPANY));
			String productId = getIdByNo(map.get(MODEL));
			if (productId != null) {
				// 处理非法价格
				float price = handlePrice(map.get(PRICE));
				String id = getIdByProductAndCompany(companyId, productId);
				// 判断是否已经存在数据
				if (id == null) {
					id = UUID.randomUUID().toString();
					sqls.add("insert into crmtile_decoration_prod_price(id,productId,companyId,price) values('" + id
							+ "','" + productId + "','" + companyId + "','" + price + "')");
				}
			}

		}
		logger.debug(sqls);
		if (!sqls.isEmpty())
			jdbcTemplate.batchUpdate(sqls.toArray(new String[] {}));
	}

	/***
	 * 导入半包（产品）
	 * 
	 * @param half
	 */
	public static void importHalf(List<Map<String, String>> half) {
		List<String> nos = new ArrayList<>();
		List<String> sqls = new ArrayList<>();
		for (Map<String, String> map : half) {
			String model = map.get(MODEL);
			if (model.length() < 3) {
				continue;
			}
			// 判断是否已经导入过该产品
			if (nos.contains(model)) {
				continue;
			}
			// 处理非法价格
			float price = handlePrice(map.get(PRICE));
			float sell = handlePrice(map.get(SELL));
			float weight = handlePrice(map.get(WEIGHT));
			String id = getIdByNo(model);
			String sql;
			// 如果已经存在则更新
			if (id != null) {
				sql = "update crmtile_products_info set product_name='" + map.get(NAME) + "',product_NO='" + model
						+ "',product_color_size='无'," + "product_specification='" + map.get(SPEC)
						+ "',product_cost_price='" + sell + "',product_unit_price='" + price + "',product_weight='"
						+ weight + "',state='1',creator='718945',editor='718945' where id='" + id + "'";
			} else {
				id = UUID.randomUUID().toString();
				sql = "insert into crmtile_products_info(id,product_name,product_NO,product_color_size,"
						+ "product_specification,product_cost_price,product_unit_price,product_weight,state,creator,editor) values('"
						+ id + "','" + map.get(NAME) + "','" + model + "','无','" + map.get(SPEC) + "','" + sell + "','"
						+ price + "','" + weight + "','1','718945','718945')";
			}
			sqls.add(sql);
			nos.add(model);
		}
		logger.debug(sqls);
		if (!sqls.isEmpty())
			jdbcTemplate.batchUpdate(sqls.toArray(new String[] {}));
	}

	/**
	 * 根据产品编号获取产品ID
	 * 
	 * @param no
	 * @return
	 */
	private static String getIdByNo(String no) {
		try {
			return jdbcTemplate.queryForObject("select ID from crmtile_products_info where product_NO=?", String.class,
					no);
		} catch (DataAccessException e) {
			return null;
		}
	}

	private static String getIdByProductAndCompany(String companyId, String productId) {
		try {
			return jdbcTemplate.queryForObject(
					"select ID from crmtile_decoration_prod_price where productId=? and companyId=?", String.class,
					productId, companyId);
		} catch (DataAccessException e) {
			return null;
		}

	}

	/**
	 * 根据公司名称获取公司ID
	 * 
	 * @param name
	 *            名称
	 * @return
	 */
	private static String getCompanyIdByName(String name) {
		try {
			return jdbcTemplate.queryForObject("select ID from crmtile_decoration_company where name=?", String.class,
					name);
		} catch (DataAccessException e) {
			String id = UUID.randomUUID().toString();
			jdbcTemplate.update("insert into crmtile_decoration_company(ID,name) values (?,?)", id, name);
			return id;
		}
	}

	/**
	 * 处理非法价格
	 * 
	 * @param strpPice
	 * @return
	 */
	private static float handlePrice(String strpPice) {
		float price = 0.0f;
		if (StringUtils.isNotBlank(strpPice)) {
			try {
				price = Float.parseFloat(strpPice);
			} catch (Exception e) {
			}
		}
		return price;
	}

	public static void importCustomerAndOrder(String fileId) {
		SzcloudJdbcTemplate jdbc = Springfactory.getBean("jdbcTemplate");
		try{
			jdbc.beginTranstaion();
			Map<String, Long> userMap = new HashMap<String, Long>();
			String sql = "select ifnull(name,'') as name,USER_ID from p_un_user_base_info";
			List<Map<String, Object>> users = jdbc.queryForList(sql);
			for (Map<String, Object> map : users) {
				String name = (String) map.get("name");
				Long userId = (Long) map.get("USER_ID");
				if (StringUtils.isNotBlank(name)) {
					userMap.put(name, userId);
				}
			}
			List<Map<String, String>> data = new ArrayList<Map<String,String>>();
			InputStream is = DocumentUtils.getIntance().getInputStream(fileId);
			if (is == null) {
				throw new PlatformException("没有找到该文件的输入流");
			}
			data.addAll(ExcelUtil.getDataFromExcel(is, "xlsx",  1, 0, new int[]{0,1,2}));
			String hasCustomerSql = "select count(*) from crmtile_customer where address=?";
			String insertCustomerSql = "insert into crmtile_customer(ID,name,address,type,mobile,salesman,shoppingGuide)"
					+ "values (?,?,?,?,?,?,?)";
			String insertOrderSql = "insert into crmtile_order(ID,orderDate,customerId,customerName,customerAddress,customerMobile,"
					+ "customerType,totalPrice,salesman,shoppingGuide,designer,sender,state,businessState)"
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,'1','6')";
			String insertReceiptSql = "insert into crmtile_receipt(ID,orderId,customerId,customerName,customerAddress,customerMobile,"
					+ "money,state) values(?,?,?,?,?,?,?,'1')";
			List<String> list1 = new ArrayList<String>();
			List<String> list2 = new ArrayList<String>();
			for (int i = 0; i < data.size(); i++) {
				Map<String, String> temp = data.get(i);
				String name = temp.get("客户姓名");
				String address = temp.get("客户地址");
				String mobile = temp.get("客户手机");
				String type = temp.get("客户类型");
				String salesman = temp.get("业务员");
				String shoppingGuide = temp.get("导购员");
				String designer = temp.get("设计师");
				String sender = temp.get("送货员");
				String totalPrice = temp.get("应收金额");
				Date orderDate = new Date();
				if(StringUtils.isNotBlank(temp.get("开单时间"))){
					orderDate = DateUtil.getJavaDate(Double.parseDouble(temp.get("开单时间")), false);
				}
				String receivedMoney = temp.get("已收金额");
				String customerId = UUID.randomUUID().toString();
				String orderId = UUID.randomUUID().toString();
				if (StringUtils.isBlank(address)) {
					list1.add(address);
					continue;					
				}
				if(StringUtils.isBlank(totalPrice)){
					totalPrice = "0";
				}
				int count = jdbc.queryForObject(hasCustomerSql, Integer.class, address);
				if (count == 1) {
					list2.add(address);
					continue;
				}
				
				if ("整装".equals(type)) {
					type = "1";
				} else if ("半包".equals(type)) {
					type = "2";
				} else {
					type = "";
				}
				jdbc.update(insertCustomerSql, customerId, name, address, type, mobile, userMap.get(salesman),
						userMap.get(shoppingGuide));
				jdbc.update(insertOrderSql, orderId, orderDate,customerId, name, address, mobile, type, totalPrice,
						userMap.get(salesman), userMap.get(shoppingGuide), userMap.get(designer), userMap.get(sender));
				if (StringUtils.isNotBlank(receivedMoney)) {
					jdbc.update(insertReceiptSql, UUID.randomUUID().toString(), 
							orderId, customerId, name, address, mobile,receivedMoney);
				}
			}
			System.out.println(list1);
			System.out.println("********************************************");
			System.out.println(list2);

			jdbc.commit();
		}catch(Exception e){
			jdbc.rollback();
			e.printStackTrace();		
		}	
	}


}

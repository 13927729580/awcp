package cn.org.awcp.venson.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import cn.org.awcp.unit.vo.PunMenuVO;
import cn.org.awcp.venson.controller.base.ControllerHelper;

public class Menu {

	private long id;
	private String name;
	private String url;
	private String target;
	private String icon;
	private List<Menu> children;
	private int flag;
	private int type;
	private int count;

	public Menu(long id, String name, String url, String icon) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.icon = icon;
		this.children = new ArrayList<Menu>();
	}

	public Menu(String name, String url, String icon) {
		this.name = name;
		this.url = url;
		this.icon = icon;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Menu() {
		this.children = new ArrayList<Menu>();
	}

	public void add(Menu menu) {
		this.children.add(menu);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<Menu> getChildren() {
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 替换url和图标
	 */
	public static void replaceIconAndUrl(List<Map<String, Object>> list) {
		for (Map<String, Object> map : list) {
			String DID = (String) map.get("DYNAMICPAGE_ID");
			String oriUrl = (String) map.get("url");
			map.put("url", getUrl(DID, oriUrl));
			String oriIcon = (String) map.get("icon");
			map.put("icon", getIcon(oriIcon, "apps/images/icon2.jpg"));
		}
	}

	/**
	 * 替换url
	 */
	public static String getUrl(String DID, String oriUrl) {
		String url;
		String base = ControllerHelper.getBasePath();
		if (DID != null) {
			url = base + "document/view.do?dynamicPageId=" + DID;
		} else {
			if (StringUtils.isBlank(oriUrl) || oriUrl.trim().equals("#")) {
				url = "#";
			} else {
				if (oriUrl.startsWith("http")) {
					url = oriUrl;
				} else {
					url = base + oriUrl;

				}
			}

		}
		// 如果都为空则显示#

		return url;
	}

	/**
	 * 添加子节点
	 */
	public void addChildren(List<PunMenuVO> resoVOs, Set<Long> pids, Menu menu) {
		for (PunMenuVO vo : resoVOs) {
			// 判断是否为子节点
			if (vo.getType() == 0 && vo.getParentMenuId() == menu.getId()) {
				// 查看图标是否为空，如果为空则显示默认图标
				Menu children = new Menu(vo.getMenuId(), vo.getMenuName(),
						getUrl(vo.getDynamicPageId(), vo.getMenuAddress()),
						getIcon(vo.getMenuIcon(), "images/icon/icon-lucency/system.png"));
				children.setFlag(vo.getMenuFlag());
				children.setType(vo.getType());
				menu.add(children);
				// 判断子节点是否为父节点
				if (pids.contains(vo.getMenuId())) {
					addChildren(resoVOs, pids, children);
				}
			}
		}
	}

	/**
	 * 替换图标
	 */
	public static String getIcon(String icon, String defaultImg) {
		if (StringUtils.isBlank(icon)) {
			icon = defaultImg;
		}
		return ControllerHelper.getBasePath() + icon;
	}

}

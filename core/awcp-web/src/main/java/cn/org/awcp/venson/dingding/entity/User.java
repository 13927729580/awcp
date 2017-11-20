package cn.org.awcp.venson.dingding.entity;

import java.util.Arrays;

import BP.Tools.PinYinF4jUtils;

public class User {

	private String name;
	private String img;
	private String mobile;
	private String userid;
	private String userName;
	private Integer[] dept;

	public User() {
	}

	public User(String name, String img, String mobile, String userid, Integer[] dept) {
		this.name = name;
		this.img = img;
		this.mobile = mobile;
		this.userid = userid;
		this.dept = dept;
		userName = PinYinF4jUtils.getPinYin(this.name);
	}

	public User(String name, String img, String mobile, String userid) {
		this.name = name;
		this.img = img;
		this.mobile = mobile;
		this.userid = userid;
		userName = PinYinF4jUtils.getPinYin(this.name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserName() {
		if (userName == null) {
			userName = PinYinF4jUtils.getPinYin(this.name);
		}
		return userName;
	}

	public Integer[] getDept() {
		return dept;
	}

	public void setDept(Integer[] dept) {
		this.dept = dept;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userid == null) ? 0 : userid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (userid == null) {
			if (other.userid != null)
				return false;
		} else if (!userid.equals(other.userid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [name=" + name + ", img=" + img + ", mobile=" + mobile + ", userid=" + userid + ", userName="
				+ userName + ", dept=" + Arrays.toString(dept) + "]";
	}

}

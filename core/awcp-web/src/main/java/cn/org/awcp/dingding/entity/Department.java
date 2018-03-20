package cn.org.awcp.dingding.entity;

public class Department {
	private String name;
	private Long id;
	private Long pid;

	public Department() {
	}

	public Department(String name, Long id, Long pid) {
		this.name = name;
		this.id = id;
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	@Override
	public String toString() {
		return "Department [name=" + name + ", id=" + id + ", pid=" + pid + "]";
	}

}

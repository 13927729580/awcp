package org.szcloud.framework.formdesigner.hallinterface.bean;


import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 回执表Entity
 * @author ysb
 *
 */
public class Huzxx {
	
	/**
	 * 业务类型
	 */
	private String yewuleixing;
	/**
	 * 大厅统一受理编号
	 */
	private String tongyibianhao;
	/**
	 * 申请事项编码
	 */
	private String shixiangbianma;
	/**
	 * 审批系统原受理编号 
	 */
	private String shoulibianhao;
	/**
	 * 大厅窗口编号
	 */
	private String datingchuangkoubianhao;
	/**
	 * 受理人编号
	 */
	private String shoulirenbianhao;
	/**
	 * 应用系统编码
	 */
	private String xitongbianma;
	/**
	 * 回执时间
	 */
	private Date huizhishijian;
	/**
	 * 回执类型
	 */
	private String huizhileixing;
	/**
	 * 错误类型
	 */
	private String cuowuleixing;
	/**
	 * 错误编号
	 */
    private String cuowubianhao;
    /**
     * 错误信息标题
     */
    private String cuowuxinxibiaoti;
    /**
     * 错误信息描述
     */
    private String cuowuxinximiaoshu;
    /**
     * 备注信息
     */
    private String beizhuxinxi;
    /**
     * 收到回执时间
     */
    private Date shoudaohuizhishijian;
    /**
     * 传输ID
     */
    private String transmissionID;

	public String getYewuleixing() {
		return yewuleixing;
	}
	public void setYewuleixing(String yewuleixing) {
		this.yewuleixing = yewuleixing;
	}
	public String getTongyibianhao() {
		return tongyibianhao;
	}
	public void setTongyibianhao(String tongyibianhao) {
		this.tongyibianhao = tongyibianhao;
	}
	public String getShixiangbianma() {
		return shixiangbianma;
	}
	public void setShixiangbianma(String shixiangbianma) {
		this.shixiangbianma = shixiangbianma;
	}
	public String getShoulibianhao() {
		return shoulibianhao;
	}
	public void setShoulibianhao(String shoulibianhao) {
		this.shoulibianhao = shoulibianhao;
	}
	public String getShoulirenbianhao() {
		return shoulirenbianhao;
	}
	public void setShoulirenbianhao(String shoulirenbianhao) {
		this.shoulirenbianhao = shoulirenbianhao;
	}
	public String getXitongbianma() {
		return xitongbianma;
	}
	public void setXitongbianma(String xitongbianma) {
		this.xitongbianma = xitongbianma;
	}
	public Date getHuizhishijian() {
		return huizhishijian;
	}
	public void setHuizhishijian(Date huizhishijian) {
		this.huizhishijian = huizhishijian;
	}
	
	public String getDatingchuangkoubianhao() {
		return datingchuangkoubianhao;
	}
	public void setDatingchuangkoubianhao(String datingchuangkoubianhao) {
		this.datingchuangkoubianhao = datingchuangkoubianhao;
	}
	public String getHuizhileixing() {
		return huizhileixing;
	}
	public void setHuizhileixing(String huizhileixing) {
		this.huizhileixing = huizhileixing;
	}
	public String getCuowuleixing() {
		return cuowuleixing;
	}
	public void setCuowuleixing(String cuowuleixing) {
		this.cuowuleixing = cuowuleixing;
	}
	public String getCuowubianhao() {
		return cuowubianhao;
	}
	public void setCuowubianhao(String cuowubianhao) {
		this.cuowubianhao = cuowubianhao;
	}
	public String getCuowuxinxibiaoti() {
		return cuowuxinxibiaoti;
	}
	public void setCuowuxinxibiaoti(String cuowuxinxibiaoti) {
		this.cuowuxinxibiaoti = cuowuxinxibiaoti;
	}
	public String getCuowuxinximiaoshu() {
		return cuowuxinximiaoshu;
	}
	public void setCuowuxinximiaoshu(String cuowuxinximiaoshu) {
		this.cuowuxinximiaoshu = cuowuxinximiaoshu;
	}
	public String getBeizhuxinxi() {
		return beizhuxinxi;
	}
	public void setBeizhuxinxi(String beizhuxinxi) {
		this.beizhuxinxi = beizhuxinxi;
	}
	public Date getShoudaohuizhishijian() {
		return shoudaohuizhishijian;
	}
	public void setShoudaohuizhishijian(Date shoudaohuizhishijian) {
		this.shoudaohuizhishijian = shoudaohuizhishijian;
	}
	public String getTransmissionID() {
		return transmissionID;
	}
	public void setTransmissionID(String transmissionID) {
		this.transmissionID = transmissionID;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}

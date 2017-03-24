package org.szcloud.framework.formdesigner.hallinterface.bean;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 受理信息 Entity
 * @author ysb
 *
 */
public class Zhuangtai {
	
	/**
	 * 大厅统一受理编号
	 */
    private String tongyibianhao;
    /**
     * 当前位置（细化内部机构名称）
     */
    private String dangqianweizhi;
    /**
     * 送达时间
     */
    private Date songdashijian;
    /**
     * 状态类型
     */
    private String zhuangtaileixing;
    /**
     * 当前办理状态描述信息
     */
    private String zhuangtaimiaoshu;
    /**
     * 办结时间
     */
    private Date banjieriqi;
    /**
     * 办理结果文件数目
     */
    private int banjiewenjianshu;
    /**
     * 办理结果文件描述
     */
    private String banjiewenjian;
    /**
     * 上报状态
     */
    private String status;
    /**
     * 上报时间
     */
    private Date shangbaoshijian;
    /**
     * 传输ID
     */
    private String transmissionID;
    
    
	public String getTongyibianhao() {
		return tongyibianhao;
	}


	public void setTongyibianhao(String tongyibianhao) {
		this.tongyibianhao = tongyibianhao;
	}


	public String getDangqianweizhi() {
		return dangqianweizhi;
	}


	public void setDangqianweizhi(String dangqianweizhi) {
		this.dangqianweizhi = dangqianweizhi;
	}


	public Date getSongdashijian() {
		return songdashijian;
	}


	public void setSongdashijian(Date songdashijian) {
		this.songdashijian = songdashijian;
	}


	public String getZhuangtaileixing() {
		return zhuangtaileixing;
	}


	public void setZhuangtaileixing(String zhuangtaileixing) {
		this.zhuangtaileixing = zhuangtaileixing;
	}


	public Date getBanjieriqi() {
		return banjieriqi;
	}


	public void setBanjieriqi(Date banjieriqi) {
		this.banjieriqi = banjieriqi;
	}


	public String getBanjiewenjian() {
		return banjiewenjian;
	}


	public void setBanjiewenjian(String banjiewenjian) {
		this.banjiewenjian = banjiewenjian;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	public String getTransmissionID() {
		return transmissionID;
	}


	public void setTransmissionID(String transmissionID) {
		this.transmissionID = transmissionID;
	}


	public String getZhuangtaimiaoshu() {
		return zhuangtaimiaoshu;
	}


	public void setZhuangtaimiaoshu(String zhuangtaimiaoshu) {
		this.zhuangtaimiaoshu = zhuangtaimiaoshu;
	}


	public int getBanjiewenjianshu() {
		return banjiewenjianshu;
	}


	public void setBanjiewenjianshu(int banjiewenjianshu) {
		this.banjiewenjianshu = banjiewenjianshu;
	}


	public Date getShangbaoshijian() {
		return shangbaoshijian;
	}


	public void setShangbaoshijian(Date shangbaoshijian) {
		this.shangbaoshijian = shangbaoshijian;
	}


	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
    
}

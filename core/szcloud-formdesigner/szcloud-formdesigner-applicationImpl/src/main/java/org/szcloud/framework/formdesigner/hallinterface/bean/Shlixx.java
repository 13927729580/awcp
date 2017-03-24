package org.szcloud.framework.formdesigner.hallinterface.bean;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 受理信息 Entity
 * @author ysb
 *
 */
public class Shlixx {
	
	/**
	 * 应用系统编码
	 */
    private String xitongbianma;
    /**
     * 申请事项编码
     */
    private String shixiangbianma;
    /**
     * 申请事项名称
     */
    private String shenqingshixiang;
    /**
     * 审批系统原受理编号，即水印号
     */
    private String shoulibianhao;
    /**
     * 缴费金额
     */
    private Double jiaofeijine;
    /**
     * 受理日期
     */
    private Date shouliriqi;
    /**
     * 审批时限
     */
    private String shenpishixian;
    /**
     * 受理单位代码
     */
    private String shoulidanweidaima;
    /**
     * 首接单位代码（组织机构代码）
     */
    private String shoujiedanweidaima;
    /**
     * 受理人编号:受理人工号
     */
    private String shoulirenbianhao;
    /**
     * 受理人姓名
     */
    private String shoulirenxingming;
    /**
     * 证件类别
     */
    private String zhengjianleibie;
    /**
     * 申请单位代码(身份证号)
     */
    private String shenqingdanweidaima;
    /**
     * 申请单位名称（姓名）
     */
    private String shenqingdanweimingcheng;
    /**
     * 联系人姓名
     */
    private String lianxirenxingming;
    /**
     * 联系电话
     */
    private String lianxidianhua;
    /**
     * 联系手机号
     */
    private String lianxishoujihao;
    /**
     * 联系传真号
     */
    private String lianxichuanzhenhao;
    /**
     * 联系人电子邮件地址
     */
    private String email;
    /**
     * 联系人地址
     */
    private String lianxirendizhi;
    /**
     * 联系人邮编
     */
    private String lianxirenyoubian;
    /**
     * 首接资料项数
     */
    private int shoujieziliaoxiangshu;
    /**
     * 首接资料
     */
    private String shoujieziliao;
    /**
     * 受理信息上报状态
     */
    private String status;
    /**
     * 上报时间
     */
    private Date shangbaoshijian;
    /**
     * 传输
     */
    private String transmissionID;
	public String getXitongbianma() {
		return xitongbianma;
	}
	public void setXitongbianma(String xitongbianma) {
		this.xitongbianma = xitongbianma;
	}
	public String getShixiangbianma() {
		return shixiangbianma;
	}
	public void setShixiangbianma(String shixiangbianma) {
		this.shixiangbianma = shixiangbianma;
	}
	public String getShenqingshixiang() {
		return shenqingshixiang;
	}
	public void setShenqingshixiang(String shenqingshixiang) {
		this.shenqingshixiang = shenqingshixiang;
	}
	public String getShoulibianhao() {
		return shoulibianhao;
	}
	public void setShoulibianhao(String shoulibianhao) {
		this.shoulibianhao = shoulibianhao;
	}
	public Double getJiaofeijine() {
		return jiaofeijine;
	}
	public void setJiaofeijine(Double jiaofeijine) {
		this.jiaofeijine = jiaofeijine;
	}
	public Date getShouliriqi() {
		return shouliriqi;
	}
	public void setShouliriqi(Date shouliriqi) {
		this.shouliriqi = shouliriqi;
	}
	public String getShenpishixian() {
		return shenpishixian;
	}
	public void setShenpishixian(String shenpishixian) {
		this.shenpishixian = shenpishixian;
	}
	public String getShoulidanweidaima() {
		return shoulidanweidaima;
	}
	public void setShoulidanweidaima(String shoulidanweidaima) {
		this.shoulidanweidaima = shoulidanweidaima;
	}
	public String getShoujiedanweidaima() {
		return shoujiedanweidaima;
	}
	public void setShoujiedanweidaima(String shoujiedanweidaima) {
		this.shoujiedanweidaima = shoujiedanweidaima;
	}
	public String getShoulirenbianhao() {
		return shoulirenbianhao;
	}
	public void setShoulirenbianhao(String shoulirenbianhao) {
		this.shoulirenbianhao = shoulirenbianhao;
	}
	public String getShoulirenxingming() {
		return shoulirenxingming;
	}
	public void setShoulirenxingming(String shoulirenxingming) {
		this.shoulirenxingming = shoulirenxingming;
	}
	public String getShenqingdanweidaima() {
		return shenqingdanweidaima;
	}
	public void setShenqingdanweidaima(String shenqingdanweidaima) {
		this.shenqingdanweidaima = shenqingdanweidaima;
	}
	public String getShenqingdanweimingcheng() {
		return shenqingdanweimingcheng;
	}
	public void setShenqingdanweimingcheng(String shenqingdanweimingcheng) {
		this.shenqingdanweimingcheng = shenqingdanweimingcheng;
	}
	public String getLianxirenxingming() {
		return lianxirenxingming;
	}
	public void setLianxirenxingming(String lianxirenxingming) {
		this.lianxirenxingming = lianxirenxingming;
	}
	public String getLianxidianhua() {
		return lianxidianhua;
	}
	public void setLianxidianhua(String lianxidianhua) {
		this.lianxidianhua = lianxidianhua;
	}
	public String getLianxishoujihao() {
		return lianxishoujihao;
	}
	public void setLianxishoujihao(String lianxishoujihao) {
		this.lianxishoujihao = lianxishoujihao;
	}
	public String getLianxichuanzhenhao() {
		return lianxichuanzhenhao;
	}
	public void setLianxichuanzhenhao(String lianxichuanzhenhao) {
		this.lianxichuanzhenhao = lianxichuanzhenhao;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLianxirendizhi() {
		return lianxirendizhi;
	}
	public void setLianxirendizhi(String lianxirendizhi) {
		this.lianxirendizhi = lianxirendizhi;
	}
	public String getLianxirenyoubian() {
		return lianxirenyoubian;
	}
	public void setLianxirenyoubian(String lianxirenyoubian) {
		this.lianxirenyoubian = lianxirenyoubian;
	}
	public int getShoujieziliaoxiangshu() {
		return shoujieziliaoxiangshu;
	}
	public void setShoujieziliaoxiangshu(int shoujieziliaoxiangshu) {
		this.shoujieziliaoxiangshu = shoujieziliaoxiangshu;
	}
	public String getShoujieziliao() {
		return shoujieziliao;
	}
	public void setShoujieziliao(String shoujieziliao) {
		this.shoujieziliao = shoujieziliao;
	}
	public String getZhengjianleibie() {
		return zhengjianleibie;
	}
	public void setZhengjianleibie(String zhengjianleibie) {
		this.zhengjianleibie = zhengjianleibie;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getShangbaoshijian() {
		return shangbaoshijian;
	}
	public void setShangbaoshijian(Date shangbaoshijian) {
		this.shangbaoshijian = shangbaoshijian;
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

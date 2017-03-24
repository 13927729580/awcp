package org.szcloud.framework.formdesigner.application.service;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.szcloud.framework.formdesigner.application.vo.StoreVO;

import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.DocumentException;

public interface PrintService {

	public  void printPDF(StoreVO printManageVO,Map dataMap,OutputStream os);
	
	public  void batchPrintPDF(List<StoreVO> printManageVOs,List<Map> dataMaps,List<List<JSONObject>> componentsList, OutputStream os);
	
	public  void printPDFByTemplate(StoreVO printManageVO,Map dataMap,OutputStream os,List<JSONObject> components) throws DocumentException;
	
	public byte[] getFileByFileId(String fileId);
	
}

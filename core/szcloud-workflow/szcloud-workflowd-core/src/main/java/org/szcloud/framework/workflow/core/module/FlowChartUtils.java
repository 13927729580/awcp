package org.szcloud.framework.workflow.core.module;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.szcloud.framework.workflow.core.business.TWorkItem;
import org.szcloud.framework.workflow.core.emun.EActivityType;
import org.szcloud.framework.workflow.core.entity.CActivity;
import org.szcloud.framework.workflow.core.entity.CFlow;
import org.szcloud.framework.workflow.core.entity.CFlowLine;
import org.szcloud.framework.workflow.core.entity.CWorkItem;
import org.szcloud.framework.workflow.core.entity.FlowChartVO;
import org.szcloud.framework.workflow.core.entity.FlowImageVO;

public class FlowChartUtils {

	public static FlowChartVO getFlowParameters(CWorkItem ao_Item) {
		FlowChartVO flowchart = new FlowChartVO();
		List<FlowImageVO> list = new ArrayList<FlowImageVO>();
		CFlow lo_Flow = ao_Item.CurFlow;
		CActivity activity = ao_Item.CurActivity;
		List<String> x_coordinates_list = new ArrayList<String>();
		List<String> y_coordinates_list = new ArrayList<String>();
		for (CFlowLine lo_Line : lo_Flow.FlowLines) {
			// CActivity lo_ActSource = lo_Line.SourceActivity;
			// CActivity lo_ActGoal = lo_Line.GoalActivity;
			x_coordinates_list.add(lo_Line.LinePointXs);
			y_coordinates_list.add(lo_Line.LinePointYs);
		}
		flowchart.setX_coordinates_list(x_coordinates_list);
		flowchart.setY_coordinates_list(y_coordinates_list);
		
		String allStatus = TWorkItem.getFlowActIDs(ao_Item, 0);
		String ids[] = allStatus.split("@");
		String statusOne[] = ids[0].trim().split(";");//已触发处理状态未知
		String statusTwo[] = ids[1].trim().split(";");//已经流转的未处理
		String statusThree[] = ids[2].trim().split(";");//已经流转的已处理
		
		for (CActivity lo_Act : lo_Flow.Activities) {
			FlowImageVO vo = new FlowImageVO();
			if(activity == lo_Act){
				vo.setCurActivity(true);
			}else {
				vo.setCurActivity(false);
			}
			vo.setX(lo_Act.Left);
			vo.setY(lo_Act.Top);
			vo.setWidth(lo_Act.Width);
			vo.setHeight(lo_Act.Height);
			switch (lo_Act.ActivityType) {
			case EActivityType.StartActivity:
				vo.setImgUrl("flowmap"+File.separator+"Start.gif");
				break;
			case EActivityType.TransactActivity:
				int k = 0;
				if(statusTwo.length>0){
					for(int i=0;i<statusTwo.length;i++){
						if(statusTwo[i] == lo_Act.ActivityID+""){
							vo.setImgUrl("flowmap"+File.separator+"Activity1.gif");
							k = 1;
							break;
						}
					}
				}
				if(k != 1 && statusThree.length>0){
					for(int i=0;i<statusThree.length;i++){
						if(statusThree[i] == lo_Act.ActivityID+""){
							vo.setImgUrl("flowmap"+File.separator+"Activity2.gif");
							k = 2;
							break;
						}
					}
				}
				if(k == 0){
					vo.setImgUrl("flowmap"+File.separator+"Activity0.gif");
				}
				break;
			case EActivityType.FYIActivity:
				boolean flag = false;
				if(statusOne.length>0){
					for(int i=0;i<statusOne.length;i++){
						if(statusOne[i] == lo_Act.ActivityID+""){
							vo.setImgUrl("flowmap"+File.separator+"FYI1.gif");
							flag = true;
							break;
						}
					}
				}
				if(!flag){
					vo.setImgUrl("flowmap"+File.separator+"FYI0.gif");
				}
				break;
			case EActivityType.TumbleInActivity:
				int k1 = 0;
				if(statusTwo.length>0){
					for(int i=0;i<statusTwo.length;i++){
						if(statusTwo[i] == lo_Act.ActivityID+""){
							vo.setImgUrl("flowmap"+File.separator+"TrumbleIn1.gif");
							k1 = 1;
							break;
						}
					}
				}
				if(k1 != 1 && statusThree.length>0){
					for(int i=0;i<statusThree.length;i++){
						if(statusThree[i] == lo_Act.ActivityID+""){
							vo.setImgUrl("flowmap"+File.separator+"TrumbleIn2.gif");
							k1 = 2;
							break;
						}
					}
				}
				if(k1==0){
					vo.setImgUrl("flowmap"+File.separator+"TrumbleIn0.gif");
				}
				break;
			case EActivityType.LaunchActivity:
				int k2 = 0;
				if(statusTwo.length>0){
					for(int i=0;i<statusTwo.length;i++){
						if(statusTwo[i] == lo_Act.ActivityID+""){
							vo.setImgUrl("flowmap"+File.separator+"Launch1.gif");
							k2 = 1;
							break;
						}
					}
				}
				if(k2 != 1 && statusThree.length>0){
					for(int i=0;i<statusThree.length;i++){
						if(statusThree[i] == lo_Act.ActivityID+""){
							vo.setImgUrl("flowmap"+File.separator+"Launch2.gif");
							k2 = 2;
							break;
						}
					}
				}
				if(k2==0){
					vo.setImgUrl("flowmap"+File.separator+"Launch0.gif");
				}
				break;
			case EActivityType.SplitActivity:
				vo.setImgUrl("flowmap"+File.separator+"Split.gif");
				break;
			case EActivityType.StopActivity:
				vo.setImgUrl("flowmap"+File.separator+"Stop.gif");
				break;
			default:
				break;
			}
			vo.setActivityName(lo_Act.ActivityName);
			list.add(vo);
		}
		flowchart.setFlowImageList(list);
		return flowchart;
	}

	public static String getImageBase64String(CWorkItem workItem) {
		FlowChartPanel panel = new FlowChartPanel(workItem);
		//DrawFrame frame = new DrawFrame();
		//frame.setVisible(false);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BufferedImage bi = new BufferedImage(800,
				600, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bi.createGraphics();
		panel.paintFlowChartComponent(g2d);
		//frame.setContentPane(panel);
		//frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		try {
			ImageIO.write(bi, "PNG", out);
			byte[] binaryData = out.toByteArray();
			String imageBase64 = MBase64.encodeBase64String(binaryData);
			return imageBase64;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}

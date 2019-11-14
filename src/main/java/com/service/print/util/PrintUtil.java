package com.service.print.util;

import java.awt.print.PrinterJob;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.Sides;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * @author liuxingyue
 *
 */
@Log4j2
public class PrintUtil {
	
	public static String print(String picUrls, String printerName) throws Exception {
		log.info("printer : " + printerName);
		InputStream fis = null;
		try {
			if(null == picUrls || picUrls.length() <= 0){
				log.error("图片路径不能为空");
				return "图片路径不能为空";
			}
			
			for(String picUrl : picUrls.split(";")){
				URL url = new URL(picUrl); 
				URLConnection urlConn = url.openConnection();  
				int contentLength = urlConn.getContentLength();
				if(contentLength == 0){
					log.error("图片url content Length == 0");
					return "图片url content Length == 0";
				}
				fis = urlConn.getInputStream();
				// 设置打印格式，如果未确定类型，可选择autosense
				DocFlavor flavor = DocFlavor.INPUT_STREAM.PNG;
				//设置打印参数
				PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
				aset.add(new Copies(1)); //份数
				aset.add(Sides.ONE_SIDED);//单双面
				aset.add(PrintQuality.HIGH);
				aset.add(Chromaticity.MONOCHROME);
				aset.add(new JobName("mk-print-service",null));
				
				DocAttributeSet das = new HashDocAttributeSet();

	            // 设置打印纸张的大小（以毫米为单位）
	            das.add(new MediaPrintableArea(0, 0, 100, 150, MediaPrintableArea.MM));
				
				//定位打印服务
				PrintService printService = null;
				
				//获得本台电脑连接的所有打印机
				PrintService[] printServices = PrinterJob.lookupPrintServices();
				if(null == printServices || 0 == printServices.length){
					log.error("未找到可用的打印机");
					return "未找到可用的打印机";
				}
				
				if("" == printerName){
					printerName = printServices[0].getName();
				}
				//匹配指定打印机
				for (int i = 0; i < printServices.length; i++) {
					if (printServices[i].getName().equals(printerName)) {
						printService = printServices[i];
						break;
					}
				}
				if(null == printService){
					log.error("未找到名称为" + printerName + "的打印机");
					return "未找到名称为" + printerName + "的打印机";
				}
				//弹出打印对话框
//				PrinterJob p = PrinterJob.getPrinterJob();
//				boolean s = p.printDialog(aset);
//				System.out.println(s);
				Doc doc = new SimpleDoc(fis, flavor, das);
				DocPrintJob job = printService.createPrintJob(); // 创建打印作业
				job.print(doc, aset);
			}
		} catch (Exception e) {
			log.error("打印异常:"+e);
			return "打印异常";
		} finally {
			// 关闭打印的文件流
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "SUCCESS";
	}
	
	public static String getPrintServices(){
		//获得本台电脑连接的所有打印机
		PrintService[] printServices = PrinterJob.lookupPrintServices();
		if(null == printServices || 0 == printServices.length){
			return "未找到可用的打印机";
		}
		String printerNames="";
		//匹配指定打印机
		for (int i = 0; i < printServices.length; i++) {
			printerNames += printServices[i].getName() +";  ";
		}
		return printerNames;
	}
	
}

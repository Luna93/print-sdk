package com.service.print.util;

import java.io.FileInputStream;
import java.io.IOException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * @author liuxingyue
 *
 */
@Log4j2
public class PrintUtil2 {
	
	public static void drawImage(String fileName, int h, boolean aleartPrint)
	  {
	    if ((fileName == null) || (fileName.trim() == "")) {
	      throw new RuntimeException("文件名为空");
	    }
	    try
	    {
	      DocFlavor dof = null;
	      if (fileName.endsWith(".gif")) {
	        dof = DocFlavor.INPUT_STREAM.GIF;
	      } else if (fileName.endsWith(".jpg")) {
	        dof = DocFlavor.INPUT_STREAM.JPEG;
	      } else if (fileName.endsWith(".png")) {
	        dof = DocFlavor.INPUT_STREAM.PNG;
	      }
	      PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
	      pras.add(OrientationRequested.PORTRAIT);
	      
	      pras.add(new Copies(1));
	      
	      DocAttributeSet das = new HashDocAttributeSet();
	      

	      das.add(new MediaPrintableArea(0, 0, 100, h, 1000));
	      FileInputStream fin = new FileInputStream(fileName);
	      
	      Doc doc = new SimpleDoc(fin, dof, das);
	      
	      print(fileName, aleartPrint, dof, pras, doc);
	      fin.close();
	    }
	    catch (IOException ie)
	    {
	      ie.printStackTrace();
	    }
	    catch (PrintException pe)
	    {
	      pe.printStackTrace();
	    }
	  }
	  
	  private static void print(String fileName, boolean aleartPrint, DocFlavor dof, PrintRequestAttributeSet pras, Doc doc)
	    throws PrintException
	  {
	    PrintService service = PrintServiceLookup.lookupDefaultPrintService();
	    if (aleartPrint)
	    {
	      PrintService[] printServices = PrintServiceLookup.lookupPrintServices(dof, pras);
	      service = ServiceUI.printDialog(null, 400, 400, printServices, service, dof, pras);
	    }
	    if (service != null)
	    {
	      DocPrintJob job = service.createPrintJob();
	      
	      job.addPrintJobListener(new PrintJobListener()
	      {
	        public void printJobRequiresAttention(PrintJobEvent arg0)
	        {
	          System.out.println("printJobRequiresAttention");
	        }
	        
	        public void printJobNoMoreEvents(PrintJobEvent arg0)
	        {
	          System.out.println("打印机已接收");
	        }
	        
	        public void printJobFailed(PrintJobEvent arg0)
	        {
	          System.out.println("打印机无法完成作业,必须重新提交");
	        }
	        
	        public void printJobCompleted(PrintJobEvent arg0)
	        {
	          System.out.println("打印结束");
	        }
	        
	        public void printJobCanceled(PrintJobEvent arg0)
	        {
	          System.out.println("作业已被用户或者程序取消");
	        }
	        
	        public void printDataTransferCompleted(PrintJobEvent arg0)
	        {
	          System.out.println("数据已成功传输打印机");
	        }
	      });
	      try
	      {
	        job.print(doc, pras);
	      }
	      catch (PrintException pe)
	      {
	        pe.printStackTrace();
	      }
	    }
	    else
	    {
	      if (aleartPrint) {
	        throw new RuntimeException("打印机未连接,请选择打印机");
	      }
	      throw new RuntimeException("请设置默认打印机");
	    }
	  }
	  
	  public static void main(String[] args) {
		  PrintUtil2.drawImage("D:\\workfile\\pic\\595a0d9e37046.jpg",100,true);//false为不弹出打印框
	}
	
}

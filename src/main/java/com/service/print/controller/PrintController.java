package com.service.print.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.print.util.PrintUtil;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("")
@Log4j2
public class PrintController {
	
	@RequestMapping("/print")
	public String print(String picUrls, String printerName){
		try {
			return PrintUtil.print(picUrls, printerName);
		} catch (Exception e) {
			log.error("打印异常,ERROR:{}",e);
			return "FAIL";
		}
	}
	
	/**
	 * 获取本台电脑的所有打印机名称
	 * @return
	 */
	@RequestMapping("/getPrintServices")
	public String getPrintServices(){
		try {
			return PrintUtil.getPrintServices();
		} catch (Exception e) {
			log.error("获取异常,ERROR:{}",e);
			return "FAIL";
		}
	}

}

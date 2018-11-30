package com.icss.hr.pic.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.icss.hr.common.Pager;
import com.icss.hr.pic.pojo.Pic;
import com.icss.hr.pic.service.PicService;

/**
 * 图库控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/pic")
public class PicController {

	@Autowired
	private PicService service;
	
	/**
	 * 上传图片
	 * @throws IOException 
	 */
	@RequestMapping("/add")
	public void add(@RequestParam("picData") MultipartFile file,String picInfo,HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		//文件名称
		String picName = file.getOriginalFilename();
		//文件大小
		long picSize = file.getSize();
		//文件mime类型
		String mime = file.getContentType();
		//文件数据
		byte[] picData = file.getBytes();
		//登录名
		HttpSession session = request.getSession();
		String empLoginName = (String) session.getAttribute("empLoginName");
		
		//pojo对象
		Pic pic = new Pic(picName, picInfo, picSize, empLoginName, picData, new Date());
		
		service.addPic(pic);	
		
	}	
	
	@RequestMapping("/get")
	@ResponseBody
	public byte[] get(Integer picId,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		Pic pic = service.queryPicById(picId);
		
		byte[] picData = pic.getPicData();
		
		return picData;
		
	}
	
	@RequestMapping("/query")
	@ResponseBody
	public HashMap<String, Object> query(int pageNum,int pageSize,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		Pager pager = new Pager(service.getPicCount(), pageSize, pageNum);
		
		List<Pic> list = service.queryPicByPage(pager);
		
		HashMap<String, Object> map = new HashMap<>();
		map.put("pager", pager);
		map.put("list", list);
		
		return map;
		
	}
	
	@RequestMapping("/download")
	public void download(Integer picId,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		Pic pic = service.queryPicById(picId);
		
		// 中文文件名转码
		String filename = new String(pic.getPicName().getBytes(), "iso-8859-1");

		// 设置浏览器以附件形式接收数据（下载文件）
		response.setHeader("content-disposition", "attachment;filename=" + filename);
		
		//输出流
		OutputStream out = response.getOutputStream();
		
		//响应二进制数据
		FileCopyUtils.copy(pic.getPicData(), out);
		
		
		
		
	}
	
	@RequestMapping("/delete")
	public void delete(Integer picId,HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		service.deletePic(picId);
		
	}
	
	
}

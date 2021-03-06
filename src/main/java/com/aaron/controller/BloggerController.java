package com.aaron.controller;

import java.io.File;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.aaron.constant.FileConstants;
import com.aaron.entity.Blogger;
import com.aaron.service.BloggerService;
import com.aaron.util.CryptographyUtil;

/**
 * 博主Controller层
 * @author aaron_小锋
 *
 */
@Controller
@RequestMapping("/blogger")
public class BloggerController {

	@Resource
	private BloggerService bloggerService;
	
	/**
	 * 用户登录
	 * @param blogger
	 * @param request
	 * @return
	 */
	@RequestMapping("/login")
	public String login(Blogger blogger,HttpServletRequest request){
		Subject subject=SecurityUtils.getSubject();
		UsernamePasswordToken token=new UsernamePasswordToken(blogger.getUserName(), CryptographyUtil.md5(blogger.getPassword(), "hbaaron"));
		try{
			subject.login(token); // 登录验证
			return "redirect:/admin/main.jsp";
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute("blogger", blogger);
			request.setAttribute("errorInfo", "用户名或密码错误！");
			return "login";
		}
	}
	
	/**
	 * 查找博主信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/aboutMe")
	public ModelAndView aboutMe()throws Exception{
		ModelAndView mav=new ModelAndView();
		Blogger blogger = bloggerService.find();
		//博主头像从nginx服务器上取
		//blogger.setImageName(FileConstants.httpPath+File.separator+blogger.getImageName());
		
		mav.addObject("blogger",blogger);
		mav.addObject("mainPage", "foreground/blogger/info.jsp");
		mav.addObject("pageTitle","关于博主_Java开源博客系统");
		mav.setViewName("mainTemp");
		return mav;
	}
}

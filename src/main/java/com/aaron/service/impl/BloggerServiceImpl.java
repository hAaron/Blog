package com.aaron.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.aaron.dao.BloggerDao;
import com.aaron.entity.Blogger;
import com.aaron.service.BloggerService;

/**
 * ����Serviceʵ����
 * @author Administrator
 *
 */
@Service("bloggerService")
public class BloggerServiceImpl implements BloggerService{

	@Resource
	private BloggerDao bloggerDao;

	public Blogger find() {
		return bloggerDao.find();
	}

	public Blogger getByUserName(String userName) {
		return bloggerDao.getByUserName(userName);
	}

	public Integer update(Blogger blogger) {
		return bloggerDao.update(blogger);
	}
	
	
}

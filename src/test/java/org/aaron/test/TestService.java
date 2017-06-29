package org.aaron.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aaron.entity.Blog;
import com.aaron.entity.Comment;
import com.aaron.service.BlogService;
import com.aaron.service.CommentService;
import com.aaron.util.SpringContextHolder;
import com.alibaba.druid.util.Base64;

/**
 * serviceģ�������
 * 
 * @author Aaron
 * @date 2017��6��23��
 * @version 1.0
 * @package_name org.aaron.test
 */
@ContextConfiguration(locations = { "classpath*:spring-context*.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class TestService {

	private static Logger logger = LoggerFactory.getLogger(TestService.class);

	/**
	 * ����comment
	 */
	@Test
	public void testAddComment() {
		Comment comment = new Comment();
		CommentService commentService = SpringContextHolder
				.getBean(CommentService.class);
		BlogService blogService = SpringContextHolder
				.getBean(BlogService.class);
		/*
		for (int i = 0; i < 1; i++) {
			String userIp = "199.147.251.126"; // ��ȡ�û�IP
			comment.setUserIp(userIp);
			Blog commentBlog = new Blog();
			commentBlog.setId(75);
			comment.setContent("����������������_"+String.valueOf(System.currentTimeMillis()));
			comment.setBlog(commentBlog);
			commentService.add(comment);
			// �ò��͵Ļظ�������1
			Blog blog = blogService.findById(comment.getBlog().getId());
			blog.setReplyHit(blog.getReplyHit() + 1);
			blogService.update(blog);
		}
		*/
	}

}

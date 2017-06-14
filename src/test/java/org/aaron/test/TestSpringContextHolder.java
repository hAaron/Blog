package org.aaron.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aaron.entity.Blogger;
import com.aaron.service.BloggerService;
import com.aaron.util.SpringContextHolder;

/**
 * ����SpringContextHolder�࣬ע�������Spring�����ļ���ָ�����ࡣ 
 * 
 * @author Aaron
 * @date 2017��6��14��
 * @version 1.0
 * @package_name org.aaron.test
 */
@ContextConfiguration(locations = { "classpath*:spring-context*.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class TestSpringContextHolder {

	@Test
	public void testHolder() {
		BloggerService bloggerService = SpringContextHolder
				.getBean(BloggerService.class);
		Blogger blogger = bloggerService.getByUserName("aaron");
		System.out.println(blogger);
	}

}

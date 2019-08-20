package com.service.microservice.transaction.fenkufenbiao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Random;

/**
 * @author Simon
 * @Date 2019-08-20 11:07
 * @Describe 清风拂袖揽明月, 皓月鉴怀渡银河
 */
@Controller
public class UserController {

	@Autowired
	UserDao userDao;

	@RequestMapping("/insert")
	public void add() {
		for (int i = 0; i < 100; i++) {
			User user = new User();
			user.setId((long) (i + 1));
			int random = new Random().nextInt(100);
			if (random % 2 == 0) {
				user.setCity("上海");
			} else {
				user.setCity("杭州");
			}
			user.setName("simon" + i);
			userDao.insert(user);
		}
	}
}

package top.yueshushu.business.repository.facade;

import org.springframework.data.mongodb.repository.MongoRepository;

import top.yueshushu.business.repository.model.UserDo;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-07-19
 */

public interface UserRepository extends MongoRepository<UserDo, Integer> {
	
}

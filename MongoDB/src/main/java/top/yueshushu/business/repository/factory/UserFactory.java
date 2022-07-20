package top.yueshushu.business.repository.factory;

import org.mapstruct.Mapper;

import top.yueshushu.business.api.model.user.User;
import top.yueshushu.business.repository.model.UserDo;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-07-19
 */
@Mapper(componentModel = "spring")
public interface UserFactory extends  BaseFactory<UserDo, User>{
	
}

package top.yueshushu.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import top.yueshushu.security.model.Role;
import top.yueshushu.security.model.User;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-09-09
 */

public interface RoleMapper extends BaseMapper<Role> {
	
	/**
	 * 根据用户编号 查询对应的 角色信息
	 *
	 * @param userId 用户编号
	 */
	List<Role> listRolesByUserId(@Param("userId") Integer userId);
}

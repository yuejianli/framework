package top.yueshushu.security.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sun.org.apache.xpath.internal.operations.Bool;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Data;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-09-09
 */
@Data
@TableName("user")
public class User implements UserDetails {
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	@TableField("name")
	private String name;
	@TableField("password")
	private String password;
	@TableField("enable")
	private boolean enable;
	@TableField("non_expired")
	private boolean nonExpired;
	@TableField("non_locked")
	private boolean nonLocked;
	@TableField("credentials_non_expired")
	private boolean credentialsNonExpired;
	
	
	@TableField(exist = false)
	private List<Role> roleList = new ArrayList<>();
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
		
		roleList.forEach(n -> {
			authorityList.add(new SimpleGrantedAuthority(n.getName()));
		});
		return authorityList;
	}
	
	@Override
	public String getUsername() {
		return this.name;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return this.nonExpired;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return this.nonLocked;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		
		return this.credentialsNonExpired;
	}
	
	@Override
	public boolean isEnabled() {
		
		return this.enable;
	}
}

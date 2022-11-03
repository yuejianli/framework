package top.yueshushu.idem.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 一个普通的请求参数信息
 *
 * @author yuejianli
 * @date 2022-11-03
 */
@Data
public class UserRequestVO implements Serializable {
    private Integer id;
    private String name;
    private Integer age;
    private String sex;
    private String description;
}

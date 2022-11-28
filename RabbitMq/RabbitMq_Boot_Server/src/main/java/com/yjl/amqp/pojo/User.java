package com.yjl.amqp.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName:User
 * @Description TODO
 * @Author 岳建立
 * @Date 2020/12/22 13:53
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private Long id;
    private String name;
    private Integer age;
    private String description;
}

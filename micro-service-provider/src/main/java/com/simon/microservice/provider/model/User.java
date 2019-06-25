package com.simon.microservice.provider.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author fengyue
 * @Date 2019-06-20 14:29
 */
@Data
@Builder
public class User {

    private Long id;

    private String name;

    private int age;

    public User(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}

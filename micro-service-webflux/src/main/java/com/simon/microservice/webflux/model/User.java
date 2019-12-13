package com.simon.microservice.webflux.model;

import lombok.Data;

/**
 * @author simon
 * @Date 2019/12/12 10:43
 * @Describe 一念花开, 一念花落
 */
@Data
public class User {

    private Long id;

    private String name;

    private String gender;
}

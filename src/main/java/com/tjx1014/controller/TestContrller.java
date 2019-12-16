package com.tjx1014.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @作者: tjx
 * @描述:
 * @创建时间: 创建于14:30 2019/12/16
 **/
@RestController
@RequestMapping("/test")
public class TestContrller {

    @GetMapping
    public String teswt(){

        return "test";
    }
}

package com.example.testypie.domain.product.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProductViewController {
    @RequestMapping("/")
    public String viewController () {
        return "index";
    }
}

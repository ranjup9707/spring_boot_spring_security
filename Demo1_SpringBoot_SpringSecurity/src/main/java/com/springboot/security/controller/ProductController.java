package com.springboot.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.security.entity.Product;
import com.springboot.security.entity.UserInfo;
import com.springboot.security.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController 
{
	@Autowired
	private ProductService productService;
	
	@GetMapping("/welcome")
	public String welcome() {
		return "This endpoint is not secure";
	}
	
	@PostMapping("/new")
	public String addNewUser(@RequestBody UserInfo userInfo) {
		return productService.addUser(userInfo);
	}
	
	@GetMapping("/all")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public List<Product> getAllTheProducts(){
		return productService.getProducts();
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public Product getProductById(@PathVariable int id) {
		return productService.getProduct(id);
	}
}

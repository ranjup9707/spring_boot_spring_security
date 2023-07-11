package com.springboot.security.service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.security.entity.Product;
import com.springboot.security.entity.UserInfo;
import com.springboot.security.repository.UserInfoRepository;

import jakarta.annotation.PostConstruct;

@Service
public class ProductService 
{
	
	List<Product> productList = null;
	
	@Autowired
	private UserInfoRepository repo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostConstruct
	public void loadProductsFromDB() {
		productList = IntStream.rangeClosed(1, 100)
				.mapToObj(i -> Product.builder()
						.productId(i)
						.name("Product " + i)
						.qty(new Random().nextInt(10))
						.price(new Random().nextInt(50)).build()).collect(Collectors.toList());
	}
	
	public List<Product> getProducts(){
		return productList;
	}
	
	public Product getProduct(int id) {
		return productList.stream().filter(product -> product.getProductId() == id)
				.findAny().orElseThrow(() -> new RuntimeException("Product not found"));
	}
	
	public String addUser(UserInfo userInfo) {
		userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
		repo.save(userInfo);
		return "User added to system.";
	}
	
}

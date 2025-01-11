package com.chhei.mall.service;

import com.chhei.mall.vo.Cart;
import com.chhei.mall.vo.CartItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ICartService {

	public Cart getCartList();

	CartItem addCart(Long skuId, Integer num) throws ExecutionException, InterruptedException, Exception;

	List<CartItem> getUserCartItems();
}
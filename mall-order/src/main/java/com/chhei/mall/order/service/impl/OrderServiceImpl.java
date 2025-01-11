package com.chhei.mall.order.service.impl;

import com.chhei.common.vo.MemberVO;
import com.chhei.mall.order.fegin.CartFeignService;
import com.chhei.mall.order.fegin.MemberFeignService;
import com.chhei.mall.order.interceptor.AuthInterceptor;
import com.chhei.mall.order.vo.MemberAddressVo;
import com.chhei.mall.order.vo.OrderConfirmVo;
import com.chhei.mall.order.vo.OrderItemVo;
import lombok.val;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chhei.common.utils.PageUtils;
import com.chhei.common.utils.Query;

import com.chhei.mall.order.dao.OrderDao;
import com.chhei.mall.order.entity.OrderEntity;
import com.chhei.mall.order.service.OrderService;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Autowired
    MemberFeignService memberFeignService;

    @Autowired
    CartFeignService cartFeignService;

    @Autowired
    ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVo confirmOrder() {
        OrderConfirmVo vo = new OrderConfirmVo();
        MemberVO memberVO = (MemberVO)AuthInterceptor.threadLocal.get();
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
			RequestContextHolder.setRequestAttributes(requestAttributes);
            Long id = memberVO.getId();
            List<MemberAddressVo> addresses = memberFeignService.getAddress(id);
            vo.setAddress(addresses);
        }, executor);

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
			RequestContextHolder.setRequestAttributes(requestAttributes);
            List<OrderItemVo> userCartItems = cartFeignService.getUserCartItems(1l);
            vo.setItems(userCartItems);
        },executor);

		try {
			CompletableFuture.allOf(future1,future2).get();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return vo;
    }
}
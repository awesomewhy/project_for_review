package com.ozon.online.mapper;

import com.ozon.online.dto.order.CreateOrderForChatShowDto;
import com.ozon.online.entity.Order;
import com.ozon.online.entity.User;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order mapCreateOrderForChatShowDtoToEntity(User id, CreateOrderForChatShowDto createOrderForChatShowDto, User user) {
        Order order = Order.builder()
                .sellerId(id)
                .buyerId(user)
                .orderType(createOrderForChatShowDto.getOrderTypeEnum())
                .description(createOrderForChatShowDto.getDescription())
                .build();
        return order;
    }
}

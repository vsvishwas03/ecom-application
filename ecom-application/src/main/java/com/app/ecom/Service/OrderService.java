package com.app.ecom.Service;

import com.app.ecom.Dto.OrderItemDto;
import com.app.ecom.Dto.OrderResponse;
import com.app.ecom.Repository.OrderRepository;
import com.app.ecom.Repository.UserRepository;
import com.app.ecom.model.Orders.Order;
import com.app.ecom.model.Orders.OrderItem;
import com.app.ecom.model.Orders.OrderStatus;
import com.app.ecom.model.UserCart.CartItem;
import com.app.ecom.model.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private  final CartItemService cis;
    private final UserService us;
    private final UserRepository ur;
    private final Object nul = null;

@Transactional
    public Optional<OrderResponse> createOrder(String userId) {
        // validate for cart items
        List<CartItem> items= cis.getAllCartItemsByUserId(userId);
        if(items.isEmpty()){
            return Optional.empty();

        }


        //validate for users
//       Optional<userResponse> userOptionals= us.getUserById(Long.valueOf(userId));
Optional<User> userOptional= ur.findById(Long.valueOf(userId));
        if(userOptional.isEmpty()){
            return Optional.empty();

        }
        User user= userOptional.get();


        //calculate TOTAL PRICE
        BigDecimal totalPrice= items.stream()
                 .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);



        //create order
        Order order= new Order();
        order.setUser(user);
        order.setTotalAmount(totalPrice);
        order.setStatus(OrderStatus.CONFIRMED);
        List<OrderItem> orderItems= items.stream()
                .map(item-> new OrderItem(null,item.getProduct(),
                        item.getQuantity(),item.getPrice(),order))
                .toList();

        order.setItems(orderItems);
        Order savedOrder= orderRepository.save(order);


        //clear the cart
        cis.clearCart(userId);

return Optional.of(mapToOrderResponse(savedOrder));

    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream()
                        .map(orderItem -> new OrderItemDto(

                                orderItem.getId(),
                                orderItem.getProduct().getId(),
                                orderItem.getQuantity(),
                                orderItem.getPrice(),
                                orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))


                        )).toList(),
                order.getCreatedAt()
        );


    }
}

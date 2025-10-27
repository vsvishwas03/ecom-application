package com.app.ecom.Repository;

import com.app.ecom.model.Product.Product;
import com.app.ecom.model.UserCart.CartItem;
import com.app.ecom.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    CartItem findByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);

    List<CartItem> findByUserId(Long userId);

    void deleteByUser(User user);
}

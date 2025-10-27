package com.app.ecom.Service;

import com.app.ecom.Dto.cartItemRequest;
import com.app.ecom.Repository.CartItemRepository;
import com.app.ecom.Repository.ProductRepository;
import com.app.ecom.Repository.UserRepository;
import com.app.ecom.model.Product.Product;
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
public class CartItemService {

    private final ProductRepository ps;
    private final UserRepository us;
    private final CartItemRepository cir;


    public Boolean addTocart(String userId, cartItemRequest request) {
        //look for product
      Optional<Product> productOptional= ps.findById(request.getProductId());
      if(productOptional.isEmpty()) return false;

      Product product= productOptional.get();
      if(product.getStockQuantity()< request.getQuantity()) return  false;

      //look for user
      Optional<User> userOpt=  us.findById(Long.valueOf(userId));
        if(userOpt.isEmpty()) return false;

        User user= userOpt.get();
        //check if prod already exists in the cart so update count

        CartItem existing = cir.findByUserAndProduct(user, product);
        if(existing!=null){
            //update the item and price
            existing.setQuantity(existing.getQuantity()+ request.getQuantity());
            existing.setPrice(existing.getPrice().multiply(BigDecimal.valueOf(existing.getQuantity())));
            cir.save(existing);


        }else{
            //add the item
            CartItem cartItem=new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cir.save(cartItem);

        }
        return true;







    }
@Transactional
    public boolean deleteCartItem(String userId, Long productId) {

        Optional<Product> productOptional= ps.findById(productId);
//        if(productOptional.isEmpty()) return false;
        Optional<User> userOpt=  us.findById(Long.valueOf(userId));
//        if(userOpt.isEmpty()) return false;

//        userOpt.flatMap(user ->
//                productOptional.map(product ->{
//                    cir.deleteByUserAndProduct(user,product);
//                    return true;
//                })
//        );

    if(productOptional.isPresent() && userOpt.isPresent()){
        cir.deleteByUserAndProduct(userOpt.get(),productOptional.get());
        return true;

    }
        return false;

    }

    public List<CartItem> getAllCartItemsByUserId(String userId) {

        List<CartItem> itemList= cir.findByUserId(Long.valueOf(userId));
        return itemList;

    }

    public void clearCart(String userId) {
        us.findById(Long.valueOf(userId)).ifPresent(cir::deleteByUser);

    }
}

package com.app.ecom.Controller;

import com.app.ecom.Dto.cartItemRequest;
import com.app.ecom.Service.CartItemService;
import com.app.ecom.model.UserCart.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cis;


    @PostMapping
    public ResponseEntity<String> addToCart(@RequestBody cartItemRequest request , @RequestHeader ("X-User-ID") String userId){
       if( !cis.addTocart(userId, request)){
           return ResponseEntity.badRequest().body("Product out of stock or user not found");
       }
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromCart(@RequestHeader("X-User-ID") String userId,@PathVariable Long productId){

     boolean deleted= cis.deleteCartItem(userId,productId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();

    }

    @GetMapping("/items")
    public ResponseEntity<List<CartItem>> getAllCartItemsByUserId(@RequestHeader("X-User-ID") String userId){
       List<CartItem> items= cis.getAllCartItemsByUserId(userId);
       if(items!=null) return new ResponseEntity<>(items,HttpStatus.OK);
       return ResponseEntity.noContent().build() ;

    }

    

}

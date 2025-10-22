package com.app.ecom.Controller;

import com.app.ecom.Dto.ProductRequest;
import com.app.ecom.Dto.ProductResposne;
import com.app.ecom.Service.ProductService;
import com.app.ecom.model.Product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {


    private final ProductService productService;


    @PostMapping
    public ResponseEntity<ProductResposne> createProduct(@RequestBody ProductRequest productRequest){
        return new ResponseEntity<ProductResposne>(productService.createProduct(productRequest),
                HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResposne> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest){
        return productService.updateProduct(id,productRequest)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());

    }

    @GetMapping
    public  ResponseEntity<List<ProductResposne>> getAllProducts(){
        return new ResponseEntity<>(productService.getAllProducts(),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product with ID " + id + " has been marked as inactive.");
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResposne>> searchProducts (@RequestParam String keyword){
        return ResponseEntity.ok(productService.searchProducts(keyword));

    }

}

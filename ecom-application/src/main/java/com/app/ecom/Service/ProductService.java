package com.app.ecom.Service;

import com.app.ecom.Dto.ProductRequest;
import com.app.ecom.Dto.ProductResposne;
import com.app.ecom.Repository.ProductRepository;
import com.app.ecom.model.Product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    public ProductResposne createProduct(ProductRequest productRequest) {
        Product product= new Product();
        updateProductFromRequest(product,productRequest);
        Product savedproduct  =productRepository.save(product);
        return mapToProductResponse(savedproduct);


    }

    private ProductResposne mapToProductResponse(Product savedproduct) {
        ProductResposne pr= new ProductResposne();
        pr.setId(savedproduct.getId());
        pr.setName(savedproduct.getName());
        pr.setDescription(savedproduct.getDescription());
        pr.setPrice(savedproduct.getPrice());
        pr.setImageUrl(savedproduct.getImageUrl());
        pr.setStockQuantity(savedproduct.getStockQuantity());
        pr.setCategory(savedproduct.getCategory());
        pr.setActive(savedproduct.getActive());
        return pr;


    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setImageUrl(productRequest.getImageUrl());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setCategory(productRequest.getCategory());


    }

    public Optional<ProductResposne> updateProduct(Long id, ProductRequest productRequest) {
       return  productRepository.findById(id)
                .map(existingProduct -> {
                    updateProductFromRequest(existingProduct,productRequest);
                   Product saved= productRepository.save(existingProduct);
                    return mapToProductResponse(saved);
                });
//               .orElseThrow(()-> new RuntimeException("Product not found with id: " + id ));

    }

    public List<ProductResposne> getAllProducts() {
        List<Product> productList= productRepository.findByActiveTrue();
        return productList.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());

    }

    public void deleteProduct(Long id) {
//        productRepository.deleteById(id); but we have to make product inactive
        Product product= productRepository.findById(id).orElseThrow(()-> new RuntimeException("NO PROD WITH ID: " +id));
        product.setActive(false);
        productRepository.save(product);


    }

    public List<ProductResposne> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword).stream()
                .map(this::mapToProductResponse).collect(Collectors.toList());


    }
}

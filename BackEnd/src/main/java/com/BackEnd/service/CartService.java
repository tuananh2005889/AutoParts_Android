package com.BackEnd.service;

import com.BackEnd.dto.AddToCartRequest;
import com.BackEnd.dto.CartBasicInfoDTO;
import com.BackEnd.dto.CartItemDTO;
import com.BackEnd.model.Product;
import com.BackEnd.repository.CartItemRepository;
import com.BackEnd.repository.CartRepository;
import com.BackEnd.repository.ProductRepository;
import com.BackEnd.repository.UserRepository;
import com.BackEnd.utils.DTOConverter;
import java.util.stream.Collectors;
import com.BackEnd.model.Cart;
import com.BackEnd.model.CartItem;
import com.BackEnd.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;


    // public CartDTO getActiveCartDTO(String userName) {
    // User user = userRepo.findByUserName(userName)
    // .orElseThrow(() -> new RuntimeException("User not found"));
    // Cart cart = cartRepo.findByUserAndStatus(user, Cart.CartStatus.ACTIVE)
    // .orElseThrow(() -> new RuntimeException("Cart not found"));
    //
    // return DTOConverter.toCartDTO(cart);
    // }
    //
    // public CartDTO createActiveCartDTO(String userName) {
    // User user = userRepo.findByUserName(userName)
    // .orElseThrow(() -> new RuntimeException("User not found"));
    //
    // Cart newCart = new Cart();
    // newCart.setUser(user);
    // newCart.setStatus(Cart.CartStatus.ACTIVE);
    //
    // cartRepo.save(newCart);
    //
    // return DTOConverter.toCartDTO(newCart);
    // }
    // ->
    // gop 2 method tren thanh 1, giup controller, frontend do rac roi
    public CartBasicInfoDTO getOrCreateActiveCartDTO(String userName) {
        User user = userRepo.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart initalCart = cartRepo.findCartByUserAndStatus(user, Cart.CartStatus.ACTIVE)
                .orElse(null);
        if (initalCart == null) {
            initalCart = new Cart();
            initalCart.setUser(user);
            initalCart.setStatus(Cart.CartStatus.ACTIVE);
            cartRepo.save(initalCart);
        }
        return DTOConverter.toCartBasicInfoDTO(initalCart);
    }

    // cartId -> getCart -> getItems in Cart
    public List<CartItemDTO> getCartItemsInActiveCart(Long cartId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItemDTO> cartItemDTOs = new ArrayList<>();

        cart.getCartItems().stream()
                .map(cartItem -> DTOConverter.toCartItemDTO(cartItem))
                .forEach(cartItemDTOs::add);

        return cartItemDTOs;
    }

    // Phương thức thanh toán giỏ hàng, cập nhật trạng thái giỏ hàng thành PAID
    public void checkoutCart(Long cartId) {
        Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.setStatus(Cart.CartStatus.PAID);
        cartRepo.save(cart);
    }




 

    public CartItemDTO addItemToCart(AddToCartRequest addToCartRequest) {

        // check quantity request
        if (addToCartRequest.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Cart cart = cartRepo.findById(addToCartRequest.getCartId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Product product = productRepo.findByProductId(addToCartRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(
                        item -> item.getProduct().getProductId().equals(addToCartRequest.getProductId()))
                .findFirst();
        if (existingCartItem.isPresent()) {
            existingCartItem.get().setQuantity(existingCartItem.get().getQuantity() + addToCartRequest.getQuantity());
            cartItemRepo.save(existingCartItem.get());

        } else {
            CartItem cartItem = new CartItem(product, addToCartRequest.getQuantity(), cart);
            cartItemRepo.save(cartItem);
            cart.getCartItems().add(cartItem);

        }

        cartRepo.save(cart);
        return DTOConverter.toCartItemDTO(existingCartItem.get());
    }

    public Cart getCartStatus(Long cartId) {
        return cartRepo.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
    }


    public List<String> getImageUrlPerCartItem(Long cartId) {
        try{
            List<String> imageUrls = cartRepo.findImageUrlPerCartItem(cartId);
             return imageUrls != null ? imageUrls : new ArrayList<>();
        }catch(Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }


    }


}

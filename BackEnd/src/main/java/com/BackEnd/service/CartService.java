package com.BackEnd.service;


import com.BackEnd.dto.AddToCartRequest;
import com.BackEnd.dto.CartBasicInfoDTO;
import com.BackEnd.dto.CartItemDTO;
import com.BackEnd.model.Product;
import com.BackEnd.repository.CartItemRepository;
import com.BackEnd.repository.CartRepository;
import com.BackEnd.repository.ProductRepository;
import com.BackEnd.repository.UserRepository;
import com.BackEnd.dto.CartDTO;
import com.BackEnd.utils.DTOConverter;
import com.BackEnd.model.Cart;
import com.BackEnd.model.CartItem;
import com.BackEnd.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;
//
//    public CartDTO getActiveCartDTO(String userName) {
//        User user = userRepo.findByUserName(userName)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        Cart cart = cartRepo.findByUserAndStatus(user, Cart.CartStatus.ACTIVE)
//                .orElseThrow(() -> new RuntimeException("Cart not found"));
//
//        return DTOConverter.toCartDTO(cart);
//    }
//
//    public CartDTO createActiveCartDTO(String userName) {
//        User user = userRepo.findByUserName(userName)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Cart newCart = new Cart();
//        newCart.setUser(user);
//        newCart.setStatus(Cart.CartStatus.ACTIVE);
//
//        cartRepo.save(newCart);
//
//        return DTOConverter.toCartDTO(newCart);
//    }
// ->
//      gop 2 method tren thanh 1, giup controller, frontend do rac roi
    public CartBasicInfoDTO getOrCreateActiveCartDTO(String userName){
        User user = userRepo.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
    Cart initalCart = cartRepo.findCartByUserAndStatus(user, Cart.CartStatus.ACTIVE)
                .orElse(null);
        if(initalCart == null){
            initalCart = new Cart();
            initalCart.setUser(user);
            initalCart.setStatus(Cart.CartStatus.ACTIVE);
           cartRepo.save(initalCart);
        }
        return DTOConverter.toCartBasicInfoDTO(initalCart);
    }

    // cartId -> getCart -> getItems in Cart
    public List<CartItemDTO> getAllItemsInActiveCart(Long cartId){
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(()-> new RuntimeException("Cart not found"));
        List<CartItemDTO> cartItemDTOs =
                Collections.emptyList();
        new ArrayList<CartItemDTO>(null);
        cart.getCartItems().stream().map(cartItem -> DTOConverter.toCartItemDTO(cartItem)
        );
        return  cartItemDTOs;
    }

    // Phương thức thanh toán giỏ hàng, cập nhật trạng thái giỏ hàng thành PAID
    public void checkoutCart(Long cartId) {
        Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.setStatus(Cart.CartStatus.PAID);  // Cập nhật trạng thái giỏ hàng thành đã thanh toán
        cartRepo.save(cart);  // Lưu lại trạng thái mới của giỏ hàng
    }

    public void addItemToCart(AddToCartRequest addToCartRequest){
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
        if(existingCartItem.isPresent()){
            existingCartItem.get().setQuantity(existingCartItem.get().getQuantity() + addToCartRequest.getQuantity());
            cartItemRepo.save(existingCartItem.get());
        }else{
            CartItem cartItem = new CartItem(product, addToCartRequest.getQuantity(), cart);
            cartItemRepo.save(cartItem);
            cart.getCartItems().add(cartItem);

        }

        cartRepo.save(cart);
    }



//    public Cart getActiveCart(String userName){
//      User user = userRepo.findByUserName(userName)
//              .orElseThrow(() -> new RuntimeException("User not found"));
//      Cart cart = cartRepo.findByUserAndStatus(user, Cart.CartStatus.ACTIVE)
//              .orElseThrow(()->new RuntimeException("Cart not found"));
//        return cart;
//    }
//
//
//
//    public Cart createActiveCart(String userName){
//        User user = userRepo.findByUserName(userName)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        Cart newCart = new Cart();
//        newCart.setUser(user);
//        newCart.setStatus(Cart.CartStatus.ACTIVE);
//
//        cartRepo.save(newCart);
//
//        return  newCart;
//    }

   //tim hieu.
//    public Cart getActiveCartIfDontHaveWeCreate (String username) {
//        User user = userRepo.findByUserName(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        return cartRepo.findByUserAndStatus(user, Cart.CartStatus.ACTIVE)
//                .orElseGet(() -> {
//                    Cart newCart = new Cart();
//                    newCart.setUser(user);
//                    newCart.setStatus(Cart.CartStatus.ACTIVE);
//                    return cartRepo.save(newCart);
//                });
//    }
    //tim.hieu
//    public Cart createCart(String userName) {
//        User user = userRepo.findByUserName(userName)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Cart existingCart = cartRepo.findByUserAndStatus(user, Cart.CartStatus.ACTIVE)
//                .orElse(null);
//
//        if (existingCart != null) {
//            return existingCart;
//        } else {
//            Cart newCart = new Cart();
//            newCart.setUser(user);
//            return cartRepo.save(newCart);
//        }
//    }

    // API kiểm tra trạng thái giỏ hàng của người dùng
    public Cart getCartStatus(Long cartId) {
        return cartRepo.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
    }

//    public Cart addItemToCart(AddToCartRequest request) {
//        User user = userRepo.findByUserName(request.getUserName())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Product product = productRepo.findByProductId(request.getProductId())
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        Cart cart = getActiveCartIfDontHaveWeCreate(user.getUserName());
//
//        Optional<CartItem> existingItemOpt = cart.getCartItems().stream()
//                .filter(item -> item.getProduct().getProductId().equals(product.getProductId()))
//                .findFirst();
//
//        if (existingItemOpt.isPresent()) {
//            CartItem existingItem = existingItemOpt.get();
//            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
//        } else {
//            CartItem newItem = new CartItem();
//            newItem.setCart(cart);
//            newItem.setProduct(product);
//            newItem.setQuantity(request.getQuantity());
//            cart.getCartItems().add(newItem);
//        }
//
//        return cartRepo.save(cart);
//    }






}


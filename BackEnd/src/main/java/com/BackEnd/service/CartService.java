package com.BackEnd.service;

import com.BackEnd.dto.AddToCartRequest;
import com.BackEnd.dto.BasicCartInfoDto;
import com.BackEnd.dto.CartItemDTO;
import com.BackEnd.model.Product;
import com.BackEnd.repository.CartItemRepository;
import com.BackEnd.repository.CartRepository;
import com.BackEnd.repository.ProductRepository;
import com.BackEnd.repository.UserRepository;
import com.BackEnd.utils.DTOConverter;
import com.BackEnd.model.Cart;
import com.BackEnd.model.CartItem;
import com.BackEnd.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public Cart getCartByCartId(Long cartId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new RuntimeException("No cart found"));
        return cart;
    }

    public String changeCartStatus(Long cartId, Cart.CartStatus status) {
        try {
            Cart cart = getCartByCartId(cartId);
            cart.setStatus(status);
            cartRepo.save(cart);
            return "Cart status updated successfully";
        } catch (EntityNotFoundException e) {
            return "Cart not found";
        } catch (DataAccessException e) {
            return "Database error while saving cart";
        } catch (Exception e) {
            return "An unexpected error occurred: " + e.getMessage();
        }
    }

    public BasicCartInfoDto getOrCreateActiveCartDTO(String userName) {
        User user = userRepo.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart initalCart = cartRepo.findCartByUserAndStatus(user, Cart.CartStatus.ACTIVE)
                .orElse(null);
        if (initalCart == null) {
            initalCart = new Cart();
            initalCart.setUser(user);
            initalCart.setStatus(Cart.CartStatus.ACTIVE);
            initalCart.setTotalPrice(0.0);
            cartRepo.save(initalCart);
        }
        return DTOConverter.toCartBasicInfoDTO(initalCart);
    }

    // cartId -> getCart -> getItems in Cart

    public List<CartItemDTO> getAllCartItemDTOsInActiveCart(Long cartId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItemDTO> cartItemDTOList = new ArrayList<>();

        cart.getCartItems().stream()
                .map(cartItem -> DTOConverter.toCartItemDTO(cartItem))
                .forEach(item -> cartItemDTOList.add(item));

        return cartItemDTOList;
    }

    public List<CartItem> getAllCartItemsInActiveCart(Long cartId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        return cart.getCartItems();

    }

    // public void checkoutCart(Long cartId) {
    // Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new
    // RuntimeException("Cart not found"));
    // cart.setStatus(Cart.CartStatus.ARCHIVED);
    // cartRepo.save(cart);
    // }

    public User getUserByCartId(Long cartId) {
        User user = cartRepo.findUserByCartId(cartId);
        return user;
    }

    @Transactional
    public CartItemDTO addItemToCart(AddToCartRequest addToCartRequest) {

        // Check quantity request
        if (addToCartRequest.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Cart cart = cartRepo.findById(addToCartRequest.getCartId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Product product = productRepo.findByProductId(addToCartRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(addToCartRequest.getProductId()))
                .findFirst();

        CartItem responseItem;
        // get current cart price
        Double cartTotalPrice = cart.getTotalPrice();

        if (existingCartItem.isPresent()) {
            CartItem item = existingCartItem.get();
            item.setQuantity(item.getQuantity() + addToCartRequest.getQuantity());

            // increase price
            Double itemTotalPrice = product.getPrice() * addToCartRequest.getQuantity();
            cartTotalPrice += itemTotalPrice;

            responseItem = item;
        } else {
            CartItem newCartItem = new CartItem(product, addToCartRequest.getQuantity(), cart);
            cart.getCartItems().add(newCartItem);
            // increase price
            Double itemTotalPrice = product.getPrice() * addToCartRequest.getQuantity();
            cartTotalPrice += itemTotalPrice;

            responseItem = newCartItem;
        }

        cart.setTotalPrice(cartTotalPrice);
        cartRepo.save(cart); // co che jpa, lay CartItem tu Cart, sau do thay doi CartItem, sau do chi can
                             // save Cart thi
        // jpa tu dong goi CartItemRepo.save(CartItem) do
        return DTOConverter.toCartItemDTO(responseItem);
    }

    public Cart getCartStatus(Long cartId) {
        return cartRepo.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    public List<String> getImageUrlPerCartItem(Long cartId) {
        try {
            List<String> imageUrls = cartRepo.findImageUrlPerCartItem(cartId);
            return imageUrls != null ? imageUrls : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Double getTotalPrice(Long cartId) {
        Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new RuntimeException("Cart not found"));
        return cart.getTotalPrice();
    }

    @Transactional
    public void removeItemFromCart(Long cartItemId) {
        CartItem item = cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cartItemId: " + cartItemId));
        Cart cart = item.getCart();

        cartItemRepo.delete(item);

        // Bắt buộc remove khỏi list để lần sau load lại entity không còn
        cart.getCartItems().removeIf(ci -> ci.getCartItemId().equals(cartItemId));

        double newTotal = cart.getCartItems().stream()
                .mapToDouble(ci -> ci.getQuantity() * ci.getProduct().getPrice())
                .sum();
        cart.setTotalPrice(newTotal);

        cartRepo.save(cart);
    }

}

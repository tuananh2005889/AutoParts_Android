package com.BackEnd.Service;

import com.BackEnd.Repository.OrderDetailRepository;
import com.BackEnd.Repository.OrderRepository;
import com.BackEnd.Repository.ProductRepository;
import com.BackEnd.Repository.UserRepository;
import com.BackEnd.model.CartItem;
import com.BackEnd.model.Order;
import com.BackEnd.model.OrderDetail;
import com.BackEnd.model.Product;
import com.BackEnd.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository; // Dùng để truy vấn User

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    public boolean addToCart(User user, String productId, int quantity) {
        try {
            // Lấy đối tượng User persistent từ DB dựa trên userName
            Optional<User> persistentUserOpt = userRepository.findByUserName(user.getUserName());
            if (!persistentUserOpt.isPresent()) {
                logger.error("User with userName '{}' not found in DB", user.getUserName());
                return false;
            }
            user = persistentUserOpt.get();

            // Lấy sản phẩm từ DB
            Optional<Product> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                logger.error("Product not found for id: {}", productId);
                return false;
            }
            if (quantity <= 0) {
                logger.error("Invalid quantity: {} provided", quantity);
                return false;
            }
            Product product = productOpt.get();

            // Sử dụng truy vấn theo userName thay vì đối tượng User
            Optional<Order> orderOpt = orderRepository.findByUser_UserNameAndStatus(user.getUserName(), "Pending");
            Order order;
            if (orderOpt.isPresent()) {
                order = orderOpt.get();
            } else {
                order = new Order();
                order.setUser(user);
                order.setOrderDate(LocalDateTime.now());
                order.setStatus("Pending");
                order.setTotalAmount(0.0);
                orderRepository.save(order);
            }

            // Thêm chi tiết đơn hàng cho sản phẩm mới
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(quantity);
            orderDetail.setPrice(product.getPrice() * quantity);
            orderDetailRepository.save(orderDetail);

            // Cập nhật tổng tiền của đơn hàng
            double newTotal = order.getTotalAmount() + (product.getPrice() * quantity);
            order.setTotalAmount(newTotal);
            orderRepository.save(order);

            return true;
        } catch (Exception e) {
            logger.error("Unexpected error in addToCart: {}", e.getMessage(), e);
            return false;
        }
    }

    // Chuyển đổi OrderDetail thành DTO để tránh lazy-loading lỗi khi serialize JSON
    public List<CartItem> getCartByUser(User user) {
        try {
            Optional<Order> orderOpt = orderRepository.findByUser_UserNameAndStatus(user.getUserName(), "Pending");
            if (orderOpt.isPresent()) {
                List<OrderDetail> details = orderDetailRepository.findByOrder(orderOpt.get());
                return details.stream().map(detail -> {
                    CartItem item = new CartItem();
                    // Giả sử Product có trường id, name, price, và danh sách images
                    item.setProductId(detail.getProduct().getProductId());
                    item.setProductName(detail.getProduct().getName());
                    item.setQuantity(detail.getQuantity());
                    item.setPrice(detail.getPrice());
                    if (detail.getProduct().getImages() != null && !detail.getProduct().getImages().isEmpty()) {
                        item.setImageUrl(detail.getProduct().getImages().get(0));
                    } else {
                        item.setImageUrl(null);
                    }
                    return item;
                }).collect(Collectors.toList());
            }
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Error fetching cart items for user '{}': {}", user.getUserName(), e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    // Phương thức removeItemFromCart không thay đổi nhiều
    public boolean removeItemFromCart(Long orderDetailId) {
        try {
            Optional<OrderDetail> detailOpt = orderDetailRepository.findById(orderDetailId);
            if (detailOpt.isPresent()) {
                OrderDetail detail = detailOpt.get();
                Order order = detail.getOrder();
                double priceReduction = detail.getPrice();

                orderDetailRepository.delete(detail);
                double newTotal = order.getTotalAmount() - priceReduction;
                order.setTotalAmount(newTotal);
                orderRepository.save(order);
                return true;
            }
            logger.error("OrderDetail not found for id: {}", orderDetailId);
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error when removing item from cart: {}", e.getMessage(), e);
            return false;
        }
    }
}

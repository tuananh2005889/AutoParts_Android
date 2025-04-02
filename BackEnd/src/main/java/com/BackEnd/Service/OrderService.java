package com.BackEnd.Service;

import com.BackEnd.Repository.OrderDetailRepository;
import com.BackEnd.Repository.OrderRepository;
import com.BackEnd.Repository.ProductRepository;
import com.BackEnd.model.Order;
import com.BackEnd.model.OrderDetail;
import com.BackEnd.model.Product;
import com.BackEnd.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    public boolean addToCart(User user, Long productId, int quantity) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty() || quantity <= 0) {
            return false; // Sản phẩm không tồn tại hoặc số lượng không hợp lệ
        }

        Product product = productOpt.get();

        // Tìm giỏ hàng "Pending" của người dùng
        Optional<Order> orderOpt = orderRepository.findByUserAndStatus(user, "Pending");

        Order order;
        if (orderOpt.isPresent()) {
            order = orderOpt.get();
        } else {
            // Tạo giỏ hàng mới nếu chưa có
            order = new Order();
            order.setUser(user);
            order.setOrderDate(LocalDateTime.now());
            order.setStatus("Pending");
            order.setTotalAmount(0.0);
            orderRepository.save(order);
        }

        // Thêm sản phẩm vào giỏ hàng
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetail.setQuantity(quantity);
        orderDetail.setPrice(product.getPrice() * quantity);

        orderDetailRepository.save(orderDetail);

        // Cập nhật tổng tiền đơn hàng
        double newTotal = order.getTotalAmount() + (product.getPrice() * quantity);
        order.setTotalAmount(newTotal);
        orderRepository.save(order);

        return true;
    }
}

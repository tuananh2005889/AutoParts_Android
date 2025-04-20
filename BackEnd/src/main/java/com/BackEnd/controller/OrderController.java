package com.BackEnd.controller;

//@RestController
//@RequestMapping("/api/order")
//@RequiredArgsConstructor
//public class OrderController {
//    private final OrderService orderService;
//
//    @PostMapping("/checkout")
//    public ResponseEntity<Order> checkout(@RequestParam String username,
//                                          @RequestParam String paymentMethod) {
//        Order order = orderService.checkout(username, paymentMethod);
//        return ResponseEntity.ok(order);
//    }
//}

//@RestController
//@RequestMapping("/cart")
//@CrossOrigin(origins = "*")
//public class OrderController {
//
//    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
//
//    @Autowired
//    private OrderService orderService;
//
//    // Tiêm UserRepository để lấy đối tượng User persistent
//    @Autowired
//    private UserRepository userRepository;
//
//    public OrderController(OrderService orderService, UserRepository userRepository) {
//        this.orderService = orderService;
//        this.userRepository = userRepository;
//    }
//
//    @PostMapping("/add")
//    public ResponseEntity<String> addToCart(@RequestBody Map<String, Object> payload) {
//        try {
//            String userName = String.valueOf(payload.get("userName"));
//            String productId = String.valueOf(payload.get("productId"));
//            int quantity = Integer.parseInt(payload.get("quantity").toString());
//
//            // Lấy đối tượng User đã persistent từ DB
//            Optional<User> userOpt = userRepository.findByUserName(userName);
//            if (!userOpt.isPresent()) {
//                logger.error("User with userName '{}' not found in DB", userName);
//                return ResponseEntity.badRequest().body("User not found");
//            }
//            User user = userOpt.get();
//
//            boolean added = orderService.addToCart(user, productId, quantity);
//            if (added) {
//                return ResponseEntity.ok("Product added to cart successfully");
//            } else {
//                logger.error("Failed to add product to cart for user '{}', productId '{}' and quantity '{}'", userName,
//                        productId, quantity);
//                return ResponseEntity.badRequest().body("Failed to add product to cart");
//            }
//        } catch (Exception e) {
//            logger.error("Error in addToCart controller: {}", e.getMessage(), e);
//            return ResponseEntity.status(500).body("Error: " + e.getMessage());
//        }
//    }
//
//    @GetMapping("/get")
//    public ResponseEntity<List<CartItem>> getCart(@RequestParam String userName) {
//        try {
//            Optional<User> userOpt = userRepository.findByUserName(userName);
//            if (!userOpt.isPresent()) {
//                return ResponseEntity.noContent().build();
//            }
//            User user = userOpt.get();
//            List<CartItem> cartItems = orderService.getCartByUser(user);
//            if (cartItems == null || cartItems.isEmpty()) {
//                return ResponseEntity.noContent().build();
//            }
//            return ResponseEntity.ok(cartItems);
//        } catch (Exception e) {
//            logger.error("Error in getCart controller for user '{}': {}", userName, e.getMessage(), e);
//            return ResponseEntity.status(500).build();
//        }
//    }
//}

# 🚗 Auto Parts Hub - Ứng dụng Mua Phụ Tùng Ô Tô

> Ứng dụng Android hiện đại giúp người dùng dễ dàng tìm kiếm và mua các phụ tùng ô tô chất lượng cao. Được xây dựng với mục tiêu tối ưu trải nghiệm người dùng và hiệu suất hệ thống.

![banner](https://your-image-link.com/banner.png) <!-- Có thể thay bằng ảnh UI app hoặc ảnh mockup -->

---

## 🛠️ Công nghệ sử dụng

### 💻 Frontend - Android App
- **Jetpack Compose**: UI hiện đại, code ngắn gọn, dễ bảo trì
- **Retrofit**: Kết nối API đơn giản, mạnh mẽ
- **Hilt**: Dependency Injection hiệu quả, tăng khả năng test và maintain
- **ViewModel + LiveData/StateFlow**: Quản lý vòng đời và dữ liệu reactive

### 🌐 Backend - Spring Boot
- **Spring Boot**: RESTful API nhanh chóng và chuẩn hóa
- **Spring Data JPA**: Tương tác cơ sở dữ liệu hiệu quả với Hibernate
- **Thymeleaf**: Template Engine để quản trị giao diện web nội bộ (admin)
- **Cloudinary**: Lưu trữ và xử lý hình ảnh đám mây

---

## ⚙️ Các tính năng chính

- 🔍 **Tìm kiếm phụ tùng** theo tên, hãng xe, loại sản phẩm
- 🛒 **Giỏ hàng**: thêm, xóa, cập nhật số lượng sản phẩm
- 💳 **Mua hàng dễ dàng**: giao diện thân thiện, đơn giản
- 📦 **Xem lịch sử đơn hàng** và trạng thái vận chuyển
- 🖼️ **Hình ảnh phụ tùng** được lưu trữ và xử lý trên Cloudinary
- 👨‍💻 **Giao diện quản trị** (web admin) để thêm/sửa/xoá sản phẩm
- 🔐 **Xác thực người dùng** (JWT / Session-based Authentication)

---

## 🖼️ Giao diện ứng dụng

| Trang chủ | Chi tiết sản phẩm | Giỏ hàng |
|----------|-------------------|----------|
| ![home](https://your-link.com/home.png) | ![detail](https://your-link.com/detail.png) | ![cart](https://your-link.com/cart.png) |

---

## 🚀 Cách chạy ứng dụng

### 1. Backend (Spring Boot)
```bash
cd backend
./mvnw spring-boot:run



## API cho Web admin và Android App

### Web
/web/product/all
/web/product/add
...
### App
/app/product/all
/app/cart/active/
/app/cart/add
...


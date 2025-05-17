import { useEffect, useState } from "react";
import axios from "axios";

const OrderList = () => {
    const [orders, setOrders] = useState([]);
    const [status, setStatus] = useState("PENDING"); // mặc định là PENDING
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const fetchOrders = async (selectedStatus) => {
        try {
            setLoading(true);
            setError("");
            const response = await axios.get("http://localhost:8080/app/order/get-orders-by-status", {
                params: { status: selectedStatus },
            });
            setOrders(Array.isArray(response.data) ? response.data : []);
        } catch (err) {
            console.error("Lỗi khi gọi API:", err);
            setError("Không thể tải danh sách đơn hàng.");
            setOrders([]);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchOrders(status);
    }, [status]);

    return (
        <div className="max-w-3xl mx-auto p-6 bg-white rounded-lg shadow">
            <div className="mb-4">
                <label htmlFor="status" className="font-semibold mr-2">
                    Trạng thái đơn hàng:
                </label>
                <select
                    id="status"
                    className="border px-3 py-1 rounded"
                    value={status}
                    onChange={(e) => setStatus(e.target.value)}
                >
                    <option value="PENDING">PENDING</option>
                    <option value="SUBMITTED">SUBMITTED</option>
                    <option value="SHIPPED">SHIPPED</option>
                    <option value="DELIVERED">DELIVERED</option>
                    <option value="CANCELLED">CANCELLED</option>
                </select>
            </div>

            {loading ? (
                <p>Đang tải...</p>
            ) : error ? (
                <p className="text-red-500">{error}</p>
            ) : orders.length === 0 ? (
                <p>Không có đơn hàng nào với trạng thái này.</p>
            ) : (
                <ul className="space-y-3">
                    {orders.map((order) => (
                        <li key={order.orderId} className="p-4 bg-gray-100 rounded shadow-sm">
                            <p><strong>Mã đơn:</strong> {order.orderCode || order.orderId}</p>
                            <p><strong>Khách hàng:</strong> {order.user?.username || "N/A"}</p>
                            <p><strong>Trạng thái:</strong> {order.status}</p>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default OrderList;

import { useEffect, useState } from "react";
import axios from "axios";
import OrderFetcher from "./OrderFetcher";
const statusTabs = ["All", "PENDING", "PAID", "CANCELLED", "SUBMITTED", "SHIPPED", "DELIVERED"];

export default function OrdersTable() {
	const [orders, setOrders] = useState([]);
	const [loading, setLoading] = useState(false);
	const [status, setStatus] = useState("All");

	useEffect(() => {
		const fetchOrders = async () => {
			setLoading(true);
			try {
				let res;
				if (status === "All") {
					res = await axios.get("http://localhost:8080/app/order/get-all-orders");
				} else {
					res = await axios.get(`http://localhost:8080/app/order/get-orders-by-status?status=${status}`);
				}
				console.log("Kết quả từ API:", res.data);
				setOrders(res.data)
				// setOrders(Array.isArray(res.data) ? res.data : []);
			} catch (error) {
				console.error("Failed to fetch orders", error);
				setOrders([]);
			}
			setLoading(false);
		};
		fetchOrders();
	}, [status]);

	useEffect(() => {
		console.log("Orders state đã được cập nhật:", orders);
	}, [orders]);

	return (

		<div className="p-4 bg-gray-900 text-white rounded shadow">
			{/* Tabs */}
			<div className="flex space-x-4 mb-4">
				{statusTabs.map((s) => (
					<button
						key={s}
						onClick={() => setStatus(s)}
						className={`px-4 py-2 rounded ${status === s ? "bg-blue-600" : "bg-gray-700 hover:bg-gray-600"
							}`}
					>
						{s}
					</button>
				))}
			</div>

			{/* Loading */}
			{loading ? (
				<p>Loading orders...</p>
			) : (
				<table className="w-full text-sm">
					<thead className="bg-gray-800">
						<tr>
							<th className="px-4 py-2 text-left">Order ID</th>
							<th className="px-4 py-2 text-left">Customer</th>
							<th className="px-4 py-2 text-left">Total</th>
							<th className="px-4 py-2 text-left">Status</th>
							<th className="px-4 py-2 text-left">Date</th>
						</tr>
					</thead>
					<tbody>
						{orders.length === 0 ? (
							<tr>
								<td colSpan="5" className="text-center py-4 text-gray-400">
									No orders found.
								</td>
							</tr>
						) : (
							orders.map((order) => (
								<tr
									key={order.orderId}
									className="border-b border-gray-700 hover:bg-gray-800"
								>
									<td className="px-4 py-2">{order.orderCode || order.orderId}</td>
									<td className="px-4 py-2">{order.user?.username || "N/A"}</td>
									<td className="px-4 py-2">${order.totalPrice?.toFixed(2) || "0.00"}</td>
									<td className="px-4 py-2">{order.status}</td>
									<td className="px-4 py-2">
										{order.createdAt
											? new Date(order.createdAt).toLocaleString()
											: "N/A"}
									</td>
								</tr>
							))
						)}
					</tbody>
				</table>
			)}
		</div>
	);
}

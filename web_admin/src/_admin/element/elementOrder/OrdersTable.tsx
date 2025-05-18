import { useEffect, useState } from "react";
import axios from "axios";

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
				setOrders(res.data);
			} catch (error) {
				console.error("Failed to fetch orders", error);
				setOrders([]);
			}
			setLoading(false);
		};
		fetchOrders();
	}, [status]);

	useEffect(() => {
		console.log(orders)
	})



	const handleStatusChange = async (orderCode, newStatus) => {
		try {
			await axios.put(`http://localhost:8080/app/order/change-order-status`, null, {
				params: {
					orderCode: orderCode,
					status: newStatus,
				},
			});
			setOrders((prevOrders) =>
				prevOrders.map((order) =>
					order.orderCode === orderCode ? { ...order, status: newStatus } : order
				)
			);
		} catch (error) {
			console.error("Failed to update order status", error);
			alert("Cập nhật trạng thái thất bại");
		}
	};



	return (
		<div className="p-4 bg-gray-900 text-white rounded shadow">
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

			{loading ? (
				<p>Loading orders...</p>
			) : (
				<table className="w-full text-sm">
					<thead className="bg-gray-800">
						<tr>
							<th className="px-4 py-2 text-left">Order ID</th>
							<th className="px-4 py-2 text-left">Order Code</th>
							<th className="px-4 py-2 text-left">Customer</th>
							<th className="px-4 py-2 text-left">Total</th>
							<th className="px-4 py-2 text-left">Status</th>
							<th className="px-4 py-2 text-left">Date</th>
						</tr>
					</thead>
					<tbody>
						{orders.length === 0 ? (
							<tr>
								<td colSpan="6" className="text-center py-4 text-gray-400">
									No orders found.
								</td>
							</tr>
						) : (
							orders.map((order) => (
								<tr
									key={order.orderId}
									className="border-b border-gray-700 hover:bg-gray-800"
								>
									<td className="px-4 py-2">{order.orderId || "ID"}</td>
									<td className="px-4 py-2">{order.orderCode || "OrderCode"}</td>
									<td className="px-4 py-2">{order.userName || "N/A"}</td>
									<td className="px-4 py-2">
										${order.totalPrice?.toFixed(2) || "0.00"}
									</td>
									<td className="px-4 py-2">
										<select
											value={order.status}
											onChange={(e) => handleStatusChange(order.orderCode, e.target.value)}
											className="bg-gray-800 text-white border border-gray-600 rounded px-2 py-1"
										>
											{statusTabs
												.filter((s) => s !== "All")
												.map((statusOption) => (
													<option key={statusOption} value={statusOption}>
														{statusOption}
													</option>
												))}
										</select>
									</td>

									<td className="px-4 py-2">{order.createTime || "N/A"}</td>
								</tr>
							))
						)}
					</tbody>
				</table>

			)
			}
		</div >
	);
}

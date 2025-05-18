import { useEffect, useState } from "react";
import axios from "axios";
import { ChevronDown, ChevronUp, Loader, Search } from "lucide-react";

const statusTabs = ["All", "PENDING", "PAID", "CANCELLED", "SUBMITTED", "SHIPPED", "DELIVERED"];

interface Order {
  orderId: number;
  orderCode: string;
  userName: string;
  totalPrice: number;
  status: string;
  createTime: string;
}

export default function OrdersTable() {
  const [orders, setOrders] = useState<Order[]>([]);
  const [filteredOrders, setFilteredOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(false);
  const [status, setStatus] = useState("All");
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const ordersPerPage = 10;
  const [sortConfig, setSortConfig] = useState<{ key: keyof Order; direction: 'asc' | 'desc' } | null>(null);

  // Search logic
  const handleSearch = (term: string) => {
    setSearchTerm(term);
    const filtered = orders.filter(order =>
      Object.values(order).some(value =>
        String(value).toLowerCase().includes(term.toLowerCase())
      )
    );
    setFilteredOrders(filtered);
    setCurrentPage(1);
  };

  // Sorting logic
  const sortedOrders = [...filteredOrders].sort((a, b) => {
    if (!sortConfig) return 0;
    if (a[sortConfig.key] < b[sortConfig.key]) return sortConfig.direction === 'asc' ? -1 : 1;
    if (a[sortConfig.key] > b[sortConfig.key]) return sortConfig.direction === 'asc' ? 1 : -1;
    return 0;
  });

  // Pagination
  const indexOfLastOrder = currentPage * ordersPerPage;
  const indexOfFirstOrder = indexOfLastOrder - ordersPerPage;
  const currentOrders = sortedOrders.slice(indexOfFirstOrder, indexOfLastOrder);
  const totalPages = Math.ceil(sortedOrders.length / ordersPerPage);

  const handleSort = (key: keyof Order) => {
    let direction: 'asc' | 'desc' = 'asc';
    if (sortConfig?.key === key && sortConfig.direction === 'asc') {
      direction = 'desc';
    }
    setSortConfig({ key, direction });
  };

  useEffect(() => {
    const fetchOrders = async () => {
      setLoading(true);
      try {
        const res = status === "All" 
          ? await axios.get("http://localhost:8080/app/order/get-all-orders")
          : await axios.get(`http://localhost:8080/app/order/get-orders-by-status?status=${status}`);
        setOrders(res.data);
        setFilteredOrders(res.data);
      } catch (error) {
        console.error("Failed to fetch orders", error);
      }
      setLoading(false);
    };
    fetchOrders();
  }, [status]);

  const handleStatusChange = async (orderCode: string, newStatus: string) => {
    try {
      await axios.put(`http://localhost:8080/app/order/change-order-status`, null, {
        params: { orderCode, status: newStatus }
      });
      setOrders(prev => prev.map(order => 
        order.orderCode === orderCode ? { ...order, status: newStatus } : order
      ));
      setFilteredOrders(prev => prev.map(order => 
        order.orderCode === orderCode ? { ...order, status: newStatus } : order
      ));
    } catch (error) {
      console.error("Failed to update status", error);
      alert("Update failed");
    }
  };
  return (
    <div className="bg-white rounded-lg shadow-sm border border-gray-100 overflow-hidden">
      {/* Header và Search */}
      <div className="px-6 py-4 border-b border-gray-100 bg-gradient-to-r from-blue-50 to-indigo-50 flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h2 className="text-xl font-semibold text-gray-800">Order Management</h2>
          <p className="text-sm text-gray-600">{filteredOrders.length} orders found</p>
        </div>
        
        <div className="relative w-full sm:w-64">
          <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <Search className="h-5 w-5 text-indigo-400" />
          </div>
          <input
            type="text"
            placeholder="Search orders..."
            className="block w-full pl-10 pr-3 py-2 border border-gray-200 rounded-lg bg-white shadow-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 text-sm"
            value={searchTerm}
            onChange={(e) => handleSearch(e.target.value)}
          />
        </div>
      </div>

      {/* Status Filters */}
      <div className="px-6 py-3 bg-gray-50 border-b border-gray-100 flex flex-wrap gap-2">
        {statusTabs.map(tab => (
          <button
            key={tab}
            onClick={() => setStatus(tab)}
            className={`px-3 py-1 rounded-full text-sm font-medium transition-colors ${
              status === tab
                ? 'bg-indigo-600 text-white hover:bg-indigo-700'
                : 'bg-white border border-gray-200 text-gray-700 hover:bg-gray-50'
            }`}
          >
            {tab}
          </button>
        ))}
      </div>

      {/* Bảng */}
      <div className="overflow-x-auto">
        <table className="min-w-full divide-y divide-gray-100">
          <thead className="bg-gray-50">
            <tr>
              {['orderCode', 'userName', 'totalPrice', 'status', 'createTime'].map((key) => (
                <th
                  key={key}
                  className="px-6 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider cursor-pointer hover:bg-gray-100 transition-colors"
                  onClick={() => handleSort(key as keyof Order)}
                >
                  <div className="flex items-center">
                    {key === 'createTime' ? 'Created Time' : key.replace(/([A-Z])/g, ' $1').trim()}
                    {sortConfig?.key === key && (
                      sortConfig.direction === 'asc' 
                        ? <ChevronUp className="ml-1 h-4 w-4 text-indigo-500" />
                        : <ChevronDown className="ml-1 h-4 w-4 text-indigo-500" />
                    )}
                  </div>
                </th>
              ))}
            </tr>
          </thead>

          <tbody className="bg-white divide-y divide-gray-100">
            {/* Phần loading và no results giữ nguyên */}

            {currentOrders.map((order) => (
              <tr key={order.orderId} className="hover:bg-indigo-50 transition-colors">
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                  {order.orderCode}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {order.userName || 'N/A'}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-semibold text-indigo-600">
                  ${order.totalPrice?.toFixed(2)}
                </td>
				<td className="px-6 py-4 whitespace-nowrap">
					<select
						value={order.status}
						onChange={(e) => handleStatusChange(order.orderCode, e.target.value)}
						className={`block w-full px-3 py-1.5 border rounded-md shadow-sm text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-colors ${
						order.status === 'PAID' 
							? 'bg-green-100 border-green-200 text-green-800' 
							: order.status === 'PENDING' 
							? 'bg-yellow-100 border-yellow-200 text-yellow-800'
							: order.status === 'CANCELLED' 
							? 'bg-red-100 border-red-200 text-red-800'
							: order.status === 'SUBMITTED' 
							? 'bg-blue-100 border-blue-200 text-blue-800'
							: order.status === 'SHIPPED' 
							? 'bg-indigo-100 border-indigo-200 text-indigo-800'
							: order.status === 'DELIVERED' 
							? 'bg-purple-100 border-purple-200 text-purple-800'
							: 'bg-gray-100 border-gray-200 text-gray-700'
						}`}
					>
						{statusTabs
						.filter(s => s !== "All")
						.map((status) => (
							<option 
							key={status} 
							value={status}
							className={`${
								status === 'PAID' ? 'bg-green-100' :
								status === 'PENDING' ? 'bg-yellow-100' :
								status === 'CANCELLED' ? 'bg-red-100' :
								status === 'SUBMITTED' ? 'bg-blue-100' :
								status === 'SHIPPED' ? 'bg-indigo-100' :
								status === 'DELIVERED' ? 'bg-purple-100' :
								'bg-white'
							}`}
							>
							{status}
							</option>
						))}
					</select>
					</td>
					<td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
					<span className="px-2 py-1 bg-indigo-50 rounded-md text-indigo-700">
						{order.createTime || 'N/A'}
					</span>
					</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Pagination - Giống TableProduct */}
      {totalPages > 1 && (
        <div className="px-6 py-3 bg-gray-50 border-t border-gray-100 flex items-center justify-between">
          <div className="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
            <div className="text-sm text-gray-700">
              Showing <span className="font-medium">{indexOfFirstOrder + 1}</span> to{' '}
              <span className="font-medium">{Math.min(indexOfLastOrder, filteredOrders.length)}</span> of{' '}
              <span className="font-medium">{filteredOrders.length}</span> results
            </div>
            <nav className="relative z-0 inline-flex rounded-md shadow-sm -space-x-px">
              <button
                onClick={() => setCurrentPage(1)}
                disabled={currentPage === 1}
                className="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-200 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
              >
                «
              </button>
              <button
                onClick={() => setCurrentPage(p => Math.max(p - 1, 1))}
                disabled={currentPage === 1}
                className="relative inline-flex items-center px-2 py-2 border border-gray-200 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
              >
                ‹
              </button>
              {/* {Array.from({ length: Math.min(5, totalPages) }, (_, i) => {
                // Logic phân trang giống TableProduct
              })} */}
              <button
                onClick={() => setCurrentPage(p => Math.min(p + 1, totalPages))}
                disabled={currentPage === totalPages}
                className="relative inline-flex items-center px-2 py-2 border border-gray-200 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
              >
                ›
              </button>
              <button
                onClick={() => setCurrentPage(totalPages)}
                disabled={currentPage === totalPages}
                className="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-200 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
              >
                »
              </button>
            </nav>
          </div>
        </div>
      )}
    </div>
  );
}
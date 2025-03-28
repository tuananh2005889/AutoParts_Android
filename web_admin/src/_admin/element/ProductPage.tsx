import { motion } from "framer-motion";
import { AlertTriangle, Package, TrendingUp } from "lucide-react";
import Header from "./comon/Header";
import StatCard from "./comon/StatCard";
import SalesTable from "./elementSales/SalesTable";
import LineOverviewChart from "./elementOverview/LineOverviewChart";
import { useState } from "react";

const ProductsPage = () => {
	const [showForm, setShowForm] = useState(false);

	return (
		<div className="flex-1 overflow-auto relative z-10">
			<Header title="Products" />

			<main className="max-w-7xl mx-auto py-6 px-4 lg:px-8">
				{/* STATS */}
				<motion.div
					className="grid grid-cols-4 gap-5 sm:grid-cols-2 lg:grid-cols-4 mb-8"
					initial={{ opacity: 0, y: 20 }}
					animate={{ opacity: 1, y: 0 }}
					transition={{ duration: 1 }}
				>
					<StatCard name="Total Products" icon={Package} value={1234} color="#6366F1" />
					<StatCard name="Top Selling" icon={TrendingUp} value={89} color="#10B981" />
					<StatCard name="Low Stock" icon={AlertTriangle} value={23} color="#F59E0B" />

					<motion.div
						className="bg-gray-800 bg-opacity-50 backdrop-blur-md overflow-hidden shadow-lg rounded-xl border border-gray-700 cursor-pointer p-4 text-center"
						whileHover={{
							y: -5,
							boxShadow: "0 25px 50px -12px rgba(0, 0, 0, 0.5)",
						}}
						onClick={() => setShowForm(true)}
					>
						<Package className="mx-auto mb-2" />
						<p className="text-3xl font-semibold text-gray-100">Add product</p>
					</motion.div>
				</motion.div>

				<div className="grid grid-col-1 lg:grid-cols-2 gap-8">
					<SalesTable />
					<LineOverviewChart />
				</div>

				{/* FORM HIỂN THỊ KHI BẤM NÚT */}
				{showForm && (
					<div className="fixed inset-0 bg-black bg-opacity-70 flex items-center justify-center z-50">
						<div className="bg-white w-full max-w-4xl rounded-xl shadow-lg p-8 relative">
							<button
								className="absolute top-4 right-4 text-white bg-red-600 p-2 rounded-full hover:bg-red-800 transition"
								onClick={() => setShowForm(false)}
							>
								X
							</button>
							<h2 className="text-2xl font-semibold text-center mb-6">Add New Product</h2>
							<div className="grid grid-cols-2 gap-4">
								<input type="text" className="p-3 border rounded-lg" placeholder="Name product..." />
								<input type="text" className="p-3 border rounded-lg" placeholder="Brand..." />
								<input type="text" className="p-3 border rounded-lg" placeholder="Compatible vehicles..." />
								<input type="text" className="p-3 border rounded-lg" placeholder="Description..." />
								<input type="text" className="p-3 border rounded-lg" placeholder="Material..." />
								<input type="text" className="p-3 border rounded-lg" placeholder="Size..." />
								<input type="text" className="p-3 border rounded-lg" placeholder="Warranty..." />
								<input type="text" className="p-3 border rounded-lg" placeholder="Weight..." />
								<input type="text" className="p-3 border rounded-lg" placeholder="Year of manufacture..." />
								<input type="text" className="p-3 border rounded-lg" placeholder="Category..." />
								<input type="text" className="p-3 border rounded-lg" placeholder="Price..." />
								<input type="text" className="p-3 border rounded-lg" placeholder="Quantity..." />
							</div>
							<button className="w-full mt-6 bg-blue-600 text-white p-3 rounded-lg hover:bg-blue-700 transition">
								Add Product
							</button>
						</div>
					</div>
				)}
			</main>
		</div>
	);
};

export default ProductsPage;

import React, { useState } from "react";
import axios from "axios";
import { motion } from "framer-motion";
import { BarChart2 } from "lucide-react";
import Header from "./comon/Header";
import toast, { Toaster } from "react-hot-toast";
const API_ADD = "http://localhost:8080/product/add";

const ProductPage = () => {
	const [productsform, setProductsform] = useState(false);

	const handleForm = () => {
		setProductsform(!productsform);
	};

	const [items, setItems] = useState({
		name: "",
		brand: "",
		category: "",
		description: "",
		compatibleVehicles: "",
		yearOfManufacture: "",
		size: "",
		material: "",
		weight: "",
		image: "",
		discount: "",
		warranty: "",
		price: "",
		quantity: "",
	});
	const [message, setMessage] = useState("");
	const handleChange = (e) => {
		const { name, value } = e.target;
		setItems({ ...items, [name]: value });
	};

	const handleAddProduct = async (e: React.FormEvent<HTMLFormElement>) => {
		e.preventDefault();
		try {
			const res = await axios.post(API_ADD, items);
			console.log(res.data);
			setMessage(res.data);
			setItems({
				...items,
				name: "",
				brand: "",
				category: "",
				description: "",
				compatibleVehicles: "",
				yearOfManufacture: "",
				size: "",
				material: "",
				weight: "",
				image: "",
				discount: "",
				warranty: "",
				price: "",
				quantity: "",
			});
			handleForm();
			toast.success("Add Product Success")
		} catch (error: unknown) {
			if (axios.isAxiosError(error)) {
				toast.error(error.message);
			} else {
				toast.error("An error occurred");
			}
		}

	};

	return (
		<div className="flex-1 w-full relative z-10">
			<Header title="Product" />
			<div className="max-w-7xl  mx-auto py-6 px-4 lg:px-8 bg-gray-900">
			<Toaster position="top-center" reverseOrder={false} />
				<motion.div
					className="grid grid-cols-2 gap-5 sm:grid-cols-2 lg:grid-cols-4 mb-8"
					initial={{ opacity: 0, y: 20 }}
					animate={{ opacity: 1, y: 0 }}
					transition={{ duration: 1 }}
				>
					<motion.div
						className="bg-gray-800 bg-opacity-50 backdrop-blur-md overflow-hidden shadow-lg rounded-xl border border-gray-700"
						whileHover={{
							y: -5,
							boxShadow: "0 25px 50px -12px rgba(0, 0, 0, 0.5)",
						}}
					>
						<div className="px-4 py-5 sm:p-6">
							<span className="flex items-center text-sm font-medium text-gray-400">
								<BarChart2 size={20} className="mr-2" />
								Total Sales
							</span>
							<p className="mt-1 text-3xl font-semibold text-gray-100">
								$1000
							</p>
						</div>
					</motion.div>
					<motion.div
						className="bg-gray-800 bg-opacity-50 backdrop-blur-md overflow-hidden shadow-lg rounded-xl border border-gray-700"
						whileHover={{
							y: -5,
							boxShadow: "0 25px 50px -12px rgba(0, 0, 0, 0.5)",
						}}
					>
						<div className="px-4 py-5 sm:p-6">
							<span className="flex items-center text-sm font-medium text-gray-400">
								<BarChart2 size={20} className="mr-2" />
								New Users
							</span>
							<p className="mt-1 text-3xl font-semibold text-gray-100">
								1000
							</p>
						</div>
					</motion.div>
					<motion.div
						className="bg-gray-800 bg-opacity-50 backdrop-blur-md overflow-hidden shadow-lg rounded-xl border border-gray-700"
						whileHover={{
							y: -5,
							boxShadow: "0 25px 50px -12px rgba(0, 0, 0, 0.5)",
						}}
					>
						<div className="px-4 py-5 sm:p-6">
							<span className="flex items-center text-sm font-medium text-gray-400">
								<BarChart2 size={20} className="mr-2" />
								Products
							</span>
							<p className="mt-1 text-3xl font-semibold text-gray-100">
								1000
							</p>
						</div>
					</motion.div>
					<motion.div
						className="bg-gray-800 bg-opacity-50 backdrop-blur-md overflow-hidden shadow-lg rounded-xl border border-gray-700"
						whileHover={{
							y: -5,
							boxShadow: "0 25px 50px -12px rgba(0, 0, 0, 0.5)",
						}}
					>
						<div
							onClick={handleForm}
							className="px-4 py-5 sm:p-6 cursor-pointer"
						>
							<span className="flex items-center text-sm font-medium text-gray-400">
								<BarChart2 size={20} className="mr-2" />
							</span>
							<p className="mt-1 text-3xl font-semibold text-gray-100">
								Add products
							</p>
						</div>
					</motion.div>
				</motion.div>
			</div>
			{productsform && (
				<div className="fixed inset-0 bg-gray-950 bg-opacity-50 flex justify-center items-center z-50">
					
					<div className="bg-white rounded-2xl shadow-2xl p-6 w-[700px] max-h-[90vh] overflow-y-auto text-black">
						<h2 className="text-2xl font-semibold mb-4 text-center">
							Add New Product
						</h2>
						<form
							onSubmit={handleAddProduct}
							className="grid grid-cols-2 gap-4"
						>
							<input
								type="text"
								name="name"
								placeholder="Name"
								value={items.name}
								onChange={handleChange}
								className="input-style"
							/>
							<input
								type="text"
								name="brand"
								placeholder="Brand"
								value={items.brand}
								onChange={handleChange}
								className="input-style"
							/>
							<input
								type="text"
								name="category"
								placeholder="Category"
								value={items.category}
								onChange={handleChange}
								className="input-style"
							/>
							<input
								type="text"
								name="description"
								placeholder="Description"
								value={items.description}
								onChange={handleChange}
								className="input-style"
							/>
							<input
								type="text"
								name="compatibleVehicles"
								placeholder="Compatible Vehicles"
								value={items.compatibleVehicles}
								onChange={handleChange}
								className="input-style"
							/>
							<input
								type="number"
								name="yearOfManufacture"
								placeholder="Year of Manufacture"
								value={items.yearOfManufacture}
								onChange={handleChange}
								className="input-style"
							/>
							<input
								type="text"
								name="size"
								placeholder="Size"
								value={items.size}
								onChange={handleChange}
								className="input-style"
							/>
							<input
								type="text"
								name="material"
								placeholder="Material"
								value={items.material}
								onChange={handleChange}
								className="input-style"
							/>
							<input
								type="text"
								name="weight"
								placeholder="Weight"
								value={items.weight}
								onChange={handleChange}
								className="input-style"
							/>
							<input
								type="text"
								name="image"
								placeholder="Image URL"
								value={items.image}
								onChange={handleChange}
								className="input-style"
							/>
							<input
								type="number"
								name="discount"
								placeholder="Discount (%)"
								value={items.discount}
								onChange={handleChange}
								className="input-style"
							/>
							<input
								type="text"
								name="warranty"
								placeholder="Warranty"
								value={items.warranty}
								onChange={handleChange}
								className="input-style"
							/>
							<input
								type="number"
								name="price"
								placeholder="Price ($)"
								value={items.price}
								onChange={handleChange}
								className="input-style"
							/>
							<input
								type="number"
								name="quantity"
								placeholder="Quantity"
								value={items.quantity}
								onChange={handleChange}
								className="input-style"
							/>

							<div className="col-span-2 flex justify-between mt-6">
								<button
									type="button"
									onClick={handleForm}
									className="bg-gray-400 hover:bg-gray-500 text-white px-4 py-2 rounded-xl"
								>
									Cancel
								</button>
								<button
									type="submit"
									className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-xl"
								>
									Confirm
								</button>

							</div>
						</form>
					
					</div>
				</div>
			)}
		</div>
	);
};

export default ProductPage;

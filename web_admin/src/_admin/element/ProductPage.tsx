import React, { useState } from "react";
import axios from "axios";
import { motion } from "framer-motion";
import { BarChart2 } from "lucide-react";
import Header from "./comon/Header";
import toast, { Toaster } from "react-hot-toast";
import ProductGrid from "./Product/TableProduct";


const API_ADD = "http://localhost:8080/product/add";

const ProductPage = () => {
  const [productsform, setProductsform] = useState(false);
  const [loading, setLoading] = useState(false); 
  const [refresh, setRefresh] = useState(false); 

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
    images: [],
    discount: "",
    warranty: "",
    price: "",
    quantity: "",
  });


  const [selectedFiles, setSelectedFiles] = useState([]);
  const [message, setMessage] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setItems({ ...items, [name]: value });
  };

  // Khi chọn file, lưu file để preview (chưa upload)
  const handleFileSelect = (e) => {
    const file = e.target.files[0];
    if (!file) return;
    setSelectedFiles((prev) => [...prev, file]);
  };

  const handleRemoveFile = (index) => {
    setSelectedFiles((prev) => prev.filter((_, i) => i !== index));
  };

  // upload tất cả file ảnh đến backend (CloudinaryController) và trả về mảng URL
  const uploadFiles = async () => {
    try {
      const uploadPromises = selectedFiles.map((file) => {
        const formData = new FormData();
        formData.append("file", file);
        return axios
          .post("http://localhost:8080/upload/image", formData)
          .then((res) => res.data); // res.data là URL ảnh
      });
      const uploadedUrls = await Promise.all(uploadPromises);
      return uploadedUrls;
    } catch (err) {
      console.error(err);
      toast.error("Error uploading images");
      return [];
    }
  };
  

  const handleAddProduct = async (e) => {
    e.preventDefault();
    setLoading(true); 
    try {
      const uploadedImageUrls = await uploadFiles();
      const productData = {
        ...items,
        images: uploadedImageUrls, /
      };

      const res = await axios.post(API_ADD, productData);
      console.log(res.data);
      setMessage(res.data);

      // Reset lại state
      setItems({
        name: "",
        brand: "",
        category: "",
        description: "",
        compatibleVehicles: "",
        yearOfManufacture: "",
        size: "",
        material: "",
        weight: "",
        images: [],
        discount: "",
        warranty: "",
        price: "",
        quantity: "",
      });
      setSelectedFiles([]);
      handleForm();
      toast.success("Add Product Success");

      // Trigger refresh cho ProductGrid
      setRefresh((prev) => !prev);
    } catch (error) {
      if (axios.isAxiosError(error)) {
        toast.error(error.message);
      } else {
        toast.error("An error occurred");
      }
    } finally {
      setLoading(false); // tắt loading sau khi hoàn tất
    }
  };

  return (
    <div className="flex-1 w-full relative z-10">
      <Header title="Product" />
      <div className="max-w-7xl mx-auto py-6 px-4 lg:px-8 bg-gray-900">
        <Toaster position="top-center" reverseOrder={false} />

        {/* Phần thống kê */}
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
              <p className="mt-1 text-3xl font-semibold text-gray-100">$1000</p>
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
              <p className="mt-1 text-3xl font-semibold text-gray-100">1000</p>
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
              <p className="mt-1 text-3xl font-semibold text-gray-100">1000</p>
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

        {/* Hiển thị danh sách sản phẩm dạng grid */}
        <ProductGrid refresh={refresh} />
      </div>

      {/* Modal thêm sản phẩm */}
      {productsform && (
        <div className="fixed inset-0 bg-gray-950 bg-opacity-50 flex justify-center items-center z-50">
          <div className="bg-white rounded-2xl shadow-2xl p-6 w-[700px] max-h-[90vh] overflow-y-auto text-black">
            <h2 className="text-2xl font-semibold mb-4 text-center">
              Add New Product
            </h2>
            <form onSubmit={handleAddProduct} className="grid grid-cols-2 gap-4">
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

              {/* Phần chọn file và preview các file đã chọn */}
              <div className="col-span-2">
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Upload Images
                </label>
                <div className="flex gap-4 flex-wrap">
                  {selectedFiles.map((file, index) => {
                    const previewUrl = URL.createObjectURL(file);
                    return (
                      <div key={index} className="relative w-24 h-24">
                        <img
                          src={previewUrl}
                          alt={`uploaded-${index}`}
                          className="w-full h-full object-cover rounded-md border"
                        />
                        <button
                          type="button"
                          onClick={() => handleRemoveFile(index)}
                          className="absolute top-[-8px] right-[-8px] bg-red-500 text-white rounded-full w-6 h-6 text-sm flex items-center justify-center"
                        >
                          ×
                        </button>
                      </div>
                    );
                  })}
                </div>
                <div className="mt-4">
                  <input
                    type="file"
                    accept="image/*"
                    onChange={handleFileSelect}
                    className="text-sm"
                  />
                </div>
              </div>

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
                  disabled={loading}
                  className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-xl flex items-center justify-center"
                >
                  {loading ? (
                    <svg
                      className="animate-spin h-5 w-5 text-white"
                      xmlns="http://www.w3.org/2000/svg"
                      fill="none"
                      viewBox="0 0 24 24"
                    >
                      <circle
                        className="opacity-25"
                        cx="12"
                        cy="12"
                        r="10"
                        stroke="currentColor"
                        strokeWidth="4"
                      ></circle>
                      <path
                        className="opacity-75"
                        fill="currentColor"
                        d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"
                      ></path>
                    </svg>
                  ) : (
                    "Confirm"
                  )}
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

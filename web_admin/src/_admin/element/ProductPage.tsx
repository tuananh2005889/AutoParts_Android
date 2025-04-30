import React, { useState } from "react";
import axios from "axios";
import { motion } from "framer-motion";
import { BarChart2 } from "lucide-react";
import Header from "./comon/Header";
import toast, { Toaster } from "react-hot-toast";
import TableProduct from "./Product/TableProduct";

const API_ADD = "http://localhost:8080/app/product/add";

const ProductPage = () => {
  const [productsform, setProductsform] = useState(false);
  const [loading, setLoading] = useState(false);
  const [refresh, setRefresh] = useState(false);

  const handleForm = () => setProductsform(!productsform);

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
    discount: "",
    warranty: "",
    price: "",
    quantity: "",
  });

  const [selectedFiles, setSelectedFiles] = useState([]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setItems({ ...items, [name]: value });
  };

  const handleFileSelect = (e) => {
    const files = Array.from(e.target.files);
    setSelectedFiles((prev) => [...prev, ...files]);
  };

  const handleRemoveFile = (index) => {
    setSelectedFiles((prev) => prev.filter((_, i) => i !== index));
  };

  const uploadFiles = async () => {
    try {
      const uploadPromises = selectedFiles.map((file) => {
        const formData = new FormData();
        formData.append("file", file);
        return axios.post("http://localhost:8080/upload/image", formData).then((res) => res.data);
      });
      return await Promise.all(uploadPromises);
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
        product: items,
        imageUrls: uploadedImageUrls,
      };
      await axios.post(API_ADD, productData);
      toast.success("Product added successfully");
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
        discount: "",
        warranty: "",
        price: "",
        quantity: "",
      });
      setSelectedFiles([]);
      handleForm();
      setRefresh((prev) => !prev);
    } catch (error) {
      toast.error("Failed to add product");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex-1 w-full relative z-10">
      <Header title="Product" />
      <div className="max-w-7xl mx-auto py-6 px-4 lg:px-8 bg-gray-900">
        <Toaster position="top-center" reverseOrder={false} />
        <motion.div className="grid grid-cols-2 gap-5 sm:grid-cols-2 lg:grid-cols-4 mb-8" initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 1 }}>
          {["Total Sales", "New Users", "Products"].map((label, idx) => (
            <motion.div key={idx} className="bg-gray-800 bg-opacity-50 backdrop-blur-md shadow-lg rounded-xl border border-gray-700" whileHover={{ y: -5, boxShadow: "0 25px 50px -12px rgba(0, 0, 0, 0.5)" }}>
              <div className="px-4 py-5 sm:p-6">
                <span className="flex items-center text-sm font-medium text-gray-400">
                  <BarChart2 size={20} className="mr-2" />{label}
                </span>
                <p className="mt-1 text-3xl font-semibold text-gray-100">1000</p>
              </div>
            </motion.div>
          ))}
          <motion.div className="bg-gray-800 bg-opacity-50 backdrop-blur-md shadow-lg rounded-xl border border-gray-700" whileHover={{ y: -5, boxShadow: "0 25px 50px -12px rgba(0, 0, 0, 0.5)" }}>
            <div onClick={handleForm} className="px-4 py-5 sm:p-6 cursor-pointer">
              <span className="flex items-center text-sm font-medium text-gray-400">
                <BarChart2 size={20} className="mr-2" />
              </span>
              <p className="mt-1 text-3xl font-semibold text-gray-100">Add products</p>
            </div>
          </motion.div>
        </motion.div>
        <TableProduct refresh={refresh} />
      </div>

      {productsform && (
        <div className="fixed inset-0 bg-gray-950 bg-opacity-50 flex justify-center items-center z-50">
          <div className="bg-white rounded-2xl shadow-2xl p-6 w-[700px] max-h-[90vh] overflow-y-auto text-black">
            <h2 className="text-2xl font-semibold mb-4 text-center">Add New Product</h2>
            <form onSubmit={handleAddProduct} className="grid grid-cols-2 gap-4">
              {Object.entries(items).map(([key, value]) => (
                key !== "images" && (
                  <input
                    key={key}
                    type={key === "description" ? "text" : key === "price" || key === "quantity" || key === "yearOfManufacture" || key === "discount" ? "number" : "text"}
                    name={key}
                    placeholder={key.charAt(0).toUpperCase() + key.slice(1)}
                    value={value}
                    onChange={handleChange}
                    className="input-style"
                  />
                )
              ))}
              <div className="col-span-2">
                <label className="block text-sm font-medium text-gray-700 mb-2">Upload Images</label>
                <div className="flex gap-4 flex-wrap">
                  {selectedFiles.map((file, index) => (
                    <div key={index} className="relative w-24 h-24">
                      <img src={URL.createObjectURL(file)} alt={`uploaded-${index}`} className="w-full h-full object-cover rounded-md border" />
                      <button type="button" onClick={() => handleRemoveFile(index)} className="absolute top-[-8px] right-[-8px] bg-red-500 text-white rounded-full w-6 h-6 text-sm flex items-center justify-center">
                        ×
                      </button>
                    </div>
                  ))}
                </div>
                <div className="mt-4">
                  <input type="file" accept="image/*" multiple onChange={handleFileSelect} className="text-sm" />
                </div>
              </div>
              <div className="col-span-2 flex justify-between mt-6">
                <button type="button" onClick={handleForm} className="bg-gray-400 hover:bg-gray-500 text-white px-4 py-2 rounded-xl">Cancel</button>
                <button type="submit" disabled={loading} className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-xl flex items-center justify-center">
                  {loading ? <span className="animate-spin">⏳</span> : "Confirm"}
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

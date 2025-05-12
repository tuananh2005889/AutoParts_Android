import React, { useState } from "react";
import axios from "axios";
import { motion } from "framer-motion";
import { BarChart2, Trash2, Upload } from "lucide-react";
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
            <form onSubmit={handleAddProduct} className="p-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {Object.entries(items).map(([key, value]) => {
                  if (key === "category") {
                    return (
                      <div key={key} className="col-span-2">
                        <label className="block text-sm font-medium text-gray-700 mb-1">{key.charAt(0).toUpperCase() + key.slice(1)}</label>
                        <select
                          name={key}
                          value={value}
                          onChange={handleChange}
                          className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition"
                          required
                        >
                          <option value="">Select a category</option>
                          <option value="Interior">Interior</option>
                          <option value="Exterior">Exterior</option>
                          <option value="Safety equipment">Safety equipment</option>
                          <option value="Entertainment system">Entertainment system</option>
                          <option value="Accessory">Accessory</option>
                        </select>
                      </div>
                    );
                  }

                  if (key === "warranty") {
                    return (
                      <div key={key}>
                        <label className="block text-sm font-medium text-gray-700 mb-1">{key.charAt(0).toUpperCase() + key.slice(1)}</label>
                        <select
                          name={key}
                          value={value}
                          onChange={handleChange}
                          className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition"
                        >
                          <option value="">Select warranty</option>
                          <option value="3 tháng">3 months</option>
                          <option value="6 tháng">6 months</option>
                          <option value="1 năm">1 year</option>
                        </select>
                      </div>
                    );
                  }

                  return (
                    <div key={key}>
                      <label className="block text-sm font-medium text-gray-700 mb-1">{key.charAt(0).toUpperCase() + key.slice(1)}</label>
                      <input
                        type={
                          key === "description" ? "text"
                            : ["price", "quantity", "yearOfManufacture", "discount"].includes(key)
                              ? "number"
                              : "text"
                        }
                        name={key}
                        placeholder={`Enter ${key}`}
                        value={value}
                        onChange={handleChange}
                        className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 outline-none transition"
                        required={["name", "brand", "price", "quantity"].includes(key)}
                      />
                    </div>
                  );
                })}
              </div>

              {/* Image Upload Section */}
              <div className="mt-6">
                <label className="block text-sm font-medium text-gray-700 mb-2">Product Images (Max 5)</label>
                <div className="flex flex-wrap gap-4 mb-4">
                  {selectedFiles.map((file, index) => (
                    <div key={index} className="relative group">
                      <div className="w-24 h-24 rounded-lg overflow-hidden border border-gray-200">
                        <img 
                          src={URL.createObjectURL(file)} 
                          alt={`preview-${index}`} 
                          className="w-full h-full object-cover"
                        />
                      </div>
                      <button 
                        type="button" 
                        onClick={() => handleRemoveFile(index)}
                        className="absolute -top-2 -right-2 bg-red-500 text-white rounded-full p-1 opacity-0 group-hover:opacity-100 transition-opacity shadow-md"
                      >
                        <Trash2 size={16} />
                      </button>
                    </div>
                  ))}
                  
                  {selectedFiles.length < 5 && (
                    <label className="w-24 h-24 flex flex-col items-center justify-center border-2 border-dashed border-gray-300 rounded-lg cursor-pointer hover:border-blue-500 transition-colors">
                      <Upload size={24} className="text-gray-400 mb-1" />
                      <span className="text-xs text-gray-500 text-center">Upload</span>
                      <input 
                        type="file" 
                        accept="image/*" 
                        multiple 
                        onChange={handleFileSelect} 
                        className="hidden" 
                      />
                    </label>
                  )}
                </div>
                <p className="text-xs text-gray-500">Upload high-quality product images (JPEG, PNG)</p>
              </div>

              {/* Form Actions */}
              <div className="mt-8 flex justify-end space-x-3">
                <button 
                  type="button" 
                  onClick={handleForm}
                  className="px-4 py-2 border border-gray-300 rounded-lg text-gray-700 text-white bg-slate-600 hover:bg-red-700 duration-300 transition-all ease-in-out hover:text-white"
                >
                  Cancel
                </button>
                <button 
                  type="submit" 
                  disabled={loading}
                  className="px-6 py-2 border-gray-300 bg-slate-600 border text-white rounded-lg hover:text-white hover:bg-blue-500 transition-colors disabled:opacity-70 flex items-center justify-center min-w-24"
                >
                  {loading ? (
                    <svg className="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                      <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                      <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                    </svg>
                  ) : (
                    "Add Product"
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

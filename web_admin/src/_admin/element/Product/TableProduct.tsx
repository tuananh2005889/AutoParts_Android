import React, { useEffect, useState } from "react";
import { motion } from "framer-motion";
import axios from "axios";
import toast from "react-hot-toast";
import { Search, Trash2 } from "lucide-react";

const API_GET_ALL = "http://localhost:8080/app/product/all";

interface Product {
  productId: number;
  name: string;
  brand: string;
  category: string;
  description: string;
  compatibleVehicles: string;
  yearOfManufacture: number | string;
  size: number | string;
  material: string;
  weight: string;
  images: string[]; // danh sách URL ảnh
  discount: number;
  warranty: string;
  price: number;
  quantity: number;
}

interface ProductGridProps {
  refresh: boolean;
}

const ProductGrid = ({ refresh }: ProductGridProps) => {
  const [searchTerm, setSearchTerm] = useState("");
  const [products, setProducts] = useState<Product[]>([]);
  const [filteredProducts, setFilteredProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(false);

  const handleGetAllProducts = async () => {
    try {
      const res = await axios.get(API_GET_ALL);
      setProducts(res.data);
      setFilteredProducts(res.data);
    } catch (error) {
      if (axios.isAxiosError(error)) {
        toast.error(error.message);
      } else {
        toast.error("Error loading products");
      }
    }
  };

  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    const term = e.target.value.toLowerCase();
    setSearchTerm(term);
    const filtered = products.filter(
      (product) =>
        product.brand.toLowerCase().includes(term) ||
        product.name.toLowerCase().includes(term)
    );
    setFilteredProducts(filtered);
  };

  const handleDeleteProduct = async (productId: number) => {
    setLoading(true);
    try {
      await axios.delete(`http://localhost:8080/app/product/delete/${productId}`);
      toast.success("Delete product success!");
      await handleGetAllProducts();
    } catch (err) {
      if (axios.isAxiosError(err)) {
        toast.error(err.message);
      } else {
        toast.error("Delete failed!");
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    handleGetAllProducts();
  }, [refresh]);

  return (
    <motion.div
      className="bg-gray-800 bg-opacity-50 backdrop-blur-md p-8 rounded-xl"
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.2 }}
    >
      {/* Header & Search */}
      <div className="flex justify-between items-center mb-8">
        <h2 className="text-2xl font-bold text-white">Sản phẩm</h2>
        <div className="relative">
          <input
            type="text"
            placeholder="Tìm kiếm sản phẩm..."
            className="bg-gray-700 text-white placeholder-gray-400 rounded-lg pl-10 pr-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
            value={searchTerm}
            onChange={handleSearch}
          />
          <Search className="absolute left-3 top-2.5 text-gray-400" size={20} />
        </div>
      </div>

      {/* Grid Card */}
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
        {filteredProducts.map((product) => (
          <motion.div
            key={product.productId}
            className="relative bg-white rounded-lg shadow-lg overflow-hidden"
            whileHover={{ scale: 1.02 }}
          >
            {/* Delete Button */}
            <div
              className="absolute top-2 right-2 z-10 p-1 bg-red-600 rounded-full cursor-pointer hover:bg-red-700"
              onClick={() => handleDeleteProduct(product.productId)}
            >
              <Trash2 size={16} className="text-white" />
            </div>
            {/* Product Image */}
            <div className="w-full h-48 overflow-hidden">
              {product.images && product.images.length > 0 ? (
                <img
                  src={product.images[0]}
                  alt={product.name}
                  className="w-full h-full object-cover "
                />
              ) : (
                <div className="w-full h-full flex items-center justify-center bg-gray-200">
                  No Image
                </div>
              )}
            </div>
            {/* Product Details */}
            <div className="p-4">
              <h3 className="text-lg font-bold text-gray-800">{product.name}</h3>
              <p className="text-sm text-gray-600">{product.brand}</p>
              <p className="mt-2 text-xl text-blue-600 font-semibold">
                ${product.price}
              </p>
              <p className="mt-1 text-sm text-gray-600">
                Quantity: {product.quantity}
              </p>
            </div>
          </motion.div>
        ))}
      </div>
    </motion.div>
  );
};

export default ProductGrid;

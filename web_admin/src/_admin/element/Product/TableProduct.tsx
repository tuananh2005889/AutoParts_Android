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
  images: string[];
  discount: number;
  warranty: string;
  price: number;
  quantity: number;
}

interface ProductGridProps {
  refresh: boolean;
}

const TableProduct = ({ refresh }: ProductGridProps) => {
  const [searchTerm, setSearchTerm] = useState("");
  const [products, setProducts] = useState<Product[]>([]);
  const [filteredProducts, setFilteredProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(false);
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);

  const handleUpdateProduct = async (updateProduct: Product) => {
    try {
      await axios.put(
        `http://localhost:8080/app/product/update/${updateProduct.productId}`,
        updateProduct
      );
      toast.success("Update product success!");
      setEditingProduct(null);
      await handleGetAllProducts();
    } catch (err) {
      toast.error("Failed to update products!");
    }
  };

  const handleGetAllProducts = async () => {
    try {
      const res = await axios.get(API_GET_ALL);
      setProducts(res.data);
      setFilteredProducts(res.data);
    } catch (error) {
      toast.error("Failed to loading products");
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
      toast.success("Remove success!");
      await handleGetAllProducts();
    } catch (err) {
      toast.error("Remove Failed!");
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
      <div className="flex justify-between items-center mb-8">
        <h2 className="text-2xl font-bold text-white">Danh sách sản phẩm</h2>
        <div className="relative">
          <input
            type="text"
            placeholder="Tìm kiếm theo tên hoặc thương hiệu..."
            className="bg-gray-700 text-white placeholder-gray-400 rounded-lg pl-10 pr-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
            value={searchTerm}
            onChange={handleSearch}
          />
          <Search className="absolute left-3 top-2.5 text-gray-400" size={20} />
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
        {filteredProducts.map((product) => (
          <motion.div
            key={product.productId}
            className="relative bg-white rounded-lg shadow-md overflow-hidden hover:shadow-xl transition-shadow duration-300"
            whileHover={{ scale: 1.02 }}
          >
            <div
              className="absolute top-2 left-2 z-10 p-1 bg-blue-600 rounded-full cursor-pointer hover:bg-blue-700"
              onClick={() => setEditingProduct(product)}
              title="Chỉnh sửa"
            >
              ✏️
            </div>
            <div
              className="absolute top-2 right-2 z-10 p-1 bg-red-600 rounded-full cursor-pointer hover:bg-red-700"
              onClick={() => handleDeleteProduct(product.productId)}
              title="Xóa"
            >
              <Trash2 size={16} className="text-white" />
            </div>
            <div className="w-full h-48 overflow-hidden">
              {product.images && product.images.length > 0 ? (
                <img
                  src={product.images[0]}
                  alt={product.name}
                  className="w-full h-full object-cover"
                />
              ) : (
                <div className="w-full h-full flex items-center justify-center bg-gray-200 text-gray-500">
                  No Image
                </div>
              )}
            </div>
            <div className="p-4">
              <h3 className="text-lg font-bold text-gray-800 truncate">{product.name}</h3>
              <p className="text-sm text-gray-600">{product.brand}</p>
              <p className="mt-2 text-xl text-blue-600 font-semibold">
                ${product.price}
              </p>
              <p className="text-sm text-gray-500">SL: {product.quantity}</p>
            </div>
          </motion.div>
        ))}
      </div>

      {editingProduct && (
        <div className="fixed inset-0 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-2xl shadow-2xl w-[700px] max-h-[90vh] overflow-y-auto text-black">
            <h3 className="text-2xl font-semibold mb-4 text-center">Chỉnh sửa sản phẩm</h3>
            <div className="grid grid-cols-2 gap-4">
              {Object.entries(editingProduct).map(([key, value]) => (
                key !== "images" && key !== "productId" && (
                  <input
                    key={key}
                    name={key}
                    className="border border-gray-300 text-black rounded-md p-3 w-full text-sm"
                    placeholder={
                      {
                        name: "Name",
                        brand: "Brand",
                        category: "Category",
                        description: "Description",
                        compatibleVehicles: "Compatible Vehicles",
                        yearOfManufacture: "Year of manufacture",
                        size: "Size",
                        material: "Material",
                        weight: "Weight",
                        discount: "Discount",
                        warranty: "warranty",
                        price: "Price",
                        quantity: "Quantity"
                      }[key] || key
                    }
                    type={
                      ["price", "quantity", "discount", "yearOfManufacture"].includes(key)
                        ? "number"
                        : "text"
                    }
                    value={value as string | number}
                    onChange={(e) =>
                      setEditingProduct({
                        ...editingProduct,
                        [key]: [
                          "price",
                          "quantity",
                          "discount",
                          "yearOfManufacture"
                        ].includes(key)
                          ? parseFloat(e.target.value)
                          : e.target.value
                      })
                    }
                  />
                )
              ))}
            </div>
            <div className="flex justify-end gap-3 mt-6">
              <button
                className="bg-gray-400 hover:bg-gray-500 text-white px-4 py-2 rounded-lg"
                onClick={() => setEditingProduct(null)}
              >
                Huỷ
              </button>
              <button
                className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg"
                onClick={() => handleUpdateProduct(editingProduct)}
              >
                Lưu
              </button>
            </div>
          </div>
        </div>
      )}
    </motion.div>
  );
};

export default TableProduct;

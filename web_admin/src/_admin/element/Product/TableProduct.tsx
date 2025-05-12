import React, { useEffect, useState } from "react";
import { motion } from "framer-motion";
import axios from "axios";
import toast from "react-hot-toast";
import { Search, Trash2, Edit, X, Check, ChevronDown, ChevronUp } from "lucide-react";

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
  const [sortConfig, setSortConfig] = useState<{ key: keyof Product; direction: 'ascending' | 'descending' } | null>(null);
  const [expandedProduct, setExpandedProduct] = useState<number | null>(null);
  const [currentPage, setCurrentPage] = useState(1);
  const productsPerPage = 12;

  // Sort products
  const sortedProducts = [...filteredProducts].sort((a, b) => {
    if (!sortConfig) return 0;
    if (a[sortConfig.key] < b[sortConfig.key]) {
      return sortConfig.direction === 'ascending' ? -1 : 1;
    }
    if (a[sortConfig.key] > b[sortConfig.key]) {
      return sortConfig.direction === 'ascending' ? 1 : -1;
    }
    return 0;
  });

  // Pagination logic
  const indexOfLastProduct = currentPage * productsPerPage;
  const indexOfFirstProduct = indexOfLastProduct - productsPerPage;
  const currentProducts = sortedProducts.slice(indexOfFirstProduct, indexOfLastProduct);
  const totalPages = Math.ceil(sortedProducts.length / productsPerPage);

  const requestSort = (key: keyof Product) => {
    let direction: 'ascending' | 'descending' = 'ascending';
    if (sortConfig && sortConfig.key === key && sortConfig.direction === 'ascending') {
      direction = 'descending';
    }
    setSortConfig({ key, direction });
  };

  const handleUpdateProduct = async (updateProduct: Product) => {
    try {
      await axios.put(
        `http://localhost:8080/app/product/update/${updateProduct.productId}`,
        updateProduct
      );
      toast.success("Product updated successfully!");
      setEditingProduct(null);
      await handleGetAllProducts();
    } catch (err) {
      toast.error("Failed to update product!");
    }
  };

  const handleGetAllProducts = async () => {
    try {
      const res = await axios.get(API_GET_ALL);
      setProducts(res.data);
      setFilteredProducts(res.data);
    } catch (error) {
      toast.error("Failed to load products");
    }
  };

  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    const term = e.target.value.toLowerCase();
    setSearchTerm(term);
    const filtered = products.filter(
      (product) =>
        product.brand.toLowerCase().includes(term) ||
        product.name.toLowerCase().includes(term) ||
        product.category.toLowerCase().includes(term)
    );
    setFilteredProducts(filtered);
    setCurrentPage(1); // Reset to first page on new search
  };

  const handleDeleteProduct = async (productId: number) => {
    if (!window.confirm("Are you sure you want to delete this product?")) return;
    
    setLoading(true);
    try {
      await axios.delete(`http://localhost:8080/app/product/delete/${productId}`);
      toast.success("Product deleted successfully!");
      await handleGetAllProducts();
    } catch (err) {
      toast.error("Failed to delete product!");
    } finally {
      setLoading(false);
    }
  };

  const toggleExpandProduct = (productId: number) => {
    setExpandedProduct(expandedProduct === productId ? null : productId);
  };

  useEffect(() => {
    handleGetAllProducts();
  }, [refresh]);
// Updated color scheme for the TableProduct component
return (
  <div className="bg-white rounded-lg shadow-sm border border-gray-100 overflow-hidden">
    {/* Header and Search - Updated with richer colors */}
    <div className="px-6 py-4 border-b border-gray-100 bg-gradient-to-r from-blue-50 to-indigo-50 flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
      <div>
        <h2 className="text-xl font-semibold text-gray-800">Product Inventory</h2>
        <p className="text-sm text-gray-600">{filteredProducts.length} products found</p>
      </div>
      
      <div className="relative w-full sm:w-64">
        <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
          <Search className="h-5 w-5 text-indigo-400" />
        </div>
        <input
          type="text"
          placeholder="Search products..."
          className="block w-full pl-10 pr-3 py-2 border border-gray-200 rounded-lg bg-white shadow-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 text-sm"
          value={searchTerm}
          onChange={handleSearch}
        />
      </div>
    </div>

    {/* Category Filters - More vibrant colors */}
    <div className="px-6 py-3 bg-gray-50 border-b border-gray-100 flex flex-wrap gap-2">
      <button 
        className="px-3 py-1 text-xs rounded-full bg-indigo-100 text-indigo-800 hover:bg-indigo-200 transition-colors"
        onClick={() => setFilteredProducts(products)}
      >
        All
      </button>
      {Array.from(new Set(products.map(p => p.category))).map(category => (
        <button
          key={category}
          className="px-3 py-1 text-xs rounded-full bg-white border border-gray-200 text-gray-700 hover:bg-gray-50 transition-colors shadow-sm"
          onClick={() => setFilteredProducts(products.filter(p => p.category === category))}
        >
          {category}
        </button>
      ))}
    </div>

    {/* Table Header - More sophisticated colors */}
    <div className="overflow-x-auto">
      <table className="min-w-full divide-y divide-gray-100">
        <thead className="bg-gray-50">
          <tr>
            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider">
              Product
            </th>
            <th 
              scope="col" 
              className="px-6 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider cursor-pointer hover:bg-gray-100 transition-colors"
              onClick={() => requestSort('category')}
            >
              <div className="flex items-center">
                Category
                {sortConfig?.key === 'category' && (
                  sortConfig.direction === 'ascending' ? 
                    <ChevronUp className="ml-1 h-4 w-4 text-indigo-500" /> : 
                    <ChevronDown className="ml-1 h-4 w-4 text-indigo-500" />
                )}
              </div>
            </th>
            <th 
              scope="col" 
              className="px-6 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider cursor-pointer hover:bg-gray-100 transition-colors"
              onClick={() => requestSort('price')}
            >
              <div className="flex items-center">
                Price
                {sortConfig?.key === 'price' && (
                  sortConfig.direction === 'ascending' ? 
                    <ChevronUp className="ml-1 h-4 w-4 text-indigo-500" /> : 
                    <ChevronDown className="ml-1 h-4 w-4 text-indigo-500" />
                )}
              </div>
            </th>
            <th 
              scope="col" 
              className="px-6 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider cursor-pointer hover:bg-gray-100 transition-colors"
              onClick={() => requestSort('quantity')}
            >
              <div className="flex items-center">
                Stock
                {sortConfig?.key === 'quantity' && (
                  sortConfig.direction === 'ascending' ? 
                    <ChevronUp className="ml-1 h-4 w-4 text-indigo-500" /> : 
                    <ChevronDown className="ml-1 h-4 w-4 text-indigo-500" />
                )}
              </div>
            </th>
            <th scope="col" className="px-6 py-3 text-right text-xs font-medium text-gray-700 uppercase tracking-wider">
              Actions
            </th>
          </tr>
        </thead>

        {/* Table Body - Improved row colors */}
        <tbody className="bg-white divide-y divide-gray-100">
          {currentProducts.length > 0 ? (
            currentProducts.map((product) => (
              <React.Fragment key={product.productId}>
                <tr className="hover:bg-indigo-50 transition-colors">
                  {/* Product Cell */}
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center">
                      <div className="flex-shrink-0 h-10 w-10 rounded-md border border-gray-200 overflow-hidden">
                        {product.images && product.images.length > 0 ? (
                          <img className="h-10 w-10 object-cover" src={product.images[0]} alt={product.name} />
                        ) : (
                          <div className="h-10 w-10 bg-gray-100 flex items-center justify-center">
                            <span className="text-xs text-gray-400">No image</span>
                          </div>
                        )}
                      </div>
                      <div className="ml-4">
                        <div className="text-sm font-medium text-gray-900">{product.name}</div>
                        <div className="text-sm text-gray-500">{product.brand}</div>
                      </div>
                    </div>
                  </td>
                  
                  {/* Category Cell */}
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm text-gray-700">
                      <span className="px-2 py-1 rounded-full bg-indigo-100 text-indigo-800 text-xs">
                        {product.category}
                      </span>
                    </div>
                  </td>
                  
                  {/* Price Cell */}
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="text-sm font-semibold text-indigo-600">
                      ${product.price.toFixed(2)}
                      {product.discount > 0 && (
                        <span className="ml-2 text-xs text-green-600 bg-green-100 px-1.5 py-0.5 rounded-full">
                          {product.discount}% off
                        </span>
                      )}
                    </div>
                  </td>
                  
                  {/* Stock Cell */}
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-2.5 py-1 inline-flex text-xs leading-4 font-medium rounded-full ${
                      product.quantity > 10 ? 'bg-green-100 text-green-800' : 
                      product.quantity > 0 ? 'bg-amber-100 text-amber-800' : 'bg-red-100 text-red-800'
                    }`}>
                      {product.quantity} in stock
                    </span>
                  </td>
                  
                  {/* Actions Cell */}
                  <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    <div className="flex justify-end space-x-3">
                      <button
                        onClick={() => setEditingProduct(product)}
                        className="text-indigo-600 hover:text-indigo-900 transition-colors"
                        title="Edit"
                      >
                        <Edit className="h-5 w-5" />
                      </button>
                      <button
                        onClick={() => handleDeleteProduct(product.productId)}
                        className="text-red-600 hover:text-red-900 transition-colors"
                        title="Delete"
                      >
                        <Trash2 className="h-5 w-5" />
                      </button>
                      <button
                        onClick={() => toggleExpandProduct(product.productId)}
                        className="text-gray-600 hover:text-gray-900 transition-colors"
                        title={expandedProduct === product.productId ? "Collapse" : "Expand"}
                      >
                        {expandedProduct === product.productId ? (
                          <ChevronUp className="h-5 w-5" />
                        ) : (
                          <ChevronDown className="h-5 w-5" />
                        )}
                      </button>
                    </div>
                  </td>
                </tr>
                
                {/* Expanded Details */}
                {expandedProduct === product.productId && (
                  <tr>
                    <td colSpan={5} className="px-6 py-4 bg-gray-50">
                      <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
                        <div>
                          <h4 className="font-medium text-gray-800 mb-2">Description</h4>
                          <p className="text-gray-600">{product.description || 'No description available'}</p>
                        </div>
                        <div>
                          <h4 className="font-medium text-gray-800 mb-2">Details</h4>
                          <div className="space-y-2">
                            <p><span className="font-medium text-gray-700">Compatible Vehicles:</span> <span className="text-gray-600">{product.compatibleVehicles || 'N/A'}</span></p>
                            <p><span className="font-medium text-gray-700">Material:</span> <span className="text-gray-600">{product.material || 'N/A'}</span></p>
                            <p><span className="font-medium text-gray-700">Size:</span> <span className="text-gray-600">{product.size || 'N/A'}</span></p>
                            <p><span className="font-medium text-gray-700">Warranty:</span> <span className="text-gray-600">{product.warranty || 'No warranty'}</span></p>
                          </div>
                        </div>
                        {product.images && product.images.length > 0 && (
                          <div className="md:col-span-2">
                            <h4 className="font-medium text-gray-800 mb-2">Images</h4>
                            <div className="flex flex-wrap gap-3">
                              {product.images.map((image, index) => (
                                <div key={index} className="relative group">
                                  <img 
                                    src={image} 
                                    alt={`Product ${index + 1}`} 
                                    className="h-24 w-24 rounded-md object-cover border border-gray-200 hover:border-indigo-300 transition-colors"
                                  />
                                </div>
                              ))}
                            </div>
                          </div>
                        )}
                      </div>
                    </td>
                  </tr>
                )}
              </React.Fragment>
            ))
          ) : (
            <tr>
              <td colSpan={5} className="px-6 py-12 text-center">
                <div className="text-gray-500 flex flex-col items-center justify-center">
                  <Search className="h-8 w-8 mb-2 text-gray-400" />
                  <p className="text-sm">No products found</p>
                  <p className="text-xs mt-1 text-gray-400">Try adjusting your search or filters</p>
                </div>
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>

    {/* Pagination - Updated styling */}
    {totalPages > 1 && (
      <div className="px-6 py-3 bg-gray-50 border-t border-gray-100 flex items-center justify-between">
        <div className="flex-1 flex justify-between sm:hidden">
          <button
            onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
            disabled={currentPage === 1}
            className="relative inline-flex items-center px-4 py-2 border border-gray-200 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50"
          >
            Previous
          </button>
          <button
            onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
            disabled={currentPage === totalPages}
            className="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-200 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50"
          >
            Next
          </button>
        </div>
        <div className="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
          <div>
            <p className="text-sm text-gray-700">
              Showing <span className="font-medium">{indexOfFirstProduct + 1}</span> to{' '}
              <span className="font-medium">{Math.min(indexOfLastProduct, filteredProducts.length)}</span> of{' '}
              <span className="font-medium">{filteredProducts.length}</span> results
            </p>
          </div>
          <div>
            <nav className="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
              <button
                onClick={() => setCurrentPage(1)}
                disabled={currentPage === 1}
                className="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-200 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
              >
                <span className="sr-only">First</span>
                «
              </button>
              <button
                onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
                disabled={currentPage === 1}
                className="relative inline-flex items-center px-2 py-2 border border-gray-200 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
              >
                <span className="sr-only">Previous</span>
                ‹
              </button>
              {Array.from({ length: Math.min(5, totalPages) }, (_, i) => {
                let pageNum;
                if (totalPages <= 5) {
                  pageNum = i + 1;
                } else if (currentPage <= 3) {
                  pageNum = i + 1;
                } else if (currentPage >= totalPages - 2) {
                  pageNum = totalPages - 4 + i;
                } else {
                  pageNum = currentPage - 2 + i;
                }
                return (
                  <button
                    key={pageNum}
                    onClick={() => setCurrentPage(pageNum)}
                    className={`relative inline-flex items-center px-4 py-2 border text-sm font-medium ${
                      currentPage === pageNum
                        ? 'z-10 bg-indigo-50 border-indigo-500 text-indigo-600'
                        : 'bg-white border-gray-200 text-gray-700 hover:bg-gray-50'
                    }`}
                  >
                    {pageNum}
                  </button>
                );
              })}
              <button
                onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
                disabled={currentPage === totalPages}
                className="relative inline-flex items-center px-2 py-2 border border-gray-200 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
              >
                <span className="sr-only">Next</span>
                ›
              </button>
              <button
                onClick={() => setCurrentPage(totalPages)}
                disabled={currentPage === totalPages}
                className="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-200 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
              >
                <span className="sr-only">Last</span>
                »
              </button>
            </nav>
          </div>
        </div>
      </div>
    )}

    {/* Edit Product Modal - Updated colors */}
    {editingProduct && (
      <div className="fixed inset-0  bg-opacity-30 flex items-center justify-center z-50 p-4 backdrop-blur-sm">
        <motion.div 
          className="bg-white rounded-xl shadow-xl w-full max-w-2xl max-h-[90vh] overflow-y-auto border border-gray-200"
          initial={{ opacity: 0, scale: 0.95 }}
          animate={{ opacity: 1, scale: 1 }}
        >
          <div className="sticky top-0 bg-white z-10 border-b border-gray-200 px-6 py-4 flex justify-between items-center">
            <h3 className="text-xl font-semibold text-gray-800">Edit Product</h3>
            <button 
              onClick={() => setEditingProduct(null)}
              className="text-gray-400 hover:text-gray-500 transition-colors"
            >
              <X size={24} />
            </button>
          </div>
          
          <div className="p-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {Object.entries(editingProduct).map(([key, value]) => (
                key !== "images" && key !== "productId" && (
                  <div key={key} className="space-y-1">
                    <label className="block text-sm font-medium text-gray-700 capitalize">
                      {key.replace(/([A-Z])/g, ' $1').trim()}
                    </label>
                    {key === 'category' ? (
                      <select
                        name={key}
                        value={value as string}
                        onChange={(e) => setEditingProduct({...editingProduct, [key]: e.target.value})}
                        className="w-full px-3 py-2 border border-gray-200 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 text-sm"
                      >
                        <option value="Interior">Interior</option>
                        <option value="Exterior">Exterior</option>
                        <option value="Safety equipment">Safety equipment</option>
                        <option value="Entertainment system">Entertainment system</option>
                        <option value="Accessory">Accessory</option>
                      </select>
                    ) : key === 'warranty' ? (
                      <select
                        name={key}
                        value={value as string}
                        onChange={(e) => setEditingProduct({...editingProduct, [key]: e.target.value})}
                        className="w-full px-3 py-2 border border-gray-200 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 text-sm"
                      >
                        <option value="3 tháng">3 months</option>
                        <option value="6 tháng">6 months</option>
                        <option value="1 năm">1 year</option>
                        <option value="">No warranty</option>
                      </select>
                    ) : (
                      <input
                        type={["price", "quantity", "discount", "yearOfManufacture"].includes(key) ? "number" : "text"}
                        name={key}
                        value={value as string | number}
                        onChange={(e) => setEditingProduct({
                          ...editingProduct,
                          [key]: ["price", "quantity", "discount", "yearOfManufacture"].includes(key)
                            ? parseFloat(e.target.value) || 0
                            : e.target.value
                        })}
                        className="w-full px-3 py-2 border border-gray-200 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 text-sm"
                      />
                    )}
                  </div>
                )
              ))}
            </div>

            {/* Image Gallery */}
            {editingProduct.images && editingProduct.images.length > 0 && (
              <div className="mt-6">
                <h4 className="text-sm font-medium text-gray-700 mb-2">Product Images</h4>
                <div className="flex flex-wrap gap-3">
                  {editingProduct.images.map((image, index) => (
                    <div key={index} className="relative group">
                      <img 
                        src={image} 
                        alt={`Product ${index + 1}`} 
                        className="h-24 w-24 rounded-md object-cover border border-gray-200 hover:border-indigo-300 transition-colors"
                      />
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* Action Buttons */}
            <div className="mt-8 flex justify-end space-x-3">
              <button
                type="button"
                onClick={() => setEditingProduct(null)}
                className="px-4 py-2 border border-gray-200 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-colors"
              >
                Cancel
              </button>
              <button
                type="button"
                onClick={() => handleUpdateProduct(editingProduct)}
                className="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-colors"
              >
                Save Changes
              </button>
            </div>
          </div>
        </motion.div>
      </div>
    )}
  </div>
);
}

export default TableProduct;

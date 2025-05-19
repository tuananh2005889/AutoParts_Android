import { useState, useEffect } from "react";
import { motion } from "framer-motion";
import { Search, ChevronUp, ChevronDown, Loader, AlertTriangle } from "lucide-react";
import axios from "axios";
import { toast } from "react-hot-toast";

interface UserDTO {
  userId: number;
  fullName: string;
  gmail: string;
  phone: string;
}

interface User {
  id: number;
  name: string;
  email: string;
  phone: string;
}

const UsersTable = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [users, setUsers] = useState<User[]>([]);
  const [filteredUsers, setFilteredUsers] = useState<User[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [sortConfig, setSortConfig] = useState<{ key: keyof User; direction: 'asc' | 'desc' } | null>(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [userToDelete, setUserToDelete] = useState<User | null>(null);
  const usersPerPage = 10;

  // Fetch và map dữ liệu
  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const res = await axios.get<UserDTO[]>("http://localhost:8080/api/user/all");
        console.log('Raw API response:', res.data);
        
        const mapped: User[] = res.data.map((u) => ({
          id: u.userId,
          name: u.fullName,
          email: u.gmail,
          phone: u.phone || "N/A"
        }));
        console.log('Mapped user data:', mapped);
        
        setUsers(mapped);
        setFilteredUsers(mapped);
      } catch (error: unknown) {
        console.error('Error fetching users:', error);
        if (axios.isAxiosError(error)) {
          setError(error.response?.data?.message || error.message);
        } else if (error instanceof Error) {
          setError(error.message);
        } else {
          setError('An unknown error occurred');
        }
      } finally {
        setIsLoading(false);
      }
    };
    fetchUsers();
  }, []);

  // Sorting logic
  const sortedUsers = [...filteredUsers].sort((a, b) => {
    if (!sortConfig) return 0;
    if (a[sortConfig.key] < b[sortConfig.key]) return sortConfig.direction === 'asc' ? -1 : 1;
    if (a[sortConfig.key] > b[sortConfig.key]) return sortConfig.direction === 'asc' ? 1 : -1;
    return 0;
  });

  // Pagination
  const indexOfLastUser = currentPage * usersPerPage;
  const indexOfFirstUser = indexOfLastUser - usersPerPage;
  const currentUsers = sortedUsers.slice(indexOfFirstUser, indexOfLastUser);
  const totalPages = Math.ceil(sortedUsers.length / usersPerPage);

  const handleSearch = (term: string) => {
    setSearchTerm(term);
    setFilteredUsers(
      users.filter(
        (u) =>
          u.name.toLowerCase().includes(term.toLowerCase()) ||
          u.email.toLowerCase().includes(term.toLowerCase())
      )
    );
    setCurrentPage(1);
  };

  const handleSort = (key: keyof User) => {
    let direction: 'asc' | 'desc' = 'asc';
    if (sortConfig?.key === key && sortConfig.direction === 'asc') {
      direction = 'desc';
    }
    setSortConfig({ key, direction });
  };

  const handleDeleteClick = (user: User) => {
    setUserToDelete(user);
    setShowDeleteModal(true);
  };

  const handleDeleteConfirm = async () => {
    if (!userToDelete) return;

    try {
      await axios.delete(`http://localhost:8080/api/user/${userToDelete.id}`);
      // Refresh user list after deletion
      const res = await axios.get<UserDTO[]>("http://localhost:8080/api/user/all");
      const mapped: User[] = res.data.map((u) => ({
        id: u.userId,
        name: u.fullName,
        email: u.gmail,
        phone: u.phone || "N/A"
      }));
      setUsers(mapped);
      setFilteredUsers(mapped);
      toast.success('User and all related data deleted successfully');
    } catch (error) {
      console.error('Error deleting user:', error);
      toast.error('Failed to delete user');
    } finally {
      setShowDeleteModal(false);
      setUserToDelete(null);
    }
  };

  if (isLoading) return (
    <div className="flex justify-center items-center h-64">
      <Loader className="animate-spin h-8 w-8 text-indigo-600" />
    </div>
  );

  if (error) return (
    <div className="p-4 bg-red-50 text-red-600 rounded-lg border border-red-100">
      Error: {error}
    </div>
  );

  return (
    <>
      <motion.div
        className="bg-white rounded-lg shadow-sm border border-gray-100 overflow-hidden"
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
      >
        {/* Header và Search */}
        <div className="px-6 py-4 border-b border-gray-100 bg-gradient-to-r from-blue-50 to-indigo-50 flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
          <div>
            <h2 className="text-xl font-semibold text-gray-800">User Management</h2>
            <p className="text-sm text-gray-600">{filteredUsers.length} users found</p>
          </div>
          
          <div className="relative w-full sm:w-64">
            <div className="absolute inset-y-0 left-0 pl-3 text-black flex items-center pointer-events-none">
              <Search className="h-5 w-5  text-black" />
            </div>
            <input
              type="text"
              placeholder="Search users..."
              className="block w-full pl-10 pr-3 py-2 border border-gray-200 rounded-lg bg-white shadow-sm focus:outline-none focus:ring-2 text-black focus:ring-indigo-500 focus:border-indigo-500 text-sm"
              value={searchTerm}
              onChange={(e) => handleSearch(e.target.value)}
            />
          </div>
        </div>

        {/* Bảng */}
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-100">
            <thead className="bg-gray-50">
              <tr>
                {['name', 'email', 'phone'].map((key) => (
                  <th
                    key={key}
                    className="px-6 py-3 text-left text-xs font-medium text-gray-700 uppercase tracking-wider cursor-pointer hover:bg-gray-100 transition-colors"
                    onClick={() => handleSort(key as keyof User)}
                  >
                    <div className="flex items-center">
                      {key.charAt(0).toUpperCase() + key.slice(1)}
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
              {currentUsers.map((user) => (
                <motion.tr
                  key={user.id}
                  className="hover:bg-indigo-50 transition-colors"
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                >
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center">
                      <div className="h-10 w-10 rounded-full bg-gradient-to-r from-indigo-400 to-blue-500 flex items-center justify-center text-white font-semibold">
                        {user.name?.charAt(0).toUpperCase() ?? ""}
                      </div>
                      <div className="ml-4">
                        <div className="text-sm font-medium text-gray-900">
                          {user.name}
                        </div>
                      </div>
                    </div>
                  </td>

                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {user.email}
                  </td>

                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {user.phone}
                  </td>

                  <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                    <button 
                      onClick={() => handleDeleteClick(user)}
                      className="text-red-600 hover:text-red-900 transition-colors"
                    >
                      Delete
                    </button>
                  </td>
                </motion.tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Pagination */}
        {totalPages > 1 && (
          <div className="px-6 py-3 bg-gray-50 border-t border-gray-100 flex items-center justify-between">
            <div className="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
              <div className="text-sm text-gray-700">
                Showing <span className="font-medium">{indexOfFirstUser + 1}</span> to{' '}
                <span className="font-medium">{Math.min(indexOfLastUser, filteredUsers.length)}</span> of{' '}
                <span className="font-medium">{filteredUsers.length}</span> results
              </div>
              <nav className="relative z-0 inline-flex rounded-md shadow-sm -space-x-px">
                <button
                  onClick={() => setCurrentPage(1)}
                  disabled={currentPage === 1}
                  className="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-200 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
                >
                  «
                </button>
                {[...Array(Math.min(5, totalPages))].map((_, i) => {
                  let pageNum;
                  if (totalPages <= 5) pageNum = i + 1;
                  else if (currentPage <= 3) pageNum = i + 1;
                  else if (currentPage >= totalPages - 2) pageNum = totalPages - 4 + i;
                  else pageNum = currentPage - 2 + i;
                  
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
                  onClick={() => setCurrentPage(p => Math.min(p + 1, totalPages))}
                  disabled={currentPage === totalPages}
                  className="relative inline-flex items-center px-2 py-2 border border-gray-200 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
                >
                  ›
                </button>
              </nav>
            </div>
          </div>
        )}
      </motion.div>

    
      {showDeleteModal && userToDelete && (
        <div className="fixed inset-0  flex shadow-lg shadow-black items-center justify-center">
          <div className="bg-white rounded-lg p-6 max-w-md w-full mx-4">
            <div className="flex items-center mb-4">
              <AlertTriangle className="h-6 w-6 text-red-500 mr-2" />
              <h3 className="text-lg font-semibold text-gray-900">Confirm Deletion</h3>
            </div>
            <p className="text-gray-600 mb-4">
              Are you sure you want to delete user "{userToDelete.name}"? This action will permanently delete:
            </p>
            <ul className="list-disc list-inside text-gray-600 mb-4">
              <li>User account</li>
              <li>All user's cart items</li>
              <li>All user's orders</li>
              <li>All related data</li>
            </ul>
            <p className="text-red-600 mb-4 font-medium">This action cannot be undone.</p>
            <div className="flex justify-end space-x-3">
              <button
                onClick={() => {
                  setShowDeleteModal(false);
                  setUserToDelete(null);
                }}
                className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
              >
                Cancel
              </button>
              <button
                onClick={handleDeleteConfirm}
                className="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700"
              >
                Delete
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default UsersTable;
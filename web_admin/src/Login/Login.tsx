import { useState } from "react";
import axios from "axios";
import { Navigate, useNavigate } from "react-router-dom";

const API_LOGIN = "http://localhost:8080/auth/login";
const API_SIGNUP = "http://localhost:8080/auth/signup";

const AuthForm = () => {
  const [isLogin, setIsLogin] = useState(true);
  const [message, setMessage] = useState("");

  const [loginData, setLoginData] = useState({
    userName: "",
    password: "",
  });

  const [signupData, setSignupData] = useState({
    gmail: "",
    userName: "",
    name: "",
    phone: "",
    address: "",
  });

  const handleLoginChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setLoginData({ ...loginData, [name]: value });
  };

  const handleSignupChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setSignupData({ ...signupData, [name]: value });
  };
  
  const navigate = useNavigate();
  const handleLogin = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      const res = await axios.post(API_LOGIN, loginData);
      setMessage(res.data);
	  navigate("/admin");
    } catch (error: unknown) {
      if (axios.isAxiosError(error)) {
        setMessage(error.response?.data || "An error occurred");
      } else {
        setMessage("Login failed! Something went wrong.");
      }
    }
  };

  const handleSignup = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    try {
      const res = await axios.post(API_SIGNUP, signupData);
      setMessage(res.data);
    } catch (error: unknown) {
      if (axios.isAxiosError(error)) {
        setMessage(error.response?.data || "An error occurred");
      } else {
        setMessage("Signup failed! Something went wrong.");
      }
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-r from-purple-800 via-gray-500 to-gray-900">
      <div className="bg-white shadow-xl rounded-lg p-8 max-w-md w-full">
        {isLogin ? (
          <>
            <h2 className="text-3xl font-bold text-center text-gray-800 mb-6">Login</h2>
            <form onSubmit={handleLogin}>
              <div className="mb-4">
                <label htmlFor="userName" className="block text-gray-700 font-medium mb-2">
                  Username
                </label>
                <input
                  type="text"
                  name="userName"
                  value={loginData.userName}
                  onChange={handleLoginChange}
                  className="w-full px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-purple-600"
                  placeholder="Enter your username"
                  required
                />
              </div>
              <div className="mb-6">
                <label htmlFor="password" className="block text-gray-700 font-medium mb-2">
                  Password
                </label>
                <input
                  type="password"
                  name="password"
                  value={loginData.password}
                  onChange={handleLoginChange}
                  className="w-full px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-purple-600"
                  placeholder="Enter your password"
                  required
                />
              </div>
              <button
                type="submit"
                className="w-full bg-purple-600 hover:bg-purple-700 text-white font-semibold py-2 px-4 rounded transition duration-200"
              >
                Login
              </button>
              <p className="mt-4 text-center cursor-pointer">
                Don't have an account?{" "}
                <span
                  className="text-purple-600 cursor-pointer font-medium"
                  onClick={() => {
                    setIsLogin(false);
                    setMessage("");
                  }}
                >
                  Sign up now
                </span>
              </p>
            </form>
          </>
        ) : (
          <>
            <h2 className="text-3xl font-bold text-center text-gray-800 mb-6">Sign Up</h2>
            <form onSubmit={handleSignup}>
              <div className="mb-4">
                <label htmlFor="gmail" className="block text-gray-700 font-medium mb-2">
                  Gmail
                </label>
                <input
                  type="email"
                  name="gmail"
                  value={signupData.gmail}
                  onChange={handleSignupChange}
                  className="w-full px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-purple-600"
                  placeholder="Enter your gmail"
                  required
                />
              </div>
              <div className="mb-4">
                <label htmlFor="userName" className="block text-gray-700 font-medium mb-2">
                  Username
                </label>
                <input
                  type="text"
                  name="userName"
                  value={signupData.userName}
                  onChange={handleSignupChange}
                  className="w-full px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-purple-600"
                  placeholder="Choose a username"
                  required
                />
              </div>
              <div className="mb-4">
                <label htmlFor="name" className="block text-gray-700 font-medium mb-2">
                  Name
                </label>
                <input
                  type="text"
                  name="name"
                  value={signupData.name}
                  onChange={handleSignupChange}
                  className="w-full px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-purple-600"
                  placeholder="Enter your full name"
                  required
                />
              </div>
              <div className="mb-4">
                <label htmlFor="phone" className="block text-gray-700 font-medium mb-2">
                  Phone Number
                </label>
                <input
                  type="tel"
                  name="phone"
                  value={signupData.phone}
                  onChange={handleSignupChange}
                  className="w-full px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-purple-600"
                  placeholder="Enter your phone number"
                  required
                />
              </div>
              <div className="mb-6">
                <label htmlFor="address" className="block text-gray-700 font-medium mb-2">
                  Address
                </label>
                <input
                  type="text"
                  name="address"
                  value={signupData.address}
                  onChange={handleSignupChange}
                  className="w-full px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-purple-600"
                  placeholder="Enter your address"
                  required
                />
              </div>
              <button
                type="submit"
                className="w-full bg-purple-600 hover:bg-purple-700 text-white font-semibold py-2 px-4 rounded transition duration-200"
              >
                Sign Up
              </button>
              <p className="mt-4 text-center">
                Already have an account?{" "}
                <span
                  className="text-purple-600 cursor-pointer font-medium"
                  onClick={() => {
                    setIsLogin(true);
                    setMessage("");
                  }}
                >
                  Login now
                </span>
              </p>
            </form>
          </>
        )}
        {message && (
          <p
            className={`mt-4 text-center font-medium ${
              message.includes("successful") ? "text-green-600" : "text-red-600"
            }`}
          >
            {message}
          </p>
        )}
      </div>
    </div>
  );
};

export default AuthForm;

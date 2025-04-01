import { useState } from "react";
import axios from "axios";

const API_LOGIN = "http://localhost:8080/auth/login";

const LoginForm = () => {
	const [loginData, setLoginData] = useState({
		userName: "",
		password: "",
	});	
	const [message, setMessage] = useState("");

	const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const { name, value } = e.target;
		setLoginData({ ...loginData, [name]: value });
	};

	const handleLogin = async (e: React.FormEvent<HTMLFormElement>) => {
		e.preventDefault();
		try {
			const res = await axios.post(API_LOGIN, loginData);
			setMessage(res.data); // Hiển thị thông báo từ backend
		} catch (error: unknown) {
			if (axios.isAxiosError(error)) {
				setMessage(error.response?.data || "An error occurred");
			} else {
				setMessage("Login failed! Something went wrong.");
			}
		}
	};
	
	return (
		<div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
			<form onSubmit={handleLogin} className="bg-white p-6 rounded shadow-md w-80">
				<h2 className="text-xl font-bold mb-4">Login</h2>
				<input
					type="text"
					name="userName"
					value={loginData.userName}
					onChange={handleChange}
					className="w-full p-2 border rounded mb-3"
					placeholder="Username"
					required
				/>
				<input
					type="password"
					name="password"
					value={loginData.password}
					onChange={handleChange}
					className="w-full p-2 border rounded mb-3"
					placeholder="Password"
					required
				/>
				<button type="submit" className="w-full bg-blue-500 text-white p-2 rounded">
					Login
				</button>
				{message && (
	<p className={`mt-2 ${message.includes("successful") ? "text-green-500" : "text-red-500"}`}>
		{message}
	</p>
)}

				{message && <p className="text-red-500 mt-2">{message}</p>}
			</form>
		</div>
	);
};

export default LoginForm;

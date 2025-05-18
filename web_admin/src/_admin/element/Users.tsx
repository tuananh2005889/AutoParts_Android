import { UserCheck, UserPlus, UsersIcon, UserX } from "lucide-react";
import { motion } from "framer-motion";
import { useState, useEffect } from "react";
import axios from "axios";
import Header from "./comon/Header";
import StatCard from "./comon/StatCard";
import UsersTable from "./elementUsers/UsersTable";

interface UserStats {
	totalUsers: number;
	newUsersToday: number;
	activeUsers: number;
	churnRate: string;
}

interface User {
	userId: number;
	fullName: string;
	gmail: string;
	status: string;
	createdAt: string;
}

const UsersPage = () => {
	const [userStats, setUserStats] = useState<UserStats>({
		totalUsers: 0,
		newUsersToday: 0,
		activeUsers: 0,
		churnRate: "0%",
	});
	const [isLoading, setIsLoading] = useState(true);
	const [error, setError] = useState<string | null>(null);

	useEffect(() => {
		const fetchUserStats = async () => {
			try {
				const response = await axios.get<User[]>("http://localhost:8080/api/user/all");
				const users = response.data;
				
				// Calculate statistics
				const totalUsers = users.length;
				const today = new Date();
				const newUsersToday = users.filter((user: User) => {
					const userDate = new Date(user.createdAt);
					return userDate.toDateString() === today.toDateString();
				}).length;
				
				const activeUsers = users.filter((user: User) => user.status === "Active").length;
				const churnRate = totalUsers > 0 
					? ((totalUsers - activeUsers) / totalUsers * 100).toFixed(1) + "%"
					: "0%";

				setUserStats({
					totalUsers,
					newUsersToday,
					activeUsers,
					churnRate,
				});
			} catch (err: unknown) {
				console.error(err);
				if (err instanceof Error) {
					setError(err.message);
				} else if (axios.isAxiosError(err)) {
					setError(err.response?.data?.message || err.message);
				} else {
					setError('An unknown error occurred');
				}
			} finally {
				setIsLoading(false);
			}
		};

		fetchUserStats();
	}, []);

	return (
		<div className='flex-1 overflow-auto relative z-10'>
			<Header title='Users' />

			<main className='max-w-7xl mx-auto py-6 px-4 lg:px-8'>
				{/* STATS */}
				<motion.div
					className='grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4 mb-8'
					initial={{ opacity: 0, y: 20 }}
					animate={{ opacity: 1, y: 0 }}
					transition={{ duration: 1 }}
				>
					<StatCard
						name='Total Users'
						icon={UsersIcon}
						value={isLoading ? "Loading..." : userStats.totalUsers.toLocaleString()}
						color='#6366F1'
					/>
					<StatCard 
						name='New Users Today' 
						icon={UserPlus} 
						value={isLoading ? "Loading..." : userStats.newUsersToday} 
						color='#10B981' 
					/>
					<StatCard
						name='Active Users'
						icon={UserCheck}
						value={isLoading ? "Loading..." : userStats.activeUsers.toLocaleString()}
						color='#F59E0B'
					/>
					<StatCard 
						name='Churn Rate' 
						icon={UserX} 
						value={isLoading ? "Loading..." : userStats.churnRate} 
						color='#EF4444' 
					/>
				</motion.div>

				{error && (
					<div className="p-4 bg-red-50 text-red-600 rounded-lg border border-red-100 mb-8">
						Error: {error}
					</div>
				)}

				<UsersTable />

				{/* USER CHARTS */}
				<div className='grid grid-cols-1 lg:grid-cols-2 gap-6 mt-8'>
					{/* <UserGrowthChart />
					<UserActivityHeatmap />
					<UserDemographicsChart /> */}
				</div>
			</main>
		</div>
	);
};

export default UsersPage;
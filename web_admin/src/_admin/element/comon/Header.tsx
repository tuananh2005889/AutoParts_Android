import { IoLogOut } from "react-icons/io5";
import { useNavigate } from "react-router-dom";

const Header = ({ title }) => {

	const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };
	
	return (
		<header className='bg-gray-800 bg-opacity-50 backdrop-blur-md shadow-lg border-b border-gray-700 flex'>
			<div className='max-w-7xl mx-auto py-4 px-4 sm:px-6 lg:px-8'>
				<h1 className='text-2xl font-semibold text-gray-100'>{title}</h1>
				
			</div>
			<div onClick={handleLogout} className="cursor-pointer flex p-6 justify-end">
			<IoLogOut className="scale-150 text-red-500"/>
				</div>
		</header>
	);
};
export default Header;
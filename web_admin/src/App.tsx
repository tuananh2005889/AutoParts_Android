import AdminBrandLayout from "./_admin/AdminBrandLayout"
import { Routes, Route } from "react-router-dom";
import Login from "./Login/Login";

function App() {

  return (
    <>
        <Routes>
          <Route path="/admin" element={<AdminBrandLayout />} />
          <Route path="/login" element={<Login/>}/>
        </Routes>
    </>
  )
}

export default App

import AdminBrandLayout from "./_admin/AdminBrandLayout"
import { Routes, Route } from "react-router-dom";

function App() {

  return (
    <>
        <Routes>
          <Route path="/admin" element={<AdminBrandLayout />} />
        </Routes>
    </>
  )
}

export default App

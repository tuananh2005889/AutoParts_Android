import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter } from "react-router-dom"; // Thêm dòng này
import "./index.css";
import "./output.css";
import App from "./App.tsx";
import ReactDOM from "react-dom/client";

ReactDOM.createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <BrowserRouter>  {/* Bọc App bên trong BrowserRouter */}
      <App />
    </BrowserRouter>
  </StrictMode>
);

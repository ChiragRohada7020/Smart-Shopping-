import { useState } from 'react'
import Home from './components/Home/Home'
import Login from "./components/Auth/Login"
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Register from './components/Auth/Register';
import { AuthProvider } from './components/Context/AuthContext';
import  ProtectedRoute from "./components/Context/ProtectedRoute"

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
    <AuthProvider>
   <Router>
      <Routes>
         <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />


<Route element={<ProtectedRoute />}>
       //ProtectedRoute will we here
        </Route>
      </Routes>
    </Router>
    </AuthProvider>
    </>
  )
}

export default App

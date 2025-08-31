import './App.css'
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom'
import NavBar from "./components/NavBar"
import Home from "./components/Home";


function App() {
  return (
    <Router>
      <NavBar></NavBar>
      <div className="container">
        <Routes>
          <Route path = "/home" element={<Home/>}/>
        </Routes>
      </div>
    </Router>
  );
}

export default App

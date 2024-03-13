import { Link } from "react-router-dom";
import FrontendRoutes from "../types/FrontendRoutes";

function Navbar() {
    return (
        <nav>
            <Link to={FrontendRoutes.HOME} >Home</Link>
            <Link to={FrontendRoutes.LOGIN}>Login</Link>
        </nav>
    );
}

export default Navbar;
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import ErrorIndicator from "../common/components/ErrorIndicator";
import ProtectedRoute from "../features/auth/ProtectedRoute";
import NewProduct from "../features/product/NewProduct";
import AdminLayout from "../layouts/AdminLayout";
import Categories from "../pages/Categories";
import Home from "../pages/Home";
import Login from "../pages/Login";
import Permissions from "../pages/Permissions";
import Products from "../pages/Products";
import Roles from "../pages/Roles";
import Units from "../pages/Units";
import Users from "../pages/Users";

const router = createBrowserRouter([
  {
    path: "/login",
    element: <Login />,
  },
  {
    element: (
      <ProtectedRoute>
        <AdminLayout />
      </ProtectedRoute>
    ),
    errorElement: <ErrorIndicator />,
    children: [
      {
        path: "/",
        index: true,
        element: <Home />,
      },
      {
        path: "/users",
        element: <Users />,
      },
      {
        path: "/roles",
        element: <Roles />,
      },
      {
        path: "/permissions",
        element: <Permissions />,
      },
      {
        path: "/categories",
        element: <Categories />,
      },
      {
        path: "/units",
        element: <Units />,
      },
      {
        path: "/products",
        children: [
          {
            path: "",
            index: true,
            element: <Products />,
          },
          {
            path: "new",
            element: <NewProduct />,
          },
        ],
      },
    ],
  },
]);

const AppRouter: React.FC = () => {
  return <RouterProvider router={router} />;
};

export default AppRouter;

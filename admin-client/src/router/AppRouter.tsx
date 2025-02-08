import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Login from "../pages/Login";
import ProtectedRoute from "../features/auth/ProtectedRoute";
import AdminLayout from "../layouts/AdminLayout";
import ErrorIndicator from "../common/components/ErrorIndicator";
import Home from "../pages/Home";
import Users from "../pages/Users";
import Roles from "../pages/Roles";
import Permissions from "../pages/Permissions";
import Categories from "../pages/Categories";

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
    ],
  },
]);

const AppRouter: React.FC = () => {
  return <RouterProvider router={router} />;
};

export default AppRouter;

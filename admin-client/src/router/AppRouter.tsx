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
import ViewProduct from "../features/product/ViewProduct";
import EditProduct from "../features/product/EditProduct";
import Suppliers from "../pages/Suppliers";
import EditProductSuppliers from "../features/product/EditProductSuppliers";
import Warehouses from "../pages/Warehouses";
import PurchaseOrders from "../pages/PurchaseOrders";
import NewPurchaseOrder from "../features/purchase-order/NewPurchaseOrder";
import ViewPurchaseOrder from "../features/purchase-order/ViewPurchaseOrder";
import ImportInvoices from "../pages/ImportInvoices";
import NewImportInvoice from "../features/import-invoice/NewImportInvoice";

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
          {
            path: ":id",
            element: <ViewProduct />,
          },
          {
            path: "update/:id",
            children: [
              {
                path: "",
                index: true,
                element: <EditProduct />,
              },
              {
                path: "suppliers",
                element: <EditProductSuppliers />,
              },
            ],
          },
        ],
      },
      {
        path: "/suppliers",
        element: <Suppliers />,
      },
      {
        path: "/warehouses",
        element: <Warehouses />,
      },
      {
        path: "/purchase-orders",
        children: [
          {
            path: "",
            index: true,
            element: <PurchaseOrders />,
          },
          {
            path: "new",
            element: <NewPurchaseOrder />,
          },
          {
            path: ":id",
            element: <ViewPurchaseOrder />,
          },
        ],
      },
      {
        path: "/import-invoices",
        children: [
          {
            path: "",
            index: true,
            element: <ImportInvoices />,
          },
          {
            path: "new",
            element: <NewImportInvoice />,
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

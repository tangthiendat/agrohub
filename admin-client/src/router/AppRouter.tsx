import { createBrowserRouter, RouterProvider } from "react-router-dom";
import ErrorIndicator from "../common/components/ErrorIndicator";
import ProtectedRoute from "../features/auth/ProtectedRoute";
import NewProduct from "../features/item/product/NewProduct";
import AdminLayout from "../layouts/AdminLayout";
import Categories from "../pages/Categories";
import Home from "../pages/Home";
import Login from "../pages/Login";
import Permissions from "../pages/Permissions";
import Products from "../pages/Products";
import Roles from "../pages/Roles";
import Units from "../pages/Units";
import Users from "../pages/Users";
import ViewProduct from "../features/item/product/ViewProduct";
import EditProduct from "../features/item/product/EditProduct";
import Suppliers from "../pages/Suppliers";
import EditProductSuppliers from "../features/item/product/EditProductSuppliers";
import Warehouses from "../pages/Warehouses";
import PurchaseOrders from "../pages/PurchaseOrders";
import NewPurchaseOrder from "../features/purchase/purchase-order/NewPurchaseOrder";
import ViewPurchaseOrder from "../features/purchase/purchase-order/ViewPurchaseOrder";
import ImportInvoices from "../pages/ImportInvoices";
import NewImportInvoice from "../features/purchase/import-invoice/NewImportInvoice";
import ProductLocations from "../pages/ProductLocations";
import ProductBatches from "../pages/ProductBatches";
import ProductStocks from "../pages/ProductStocks";
import SupplierDebt from "../features/debt/SupplierDebt";
import Customers from "../pages/Customers";
import ExportInvoices from "../pages/ExportInvoices";
import NewExportInvoice from "../features/sales/export-invoice/NewExportInvoice";

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
        children: [
          {
            path: "",
            index: true,
            element: <Suppliers />,
          },
          {
            path: ":id/debt",
            element: <SupplierDebt />,
          },
        ],
      },
      {
        path: "/customers",
        children: [
          {
            path: "",
            index: true,
            element: <Customers />,
          },
        ],
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
      {
        path: "/product-locations",
        element: <ProductLocations />,
      },
      {
        path: "/product-batches",
        element: <ProductBatches />,
      },
      {
        path: "/product-stocks",
        element: <ProductStocks />,
      },

      {
        path: "/export-invoices",
        children: [
          {
            path: "",
            index: true,
            element: <ExportInvoices />,
          },
          {
            path: "new",
            element: <NewExportInvoice />,
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

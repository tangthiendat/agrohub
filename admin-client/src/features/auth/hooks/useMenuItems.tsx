import { MenuProps } from "antd";
import { useEffect, useState } from "react";
import { NavLink } from "react-router";
import { MdCategory, MdDashboard, MdInventory } from "react-icons/md";
import { IoShieldCheckmark } from "react-icons/io5";
import { FaFileInvoice, FaKey, FaUserCog, FaUsers } from "react-icons/fa";
import { FaBuilding, FaList } from "react-icons/fa";
import { FiPackage } from "react-icons/fi";
import { FaWarehouse } from "react-icons/fa6";
import { BiPurchaseTag } from "react-icons/bi";
import { TbPackageImport } from "react-icons/tb";
import { IUser } from "../../../interfaces";
import { PERMISSIONS } from "../../../common/constants";
import { Module } from "../../../common/enums";

export function useMenuItems(user?: IUser): MenuProps["items"] {
  const [menuItems, setMenuItems] = useState<MenuProps["items"]>([]);

  useEffect(() => {
    if (user?.role.permissions) {
      const permissions = user.role.permissions;

      const viewUsers = permissions.find(
        (item) =>
          item.apiPath === PERMISSIONS[Module.USER].GET_PAGE.apiPath &&
          item.httpMethod === PERMISSIONS[Module.USER].GET_PAGE.httpMethod,
      );
      const viewRoles = permissions.find(
        (item) =>
          item.apiPath === PERMISSIONS[Module.ROLE].GET_PAGE.apiPath &&
          item.httpMethod === PERMISSIONS[Module.ROLE].GET_PAGE.httpMethod,
      );

      const viewPermissions = permissions.find(
        (item) =>
          item.apiPath === PERMISSIONS[Module.PERMISSION].GET_PAGE.apiPath &&
          item.httpMethod ===
            PERMISSIONS[Module.PERMISSION].GET_PAGE.httpMethod,
      );

      const hasAuthChildren: boolean = Boolean(
        viewUsers || viewRoles || viewPermissions,
      );

      const viewCategories = permissions.find(
        (item) =>
          item.apiPath === PERMISSIONS[Module.CATEGORY].GET_PAGE.apiPath &&
          item.httpMethod === PERMISSIONS[Module.CATEGORY].GET_PAGE.httpMethod,
      );

      const viewUnits = permissions.find(
        (item) =>
          item.apiPath === PERMISSIONS[Module.UNIT].GET_PAGE.apiPath &&
          item.httpMethod === PERMISSIONS[Module.UNIT].GET_PAGE.httpMethod,
      );

      const viewProducts = permissions.find(
        (item) =>
          item.apiPath === PERMISSIONS[Module.PRODUCT].GET_PAGE.apiPath &&
          item.httpMethod === PERMISSIONS[Module.PRODUCT].GET_PAGE.httpMethod,
      );

      const viewSuppliers = permissions.find(
        (item) =>
          item.apiPath === PERMISSIONS[Module.SUPPLIER].GET_PAGE.apiPath &&
          item.httpMethod === PERMISSIONS[Module.SUPPLIER].GET_PAGE.httpMethod,
      );

      const viewWarehouses = permissions.find(
        (item) =>
          item.apiPath === PERMISSIONS[Module.WAREHOUSE].GET_PAGE.apiPath &&
          item.httpMethod === PERMISSIONS[Module.WAREHOUSE].GET_PAGE.httpMethod,
      );

      const hasItemListChildren: boolean = Boolean(
        viewCategories ||
          viewUnits ||
          viewProducts ||
          viewSuppliers ||
          viewWarehouses,
      );

      const viewPurchaseOrders = permissions.find(
        (item) =>
          item.apiPath ===
            PERMISSIONS[Module.PURCHASE_ORDER].GET_PAGE.apiPath &&
          item.httpMethod ===
            PERMISSIONS[Module.PURCHASE_ORDER].GET_PAGE.httpMethod,
      );

      const viewImportInvoices = permissions.find(
        (item) =>
          item.apiPath ===
            PERMISSIONS[Module.IMPORT_INVOICE].GET_PAGE.apiPath &&
          item.httpMethod ===
            PERMISSIONS[Module.IMPORT_INVOICE].GET_PAGE.httpMethod,
      );

      const hasPurchaseItem = Boolean(viewPurchaseOrders || viewImportInvoices);

      const menuItems = [
        {
          label: (
            <NavLink className="" to="/">
              Trang chủ
            </NavLink>
          ),
          key: "dashboard",
          icon: <MdDashboard />,
        },
        ...(hasAuthChildren
          ? [
              {
                label: "Quản trị",
                key: "auth",
                icon: <IoShieldCheckmark />,
                children: [
                  ...(viewUsers
                    ? [
                        {
                          label: <NavLink to="/users">Người dùng</NavLink>,
                          key: "users",
                          icon: <FaUsers />,
                        },
                      ]
                    : []),
                  ...(viewRoles
                    ? [
                        {
                          label: <NavLink to="/roles">Vai trò</NavLink>,
                          key: "roles",
                          icon: <FaUserCog />,
                        },
                      ]
                    : []),
                  ...(viewPermissions
                    ? [
                        {
                          label: <NavLink to="/permissions">Quyền hạn</NavLink>,
                          key: "permissions",
                          icon: <FaKey />,
                        },
                      ]
                    : []),
                ],
              },
            ]
          : []),
        ...(hasItemListChildren
          ? [
              {
                label: "Danh mục",
                key: "itemList",
                icon: <FaList />,
                children: [
                  ...(viewCategories
                    ? [
                        {
                          label: (
                            <NavLink to="/categories">Loại sản phẩm</NavLink>
                          ),
                          key: "categories",
                          icon: <MdCategory />,
                        },
                      ]
                    : []),
                  ...(viewUnits
                    ? [
                        {
                          label: <NavLink to="/units">Đơn vị tính</NavLink>,
                          key: "units",
                          icon: <FiPackage />,
                        },
                      ]
                    : []),
                  ...(viewProducts
                    ? [
                        {
                          label: <NavLink to="/products">Sản phẩm</NavLink>,
                          key: "products",
                          icon: <MdInventory />,
                        },
                      ]
                    : []),
                  ...(viewSuppliers
                    ? [
                        {
                          label: (
                            <NavLink to="/suppliers">Nhà cung cấp</NavLink>
                          ),
                          key: "suppliers",
                          icon: <FaBuilding />,
                        },
                      ]
                    : []),
                  ...(viewWarehouses
                    ? [
                        {
                          label: <NavLink to="/warehouses">Kho hàng</NavLink>,
                          key: "warehouses",
                          icon: <FaWarehouse />,
                        },
                      ]
                    : []),
                ],
              },
            ]
          : []),
        ...(hasPurchaseItem
          ? [
              {
                label: "Mua hàng",
                key: "purchase",
                icon: <BiPurchaseTag />,
                children: [
                  ...(viewPurchaseOrders
                    ? [
                        {
                          label: (
                            <NavLink to="/purchase-orders">
                              Đơn đặt hàng
                            </NavLink>
                          ),
                          key: "purchase-orders",
                          icon: <FaFileInvoice />,
                        },
                      ]
                    : []),
                  ...(viewImportInvoices
                    ? [
                        {
                          label: (
                            <NavLink to="/import-invoices">
                              Phiếu nhập kho
                            </NavLink>
                          ),
                          key: "import-invoices",
                          icon: <TbPackageImport size={18} />,
                        },
                      ]
                    : []),
                ],
              },
            ]
          : []),
      ];
      setMenuItems(menuItems);
    }
  }, [user]);

  return menuItems;
}

import { MenuProps } from "antd";
import { useEffect, useState } from "react";
import { NavLink } from "react-router";
import { MdDashboard } from "react-icons/md";
import { IoShieldCheckmark } from "react-icons/io5";
import { FaKey, FaUserCog, FaUsers } from "react-icons/fa";
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
                label: "Xác thực",
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
      ];
      setMenuItems(menuItems);
    }
  }, [user]);

  return menuItems;
}

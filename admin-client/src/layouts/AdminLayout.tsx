import { useMutation, useQueryClient } from "@tanstack/react-query";
import { Button, Dropdown, Layout, Menu, MenuProps, theme } from "antd";
import { Header } from "antd/es/layout/layout";
import Sider from "antd/es/layout/Sider";
import { useState } from "react";
import { Outlet, useLocation, useNavigate } from "react-router";
import { AiOutlineMenuFold, AiOutlineMenuUnfold } from "react-icons/ai";
import { FaUserCircle } from "react-icons/fa";
import { MdLogout } from "react-icons/md";
import Loading from "../common/components/Loading";
import { useLoggedInUser } from "../common/hooks";
import { useMenuItems } from "../features/auth/hooks/useMenuItems";
import { authService } from "../services/auth/auth-service";

const AdminLayout: React.FC = () => {
  const location = useLocation();
  const [selectedKeys, setSelectedKeys] = useState<string[]>(
    location.pathname === "/"
      ? ["dashboard"]
      : location.pathname.slice(1).split("/"),
  );
  const [collapsed, setCollapsed] = useState<boolean>(false);
  const { user, isLoading } = useLoggedInUser();
  const menuItems: MenuProps["items"] = useMenuItems(user);
  const queryClient = useQueryClient();
  const navigate = useNavigate();
  const {
    token: { colorBgContainer },
  } = theme.useToken();

  const { mutate: logout } = useMutation({
    mutationFn: () => authService.logout(),
    onSuccess: () => {
      window.localStorage.removeItem("access_token");
      queryClient.removeQueries();
      navigate("/login");
    },
  });

  const items: MenuProps["items"] = [
    {
      key: "logout",
      icon: <MdLogout />,
      label: (
        <span onClick={() => logout()} className="px-1">
          Đăng xuất
        </span>
      ),
    },
  ];

  if (isLoading) {
    return <Loading />;
  }

  const siderStyle: React.CSSProperties = {
    overflow: "auto",
    height: "100vh",
    position: "fixed",
    insetInlineStart: 0,
    top: 0,
    bottom: 0,
    scrollbarWidth: "thin",
    scrollbarGutter: "stable",
    scrollbarColor: "black",
    boxShadow: "0 0 10px 1px rgba(0, 0, 0, 0.1)",
  };

  const headerStyle: React.CSSProperties = {
    background: colorBgContainer,
    padding: 0,
    zIndex: 1,
    overflow: "auto",
    position: "fixed",
    insetInlineStart: collapsed ? 80 : 230,
    top: 0,
    right: 0,
  };

  return (
    <Layout className="min-h-screen">
      <Sider
        style={siderStyle}
        width={230}
        trigger={null}
        collapsible
        collapsed={collapsed}
        theme="light"
      >
        {/* <div className="demo-logo-vertical flex flex-col items-center">
          <img src="/logo.png" alt="Logo" className="w-36" />
          
          {!collapsed && <h1 className="font-bold">Trang quản trị</h1>}
        </div> */}
        {!collapsed && (
          <div className="logo-font m-4 text-center text-[2rem] font-bold text-primary">
            AGROHUB
          </div>
        )}
        <Menu
          theme="light"
          mode="inline"
          selectedKeys={selectedKeys}
          items={menuItems}
          onClick={({ key }) => {
            setSelectedKeys([key]);
          }}
        />
      </Sider>
      <Layout
        className="transition-all duration-200"
        style={{ marginInlineStart: collapsed ? 80 : 230 }}
      >
        <Header
          style={headerStyle}
          className="shadow-md transition-all duration-200"
        >
          <div className="flex items-center justify-between">
            <Button
              type="text"
              className="ml-1"
              icon={collapsed ? <AiOutlineMenuUnfold /> : <AiOutlineMenuFold />}
              onClick={() => setCollapsed(!collapsed)}
              style={{
                fontSize: "20px",
              }}
            />
            <div className="relative mr-5 flex items-center gap-2">
              <Button
                type="text"
                icon={<FaUserCircle />}
                style={{
                  fontSize: "30px",
                }}
              />
              <Dropdown
                menu={{ items }}
                placement="bottom"
                overlayStyle={{
                  position: "absolute",
                  top: "50px",
                }}
              >
                <p className="cursor-pointer">
                  {user ? `${user.fullName}` : ""}
                </p>
              </Dropdown>
            </div>
          </div>
        </Header>

        <Layout.Content className="bg-accent">
          <div className="m-3 mt-[75px]">
            <Outlet />
          </div>
        </Layout.Content>
      </Layout>
    </Layout>
  );
};

export default AdminLayout;

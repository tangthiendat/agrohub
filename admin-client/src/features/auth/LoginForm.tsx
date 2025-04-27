import { LockOutlined, UserOutlined } from "@ant-design/icons";
import { useMutation } from "@tanstack/react-query";
import { Button, Form, Input } from "antd";
import { useEffect } from "react";
import toast from "react-hot-toast";
import { useNavigate } from "react-router";
import { ApiResponse, IAuthRequest, IAuthResponse } from "../../interfaces";
import { authService } from "../../services/auth/auth-service";
import { getNotificationMessage } from "../../utils/notification";

const LoginForm: React.FC = () => {
  const [loginForm] = Form.useForm<IAuthRequest>();
  const navigate = useNavigate();
  const accessToken = window.localStorage.getItem("access_token");
  const roleName = window.localStorage.getItem("role_name");

  useEffect(() => {
    if (accessToken) {
      if (roleName === "EMPLOYEE") {
        navigate("/customers");
      } else {
        navigate("/");
      }
    }
  }, [accessToken, navigate, roleName]);

  const { mutate: login } = useMutation({
    mutationFn: authService.login,
    onSuccess: (data: ApiResponse<IAuthResponse>) => {
      if (data.payload) {
        const { accessToken, roleName } = data.payload;
        window.localStorage.setItem("access_token", accessToken);
        window.localStorage.setItem("role_name", roleName);
        if (roleName === "EMPLOYEE") {
          navigate("/customers");
        } else {
          navigate("/");
        }
      }
    },
  });

  function onFinish(data: IAuthRequest) {
    login(data, {
      onSuccess: () => {
        toast.success("Đăng nhập thành công");
      },
      onError: (error: Error) => {
        toast.error(
          getNotificationMessage(error) ||
            "Có lỗi xảy ra. Vui lòng thử lại sau.",
        );
      },
    });
  }

  return (
    <>
      <Form
        className="flex flex-col"
        onFinish={onFinish}
        form={loginForm}
        layout="vertical"
        size="large"
      >
        <Form.Item
          label="Email"
          name="email"
          rules={[
            {
              required: true,
              message: "Vui lòng nhập email",
            },
            {
              type: "email",
              message: "Email không hợp lệ",
            },
          ]}
        >
          <Input prefix={<UserOutlined />} placeholder="Email" />
        </Form.Item>

        <Form.Item
          label="Mật khẩu"
          name="password"
          rules={[
            {
              required: true,
              message: "Vui lòng nhập mật khẩu",
            },
          ]}
        >
          <Input.Password prefix={<LockOutlined />} placeholder="Mật khẩu" />
        </Form.Item>

        <Form.Item>
          {/* <button
            type="submit"
            className="focus:shadow-outline mt-2 w-full rounded bg-blue-700 py-2 font-bold text-white hover:bg-blue-900 focus:outline-none"
          >
            Đăng nhập
          </button> */}
          <Button type="primary" htmlType="submit" block>
            Đăng nhập
          </Button>
        </Form.Item>

        <div className="flex flex-col gap-5 text-center text-xs">
          <a
            href="#"
            className="text-sm font-semibold text-primary hover:text-secondary"
          >
            Quên mật khẩu?
          </a>
        </div>
      </Form>
    </>
  );
};

export default LoginForm;

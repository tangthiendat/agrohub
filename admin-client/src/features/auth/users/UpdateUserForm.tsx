import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import {
  Button,
  DatePicker,
  Form,
  Input,
  Radio,
  Select,
  Space,
  Switch,
} from "antd";
import toast from "react-hot-toast";
import dayjs, { Dayjs } from "dayjs";
import { DatePickerProps } from "antd/lib";
import Loading from "../../../common/components/Loading";
import { GENDER_NAME } from "../../../common/constants";
import { IUser } from "../../../interfaces";
import { userService } from "../../../services";
import { roleService } from "../../../services/auth/role-service";
import { useEffect } from "react";

interface UpdateUserFormProps {
  userToUpdate?: IUser;
  onCancel: () => void;
}

interface UpdateUserArgs {
  userId: string;
  user: IUser;
}

const genderOptions = Object.entries(GENDER_NAME).map(
  ([genderValue, genderName]) => ({
    value: genderValue,
    label: genderName,
  }),
);

const UpdateUserForm: React.FC<UpdateUserFormProps> = ({
  userToUpdate,
  onCancel,
}) => {
  const [form] = Form.useForm<IUser>();
  const queryClient = useQueryClient();

  useEffect(() => {
    if (userToUpdate) {
      form.setFieldsValue({
        ...userToUpdate,
      });
    }
  }, [userToUpdate, form]);

  const { data: roleOptions, isLoading: isRolesLoading } = useQuery({
    queryKey: ["roles"],
    queryFn: roleService.getAll,
    select: (data) => {
      if (data.payload) {
        return data.payload.map((role) => ({
          value: role.roleId,
          label: role.roleName,
        }));
      }
    },
  });

  const { mutate: createUser, isPending: isCreating } = useMutation({
    mutationFn: userService.create,
    onSuccess: () => {
      queryClient.invalidateQueries({
        predicate: (query) => {
          return query.queryKey.includes("users");
        },
      });
    },
  });

  const { mutate: updateUser, isPending: isUpdating } = useMutation({
    mutationFn: ({ userId, user }: UpdateUserArgs) =>
      userService.update(userId, user),
    onSuccess: () => {
      queryClient.invalidateQueries({
        predicate: (query) => {
          return query.queryKey.includes("users");
        },
      });
    },
  });

  const disabledDate: DatePickerProps["disabledDate"] = (current) => {
    return current && dayjs(current).isAfter(dayjs().endOf("day"));
  };

  function handleFinish(values: IUser) {
    if (userToUpdate) {
      const updatedUser = { ...userToUpdate, ...values };
      updateUser(
        { userId: userToUpdate.userId, user: updatedUser },
        {
          onSuccess: () => {
            toast.success("Cập nhật người dùng thành công");
            onCancel();
            form.resetFields();
          },
          onError: () => {
            toast.error("Cập nhật người dùng thất bại");
          },
        },
      );
    } else {
      const newUser = values;
      console.log(newUser);
      createUser(newUser, {
        onSuccess: () => {
          toast.success("Thêm mới người dùng thành công");
          onCancel();
          form.resetFields();
        },
        onError: () => {
          toast.error("Thêm mới người dùng thất bại");
        },
      });
    }
  }

  if (isRolesLoading) {
    return <Loading />;
  }

  return (
    <Form
      layout="vertical"
      form={form}
      onFinish={handleFinish}
      initialValues={{ active: true }}
    >
      <div className="flex gap-8">
        <Form.Item
          className="flex-1"
          label="Họ và tên"
          name="fullName"
          rules={[
            {
              required: true,
              message: "Vui lòng nhập họ và tên",
            },
          ]}
        >
          <Input placeholder="Họ và tên" />
        </Form.Item>

        <Form.Item
          className="flex-1"
          label="Giới tính"
          name="gender"
          rules={[
            {
              required: true,
              message: "Vui lòng chọn giới tính",
            },
          ]}
        >
          <Radio.Group className="space-x-4" options={genderOptions} />
        </Form.Item>
      </div>
      <div className="flex gap-8">
        <Form.Item
          className="flex-1"
          label="Ngày sinh"
          name="dob"
          rules={[
            {
              required: true,
              message: "Ngày sinh không hợp lệ",
            },
          ]}
          getValueProps={(value: string) => ({
            value: value && dayjs(value),
          })}
          normalize={(value: Dayjs) => value && value.tz().format("YYYY-MM-DD")}
        >
          <DatePicker
            className="w-full"
            format="DD/MM/YYYY"
            disabledDate={disabledDate}
            placeholder="Chọn ngày sinh"
          />
        </Form.Item>
        <Form.Item
          className="flex-1"
          label="Trạng thái"
          name="active"
          valuePropName="checked"
        >
          <Switch checkedChildren="ACTIVE" unCheckedChildren="INACTIVE" />
        </Form.Item>
      </div>
      <div className="flex gap-8">
        <Form.Item
          className="flex-1"
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
          <Input placeholder="Email" />
        </Form.Item>

        <Form.Item
          className="flex-1"
          label="Số điện thoại"
          name="phoneNumber"
          rules={[
            {
              required: true,
              message: "Vui lòng nhập số điện thoại",
            },
            {
              pattern: /(84|0[3|5|7|8|9])+([0-9]{8})\b/,
              message: "Số điện thoại không hợp lệ",
            },
          ]}
        >
          <Input placeholder="Số điện thoại" />
        </Form.Item>
      </div>
      <div className="flex gap-8">
        <Form.Item
          className="flex-1"
          label="Vai trò"
          name={["role", "roleId"]}
          rules={[
            {
              required: true,
              message: "Vui lòng chọn vai trò",
            },
          ]}
        >
          <Select placeholder="Chọn vai trò" options={roleOptions} />
        </Form.Item>
        {!userToUpdate && (
          <Form.Item
            className="flex-1"
            label="Mật khẩu"
            name="password"
            rules={[
              {
                required: true,
                message: "Vui lòng nhập mật khẩu",
              },
              {
                min: 6,
                message: "Mật khẩu phải chứa ít nhất 6 ký tự",
              },
            ]}
          >
            <Input.Password placeholder="Mật khẩu" />
          </Form.Item>
        )}
      </div>

      <Form.Item className="text-right" wrapperCol={{ span: 24 }}>
        <Space>
          <Button onClick={onCancel} loading={isCreating || isUpdating}>
            Hủy
          </Button>

          <Button
            type="primary"
            htmlType="submit"
            loading={isCreating || isUpdating}
          >
            {userToUpdate ? "Cập nhật" : "Thêm mới"}
          </Button>
        </Space>
      </Form.Item>
    </Form>
  );
};

export default UpdateUserForm;

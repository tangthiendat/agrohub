import { Descriptions, DescriptionsProps, Space } from "antd";
import { useQuery } from "@tanstack/react-query";
import { useParams } from "react-router";
import Loading from "../../common/components/Loading";
import AddPayment from "./payment/AddPayment";
import BackButton from "../../common/components/BackButton";
import { supplierService } from "../../services";

const SupplierDebt: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const { data: supplier, isLoading } = useQuery({
    queryKey: ["suppliers", id],
    queryFn: () => supplierService.getById(id || ""),
    enabled: !!id,
    select: (data) => data.payload,
  });

  if (isLoading) {
    return <Loading />;
  }

  const items: DescriptionsProps["items"] = [
    {
      label: "Tên nhà cung cấp",
      key: "supplierName",
      children: supplier?.supplierName,
    },
    {
      label: "Số điện thoại",
      key: "phoneNumber",
      children: supplier?.phoneNumber,
    },
    {
      label: "Email",
      key: "email",
      children: supplier?.email,
    },
    {
      label: "Địa chỉ",
      key: "address",
      children: supplier?.address,
    },
  ];

  return (
    <div className="card">
      <div className="mb-5 flex items-center justify-between">
        <Space align="start" size="middle">
          <BackButton />
          <h2 className="text-xl font-semibold">Công nợ nhà cung cấp</h2>
        </Space>
        <AddPayment supplier={supplier!} />
      </div>
      <Descriptions items={items} />
    </div>
  );
};

export default SupplierDebt;

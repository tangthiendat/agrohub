import { useQuery } from "@tanstack/react-query";
import { useParams } from "react-router";
import { useTitle } from "../../common/hooks";
import { CustomerType } from "../../common/enums";
import { Descriptions, DescriptionsProps, Space } from "antd";
import BackButton from "../../common/components/BackButton";
import Access from "../auth/Access";
import { PERMISSIONS } from "../../common/constants";
import AddReceipt from "./receipt/AddReceipt";

const customer = {
  customerId: "8076750179",
  customerName: "Trần Văn Huy",
  customerType: CustomerType.INDIVIDUAL,
  phoneNumber: "0947232355",
  active: true,
};

const CustomerDebt: React.FC = () => {
  useTitle("Công nợ khách hàng");
  const { id } = useParams<{ id: string }>();

  // const { data: customer, isLoading } = useQuery({
  //   queryKey: ["customers", id],
  //   queryFn: () => customerService.getById(id || ""),
  //   enabled: !!id,
  //   select: (data) => data.payload,
  // });

  // if (isLoading) {
  //   return <Loading />;
  // }

  const items: DescriptionsProps["items"] = [
    {
      label: "Tên khách hàng",
      key: "customerName",
      children: customer?.customerName,
    },
    {
      label: "Số điện thoại",
      key: "phoneNumber",
      children: customer?.phoneNumber,
    },
    {
      label: "Loại khách hàng",
      key: "customerType",
      children:
        customer?.customerType === CustomerType.INDIVIDUAL
          ? "Cá nhân"
          : "Doanh nghiệp",
    },
  ];

  return (
    <div className="card">
      <div className="mb-5 flex items-center justify-between">
        <Space align="start" size="middle">
          <BackButton />
          <h2 className="text-xl font-semibold">Công nợ khách hàng</h2>
        </Space>
        {/* <Access permission={PERMISSIONS[Module.PAYMENT].CREATE}>
          
        </Access> */}
        <AddReceipt customer={customer!} />
      </div>
      <Descriptions items={items} />
    </div>
  );
};

export default CustomerDebt;

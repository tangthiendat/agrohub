import { useQuery } from "@tanstack/react-query";
import { useParams, useSearchParams } from "react-router";
import AddReceipt from "./receipt/AddReceipt";
import { useTitle } from "../../common/hooks";
import { CustomerType, Module } from "../../common/enums";
import { Descriptions, DescriptionsProps, Space } from "antd";
import BackButton from "../../common/components/BackButton";
import Loading from "../../common/components/Loading";
import Access from "../auth/Access";
import DebtAccountTable from "./DebtAccountTable";
import { CUSTOMER_TYPE_NAME, PERMISSIONS } from "../../common/constants";
import { customerService, debtAccountService } from "../../services";
import {
  PaginationParams,
  PartyDebtAccountFilterCriteria,
  SortParams,
} from "../../interfaces";

const CustomerDebt: React.FC = () => {
  useTitle("Công nợ khách hàng");
  const { id } = useParams<{ id: string }>();
  const [searchParams] = useSearchParams();

  const { data: customer, isLoading } = useQuery({
    queryKey: ["customers", id],
    queryFn: () => customerService.getById(id || ""),
    enabled: !!id,
    select: (data) => data.payload,
  });

  const pagination: PaginationParams = {
    page: Number(searchParams.get("page")) || 1,
    pageSize: Number(searchParams.get("pageSize")) || 10,
  };

  const sort: SortParams = {
    sortBy: searchParams.get("sortBy") || "",
    direction: searchParams.get("direction") || "",
  };

  const filter: PartyDebtAccountFilterCriteria = {
    debtStatus: searchParams.get("debtStatus") || undefined,
  };

  const { data: partyDebtAccount, isLoading: isCustomerDebtLoading } = useQuery(
    {
      queryKey: [
        "debt-accounts",
        "customer",
        id,
        pagination,
        sort,
        filter,
      ].filter((key) => {
        if (typeof key === "string") {
          return key !== "";
        } else if (key instanceof Object) {
          return Object.values(key).some(
            (value) => value !== undefined && value !== "",
          );
        }
      }),
      queryFn: () =>
        debtAccountService.getCustomerDebtAccount(
          id || "",
          pagination,
          sort,
          filter,
        ),
    },
  );

  if (isLoading || isCustomerDebtLoading) {
    return <Loading />;
  }

  const items: DescriptionsProps["items"] = [
    {
      label: "Tên khách hàng",
      key: "customerName",
      children: customer?.customerName,
    },
    {
      label: "Loại khách hàng",
      key: "customerType",
      children: CUSTOMER_TYPE_NAME[customer?.customerType as CustomerType],
    },
    {
      label: "Số điện thoại",
      key: "phoneNumber",
      children: customer?.phoneNumber,
    },
    ...(customer?.email
      ? [
          {
            label: "Email",
            key: "email",
            children: customer?.email,
          },
        ]
      : []),
    ...(customer?.address
      ? [
          {
            label: "Địa chỉ",
            key: "address",
            children: customer?.address,
          },
        ]
      : []),
  ];

  return (
    <div className="card">
      <div className="mb-5 flex items-center justify-between">
        <Space align="start" size="middle">
          <BackButton />
          <h2 className="text-xl font-semibold">Công nợ khách hàng</h2>
        </Space>
        <Access permission={PERMISSIONS[Module.RECEIPT].CREATE}>
          <AddReceipt customer={customer!} />
        </Access>
      </div>
      <Descriptions items={items} />
      <div className="mt-5">
        <DebtAccountTable
          partyDebtAccountPage={partyDebtAccount?.payload}
          isLoading={isCustomerDebtLoading}
        />
      </div>
    </div>
  );
};

export default CustomerDebt;

import { Descriptions, DescriptionsProps, Space } from "antd";
import { useQuery } from "@tanstack/react-query";
import { useNavigate, useParams, useSearchParams } from "react-router";
import Loading from "../../common/components/Loading";
import AddPayment from "./payment/AddPayment";
import BackButton from "../../common/components/BackButton";
import { supplierService } from "../../services";
import { useTitle } from "../../common/hooks";
import { PaginationParams, SortParams } from "../../interfaces";
import { debtAccountService } from "../../services/debt/debt-account-service";
import DebtAccountTable from "./DebtAccountTable";

const SupplierDebt: React.FC = () => {
  useTitle("Công nợ nhà cung cấp");
  const { id } = useParams<{ id: string }>();
  const [searchParams, setSearchParams] = useSearchParams();
  const navigate = useNavigate();

  const { data: supplier, isLoading } = useQuery({
    queryKey: ["suppliers", id],
    queryFn: () => supplierService.getById(id || ""),
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

  const { data: partyDebtAccount, isLoading: isSupplierDebtLoading } = useQuery(
    {
      queryKey: ["debt-accounts", "party", id, pagination, sort].filter(
        (key) => {
          if (typeof key === "string") {
            return key !== "";
          } else if (key instanceof Object) {
            return Object.values(key).some(
              (value) => value !== undefined && value !== "",
            );
          }
        },
      ),
      queryFn: () =>
        debtAccountService.getPartyDebtAccount(id || "", pagination, sort),
    },
  );

  if (isLoading || isSupplierDebtLoading) {
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
      <div className="mt-5">
        <DebtAccountTable
          partyDebtAccountPage={partyDebtAccount?.payload}
          isLoading={isSupplierDebtLoading}
        />
      </div>
    </div>
  );
};

export default SupplierDebt;

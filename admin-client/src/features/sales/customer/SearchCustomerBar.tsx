import { useCallback, useEffect, useState } from "react";
import { debounce } from "lodash";
import { useQuery } from "@tanstack/react-query";
import { ICustomer } from "../../../interfaces";
import { customerService } from "../../../services";
import { AutoComplete } from "antd";

interface SearchCustomerBarProps {
  className?: string;
  placeholder?: string;
  onClear?: () => void;
  onSelect: (customer: ICustomer) => void;
}

const SearchCustomerBar: React.FC<SearchCustomerBarProps> = ({
  className,
  onSelect,
  onClear,
  placeholder,
}) => {
  const [inputValue, setInputValue] = useState<string>("");
  const [searchQuery, setSearchQuery] = useState<string>("");
  const { data: searchCustomers } = useQuery({
    queryKey: ["customers", "search", searchQuery],
    queryFn: () => customerService.search(searchQuery),
    enabled: searchQuery !== "",
    select: (data) => data.payload,
  });

  const customerOptions = searchCustomers?.map((customer) => ({
    value: customer.customerId,
    label: (
      <div>
        <div className="text-wrap">{customer.customerName}</div>
        <div className="text-wrap text-xs text-gray-500">
          {customer.phoneNumber}
        </div>
      </div>
    ),
  }));

  useEffect(() => {
    if (inputValue === "") {
      onClear?.();
    }
  }, [inputValue, onClear]);

  function handleSearch(value: string) {
    setSearchQuery(value);
  }

  const debouncedHandleSearch = useCallback(debounce(handleSearch, 300), []);

  function handleSelect(value: string) {
    const selectedCustomer = searchCustomers?.find(
      (customer) => customer.customerId === value,
    );

    if (selectedCustomer) {
      onSelect(selectedCustomer);
      setInputValue(selectedCustomer.customerName);
      setSearchQuery("");
    }
  }

  return (
    <AutoComplete
      defaultActiveFirstOption
      value={inputValue}
      className={className}
      placeholder={placeholder}
      options={customerOptions}
      onSelect={handleSelect}
      allowClear
      onChange={setInputValue}
      onSearch={debouncedHandleSearch}
    />
  );
};

export default SearchCustomerBar;

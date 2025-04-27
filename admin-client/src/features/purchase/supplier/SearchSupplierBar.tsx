import { useCallback, useEffect, useState } from "react";
import { useQuery } from "@tanstack/react-query";
import { ISupplier } from "../../../interfaces";
import { supplierService } from "../../../services";
import { AutoComplete } from "antd";
import { debounce } from "lodash";

interface SearchSupplierBarProps {
  className?: string;
  placeholder?: string;
  onClear?: () => void;
  onSelect: (supplier: ISupplier) => void;
}

const SearchSupplierBar: React.FC<SearchSupplierBarProps> = ({
  className,
  onSelect,
  onClear,
  placeholder,
}) => {
  const [inputValue, setInputValue] = useState<string>("");
  const [searchQuery, setSearchQuery] = useState<string>("");
  const { data: searchSuppliers } = useQuery({
    queryKey: ["suppliers", "search", searchQuery],
    queryFn: () => supplierService.search(searchQuery),
    enabled: searchQuery !== "",
    select: (data) => data.payload,
  });

  const supplierOptions = searchSuppliers?.map((supplier) => ({
    value: supplier.supplierId,
    label: (
      <div>
        <div className="text-wrap">{supplier.supplierName}</div>
        <div className="text-wrap text-xs text-gray-500">
          {supplier.phoneNumber}
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
    const selectedSupplier = searchSuppliers?.find(
      (supplier) => supplier.supplierId === value,
    );

    if (selectedSupplier) {
      onSelect(selectedSupplier);
      setInputValue(selectedSupplier.supplierName);
      setSearchQuery("");
    }
  }

  return (
    <AutoComplete
      defaultActiveFirstOption
      value={inputValue}
      className={className}
      placeholder={placeholder}
      options={supplierOptions}
      onSelect={handleSelect}
      allowClear
      onChange={setInputValue}
      onSearch={debouncedHandleSearch}
    />
  );
};

export default SearchSupplierBar;

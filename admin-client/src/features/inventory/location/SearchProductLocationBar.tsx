import { useCallback, useEffect, useState } from "react";
import { IProductLocation } from "../../../interfaces";
import { productLocationService } from "../../../services";
import { useQuery } from "@tanstack/react-query";
import { debounce } from "lodash";
import { AutoComplete } from "antd";

interface SearchProductLocationBarProps {
  className?: string;
  placeholder?: string;
  onClear?: () => void;
  onSelect: (productLocation: IProductLocation) => void;
  optionRenderer: (productLocation: IProductLocation) => {
    value: string;
    label: React.ReactNode;
  };
}

const SearchProductLocationBar: React.FC<SearchProductLocationBarProps> = ({
  className,
  placeholder,
  onSelect,
  onClear,
  optionRenderer,
}) => {
  const [inputValue, setInputValue] = useState<string>("");
  const [searchQuery, setSearchQuery] = useState<string>("");
  const { data: searchProductLocations } = useQuery({
    queryKey: ["product-locations", "search", searchQuery],
    queryFn: () => productLocationService.search(searchQuery),
    enabled: searchQuery !== "",
    select: (data) => data.payload,
  });

  const productOptions = searchProductLocations?.map(optionRenderer);

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
    const selectedProductLocation = searchProductLocations?.find(
      (location) => location.locationId === value,
    );

    if (selectedProductLocation) {
      onSelect(selectedProductLocation);
      setInputValue("");
      setSearchQuery("");
    }
  }

  return (
    <AutoComplete
      defaultActiveFirstOption
      value={inputValue}
      className={className}
      placeholder={placeholder}
      options={productOptions}
      onSelect={handleSelect}
      allowClear
      onChange={setInputValue}
      onSearch={debouncedHandleSearch}
    />
  );
};

export default SearchProductLocationBar;

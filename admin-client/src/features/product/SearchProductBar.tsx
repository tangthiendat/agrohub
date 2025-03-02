import { useCallback, useEffect, useState } from "react";
import { IProduct } from "../../interfaces";
import { useQuery } from "@tanstack/react-query";
import { productService } from "../../services";
import { debounce } from "lodash";
import { AutoComplete } from "antd";

interface SearchProductBarProps {
  className?: string;
  placeholder?: string;
  onClear?: () => void;
  onSelect: (product: IProduct) => void;
  optionRenderer: (product: IProduct) => {
    value: string;
    label: React.ReactNode;
  };
}

const SearchProductBar: React.FC<SearchProductBarProps> = ({
  className,
  placeholder,
  onSelect,
  onClear,
  optionRenderer,
}) => {
  const [inputValue, setInputValue] = useState<string>("");
  const [searchQuery, setSearchQuery] = useState<string>("");
  const { data: searchProducts } = useQuery({
    queryKey: ["products", "search", searchQuery],
    queryFn: () => productService.search(searchQuery),
    enabled: searchQuery !== "",
    select: (data) => data.payload,
  });

  const productOptions = searchProducts?.map(optionRenderer);

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
    const selectedProduct = searchProducts?.find(
      (product) => product.productId === value,
    );

    if (selectedProduct) {
      onSelect(selectedProduct);
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

export default SearchProductBar;

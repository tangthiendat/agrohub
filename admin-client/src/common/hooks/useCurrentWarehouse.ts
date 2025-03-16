import { useQuery } from "@tanstack/react-query";
import { warehouseService } from "../../services";

export function useCurrentWarehouse() {
  const { data, isLoading } = useQuery({
    queryKey: ["warehouse", "me"],
    queryFn: warehouseService.getCurrentUserWarehouse,
  });
  return { currentWarehouse: data?.payload, isLoading };
}

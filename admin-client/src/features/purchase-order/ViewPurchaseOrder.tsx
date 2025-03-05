import { useQueries, useQuery } from "@tanstack/react-query";
import { useParams } from "react-router";
import {
  productService,
  purchaseOrderService,
  userService,
  warehouseService,
} from "../../services";
import { IProduct } from "../../interfaces";
import Loading from "../../common/components/Loading";

const ViewPurchaseOrder: React.FC = () => {
  const { id } = useParams();
  const { data: purchaseOrder, isLoading: isPurchaseOrderLoading } = useQuery({
    queryKey: ["purchase-orders", id],
    queryFn: () => purchaseOrderService.getById(id!),
    enabled: !!id,
    select: (data) => data.payload,
  });

  // const {} = useQuery({
  //   queryKey: ["user", purchaseOrder?.user.userId],
  //   queryFn: () => userService.getUserInfo(),
  // });
  const { data: warehouse, isLoading: isWarehouseLoading } = useQuery({
    queryKey: ["warehouses", purchaseOrder?.warehouse.warehouseId],
    queryFn: () => warehouseService.getCurrentUserWarehouse(),
    enabled: !!purchaseOrder,
    select: (data) => data.payload,
  });

  const { data: user, isLoading: isUserLoading } = useQuery({
    queryKey: ["users", purchaseOrder?.user.userId],
    queryFn: () => userService.getUserInfo(),
    enabled: !!purchaseOrder,
    select: (data) => data.payload,
  });

  const { productsMap, isProductLoading } = useQueries({
    queries:
      purchaseOrder?.purchaseOrderDetails.map((pod) => ({
        queryKey: ["products", pod.product.productId],
        queryFn: () => productService.getById(pod.product.productId),
        enabled: !!purchaseOrder,
      })) || [],
    combine: (results) => {
      const productsMap = results.reduce(
        (acc, result) => {
          if (result.data?.payload) {
            acc[result.data.payload.productId] = result.data.payload;
          }
          return acc;
        },
        {} as Record<string, IProduct>,
      );

      return {
        productsMap: productsMap,
        isProductLoading: results.some((result) => result.isLoading),
      };
    },
  });

  if (
    isPurchaseOrderLoading ||
    isProductLoading ||
    isUserLoading ||
    isWarehouseLoading
  ) {
    return <Loading />;
  }

  console.log(productsMap);

  return <div></div>;
};

export default ViewPurchaseOrder;

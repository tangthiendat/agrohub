import { useQuery } from "@tanstack/react-query";
import { userService } from "../../services";

export function useCurrentUserInfo() {
  const { data, isLoading } = useQuery({
    queryKey: ["user", "info"],
    queryFn: userService.getUserInfo,
  });
  return { currentUserInfo: data?.payload, isLoading };
}

import { useQuery } from "@tanstack/react-query";
import { userService } from "../../services/auth/user-service";

export function useLoggedInUser() {
  const { data, isLoading } = useQuery({
    queryKey: ["users, logged-in"],
    queryFn: userService.getMe,
  });
  return { user: data?.payload, isLoading };
}

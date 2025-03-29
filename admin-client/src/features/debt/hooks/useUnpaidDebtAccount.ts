import { useQuery } from "@tanstack/react-query";
import { debtAccountService } from "../../../services";

export function useUnpaidDebtAccount(partyId: string) {
  const { data: partyDebtAccounts, isLoading } = useQuery({
    queryKey: ["debt-accounts", "party", partyId, "unpaid"],
    queryFn: () => debtAccountService.getUnpaidPartyDebtAccount(partyId),
    enabled: !!partyId,
    select: (data) => data.payload,
  });
  return {
    partyDebtAccounts,
    isLoading,
  };
}

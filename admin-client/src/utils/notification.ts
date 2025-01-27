import axios from "axios";
import { ApiError } from "../interfaces";
import { NOTIFICATION_MESSAGE } from "../common/constants";

export function getNotificationMessage(error: Error): string | undefined {
  if (axios.isAxiosError(error)) {
    const apiError = error.response?.data?.error as ApiError | undefined;
    if (apiError && apiError.errorCode in NOTIFICATION_MESSAGE) {
      return NOTIFICATION_MESSAGE[apiError.errorCode];
    }
  }
  return undefined;
}

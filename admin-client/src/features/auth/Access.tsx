import { PropsWithChildren, useEffect, useState } from "react";
import { HttpMethod } from "../../common/enums";
import { useLoggedInUser } from "../../common/hooks";
import NotPermitted from "./NotPermitted";

interface Props extends PropsWithChildren {
  hideChildren?: boolean;
  permission: { apiPath: string; httpMethod: HttpMethod };
}

const Access: React.FC<Props> = ({
  children,
  hideChildren = false,
  permission,
}) => {
  //hideChildren = false: check quyền và hiển thị children, nếu không sẽ hiển thị NotPermitted
  //hideChildren = true: check quyền và hiển thị children, nếu không sẽ không hiển thị gì cả
  const [isAllowed, setIsAllowed] = useState<boolean>(true);
  const { user } = useLoggedInUser();
  useEffect(() => {
    if (user) {
      const { permissions } = user.role;
      const isAllowed = permissions.some(
        (p) =>
          p.apiPath === permission.apiPath &&
          p.httpMethod === permission.httpMethod,
      );
      setIsAllowed(isAllowed);
    }
  }, [user, permission]);
  return <>{isAllowed ? children : hideChildren ? null : <NotPermitted />}</>;
};

export default Access;

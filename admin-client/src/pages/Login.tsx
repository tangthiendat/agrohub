import LoginForm from "../features/auth/LoginForm";

const Login: React.FC = () => {
  return (
    <div className="flex min-h-screen">
      <div className="flex min-h-full flex-1 flex-col justify-center lg:px-8">
        <div className="flex items-center justify-between rounded-xl sm:bg-white">
          <div className="hidden w-[60%] lg:block">
            <img src="/login-bg.png" alt="Login background" />
          </div>
          <div className="mx-auto w-[80%] border bg-slate-100 p-6 shadow md:w-[60%] lg:w-[40%] xl:w-[30%]">
            <div className="sm:mx-auto sm:w-full sm:max-w-sm">
              {/* <p className="text-center text-lg font-semibold">
                Chào mừng đến AgrisuWH Admin
              </p> */}
              <div className="mt-2 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
                Đăng nhập
              </div>
            </div>

            <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-xl">
              <LoginForm />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;

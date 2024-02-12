import Button from "@/components/components/Button/Button";
import Input from "@/components/components/Input/Input";
import ErrorAlert from "@/components/pages/login/ErrorAlert";
import { useForm } from "react-hook-form";

type LoginCardProps = {
  username: string;
  password: string;
  csrfToken: string;
  error: string | undefined;
  setUsername: (username: string) => void;
  setPassword: (password: string) => void;
  onLogin: (data: LoginFormInputs) => void;
};

const LoginCard = ({ ...props }: LoginCardProps) => {
  const { handleSubmit, control, formState: {isValid, isSubmitting} } = useForm<LoginFormInputs>({
    defaultValues: async () => {
      return {
        username: props.username,
        password: props.password,
      };
    },
    mode: "onBlur",
  });

  return (
    <div className="mx-auto max-w-md rounded-lg bg-white shadow">
      <div className="py-4 px-10">
        <div className="text-xl font-medium text-gray-900 text-center">
          Login
        </div>
        {props.error && (
          <div className="mt-10"> 
            <ErrorAlert error={props.error} />
          </div>
        )}
        <form onSubmit={handleSubmit(props.onLogin)}>
          <div className="flex flex-col mt-5">
            <Input
              label="Username"
              type="text"
              name="username"
              placeholder="test@example.com"
              control={control}
              rules={{
                required: "Username is required",
              }}
            />
          </div>
          <div className="flex flex-col mt-5">
            <Input
              label="Password"
              type="password"
              name="password"
              control={control}
              rules={{
                required: "Password is required",
              }}
            />
          </div>
          <input type="hidden" value={props.csrfToken} />
          <div className="mt-5 text-center">
            <Button type="submit" disabled={!isValid || isSubmitting}>Login</Button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default LoginCard;

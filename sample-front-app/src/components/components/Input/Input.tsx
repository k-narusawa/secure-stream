import {
  Control,
  FieldValues,
  UseControllerProps,
  useController,
  useForm,
} from "react-hook-form";

type Props = {
  label: string;
  className?: string | undefined;
  placeholder?: string | undefined;
  type: string;
};

type InputItemComponentProps<T extends FieldValues> = UseControllerProps<T> &
  Props;

const Input = <T extends FieldValues>(props: InputItemComponentProps<T>) => {
  const defaultControl = useForm().control as unknown as Control<T>;
  const defaultRules = { required: "This field is required" };
  const {
    name,
    control = defaultControl,
    rules = defaultRules,
    className,
    placeholder,
  } = props;
  const { field, fieldState } = useController<T>({ name, control, rules });
  const { error } = fieldState;

  return (
    <div>
      <label
        className={`
        mb-1 block text-sm font-medium text-gray-700 
        after:ml-0.5 after:text-red-500 after:content-['*']
        `}
      >
        {props.label}
      </label>
      <input
        type={props.type}
        placeholder={placeholder}
        className={`
          h-10 px-4
          block w-full rounded-md 
          border-2 border-gray-300 shadow-sm
          focus:border-primary-400 focus:ring focus:ring-primary-200 focus:ring-opacity-50 
          disabled:cursor-not-allowed disabled:bg-gray-50 disabled:text-gray-500
          ${error ? "border-rose-300" : ""}
          ${className}
        `}
        {...field}
      />
      {error && (
        <span className="mt-1 text-sm text-red-500">
          {error ? error.message : ""}
        </span>
      )}
    </div>
  );
};

export default Input;

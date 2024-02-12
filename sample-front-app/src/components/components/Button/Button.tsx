const Button = ({ children, variant = 'primary', disabled, ...props }: { children: React.ReactNode, variant?: string, disabled: boolean, [key: string]: any }) => {
  const baseStyle = 'w-full py-2 px-4 font-semibold rounded-lg shadow-md';
  let variantStyle = '';
  if (disabled) {
    variantStyle = 'text-gray-500 bg-gray-200 cursor-not-allowed';
  } else if (variant === 'primary') {
    variantStyle = 'text-white bg-blue-500 hover:bg-blue-700';
  } else if (variant === 'secondary') {
    variantStyle = 'text-gray-800 bg-gray-300 hover:bg-gray-400';
  }

  return (
    <button className={`${baseStyle} ${variantStyle}`} {...props}>
      {children}
    </button>
  );
};

export default Button;
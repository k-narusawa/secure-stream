const Button = ({ children, variant = 'primary', ...props }: { children: React.ReactNode, variant?: string, [key: string]: any }) => {
  const baseStyle = 'py-2 px-4 font-semibold rounded-lg shadow-md';
  let variantStyle = '';
  if (variant === 'primary') {
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
const Card = ({children}: {children: React.ReactNode}) => {
  return (
    <div className="mx-auto max-w-md rounded-lg bg-white shadow">
      <div className="py-4 px-10">
        {children}
      </div>  
    </div>
  );
}

export default Card;
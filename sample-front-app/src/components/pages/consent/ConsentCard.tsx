import Button from "@/components/components/Button/Button";
import Card from "@/components/components/Card/Card";
import { useForm } from "react-hook-form";

type ConsentCardProps = {
  challenge: string | undefined;
  scopes: Scope[];
  onAccept: (input: ConsentFormInputs) => void;
};

const ConsentCard = ({ ...props }: ConsentCardProps) => {
  const { register, handleSubmit } = useForm<ConsentFormInputs>();
  
  return (
    <Card>
      <form onSubmit={handleSubmit(props.onAccept)}>
        {props.scopes.map((scope, index) => (
          <div key={index}>
            <div className="flex">
              <div className="flex items-center h-5">
                <input 
                  id={scope.name}
                  aria-describedby={props.scopes[index].name}
                  type="checkbox" 
                  value={scope.name}
                  className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"
                  { ...register("scopes") }
                >
                </input>
              </div>
              <div className="ms-2 text-sm">
                <label htmlFor="helper-checkbox" className="font-medium text-gray-900 dark:text-gray-300">
                  {scope.name}
                </label>
                <p id={props.scopes[index].name} className="text-xs font-normal text-gray-500 dark:text-gray-300">
                  scope description
                </p>
              </div>
            </div>
          </div>
        ))}
        <div className="mt-5">
          <Button type="submit">I accept</Button>
        </div>
      </form>
    </Card>
  )
};

export default ConsentCard;
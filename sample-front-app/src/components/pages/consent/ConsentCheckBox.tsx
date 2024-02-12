type ConsentCheckBoxProps = {
  index: number;
  scope: Scope;
  register: any;
}

const ConsentCheckBox = ({ ...props }: ConsentCheckBoxProps) => {
  return (
    <div className="flex">
      <div className="flex items-center h-5">
        <input 
          id={props.scope.name}
          aria-describedby={props.scope.name}
          type="checkbox" 
          checked={props.scope.is_checked}
          className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"
        >
        </input>
      </div>
      <div className="ms-2 text-sm">
        <label htmlFor="helper-checkbox" className="font-medium text-gray-900 dark:text-gray-300">
          {props.scope.name}
        </label>
        <p id={props.scope.name} className="text-xs font-normal text-gray-500 dark:text-gray-300">
          scope description
        </p>
      </div>
    </div>
  )
}

export default ConsentCheckBox;
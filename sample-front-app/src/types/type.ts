type LoginFormInputs = {
  username: string;
  password: string;
};

type ConsentFormInputs = {
  scopes: Scope[];
}; 

type Scope = {
  name: string;
  is_checked: boolean;
  required: boolean;
};
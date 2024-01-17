import axios from "axios";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";

const UserinfoPage = () => {
  const [userinfo, setUserInfo] = useState<any>();
  const router = useRouter();
  
  useEffect(() => {
    const fetchUserInfo = async () => {
      await axios('http://localhost:8080/api/v1/userinfo',
      {
        withCredentials: true,
      })
      .then((response) => {
        console.log(response.data);
        setUserInfo(response.data);
      }).catch((error) => {
        console.log(error.response)
        console.log(error.response.status);
        console.log(error.response.data);
        router.push('/login');
      });
    }
    fetchUserInfo();
  }, [])
  return (
    <div>
      <h1>User Info</h1>
      <pre>{JSON.stringify(userinfo, null, 2)}</pre>
    </div>
  );
}

export default UserinfoPage;
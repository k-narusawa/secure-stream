import Button from "@/components/components/Button/Button";
import axios from "axios";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";

const UserinfoPage = () => {
  const [userinfo, setUserInfo] = useState<any>();
  const [csrfToken, setCsrfToken] = useState("");
  const router = useRouter();
  const apiHost = process.env.NEXT_PUBLIC_SECURE_STREAM_HOST;

  useEffect(() => {
    const fetchUserInfo = async () => {
      await axios(`${apiHost}/api/v1/userinfo`, {
        withCredentials: true,
      })
        .then((response) => {
          setUserInfo(response.data);
        })
        .catch(() => {
          router.push("/login");
        });
    };
    const fetchCsrfToken = async () => {
      await axios(`${apiHost}/api/v1/login`, {
        withCredentials: true,
      })
        .then((response) => {
          console.log(response.data);
          setCsrfToken(response.data.csrf_token);
        })
        .catch((error) => {
          console.log(error.response);
          console.log(error.response.status);
          console.log(error.response.data);
        });
    };
    fetchCsrfToken();
    fetchUserInfo();
  }, [apiHost, router]);

  const onLogout = async () => {
    const params = new URLSearchParams();
    params.append('_csrf', csrfToken);

    await axios.post(`${apiHost}/api/v1/logout`, 
    params, 
    {
      withCredentials: true,
    })
      .then(() => {
        router.push("/login");
      })
      .catch((error) => {
        console.log(error.response);
        console.log(error.response.status);
        console.log(error.response.data);
      });
  }

  return (
    <div>
      <h1>User Info</h1>
      <pre>{JSON.stringify(userinfo, null, 2)}</pre>
      <Button onClick={onLogout} variant="primary">
        Logout
      </Button>
    </div>
  );
};

async function getStaticProps() {
  return {
    props: {},
  };
}

export default UserinfoPage;

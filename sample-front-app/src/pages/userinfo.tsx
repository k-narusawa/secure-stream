import axios from "axios";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";

const UserinfoPage = () => {
  const [userinfo, setUserInfo] = useState<any>();
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
    fetchUserInfo();
  }, [apiHost, router]);
  return (
    <div>
      <h1>User Info</h1>
      <pre>{JSON.stringify(userinfo, null, 2)}</pre>
    </div>
  );
};

async function getStaticProps() {
  return {
    props: {},
  };
}

export default UserinfoPage;

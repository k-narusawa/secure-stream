import Button from "@/components/components/Button/Button";
import Input from "@/components/components/Input/Input";
import LoginCard from "@/components/pages/login/LoginCard";
import axios from "axios";
import { useSearchParams } from "next/navigation";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";

const LoginPage = () => {
  const [username, setUsername] = useState("test@example.com");
  const [password, setPassword] = useState("!Password0");
  const [csrfToken, setCsrfToken] = useState("");
  const router = useRouter();
  const searchParams = useSearchParams();
  const loginChallenge = searchParams.get("login_challenge");
  const apiHost = process.env.NEXT_PUBLIC_SECURE_STREAM_HOST;

  useEffect(() => {
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
  }, [apiHost]);

  const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const data = {
      username: username,
      password: password,
      _csrf: csrfToken,
      login_challenge: loginChallenge,
    };

    await axios
      .post(`${apiHost}/api/v1/login`, data, {
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        withCredentials: true,
      })
      .then(function (response) {
        console.log(response.data);
        router.push("/userinfo");
      })
      .catch(function (error) {
        console.log(error.response.data);
      });
  };

  return (
    <div className="pt-10">
      <LoginCard
        username={username}
        password={password}
        csrfToken={csrfToken}
        setUsername={setUsername}
        setPassword={setPassword}
        onLogin={onSubmit}
      />
    </div>
  );
};

async function getStaticProps() {
  return {
    props: {},
  };
}

export default LoginPage;

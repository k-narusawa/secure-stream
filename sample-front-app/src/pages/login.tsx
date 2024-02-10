import LoginCard from "@/components/pages/login/LoginCard";
import axios from "axios";
import { useSearchParams } from "next/navigation";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";
import { set } from "react-hook-form";

const LoginPage = () => {
  const [username, setUsername] = useState("test@example.com");
  const [password, setPassword] = useState("!Password0");
  const [csrfToken, setCsrfToken] = useState("");
  const [error, setError] = useState<string | undefined>(undefined);
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
          setCsrfToken(response.data.csrf_token);
        })
        .catch(() => {
          setError("Internal Server Error");
        });
    };
    fetchCsrfToken();
  }, [apiHost]);

  const onSubmit = async (input: LoginFormInputs) => {
    const data = {
      username: input.username,
      password: input.password,
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
      .then(() => {
        router.push("/userinfo");
      })
      .catch((error) => {
        if (error.response.status === 401) {
          setError("Unauthorized");
        }
      });
  };

  return (
    <div className="pt-10">
      <LoginCard
        username={username}
        password={password}
        csrfToken={csrfToken}
        error={error}
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

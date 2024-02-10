import LoginCard from "@/components/pages/login/LoginCard";
import axios from "axios";
import { useSearchParams } from "next/navigation";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";

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
    let ignore = false;

    const fetchCsrfToken = async () => {
      if (!ignore) {
        await axios(`${apiHost}/api/v1/csrf`, {
          withCredentials: true,
        })
          .then((response) => {
            setCsrfToken(response.data.csrf_token);
          })
          .catch(() => {
            setError("Internal Server Error");
          });
      }
    };

    fetchCsrfToken();

    return () => {
      ignore = true;
    }
  }, [apiHost]);

  const onSubmit = async (input: LoginFormInputs) => {
    const data = {
      username: input.username,
      password: input.password,
      _csrf: csrfToken,
      login_challenge: loginChallenge,
    };

    const redirectTo = await axios
      .post(`${apiHost}/api/v1/login`, data, {
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        withCredentials: true,
      })
      .then((response) => {
        // router.push("/userinfo");
        return response.data.redirect_to;
      })
      .catch((error) => {
        if (error.response.status === 401) {
          setError("Unauthorized");
        }
      });

      await axios.get(redirectTo, {
        withCredentials: true,
        headers: {
          "Access-Control-Allow-Origin": "*",
        },
      })
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

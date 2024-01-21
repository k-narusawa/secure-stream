import axios from 'axios';
import { useSearchParams } from 'next/navigation';
import { useRouter } from 'next/router';
import React, { useEffect, useState } from 'react';

const LoginPage = () => {
  const [username, setUsername] = useState('test@example.com');
  const [password, setPassword] = useState('!Password0');
  const [csrfToken, setCsrfToken] = useState('');
  const router = useRouter();
  const searchParams = useSearchParams();
  const loginChallenge = searchParams.get("login_challenge");

  useEffect(() => {
    const fetchCsrfToken = async () => {
      await axios('http://127.0.0.1:8080/api/v1/login',
        {
          withCredentials: true,
        })
        .then((response) => {
          console.log(response.data);
          setCsrfToken(response.data.csrf_token);
        }).catch((error) => {
          console.log(error.response)
          console.log(error.response.status);
          console.log(error.response.data);
        });
    }
    fetchCsrfToken();
  }, [])

  const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const data = {
      username: username,
      password: password,
      _csrf: csrfToken,
      login_challenge: loginChallenge
    }

    await axios.post('http://127.0.0.1:8080/api/v1/login', data, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      withCredentials: true
    })
      .then(function (response) {
        console.log(response.data);
        router.push('/userinfo')
      })
      .catch(function (error) {
        console.log(error.response.data);
      })
  }

  return (
    <>
      <h1>Login Page</h1>
      <form onSubmit={onSubmit}>
        <label>
          Username:
          <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} />
        </label>
        <br />
        <label>
          Password:
          <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
        </label>
        <input type="hidden" value={csrfToken} />
        <br />
        <input type="submit" value="Submit" />
      </form>
    </>
  )
}

export default LoginPage
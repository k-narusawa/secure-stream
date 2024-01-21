import axios from 'axios';
import { useRouter } from 'next/router';
import React, { useEffect, useState } from 'react';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [csrfToken, setCsrfToken] = useState('');
  const router = useRouter();

  const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const data = {
      username: username,
      password: password,
      _csrf: csrfToken
    }

    await axios.post('http://localhost:8080/api/v1/login', data, {
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
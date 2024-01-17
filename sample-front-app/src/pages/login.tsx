import axios from 'axios';
import { useRouter } from 'next/router';
import React, { useState } from 'react';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const csrfToken = 'your-csrf-token'; // ここに実際のCSRFトークンを設定します
  const router = useRouter();

  const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const data = {
      username: username,
      password: password,
      csrf_token: csrfToken
    }

    var params = new URLSearchParams();
    params.append('username', data.username);
    params.append('password', data.password);
    params.append('csrf_token', data.csrf_token);

    await axios.post('http://localhost:8080/api/v1/login', data, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      },
      params: params
    })
      .then(function (response) {
        console.log(response.data);
        router.push('/userinfo');
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
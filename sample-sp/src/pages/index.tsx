import { Inter } from 'next/font/google'
import { signIn, useSession } from 'next-auth/react'

const inter = Inter({ subsets: ['latin'] })

export default function Home() {
  const { data: session } = useSession()
  
  if(session) {
    return (
      <>
        <div>user_id: {session.id}</div>
        <div>access_token: {session.accessToken}</div>
        <div>refresh_token: {session.refreshToken}</div>
        <div>id_token: {session.idToken}</div>
        <div>expires_at: {session.expiresAt}</div>
      </>
    )
  }

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col justify-center items-center">
      <div className="bg-white p-6 rounded-lg shadow-lg">
        <h1 className="text-xl font-bold mb-4">ログイン</h1>
        <button 
          className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline" 
          onClick={() => signIn('hydra')}>
          ログイン
        </button>
      </div>
    </div>
  );
}
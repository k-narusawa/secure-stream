import axios from "axios";
import type { NextApiRequest, NextApiResponse } from "next";
import { useSession } from "next-auth/react";
import { NextResponse } from "next/server";

const logoutEndpointUrl = "http://localhost:44444/oauth2/auth/logout";

const logoutUrl = `${logoutEndpointUrl}`;

// eslint-disable-next-line import/no-anonymous-default-export
export default async(req: NextApiRequest, res: NextApiResponse) => {
  const corsHeaders = {
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "GET, POST, PUT, DELETE, OPTIONS",
    "Access-Control-Allow-Headers": "Content-Type, Authorization",
  };
  
  res.setHeader("Access-Control-Allow-Origin", "http://localhost:3000")
  res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
  res.status(200).json({ message: 'Hello from Next.js!' })
};
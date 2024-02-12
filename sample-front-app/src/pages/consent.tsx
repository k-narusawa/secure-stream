import Card from "@/components/components/Card/Card";
import ConsentCard from "@/components/pages/consent/ConsentCard";
import axios from "axios";
import { useSearchParams } from "next/navigation";
import { useRouter } from "next/router";
import { useEffect, useState } from "react";

const ConsentPage = () => {
  const apiHost = process.env.NEXT_PUBLIC_SECURE_STREAM_HOST;
  const [ challenge, setChallenge ] = useState<string | undefined>(undefined);
  const [ scopes, setScopes ] = useState<Scope[]>([]);
  const [ csrfToken, setCsrfToken ] = useState("");
  const searchParams = useSearchParams();
  const redirectTo = searchParams.get("redirect_to");
  const consentChallenge = searchParams.get("consent_challenge");
  const router = useRouter();
  
  useEffect(() => {
    const fetchConsent = async () => {
      if(consentChallenge){
        await axios(`${apiHost}/api/v1/consent?consent_challenge=${consentChallenge}`, {
          withCredentials: true,
        })
          .then((response) => {
            console.log(response.data);
            setChallenge(response.data.challenge);
            setScopes(response.data.scopes);
            setCsrfToken(response.data.csrf_token);
          })
          .catch((error) => {
            console.log(error.response);
            console.log(error.response.status);
            console.log(error.response.data);
          });
      }
    }

    fetchConsent();
  }, [apiHost, consentChallenge, redirectTo]);

  const onAccept = async (data: ConsentFormInputs) => {
    console.log(data);

    const req = {
      challenge: challenge,
      scopes: data.scopes,
      _csrf: csrfToken,
    }

    const redirectTo = await axios.post(`${apiHost}/api/v1/consent`, req, {
      withCredentials: true,
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
    }).then((response) => {
        return response.data.redirect_to;
      }).catch((error) => {
        console.log(error.response.data);
        console.log(error.response.status);
      });

    await router.push(redirectTo)
  }

  return (
    <div className="pt-10">
      <ConsentCard challenge={challenge} scopes={scopes} onAccept={onAccept} />
    </div>
  );
}

export default ConsentPage;
import axios from "axios";
import { useSearchParams } from "next/navigation";
import { useEffect } from "react";

const ConsentPage = () => {
  const apiHost = process.env.NEXT_PUBLIC_SECURE_STREAM_HOST;
  const searchParams = useSearchParams();
  const redirectTo = searchParams.get("redirect_to");
  const consentChallenge = searchParams.get("consent_challenge");
  
  useEffect(() => {
    const fetchConsent = async () => {
      if(redirectTo) {
        await axios(redirectTo)
        .then((response) => {
          console.log(response.data);
        })
        .catch((error) => {
          console.log(error.response);
          console.log(error.response.status);
          console.log(error.response.data);
        });
      } else if(consentChallenge){
        await axios(`${apiHost}/api/v1/consent?consent_challenge=${consentChallenge}`, {
          withCredentials: true,
        })
          .then((response) => {
            console.log(response.data);
          })
          .catch((error) => {
            console.log(error.response);
            console.log(error.response.status);
            console.log(error.response.data);
          });
      
      }
    }
    fetchConsent();
  }, []);

  return (
    <div>
      <h1>Consent</h1>
    </div>
  );
}

export default ConsentPage;
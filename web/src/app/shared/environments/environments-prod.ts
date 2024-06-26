import getConfigProd from "../configuration/auth-prod.configuration";

export const environment = {
  production: false,
  authConfiguration: getConfigProd()
};

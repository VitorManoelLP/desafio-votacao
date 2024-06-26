import getConfigDev from "../configuration/auth-dev.configuration";

export const environment = {
  production: true,
  authConfiguration: getConfigDev()
};

import { KeycloakService } from "keycloak-angular";
import { environment } from "../environments/environments";
import { Injectable } from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class Keycloak extends KeycloakService {

  constructor() {
    super();
  }

  public initKeycloak(): Promise<boolean> {
    return this.init({
      config: environment.authConfiguration,
      initOptions: {
        onLoad: 'login-required',
        checkLoginIframe: false,
      },
      enableBearerInterceptor: true,
      bearerPrefix: 'Bearer'
    });
  }

}

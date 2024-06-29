import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Observable, from, switchMap } from "rxjs";
import keycloak from "../../../main";

export class KeycloakInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return from(keycloak.getToken())
      .pipe(switchMap((token: string) => {

        if (keycloak.isTokenExpired()) {
          keycloak.updateToken();
        }

        if (keycloak.isLoggedIn()) {
          req = req.clone({
            headers: req.headers.set("Authorization", "Bearer " + token)
          });
        }
        return next.handle(req);
      }));
  }

}

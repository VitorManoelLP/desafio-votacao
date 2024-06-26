import { KeycloakProfile } from "keycloak-js";

export default class Profile {

  private static profile: Profile;
  private _user: KeycloakProfile | undefined;
  public hasProfile: boolean = false;

  set attrUser(user: KeycloakProfile) {
    this._user = user;
    this.hasProfile = true;
  }

  get user() {
    return this._user || {};
  }

  static getInstance() {
    if (!this.profile) {
      this.profile = new Profile();
    }
    return this.profile;
  }

}

import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { AppModule } from './app/app.module';
import Profile from './app/shared/model/profile';
import { Keycloak } from './app/shared/security/keycloak.service'


const keycloak = new Keycloak();

async function initialize(): Promise<void> {
  try {
    await keycloak.initKeycloak();
    const profile = await keycloak.loadUserProfile();
    Profile.getInstance().attrUser = profile;
  } catch (error) {
    console.error(error);
  } finally {
    await platformBrowserDynamic().bootstrapModule(AppModule).catch(err => console.error(err));
  }
}

initialize();

export default keycloak;

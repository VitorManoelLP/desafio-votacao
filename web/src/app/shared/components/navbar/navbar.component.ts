import { Component } from '@angular/core';
import keycloak from '../../../../main';
import Profile from '../../model/profile';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  standalone: true,
  imports: [CommonModule, RouterModule],
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  readonly profile: Profile = Profile.getInstance();
  readonly routes: { [key: string]: { route: string, active: boolean } } = {
    'CREATE_SESSION': {
      route: '/session/create',
      active: false
    },
    'ENTER_SESSION': {
      route: '/session/enter',
      active: false
    },
    'HOME': {
      route: '/home',
      active: true
    }
  };

  async logout() {
    keycloak.clearToken();
    keycloak.logout(window.location.origin);
  }

  public toggleRoute(routeToggled: string) {
    Object.keys(this.routes)
      .filter(route => route !== routeToggled)
      .forEach(route => this.routes[route].active = false);
    this.routes[routeToggled].active = true;
  }


}

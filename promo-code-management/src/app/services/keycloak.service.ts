import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';

@Injectable({ providedIn: 'root' })
export class KeycloakService {
  private keycloak: Keycloak.KeycloakInstance;

  constructor() {
    this.keycloak = new Keycloak({
      url: 'http://localhost:8081/',
      realm: 'promo-realm',
      clientId: 'promo-management'
    });
  }

  init(): Promise<boolean> {
    return this.keycloak.init({ onLoad: 'login-required' }).then(authenticated => {
      if (authenticated) {
        localStorage.setItem('access_token', this.keycloak.token || '');
      }
      return authenticated;
    });
  }

  login() {
    this.keycloak.login();
  }

  logout() {
    this.keycloak.logout();
    localStorage.removeItem('access_token');
  }

  getToken(): string | undefined {
    return this.keycloak.token;
  }
}
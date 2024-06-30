import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'home',
    loadChildren: () => import('./components/home/home.module').then(m => m.HomeModule)
  },
  {
    path: 'session',
    loadChildren: () => import('./components/session/session.module').then(m => m.SessionModule)
  },
  {
    path: 'session-list',
    loadChildren: () => import('./components/home/sessions/session-list/session-list.module').then(m => m.SessionListModule)
  },
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: 'home',
    pathMatch: 'full'
  }
];

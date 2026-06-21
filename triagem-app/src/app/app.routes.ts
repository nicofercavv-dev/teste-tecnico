import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'triagem',
    pathMatch: 'full',
  },
  {
    path: 'triagem',
    loadComponent: () => import('./presentation/features/triagem/triagem.component'),
  },
];

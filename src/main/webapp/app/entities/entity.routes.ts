import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'catalogo',
    data: { pageTitle: 'pruebasApp.catalogo.home.title' },
    loadChildren: () => import('./catalogo/catalogo.routes'),
  },
  {
    path: 'item',
    data: { pageTitle: 'pruebasApp.item.home.title' },
    loadChildren: () => import('./item/item.routes'),
  },
  {
    path: 'pais',
    data: { pageTitle: 'pruebasApp.pais.home.title' },
    loadChildren: () => import('./pais/pais.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;

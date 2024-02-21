import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CatalogoComponent } from './list/catalogo.component';
import { CatalogoDetailComponent } from './detail/catalogo-detail.component';
import { CatalogoUpdateComponent } from './update/catalogo-update.component';
import CatalogoResolve from './route/catalogo-routing-resolve.service';

const catalogoRoute: Routes = [
  {
    path: '',
    component: CatalogoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CatalogoDetailComponent,
    resolve: {
      catalogo: CatalogoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CatalogoUpdateComponent,
    resolve: {
      catalogo: CatalogoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CatalogoUpdateComponent,
    resolve: {
      catalogo: CatalogoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default catalogoRoute;

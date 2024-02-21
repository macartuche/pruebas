import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PaisComponent } from './list/pais.component';
import { PaisDetailComponent } from './detail/pais-detail.component';
import { PaisUpdateComponent } from './update/pais-update.component';
import PaisResolve from './route/pais-routing-resolve.service';

const paisRoute: Routes = [
  {
    path: '',
    component: PaisComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaisDetailComponent,
    resolve: {
      pais: PaisResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaisUpdateComponent,
    resolve: {
      pais: PaisResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaisUpdateComponent,
    resolve: {
      pais: PaisResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default paisRoute;

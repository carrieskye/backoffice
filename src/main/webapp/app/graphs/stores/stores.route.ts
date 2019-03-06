import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { StoresComponent } from 'app/graphs/stores/stores.component';

export const storesRoute: Routes = [
    {
        path: '',
        component: StoresComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backofficeApp.graphs.stores.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

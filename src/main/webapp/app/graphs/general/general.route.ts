import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { GeneralComponent } from 'app/graphs/general/general.component';

export const generalRoute: Routes = [
    {
        path: '',
        component: GeneralComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backofficeApp.graphs.general.demo'
        },
        canActivate: [UserRouteAccessService]
    }
];

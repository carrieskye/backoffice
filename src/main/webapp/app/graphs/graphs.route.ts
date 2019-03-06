import { Route } from '@angular/router';

import { GraphsComponent } from 'app/graphs/graphs.component';

export const GRAPHS_ROUTE: Route = {
    path: 'graphs',
    component: GraphsComponent,
    data: {
        authorities: [],
        pageTitle: 'backofficeApp.graphs.title'
    }
};

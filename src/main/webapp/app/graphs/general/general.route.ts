import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { GeneralComponent } from 'app/graphs/general/general.component';
import { ClassificationService } from 'app/entities/classification';
import { Classification, IClassification } from 'app/shared/model/classification.model';

@Injectable({ providedIn: 'root' })
export class GeneralResolve implements Resolve<IClassification> {
    constructor(private service: ClassificationService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IClassification> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Classification>) => response.ok),
                map((classification: HttpResponse<Classification>) => classification.body)
            );
        }
        return of(new Classification());
    }
}

export const generalRoute: Routes = [
    {
        path: '',
        component: GeneralComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backofficeApp.graphs.general'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const generalPopupRoute: Routes = [
    {
        path: '',
        component: GeneralComponent,
        resolve: {
            classification: GeneralResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backofficeApp.graphs.general'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

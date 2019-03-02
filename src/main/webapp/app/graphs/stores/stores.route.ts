import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ClassificationService } from 'app/entities/classification';
import { Classification, IClassification } from 'app/shared/model/classification.model';
import { StoresComponent } from 'app/graphs/stores/stores.component';

@Injectable({ providedIn: 'root' })
export class StoresResolve implements Resolve<IClassification> {
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

export const storesRoute: Routes = [
    {
        path: '',
        component: StoresComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backofficeApp.graphs.stores.demo'
        },
        canActivate: [UserRouteAccessService]
    }
];

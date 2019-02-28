import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Slide } from 'app/shared/model/slide.model';
import { SlideService } from './slide.service';
import { SlideComponent } from './slide.component';
import { SlideDetailComponent } from './slide-detail.component';
import { SlideUpdateComponent } from './slide-update.component';
import { SlideDeletePopupComponent } from './slide-delete-dialog.component';
import { ISlide } from 'app/shared/model/slide.model';

@Injectable({ providedIn: 'root' })
export class SlideResolve implements Resolve<ISlide> {
    constructor(private service: SlideService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISlide> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Slide>) => response.ok),
                map((slide: HttpResponse<Slide>) => slide.body)
            );
        }
        return of(new Slide());
    }
}

export const slideRoute: Routes = [
    {
        path: '',
        component: SlideComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backofficeApp.slide.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: SlideDetailComponent,
        resolve: {
            slide: SlideResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backofficeApp.slide.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: SlideUpdateComponent,
        resolve: {
            slide: SlideResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backofficeApp.slide.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: SlideUpdateComponent,
        resolve: {
            slide: SlideResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backofficeApp.slide.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const slidePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: SlideDeletePopupComponent,
        resolve: {
            slide: SlideResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backofficeApp.slide.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

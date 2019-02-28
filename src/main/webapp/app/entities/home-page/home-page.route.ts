import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { HomePage } from 'app/shared/model/home-page.model';
import { HomePageService } from './home-page.service';
import { HomePageComponent } from './home-page.component';
import { HomePageDetailComponent } from './home-page-detail.component';
import { HomePageUpdateComponent } from './home-page-update.component';
import { HomePageDeletePopupComponent } from './home-page-delete-dialog.component';
import { IHomePage } from 'app/shared/model/home-page.model';

@Injectable({ providedIn: 'root' })
export class HomePageResolve implements Resolve<IHomePage> {
    constructor(private service: HomePageService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IHomePage> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<HomePage>) => response.ok),
                map((homePage: HttpResponse<HomePage>) => homePage.body)
            );
        }
        return of(new HomePage());
    }
}

export const homePageRoute: Routes = [
    {
        path: '',
        component: HomePageComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backofficeApp.homePage.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: HomePageDetailComponent,
        resolve: {
            homePage: HomePageResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backofficeApp.homePage.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: HomePageUpdateComponent,
        resolve: {
            homePage: HomePageResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backofficeApp.homePage.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: HomePageUpdateComponent,
        resolve: {
            homePage: HomePageResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backofficeApp.homePage.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const homePagePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: HomePageDeletePopupComponent,
        resolve: {
            homePage: HomePageResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'backofficeApp.homePage.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

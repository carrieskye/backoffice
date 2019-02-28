import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { BackofficeSharedModule } from 'app/shared';
import {
    HomePageComponent,
    HomePageDetailComponent,
    HomePageUpdateComponent,
    HomePageDeletePopupComponent,
    HomePageDeleteDialogComponent,
    homePageRoute,
    homePagePopupRoute
} from './';

const ENTITY_STATES = [...homePageRoute, ...homePagePopupRoute];

@NgModule({
    imports: [BackofficeSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        HomePageComponent,
        HomePageDetailComponent,
        HomePageUpdateComponent,
        HomePageDeleteDialogComponent,
        HomePageDeletePopupComponent
    ],
    entryComponents: [HomePageComponent, HomePageUpdateComponent, HomePageDeleteDialogComponent, HomePageDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BackofficeHomePageModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}

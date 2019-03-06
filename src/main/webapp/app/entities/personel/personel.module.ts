import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { BackofficeSharedModule } from 'app/shared';
import {
    PersonelComponent,
    PersonelDetailComponent,
    PersonelUpdateComponent,
    PersonelDeletePopupComponent,
    PersonelDeleteDialogComponent,
    personelRoute,
    personelPopupRoute
} from './';

const ENTITY_STATES = [...personelRoute, ...personelPopupRoute];

@NgModule({
    imports: [BackofficeSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PersonelComponent,
        PersonelDetailComponent,
        PersonelUpdateComponent,
        PersonelDeleteDialogComponent,
        PersonelDeletePopupComponent
    ],
    entryComponents: [PersonelComponent, PersonelUpdateComponent, PersonelDeleteDialogComponent, PersonelDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BackofficePersonelModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}

import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { BackofficeSharedModule } from 'app/shared';
import {
    ClassificationComponent,
    ClassificationDetailComponent,
    ClassificationUpdateComponent,
    ClassificationDeletePopupComponent,
    ClassificationDeleteDialogComponent,
    classificationRoute,
    classificationPopupRoute
} from './';

const ENTITY_STATES = [...classificationRoute, ...classificationPopupRoute];

@NgModule({
    imports: [BackofficeSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ClassificationComponent,
        ClassificationDetailComponent,
        ClassificationUpdateComponent,
        ClassificationDeleteDialogComponent,
        ClassificationDeletePopupComponent
    ],
    entryComponents: [
        ClassificationComponent,
        ClassificationUpdateComponent,
        ClassificationDeleteDialogComponent,
        ClassificationDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BackofficeClassificationModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}

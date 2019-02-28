import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { BackofficeSharedModule } from 'app/shared';
import {
    SlideComponent,
    SlideDetailComponent,
    SlideUpdateComponent,
    SlideDeletePopupComponent,
    SlideDeleteDialogComponent,
    slideRoute,
    slidePopupRoute
} from './';

const ENTITY_STATES = [...slideRoute, ...slidePopupRoute];

@NgModule({
    imports: [BackofficeSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [SlideComponent, SlideDetailComponent, SlideUpdateComponent, SlideDeleteDialogComponent, SlideDeletePopupComponent],
    entryComponents: [SlideComponent, SlideUpdateComponent, SlideDeleteDialogComponent, SlideDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BackofficeSlideModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}

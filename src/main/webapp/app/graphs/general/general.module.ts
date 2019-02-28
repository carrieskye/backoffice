import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { BackofficeSharedModule } from 'app/shared';
import { generalRoute, GeneralComponent, generalPopupRoute } from './';

const GRAPH_STATES = [...generalRoute, ...generalPopupRoute];

@NgModule({
    imports: [BackofficeSharedModule, RouterModule.forChild(GRAPH_STATES)],
    declarations: [GeneralComponent],
    entryComponents: [GeneralComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BackOfficeGeneralModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}

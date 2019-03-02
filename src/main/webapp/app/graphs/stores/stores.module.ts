import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { BackofficeSharedModule } from 'app/shared';
import { DemoComponent, storesRoute } from './';

import { ChartsModule } from 'ng2-charts';

const GRAPH_STATES = [...storesRoute];

@NgModule({
    imports: [BackofficeSharedModule, ChartsModule, RouterModule.forChild(GRAPH_STATES)],
    declarations: [DemoComponent],
    entryComponents: [DemoComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BackOfficeStoresModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}

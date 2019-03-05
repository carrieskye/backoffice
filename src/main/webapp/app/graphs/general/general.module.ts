import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { BackofficeSharedModule } from 'app/shared';
import { DemoComponent, generalRoute } from './';

import { ChartsModule } from 'ng2-charts';
import { ActivityComponent } from './activity/activity.component';
import { GeneralComponent } from './general.component';
import { AgeDistributionComponent } from './age-distribution/age-distribution.component';

const GRAPH_STATES = [...generalRoute];

@NgModule({
    imports: [BackofficeSharedModule, ChartsModule, RouterModule.forChild(GRAPH_STATES)],
    declarations: [GeneralComponent, DemoComponent, ActivityComponent, AgeDistributionComponent],
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

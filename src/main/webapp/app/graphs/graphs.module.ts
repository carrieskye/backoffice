import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BackofficeSharedModule } from 'app/shared';
import { GraphsComponent } from 'app/graphs/graphs.component';
import { GRAPHS_ROUTE } from 'app/graphs/graphs.route';
import { JhiLanguageHelper } from 'app/core';
import { ChartsModule } from 'ng2-charts';
import { JhiLanguageService } from 'ng-jhipster';
import { ActivityComponent } from 'app/graphs/activity/activity.component';
import { AgeDistributionComponent } from 'app/graphs/age-distribution/age-distribution.component';

@NgModule({
    imports: [BackofficeSharedModule, ChartsModule, RouterModule.forChild([GRAPHS_ROUTE])],
    declarations: [GraphsComponent, ActivityComponent, AgeDistributionComponent],
    entryComponents: [GraphsComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BackOfficeGraphsModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}

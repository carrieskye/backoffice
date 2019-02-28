import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { BackofficeSharedModule } from 'app/shared';
import {
    DeviceComponent,
    DeviceDetailComponent,
    DeviceUpdateComponent,
    DeviceDeletePopupComponent,
    DeviceDeleteDialogComponent,
    deviceRoute,
    devicePopupRoute
} from './';

const ENTITY_STATES = [...deviceRoute, ...devicePopupRoute];

@NgModule({
    imports: [BackofficeSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [DeviceComponent, DeviceDetailComponent, DeviceUpdateComponent, DeviceDeleteDialogComponent, DeviceDeletePopupComponent],
    entryComponents: [DeviceComponent, DeviceUpdateComponent, DeviceDeleteDialogComponent, DeviceDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BackofficeDeviceModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}

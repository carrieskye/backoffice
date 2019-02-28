import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'device',
                loadChildren: './device/device.module#BackofficeDeviceModule'
            },
            {
                path: 'home-page',
                loadChildren: './home-page/home-page.module#BackofficeHomePageModule'
            },
            {
                path: 'slide',
                loadChildren: './slide/slide.module#BackofficeSlideModule'
            },
            {
                path: 'classification',
                loadChildren: './classification/classification.module#BackofficeClassificationModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BackofficeEntityModule {}

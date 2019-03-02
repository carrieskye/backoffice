import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ChartsModule } from 'ng2-charts';

@NgModule({
    imports: [
        ChartsModule,
        RouterModule.forChild([
            {
                path: 'general',
                loadChildren: './general/general.module#BackOfficeGeneralModule'
            },
            {
                path: 'stores',
                // TODO: this needs to route to stores when created
                loadChildren: './stores/stores.module#BackOfficeStoresModule'
            }
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BackOfficeGraphsModule {}

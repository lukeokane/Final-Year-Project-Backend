import { NgModule } from '@angular/core';

import { ThelearningzoneSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [ThelearningzoneSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [ThelearningzoneSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class ThelearningzoneSharedCommonModule {}

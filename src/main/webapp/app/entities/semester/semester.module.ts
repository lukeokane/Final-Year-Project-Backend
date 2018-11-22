import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ThelearningzoneSharedModule } from 'app/shared';
import {
    SemesterComponent,
    SemesterDetailComponent,
    SemesterUpdateComponent,
    SemesterDeletePopupComponent,
    SemesterDeleteDialogComponent,
    semesterRoute,
    semesterPopupRoute
} from './';

const ENTITY_STATES = [...semesterRoute, ...semesterPopupRoute];

@NgModule({
    imports: [ThelearningzoneSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SemesterComponent,
        SemesterDetailComponent,
        SemesterUpdateComponent,
        SemesterDeleteDialogComponent,
        SemesterDeletePopupComponent
    ],
    entryComponents: [SemesterComponent, SemesterUpdateComponent, SemesterDeleteDialogComponent, SemesterDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ThelearningzoneSemesterModule {}

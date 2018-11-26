import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ThelearningzoneSharedModule } from 'app/shared';
import {
    SemesterGroupComponent,
    SemesterGroupDetailComponent,
    SemesterGroupUpdateComponent,
    SemesterGroupDeletePopupComponent,
    SemesterGroupDeleteDialogComponent,
    semesterGroupRoute,
    semesterGroupPopupRoute
} from './';

const ENTITY_STATES = [...semesterGroupRoute, ...semesterGroupPopupRoute];

@NgModule({
    imports: [ThelearningzoneSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SemesterGroupComponent,
        SemesterGroupDetailComponent,
        SemesterGroupUpdateComponent,
        SemesterGroupDeleteDialogComponent,
        SemesterGroupDeletePopupComponent
    ],
    entryComponents: [
        SemesterGroupComponent,
        SemesterGroupUpdateComponent,
        SemesterGroupDeleteDialogComponent,
        SemesterGroupDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ThelearningzoneSemesterGroupModule {}

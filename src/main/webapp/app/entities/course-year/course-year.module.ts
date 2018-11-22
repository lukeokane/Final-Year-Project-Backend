import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ThelearningzoneSharedModule } from 'app/shared';
import {
    CourseYearComponent,
    CourseYearDetailComponent,
    CourseYearUpdateComponent,
    CourseYearDeletePopupComponent,
    CourseYearDeleteDialogComponent,
    courseYearRoute,
    courseYearPopupRoute
} from './';

const ENTITY_STATES = [...courseYearRoute, ...courseYearPopupRoute];

@NgModule({
    imports: [ThelearningzoneSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        CourseYearComponent,
        CourseYearDetailComponent,
        CourseYearUpdateComponent,
        CourseYearDeleteDialogComponent,
        CourseYearDeletePopupComponent
    ],
    entryComponents: [CourseYearComponent, CourseYearUpdateComponent, CourseYearDeleteDialogComponent, CourseYearDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ThelearningzoneCourseYearModule {}

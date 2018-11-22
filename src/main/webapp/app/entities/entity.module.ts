import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ThelearningzoneUserInfoModule } from './user-info/user-info.module';
import { ThelearningzoneBookingModule } from './booking/booking.module';
import { ThelearningzoneBookingUserDetailsModule } from './booking-user-details/booking-user-details.module';
import { ThelearningzoneNotificationModule } from './notification/notification.module';
import { ThelearningzoneCourseModule } from './course/course.module';
import { ThelearningzoneCourseYearModule } from './course-year/course-year.module';
import { ThelearningzoneSemesterModule } from './semester/semester.module';
import { ThelearningzoneSubjectModule } from './subject/subject.module';
import { ThelearningzoneTopicModule } from './topic/topic.module';
import { ThelearningzoneResourceModule } from './resource/resource.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        ThelearningzoneUserInfoModule,
        ThelearningzoneBookingModule,
        ThelearningzoneBookingUserDetailsModule,
        ThelearningzoneNotificationModule,
        ThelearningzoneCourseModule,
        ThelearningzoneCourseYearModule,
        ThelearningzoneSemesterModule,
        ThelearningzoneSubjectModule,
        ThelearningzoneTopicModule,
        ThelearningzoneResourceModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ThelearningzoneEntityModule {}

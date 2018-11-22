import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ThelearningzoneSharedModule } from 'app/shared';
import {
    BookingUserDetailsComponent,
    BookingUserDetailsDetailComponent,
    BookingUserDetailsUpdateComponent,
    BookingUserDetailsDeletePopupComponent,
    BookingUserDetailsDeleteDialogComponent,
    bookingUserDetailsRoute,
    bookingUserDetailsPopupRoute
} from './';

const ENTITY_STATES = [...bookingUserDetailsRoute, ...bookingUserDetailsPopupRoute];

@NgModule({
    imports: [ThelearningzoneSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        BookingUserDetailsComponent,
        BookingUserDetailsDetailComponent,
        BookingUserDetailsUpdateComponent,
        BookingUserDetailsDeleteDialogComponent,
        BookingUserDetailsDeletePopupComponent
    ],
    entryComponents: [
        BookingUserDetailsComponent,
        BookingUserDetailsUpdateComponent,
        BookingUserDetailsDeleteDialogComponent,
        BookingUserDetailsDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ThelearningzoneBookingUserDetailsModule {}

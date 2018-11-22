import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBookingUserDetails } from 'app/shared/model/booking-user-details.model';

@Component({
    selector: 'jhi-booking-user-details-detail',
    templateUrl: './booking-user-details-detail.component.html'
})
export class BookingUserDetailsDetailComponent implements OnInit {
    bookingUserDetails: IBookingUserDetails;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ bookingUserDetails }) => {
            this.bookingUserDetails = bookingUserDetails;
        });
    }

    previousState() {
        window.history.back();
    }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IBookingUserDetails } from 'app/shared/model/booking-user-details.model';
import { BookingUserDetailsService } from './booking-user-details.service';
import { IUserInfo } from 'app/shared/model/user-info.model';
import { UserInfoService } from 'app/entities/user-info';
import { IBooking } from 'app/shared/model/booking.model';
import { BookingService } from 'app/entities/booking';

@Component({
    selector: 'jhi-booking-user-details-update',
    templateUrl: './booking-user-details-update.component.html'
})
export class BookingUserDetailsUpdateComponent implements OnInit {
    bookingUserDetails: IBookingUserDetails;
    isSaving: boolean;

    userinfos: IUserInfo[];

    bookings: IBooking[];
    usercheckInTime: string;
    usercheckOutTime: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private bookingUserDetailsService: BookingUserDetailsService,
        private userInfoService: UserInfoService,
        private bookingService: BookingService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ bookingUserDetails }) => {
            this.bookingUserDetails = bookingUserDetails;
            this.usercheckInTime =
                this.bookingUserDetails.usercheckInTime != null ? this.bookingUserDetails.usercheckInTime.format(DATE_TIME_FORMAT) : null;
            this.usercheckOutTime =
                this.bookingUserDetails.usercheckOutTime != null ? this.bookingUserDetails.usercheckOutTime.format(DATE_TIME_FORMAT) : null;
        });
        this.userInfoService.query().subscribe(
            (res: HttpResponse<IUserInfo[]>) => {
                this.userinfos = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.bookingService.query().subscribe(
            (res: HttpResponse<IBooking[]>) => {
                this.bookings = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.bookingUserDetails.usercheckInTime = this.usercheckInTime != null ? moment(this.usercheckInTime, DATE_TIME_FORMAT) : null;
        this.bookingUserDetails.usercheckOutTime = this.usercheckOutTime != null ? moment(this.usercheckOutTime, DATE_TIME_FORMAT) : null;
        if (this.bookingUserDetails.id !== undefined) {
            this.subscribeToSaveResponse(this.bookingUserDetailsService.update(this.bookingUserDetails));
        } else {
            this.subscribeToSaveResponse(this.bookingUserDetailsService.create(this.bookingUserDetails));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBookingUserDetails>>) {
        result.subscribe((res: HttpResponse<IBookingUserDetails>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserInfoById(index: number, item: IUserInfo) {
        return item.id;
    }

    trackBookingById(index: number, item: IBooking) {
        return item.id;
    }
}

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { HttpClient, HttpResponse } from '@angular/common/http';
import * as moment from 'moment';
// import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

// import { SERVER_API_URL } from 'app/app.constants';
import { Api } from '../api/api';
// import { createRequestOption } from 'app/shared';
// import { IBooking } from 'app/shared/model/booking.model';
import { Booking } from '../../class/Booking';

type EntityResponseType = HttpResponse<Booking>;
type EntityArrayResponseType = HttpResponse<Booking[]>;

@Injectable()
export class BookingService  {
    public resourceUrl = Api.API_URL + 'api/bookings';
    constructor(private http: HttpClient) { }

    createBookingWithAdminNotification(booking: Booking): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(booking);
        return this.http
            .post<Booking>(`${this.resourceUrl}/createBookingWithAdminNotification`, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    updateBookingAssignTutor(booking: Booking): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(booking);
        return this.http
            .put<Booking>(`${this.resourceUrl}/updateBookingAssignTutor`, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    updateBookingAcceptedByTutor(booking: Booking): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(booking);
        return this.http
            .put<Booking>(`${this.resourceUrl}/updateBookingAcceptedByTutor`, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    updateBookingRejectedByTutor(booking: Booking): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(booking);
        return this.http
            .put<Booking>(`${this.resourceUrl}/updateBookingRejectedByTutor`, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    updateBookingCancelledByTutor(booking: Booking): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(booking);
        return this.http
            .put<Booking>(`${this.resourceUrl}/updateBookingCancelledByTutor`, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    protected convertDateFromClient(booking: Booking): Booking {
        const copy: Booking = Object.assign({}, booking, {
            // startTime: booking.startTime != null && booking.startTime.isValid() ? booking.startTime.toJSON() : null,
            // endTime: booking.endTime != null && booking.endTime.isValid() ? booking.endTime.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        // if (res.body) {
        //     res.body.startTime = res.body.startTime != null ? moment(res.body.startTime) : null;
        //     res.body.endTime = res.body.endTime != null ? moment(res.body.endTime) : null;
        // }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((booking: Booking) => {
                // booking.startTime = booking.startTime != null ? moment(booking.startTime) : null;
                // booking.endTime = booking.endTime != null ? moment(booking.endTime) : null;
            });
        }
        return res;
    }


}
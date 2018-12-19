import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBooking } from 'app/shared/model/booking.model';

type EntityResponseType = HttpResponse<IBooking>;
type EntityArrayResponseType = HttpResponse<IBooking[]>;

@Injectable({ providedIn: 'root' })
export class BookingService {
    public resourceUrl = SERVER_API_URL + 'api/bookings';

    constructor(private http: HttpClient) {}

    create(booking: IBooking): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(booking);
        return this.http
            .post<IBooking>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    createBookingWithAdminNotification(booking: IBooking): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(booking);
        return this.http
            .post<IBooking>(`${this.resourceUrl}/createBookingWithAdminNotification`, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(booking: IBooking): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(booking);
        return this.http
            .put<IBooking>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    updateBookingAssignTutor(booking: IBooking): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(booking);
        return this.http
            .put<IBooking>(`${this.resourceUrl}/updateBookingAssignTutor`, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    updateBookingAcceptedByTutor(booking: IBooking): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(booking);
        return this.http
            .put<IBooking>(`${this.resourceUrl}/updateBookingAcceptedByTutor`, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    updateBookingRejectedByTutor(booking: IBooking): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(booking);
        return this.http
            .put<IBooking>(`${this.resourceUrl}/updateBookingRejectedByTutor`, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    updateBookingCancelledByTutor(booking: IBooking): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(booking);
        return this.http
            .put<IBooking>(`${this.resourceUrl}/updateBookingCancelledByTutor`, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    updateBookingRejectedByAdmin(booking: IBooking): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(booking);
        return this.http
            .put<IBooking>(`${this.resourceUrl}/updateBookingRequestRejectedByAdmin`, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IBooking>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IBooking[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    // findAllNotificationsByUserLoggedIn(req?: any): Observable<EntityArrayResponseType> {
    //     const options = createRequestOption(req);
    //     return this.http
    //     .get<INotification[]>(this.resourceUrl, { params: options, observe: 'response' })
    //         .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    // }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(booking: IBooking): IBooking {
        const copy: IBooking = Object.assign({}, booking, {
            startTime: booking.startTime != null && booking.startTime.isValid() ? booking.startTime.toJSON() : null,
            endTime: booking.endTime != null && booking.endTime.isValid() ? booking.endTime.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.startTime = res.body.startTime != null ? moment(res.body.startTime) : null;
            res.body.endTime = res.body.endTime != null ? moment(res.body.endTime) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((booking: IBooking) => {
                booking.startTime = booking.startTime != null ? moment(booking.startTime) : null;
                booking.endTime = booking.endTime != null ? moment(booking.endTime) : null;
            });
        }
        return res;
    }
}

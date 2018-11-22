import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBookingUserDetails } from 'app/shared/model/booking-user-details.model';

type EntityResponseType = HttpResponse<IBookingUserDetails>;
type EntityArrayResponseType = HttpResponse<IBookingUserDetails[]>;

@Injectable({ providedIn: 'root' })
export class BookingUserDetailsService {
    public resourceUrl = SERVER_API_URL + 'api/booking-user-details';

    constructor(private http: HttpClient) {}

    create(bookingUserDetails: IBookingUserDetails): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(bookingUserDetails);
        return this.http
            .post<IBookingUserDetails>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(bookingUserDetails: IBookingUserDetails): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(bookingUserDetails);
        return this.http
            .put<IBookingUserDetails>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IBookingUserDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IBookingUserDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(bookingUserDetails: IBookingUserDetails): IBookingUserDetails {
        const copy: IBookingUserDetails = Object.assign({}, bookingUserDetails, {
            usercheckInTime:
                bookingUserDetails.usercheckInTime != null && bookingUserDetails.usercheckInTime.isValid()
                    ? bookingUserDetails.usercheckInTime.toJSON()
                    : null,
            usercheckOutTime:
                bookingUserDetails.usercheckOutTime != null && bookingUserDetails.usercheckOutTime.isValid()
                    ? bookingUserDetails.usercheckOutTime.toJSON()
                    : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.usercheckInTime = res.body.usercheckInTime != null ? moment(res.body.usercheckInTime) : null;
            res.body.usercheckOutTime = res.body.usercheckOutTime != null ? moment(res.body.usercheckOutTime) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((bookingUserDetails: IBookingUserDetails) => {
                bookingUserDetails.usercheckInTime =
                    bookingUserDetails.usercheckInTime != null ? moment(bookingUserDetails.usercheckInTime) : null;
                bookingUserDetails.usercheckOutTime =
                    bookingUserDetails.usercheckOutTime != null ? moment(bookingUserDetails.usercheckOutTime) : null;
            });
        }
        return res;
    }
}

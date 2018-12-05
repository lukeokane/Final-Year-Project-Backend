
import { Observable } from 'rxjs/Rx';
import { Api } from '../api/api';
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from '../../../app/shared';
import { INotification } from 'app/shared/model/notification.model';

type EntityResponseType = HttpResponse<INotification>;
type EntityArrayResponseType = HttpResponse<INotification[]>;

@Injectable()
export class NotificationService  {
    public resourceUrl = SERVER_API_URL + 'api/notifications';
    constructor(private http: HttpClient) { }

    findAllNotificationsByUserLoggedIn(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
        .get<INotification[]>(`${this.resourceUrl}/findAllNotificationsDateAscList`, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    findAllNotificationsByUserLoggedInPagable(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<INotification[]>(`${this.resourceUrl}/findAllNotificationsDateAscPageable`, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    update(notification: INotification): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(notification);
        return this.http
            .put<INotification>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((notification: INotification) => {
                notification.timestamp = notification.timestamp != null ? moment(notification.timestamp) : null;
            });
        }
        return res;
    }

    protected convertDateFromClient(notification: INotification): INotification {
        const copy: INotification = Object.assign({}, notification, {
            timestamp: notification.timestamp != null && notification.timestamp.isValid() ? notification.timestamp.toJSON() : null
        });
        return copy;
    }
    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.timestamp = res.body.timestamp != null ? moment(res.body.timestamp) : null;
        }
        return res;
    }

   
    
}
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISubject } from 'app/shared/model/subject.model';

type EntityResponseType = HttpResponse<ISubject>;
type EntityArrayResponseType = HttpResponse<ISubject[]>;

@Injectable({ providedIn: 'root' })
export class SubjectService {
    public resourceUrl = SERVER_API_URL + 'api/subjects';

    constructor(private http: HttpClient) {}

    create(subject: ISubject): Observable<EntityResponseType> {
        return this.http.post<ISubject>(this.resourceUrl, subject, { observe: 'response' });
    }

    update(subject: ISubject): Observable<EntityResponseType> {
        return this.http.put<ISubject>(this.resourceUrl, subject, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISubject>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISubject[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}

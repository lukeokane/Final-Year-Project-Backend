import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISemesterGroup } from 'app/shared/model/semester-group.model';

type EntityResponseType = HttpResponse<ISemesterGroup>;
type EntityArrayResponseType = HttpResponse<ISemesterGroup[]>;

@Injectable({ providedIn: 'root' })
export class SemesterGroupService {
    public resourceUrl = SERVER_API_URL + 'api/semester-groups';

    constructor(private http: HttpClient) {}

    create(semesterGroup: ISemesterGroup): Observable<EntityResponseType> {
        return this.http.post<ISemesterGroup>(this.resourceUrl, semesterGroup, { observe: 'response' });
    }

    update(semesterGroup: ISemesterGroup): Observable<EntityResponseType> {
        return this.http.put<ISemesterGroup>(this.resourceUrl, semesterGroup, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISemesterGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISemesterGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}

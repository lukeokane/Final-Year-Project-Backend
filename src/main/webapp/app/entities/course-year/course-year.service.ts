import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICourseYear } from 'app/shared/model/course-year.model';

type EntityResponseType = HttpResponse<ICourseYear>;
type EntityArrayResponseType = HttpResponse<ICourseYear[]>;

@Injectable({ providedIn: 'root' })
export class CourseYearService {
    public resourceUrl = SERVER_API_URL + 'api/course-years';

    constructor(private http: HttpClient) {}

    create(courseYear: ICourseYear): Observable<EntityResponseType> {
        return this.http.post<ICourseYear>(this.resourceUrl, courseYear, { observe: 'response' });
    }

    update(courseYear: ICourseYear): Observable<EntityResponseType> {
        return this.http.put<ICourseYear>(this.resourceUrl, courseYear, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ICourseYear>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICourseYear[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}

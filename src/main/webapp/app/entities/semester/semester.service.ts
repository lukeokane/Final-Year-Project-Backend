import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISemester } from 'app/shared/model/semester.model';

type EntityResponseType = HttpResponse<ISemester>;
type EntityArrayResponseType = HttpResponse<ISemester[]>;

@Injectable({ providedIn: 'root' })
export class SemesterService {
    public resourceUrl = SERVER_API_URL + 'api/semesters';

    constructor(private http: HttpClient) {}

    create(semester: ISemester): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(semester);
        return this.http
            .post<ISemester>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(semester: ISemester): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(semester);
        return this.http
            .put<ISemester>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ISemester>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ISemester[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(semester: ISemester): ISemester {
        const copy: ISemester = Object.assign({}, semester, {
            semesterStartDate:
                semester.semesterStartDate != null && semester.semesterStartDate.isValid()
                    ? semester.semesterStartDate.format(DATE_FORMAT)
                    : null,
            semesterEndDate:
                semester.semesterEndDate != null && semester.semesterEndDate.isValid() ? semester.semesterEndDate.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.semesterStartDate = res.body.semesterStartDate != null ? moment(res.body.semesterStartDate) : null;
            res.body.semesterEndDate = res.body.semesterEndDate != null ? moment(res.body.semesterEndDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((semester: ISemester) => {
                semester.semesterStartDate = semester.semesterStartDate != null ? moment(semester.semesterStartDate) : null;
                semester.semesterEndDate = semester.semesterEndDate != null ? moment(semester.semesterEndDate) : null;
            });
        }
        return res;
    }
}

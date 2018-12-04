import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';
import { Api } from '../providers/api/api';
import { CourseYear } from '../class/CourseYear';

@Injectable()
export class CourseYearService {
    private resourceUrl = Api.API_URL + '/course-years';

    constructor(private http: HttpClient) { }

    create(courseYear: CourseYear): Observable<CourseYear> {
        return this.http.post(this.resourceUrl, courseYear);
    }

    update(courseYear: CourseYear): Observable<CourseYear> {
        return this.http.put(this.resourceUrl, courseYear);
    }

    find(id: number): Observable<CourseYear> {
        return this.http.get(`${this.resourceUrl}/${id}`);
    }

    query(req?: any): Observable<any> {
        return this.http.get(this.resourceUrl);
    }

    delete(id: number): Observable<any> {
        return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response', responseType: 'text' });
    }

}
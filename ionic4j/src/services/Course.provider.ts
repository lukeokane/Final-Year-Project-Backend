import { Course } from './../class/Course';
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';
import { Api } from '../providers/api/api';

@Injectable()
export class CourseService {
    private resourceUrl = Api.API_URL + '/courses';

    constructor(private http: HttpClient) { }

    create(course: Course): Observable<Course> {
        return this.http.post(this.resourceUrl, course);
    }

    update(course: Course): Observable<Course> {
        return this.http.put(this.resourceUrl, course);
    }

    find(id: number): Observable<Course> {
        return this.http.get(`${this.resourceUrl}/${id}`);
    }

    query(req?: any): Observable<any> {
        return this.http.get(this.resourceUrl);
    }

    delete(id: number): Observable<any> {
        return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response', responseType: 'text' });
    }

}
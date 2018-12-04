import { Semester } from './../class/Semester';
import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';
import { Api } from '../providers/api/api';

@Injectable()
export class SemesterService {
    private resourceUrl = Api.API_URL + '/semesters';

    constructor(private http: HttpClient) { }

    create(semester: Semester): Observable<Semester> {
        return this.http.post(this.resourceUrl, semester);
    }

    update(semester: Semester): Observable<Semester> {
        return this.http.put(this.resourceUrl, semester);
    }

    find(id: number): Observable<Semester> {
        return this.http.get(`${this.resourceUrl}/${id}`);
    }

    query(req?: any): Observable<any> {
        return this.http.get(this.resourceUrl);
    }

    delete(id: number): Observable<any> {
        return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response', responseType: 'text' });
    }

}
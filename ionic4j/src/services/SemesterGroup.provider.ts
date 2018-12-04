import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Rx';
import { Api } from '../providers/api/api';
import { SemesterGroup } from '../class/SemesterGroup';

@Injectable()
export class SemesterGroupService {
    private resourceUrl = Api.API_URL + '/semester-groups';

    constructor(private http: HttpClient) { }

    create(semesterGroup: SemesterGroup): Observable<SemesterGroup> {
        return this.http.post(this.resourceUrl, semesterGroup);
    }

    update(semesterGroup: SemesterGroup): Observable<SemesterGroup> {
        return this.http.put(this.resourceUrl, semesterGroup);
    }

    find(id: number): Observable<SemesterGroup> {
        return this.http.get(`${this.resourceUrl}/${id}`);
    }

    query(req?: any): Observable<any> {
        return this.http.get(this.resourceUrl);
    }

    delete(id: number): Observable<any> {
        return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response', responseType: 'text' });
    }

}
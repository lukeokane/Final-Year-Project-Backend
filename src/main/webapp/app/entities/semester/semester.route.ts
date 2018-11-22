import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Semester } from 'app/shared/model/semester.model';
import { SemesterService } from './semester.service';
import { SemesterComponent } from './semester.component';
import { SemesterDetailComponent } from './semester-detail.component';
import { SemesterUpdateComponent } from './semester-update.component';
import { SemesterDeletePopupComponent } from './semester-delete-dialog.component';
import { ISemester } from 'app/shared/model/semester.model';

@Injectable({ providedIn: 'root' })
export class SemesterResolve implements Resolve<ISemester> {
    constructor(private service: SemesterService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Semester> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Semester>) => response.ok),
                map((semester: HttpResponse<Semester>) => semester.body)
            );
        }
        return of(new Semester());
    }
}

export const semesterRoute: Routes = [
    {
        path: 'semester',
        component: SemesterComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Semesters'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'semester/:id/view',
        component: SemesterDetailComponent,
        resolve: {
            semester: SemesterResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Semesters'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'semester/new',
        component: SemesterUpdateComponent,
        resolve: {
            semester: SemesterResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Semesters'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'semester/:id/edit',
        component: SemesterUpdateComponent,
        resolve: {
            semester: SemesterResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Semesters'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const semesterPopupRoute: Routes = [
    {
        path: 'semester/:id/delete',
        component: SemesterDeletePopupComponent,
        resolve: {
            semester: SemesterResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Semesters'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

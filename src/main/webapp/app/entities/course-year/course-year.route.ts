import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CourseYear } from 'app/shared/model/course-year.model';
import { CourseYearService } from './course-year.service';
import { CourseYearComponent } from './course-year.component';
import { CourseYearDetailComponent } from './course-year-detail.component';
import { CourseYearUpdateComponent } from './course-year-update.component';
import { CourseYearDeletePopupComponent } from './course-year-delete-dialog.component';
import { ICourseYear } from 'app/shared/model/course-year.model';

@Injectable({ providedIn: 'root' })
export class CourseYearResolve implements Resolve<ICourseYear> {
    constructor(private service: CourseYearService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<CourseYear> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<CourseYear>) => response.ok),
                map((courseYear: HttpResponse<CourseYear>) => courseYear.body)
            );
        }
        return of(new CourseYear());
    }
}

export const courseYearRoute: Routes = [
    {
        path: 'course-year',
        component: CourseYearComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'CourseYears'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'course-year/:id/view',
        component: CourseYearDetailComponent,
        resolve: {
            courseYear: CourseYearResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CourseYears'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'course-year/new',
        component: CourseYearUpdateComponent,
        resolve: {
            courseYear: CourseYearResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CourseYears'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'course-year/:id/edit',
        component: CourseYearUpdateComponent,
        resolve: {
            courseYear: CourseYearResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CourseYears'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const courseYearPopupRoute: Routes = [
    {
        path: 'course-year/:id/delete',
        component: CourseYearDeletePopupComponent,
        resolve: {
            courseYear: CourseYearResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CourseYears'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

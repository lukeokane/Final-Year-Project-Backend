import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SemesterGroup } from 'app/shared/model/semester-group.model';
import { SemesterGroupService } from './semester-group.service';
import { SemesterGroupComponent } from './semester-group.component';
import { SemesterGroupDetailComponent } from './semester-group-detail.component';
import { SemesterGroupUpdateComponent } from './semester-group-update.component';
import { SemesterGroupDeletePopupComponent } from './semester-group-delete-dialog.component';
import { ISemesterGroup } from 'app/shared/model/semester-group.model';

@Injectable({ providedIn: 'root' })
export class SemesterGroupResolve implements Resolve<ISemesterGroup> {
    constructor(private service: SemesterGroupService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<SemesterGroup> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<SemesterGroup>) => response.ok),
                map((semesterGroup: HttpResponse<SemesterGroup>) => semesterGroup.body)
            );
        }
        return of(new SemesterGroup());
    }
}

export const semesterGroupRoute: Routes = [
    {
        path: 'semester-group',
        component: SemesterGroupComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'SemesterGroups'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'semester-group/:id/view',
        component: SemesterGroupDetailComponent,
        resolve: {
            semesterGroup: SemesterGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SemesterGroups'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'semester-group/new',
        component: SemesterGroupUpdateComponent,
        resolve: {
            semesterGroup: SemesterGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SemesterGroups'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'semester-group/:id/edit',
        component: SemesterGroupUpdateComponent,
        resolve: {
            semesterGroup: SemesterGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SemesterGroups'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const semesterGroupPopupRoute: Routes = [
    {
        path: 'semester-group/:id/delete',
        component: SemesterGroupDeletePopupComponent,
        resolve: {
            semesterGroup: SemesterGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SemesterGroups'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { BookingUserDetails } from 'app/shared/model/booking-user-details.model';
import { BookingUserDetailsService } from './booking-user-details.service';
import { BookingUserDetailsComponent } from './booking-user-details.component';
import { BookingUserDetailsDetailComponent } from './booking-user-details-detail.component';
import { BookingUserDetailsUpdateComponent } from './booking-user-details-update.component';
import { BookingUserDetailsDeletePopupComponent } from './booking-user-details-delete-dialog.component';
import { IBookingUserDetails } from 'app/shared/model/booking-user-details.model';

@Injectable({ providedIn: 'root' })
export class BookingUserDetailsResolve implements Resolve<IBookingUserDetails> {
    constructor(private service: BookingUserDetailsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<BookingUserDetails> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<BookingUserDetails>) => response.ok),
                map((bookingUserDetails: HttpResponse<BookingUserDetails>) => bookingUserDetails.body)
            );
        }
        return of(new BookingUserDetails());
    }
}

export const bookingUserDetailsRoute: Routes = [
    {
        path: 'booking-user-details',
        component: BookingUserDetailsComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'BookingUserDetails'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'booking-user-details/:id/view',
        component: BookingUserDetailsDetailComponent,
        resolve: {
            bookingUserDetails: BookingUserDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BookingUserDetails'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'booking-user-details/new',
        component: BookingUserDetailsUpdateComponent,
        resolve: {
            bookingUserDetails: BookingUserDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BookingUserDetails'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'booking-user-details/:id/edit',
        component: BookingUserDetailsUpdateComponent,
        resolve: {
            bookingUserDetails: BookingUserDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BookingUserDetails'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const bookingUserDetailsPopupRoute: Routes = [
    {
        path: 'booking-user-details/:id/delete',
        component: BookingUserDetailsDeletePopupComponent,
        resolve: {
            bookingUserDetails: BookingUserDetailsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'BookingUserDetails'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

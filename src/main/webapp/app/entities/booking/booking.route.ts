import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Booking } from 'app/shared/model/booking.model';
import { BookingService } from './booking.service';
import { BookingComponent } from './booking.component';
import { BookingDetailComponent } from './booking-detail.component';
import { BookingUpdateComponent } from './booking-update.component';
import { BookingDeletePopupComponent } from './booking-delete-dialog.component';
import { IBooking } from 'app/shared/model/booking.model';

@Injectable({ providedIn: 'root' })
export class BookingResolve implements Resolve<IBooking> {
    constructor(private service: BookingService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Booking> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Booking>) => response.ok),
                map((booking: HttpResponse<Booking>) => booking.body)
            );
        }
        return of(new Booking());
    }
}

export const bookingRoute: Routes = [
    {
        path: 'booking',
        component: BookingComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'Bookings'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'booking/:id/view',
        component: BookingDetailComponent,
        resolve: {
            booking: BookingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bookings'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'booking/new',
        component: BookingUpdateComponent,
        resolve: {
            booking: BookingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bookings'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'booking/:id/edit',
        component: BookingUpdateComponent,
        resolve: {
            booking: BookingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bookings'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const bookingPopupRoute: Routes = [
    {
        path: 'booking/:id/delete',
        component: BookingDeletePopupComponent,
        resolve: {
            booking: BookingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Bookings'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

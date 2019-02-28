import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IUserInfo } from 'app/shared/model/user-info.model';
import { UserInfoService } from './user-info.service';
import { IUser, UserService } from 'app/core';
import { ICourseYear } from 'app/shared/model/course-year.model';
import { CourseYearService } from 'app/entities/course-year';
import { IBooking } from 'app/shared/model/booking.model';
import { BookingService } from 'app/entities/booking';

@Component({
    selector: 'jhi-user-info-update',
    templateUrl: './user-info-update.component.html'
})
export class UserInfoUpdateComponent implements OnInit {
    userInfo: IUserInfo;
    isSaving: boolean;

    users: IUser[];

    courseyears: ICourseYear[];

    bookings: IBooking[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private userInfoService: UserInfoService,
        private userService: UserService,
        private courseYearService: CourseYearService,
        private bookingService: BookingService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ userInfo }) => {
            this.userInfo = userInfo;
        });
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.courseYearService.query().subscribe(
            (res: HttpResponse<ICourseYear[]>) => {
                this.courseyears = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.bookingService.query().subscribe(
            (res: HttpResponse<IBooking[]>) => {
                this.bookings = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.userInfo.id !== undefined) {
            this.subscribeToSaveResponse(this.userInfoService.update(this.userInfo));
        } else {
            this.subscribeToSaveResponse(this.userInfoService.create(this.userInfo));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IUserInfo>>) {
        result.subscribe((res: HttpResponse<IUserInfo>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackCourseYearById(index: number, item: ICourseYear) {
        return item.id;
    }

    trackBookingById(index: number, item: IBooking) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

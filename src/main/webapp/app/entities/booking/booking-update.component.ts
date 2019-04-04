import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IBooking } from 'app/shared/model/booking.model';
import { BookingService } from './booking.service';
import { ISubject } from 'app/shared/model/subject.model';
import { SubjectService } from 'app/entities/subject';
import { IUser, UserService } from 'app/core';
import { IUserInfo } from 'app/shared/model/user-info.model';
import { UserInfoService } from 'app/entities/user-info';
import { ITopic } from 'app/shared/model/topic.model';
import { TopicService } from 'app/entities/topic';

@Component({
    selector: 'jhi-booking-update',
    templateUrl: './booking-update.component.html'
})
export class BookingUpdateComponent implements OnInit {
    booking: IBooking;
    isSaving: boolean;

    subjects: ISubject[];

    users: IUser[];

    userinfos: IUserInfo[];

    topics: ITopic[];
    startTime: string;
    endTime: string;
    modifiedTimestamp: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private bookingService: BookingService,
        private subjectService: SubjectService,
        private userService: UserService,
        private userInfoService: UserInfoService,
        private topicService: TopicService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ booking }) => {
            this.booking = booking;
            this.startTime = this.booking.startTime != null ? this.booking.startTime.format(DATE_TIME_FORMAT) : null;
            this.endTime = this.booking.endTime != null ? this.booking.endTime.format(DATE_TIME_FORMAT) : null;
            this.modifiedTimestamp =
                this.booking.modifiedTimestamp != null ? this.booking.modifiedTimestamp.format(DATE_TIME_FORMAT) : null;
        });
        this.subjectService.query().subscribe(
            (res: HttpResponse<ISubject[]>) => {
                this.subjects = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.userInfoService.query().subscribe(
            (res: HttpResponse<IUserInfo[]>) => {
                this.userinfos = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.topicService.query().subscribe(
            (res: HttpResponse<ITopic[]>) => {
                this.topics = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.booking.startTime = this.startTime != null ? moment(this.startTime, DATE_TIME_FORMAT) : null;
        this.booking.endTime = this.endTime != null ? moment(this.endTime, DATE_TIME_FORMAT) : null;
        this.booking.modifiedTimestamp = this.modifiedTimestamp != null ? moment(this.modifiedTimestamp, DATE_TIME_FORMAT) : null;
        if (this.booking.id !== undefined) {
            this.subscribeToSaveResponse(this.bookingService.update(this.booking));
        } else {
            this.subscribeToSaveResponse(this.bookingService.create(this.booking));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IBooking>>) {
        result.subscribe((res: HttpResponse<IBooking>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackSubjectById(index: number, item: ISubject) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackUserInfoById(index: number, item: IUserInfo) {
        return item.id;
    }

    trackTopicById(index: number, item: ITopic) {
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

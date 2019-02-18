import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';

import { ISemester } from 'app/shared/model/semester.model';
import { SemesterService } from './semester.service';
import { ICourseYear } from 'app/shared/model/course-year.model';
import { CourseYearService } from 'app/entities/course-year';

@Component({
    selector: 'jhi-semester-update',
    templateUrl: './semester-update.component.html'
})
export class SemesterUpdateComponent implements OnInit {
    semester: ISemester;
    isSaving: boolean;

    courseyears: ICourseYear[];
    semesterStartDateDp: any;
    semesterEndDateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private semesterService: SemesterService,
        private courseYearService: CourseYearService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ semester }) => {
            this.semester = semester;
        });
        this.courseYearService.query().subscribe(
            (res: HttpResponse<ICourseYear[]>) => {
                this.courseyears = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.semester.id !== undefined) {
            this.subscribeToSaveResponse(this.semesterService.update(this.semester));
        } else {
            this.subscribeToSaveResponse(this.semesterService.create(this.semester));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISemester>>) {
        result.subscribe((res: HttpResponse<ISemester>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackCourseYearById(index: number, item: ICourseYear) {
        return item.id;
    }
}

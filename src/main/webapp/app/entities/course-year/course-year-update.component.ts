import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ICourseYear } from 'app/shared/model/course-year.model';
import { CourseYearService } from './course-year.service';
import { ISubject } from 'app/shared/model/subject.model';
import { SubjectService } from 'app/entities/subject';
import { ICourse } from 'app/shared/model/course.model';
import { CourseService } from 'app/entities/course';

@Component({
    selector: 'jhi-course-year-update',
    templateUrl: './course-year-update.component.html'
})
export class CourseYearUpdateComponent implements OnInit {
    courseYear: ICourseYear;
    isSaving: boolean;

    subjects: ISubject[];

    courses: ICourse[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private courseYearService: CourseYearService,
        private subjectService: SubjectService,
        private courseService: CourseService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ courseYear }) => {
            this.courseYear = courseYear;
        });
        this.subjectService.query().subscribe(
            (res: HttpResponse<ISubject[]>) => {
                this.subjects = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.courseService.query().subscribe(
            (res: HttpResponse<ICourse[]>) => {
                this.courses = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.courseYear.id !== undefined) {
            this.subscribeToSaveResponse(this.courseYearService.update(this.courseYear));
        } else {
            this.subscribeToSaveResponse(this.courseYearService.create(this.courseYear));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ICourseYear>>) {
        result.subscribe((res: HttpResponse<ICourseYear>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackCourseById(index: number, item: ICourse) {
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

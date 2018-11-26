import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ISemesterGroup } from 'app/shared/model/semester-group.model';
import { SemesterGroupService } from './semester-group.service';
import { ISemester } from 'app/shared/model/semester.model';
import { SemesterService } from 'app/entities/semester';

@Component({
    selector: 'jhi-semester-group-update',
    templateUrl: './semester-group-update.component.html'
})
export class SemesterGroupUpdateComponent implements OnInit {
    semesterGroup: ISemesterGroup;
    isSaving: boolean;

    semesters: ISemester[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private semesterGroupService: SemesterGroupService,
        private semesterService: SemesterService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ semesterGroup }) => {
            this.semesterGroup = semesterGroup;
        });
        this.semesterService.query().subscribe(
            (res: HttpResponse<ISemester[]>) => {
                this.semesters = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.semesterGroup.id !== undefined) {
            this.subscribeToSaveResponse(this.semesterGroupService.update(this.semesterGroup));
        } else {
            this.subscribeToSaveResponse(this.semesterGroupService.create(this.semesterGroup));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISemesterGroup>>) {
        result.subscribe((res: HttpResponse<ISemesterGroup>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackSemesterById(index: number, item: ISemester) {
        return item.id;
    }
}

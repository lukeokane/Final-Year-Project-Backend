import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISemesterGroup } from 'app/shared/model/semester-group.model';

@Component({
    selector: 'jhi-semester-group-detail',
    templateUrl: './semester-group-detail.component.html'
})
export class SemesterGroupDetailComponent implements OnInit {
    semesterGroup: ISemesterGroup;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ semesterGroup }) => {
            this.semesterGroup = semesterGroup;
        });
    }

    previousState() {
        window.history.back();
    }
}

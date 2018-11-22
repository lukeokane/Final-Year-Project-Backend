import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISemester } from 'app/shared/model/semester.model';

@Component({
    selector: 'jhi-semester-detail',
    templateUrl: './semester-detail.component.html'
})
export class SemesterDetailComponent implements OnInit {
    semester: ISemester;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ semester }) => {
            this.semester = semester;
        });
    }

    previousState() {
        window.history.back();
    }
}

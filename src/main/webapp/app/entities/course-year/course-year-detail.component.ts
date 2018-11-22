import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICourseYear } from 'app/shared/model/course-year.model';

@Component({
    selector: 'jhi-course-year-detail',
    templateUrl: './course-year-detail.component.html'
})
export class CourseYearDetailComponent implements OnInit {
    courseYear: ICourseYear;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ courseYear }) => {
            this.courseYear = courseYear;
        });
    }

    previousState() {
        window.history.back();
    }
}

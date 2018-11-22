import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICourseYear } from 'app/shared/model/course-year.model';
import { CourseYearService } from './course-year.service';

@Component({
    selector: 'jhi-course-year-delete-dialog',
    templateUrl: './course-year-delete-dialog.component.html'
})
export class CourseYearDeleteDialogComponent {
    courseYear: ICourseYear;

    constructor(private courseYearService: CourseYearService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.courseYearService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'courseYearListModification',
                content: 'Deleted an courseYear'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-course-year-delete-popup',
    template: ''
})
export class CourseYearDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ courseYear }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CourseYearDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.courseYear = courseYear;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}

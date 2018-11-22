import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISemester } from 'app/shared/model/semester.model';
import { SemesterService } from './semester.service';

@Component({
    selector: 'jhi-semester-delete-dialog',
    templateUrl: './semester-delete-dialog.component.html'
})
export class SemesterDeleteDialogComponent {
    semester: ISemester;

    constructor(private semesterService: SemesterService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.semesterService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'semesterListModification',
                content: 'Deleted an semester'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-semester-delete-popup',
    template: ''
})
export class SemesterDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ semester }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SemesterDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.semester = semester;
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

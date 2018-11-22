import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBookingUserDetails } from 'app/shared/model/booking-user-details.model';
import { BookingUserDetailsService } from './booking-user-details.service';

@Component({
    selector: 'jhi-booking-user-details-delete-dialog',
    templateUrl: './booking-user-details-delete-dialog.component.html'
})
export class BookingUserDetailsDeleteDialogComponent {
    bookingUserDetails: IBookingUserDetails;

    constructor(
        private bookingUserDetailsService: BookingUserDetailsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.bookingUserDetailsService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'bookingUserDetailsListModification',
                content: 'Deleted an bookingUserDetails'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-booking-user-details-delete-popup',
    template: ''
})
export class BookingUserDetailsDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ bookingUserDetails }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(BookingUserDetailsDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.bookingUserDetails = bookingUserDetails;
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

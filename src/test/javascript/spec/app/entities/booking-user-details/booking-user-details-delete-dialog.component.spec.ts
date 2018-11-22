/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ThelearningzoneTestModule } from '../../../test.module';
import { BookingUserDetailsDeleteDialogComponent } from 'app/entities/booking-user-details/booking-user-details-delete-dialog.component';
import { BookingUserDetailsService } from 'app/entities/booking-user-details/booking-user-details.service';

describe('Component Tests', () => {
    describe('BookingUserDetails Management Delete Component', () => {
        let comp: BookingUserDetailsDeleteDialogComponent;
        let fixture: ComponentFixture<BookingUserDetailsDeleteDialogComponent>;
        let service: BookingUserDetailsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ThelearningzoneTestModule],
                declarations: [BookingUserDetailsDeleteDialogComponent]
            })
                .overrideTemplate(BookingUserDetailsDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BookingUserDetailsDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BookingUserDetailsService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});

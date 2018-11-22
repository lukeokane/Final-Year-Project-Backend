/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ThelearningzoneTestModule } from '../../../test.module';
import { SemesterDeleteDialogComponent } from 'app/entities/semester/semester-delete-dialog.component';
import { SemesterService } from 'app/entities/semester/semester.service';

describe('Component Tests', () => {
    describe('Semester Management Delete Component', () => {
        let comp: SemesterDeleteDialogComponent;
        let fixture: ComponentFixture<SemesterDeleteDialogComponent>;
        let service: SemesterService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ThelearningzoneTestModule],
                declarations: [SemesterDeleteDialogComponent]
            })
                .overrideTemplate(SemesterDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SemesterDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SemesterService);
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

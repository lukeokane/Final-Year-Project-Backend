/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ThelearningzoneTestModule } from '../../../test.module';
import { SemesterGroupDeleteDialogComponent } from 'app/entities/semester-group/semester-group-delete-dialog.component';
import { SemesterGroupService } from 'app/entities/semester-group/semester-group.service';

describe('Component Tests', () => {
    describe('SemesterGroup Management Delete Component', () => {
        let comp: SemesterGroupDeleteDialogComponent;
        let fixture: ComponentFixture<SemesterGroupDeleteDialogComponent>;
        let service: SemesterGroupService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ThelearningzoneTestModule],
                declarations: [SemesterGroupDeleteDialogComponent]
            })
                .overrideTemplate(SemesterGroupDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SemesterGroupDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SemesterGroupService);
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

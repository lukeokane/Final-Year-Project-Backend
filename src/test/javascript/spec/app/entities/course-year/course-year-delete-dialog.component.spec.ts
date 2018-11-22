/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ThelearningzoneTestModule } from '../../../test.module';
import { CourseYearDeleteDialogComponent } from 'app/entities/course-year/course-year-delete-dialog.component';
import { CourseYearService } from 'app/entities/course-year/course-year.service';

describe('Component Tests', () => {
    describe('CourseYear Management Delete Component', () => {
        let comp: CourseYearDeleteDialogComponent;
        let fixture: ComponentFixture<CourseYearDeleteDialogComponent>;
        let service: CourseYearService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ThelearningzoneTestModule],
                declarations: [CourseYearDeleteDialogComponent]
            })
                .overrideTemplate(CourseYearDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CourseYearDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CourseYearService);
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

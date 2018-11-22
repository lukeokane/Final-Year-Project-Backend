/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ThelearningzoneTestModule } from '../../../test.module';
import { UserInfoDeleteDialogComponent } from 'app/entities/user-info/user-info-delete-dialog.component';
import { UserInfoService } from 'app/entities/user-info/user-info.service';

describe('Component Tests', () => {
    describe('UserInfo Management Delete Component', () => {
        let comp: UserInfoDeleteDialogComponent;
        let fixture: ComponentFixture<UserInfoDeleteDialogComponent>;
        let service: UserInfoService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ThelearningzoneTestModule],
                declarations: [UserInfoDeleteDialogComponent]
            })
                .overrideTemplate(UserInfoDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UserInfoDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserInfoService);
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

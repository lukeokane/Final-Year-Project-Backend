/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ThelearningzoneTestModule } from '../../../test.module';
import { UserInfoUpdateComponent } from 'app/entities/user-info/user-info-update.component';
import { UserInfoService } from 'app/entities/user-info/user-info.service';
import { UserInfo } from 'app/shared/model/user-info.model';

describe('Component Tests', () => {
    describe('UserInfo Management Update Component', () => {
        let comp: UserInfoUpdateComponent;
        let fixture: ComponentFixture<UserInfoUpdateComponent>;
        let service: UserInfoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ThelearningzoneTestModule],
                declarations: [UserInfoUpdateComponent]
            })
                .overrideTemplate(UserInfoUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(UserInfoUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserInfoService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new UserInfo(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.userInfo = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new UserInfo();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.userInfo = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});

/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ThelearningzoneTestModule } from '../../../test.module';
import { SemesterGroupUpdateComponent } from 'app/entities/semester-group/semester-group-update.component';
import { SemesterGroupService } from 'app/entities/semester-group/semester-group.service';
import { SemesterGroup } from 'app/shared/model/semester-group.model';

describe('Component Tests', () => {
    describe('SemesterGroup Management Update Component', () => {
        let comp: SemesterGroupUpdateComponent;
        let fixture: ComponentFixture<SemesterGroupUpdateComponent>;
        let service: SemesterGroupService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ThelearningzoneTestModule],
                declarations: [SemesterGroupUpdateComponent]
            })
                .overrideTemplate(SemesterGroupUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SemesterGroupUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SemesterGroupService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new SemesterGroup(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.semesterGroup = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new SemesterGroup();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.semesterGroup = entity;
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

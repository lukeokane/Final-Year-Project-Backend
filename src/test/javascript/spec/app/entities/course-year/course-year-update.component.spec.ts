/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ThelearningzoneTestModule } from '../../../test.module';
import { CourseYearUpdateComponent } from 'app/entities/course-year/course-year-update.component';
import { CourseYearService } from 'app/entities/course-year/course-year.service';
import { CourseYear } from 'app/shared/model/course-year.model';

describe('Component Tests', () => {
    describe('CourseYear Management Update Component', () => {
        let comp: CourseYearUpdateComponent;
        let fixture: ComponentFixture<CourseYearUpdateComponent>;
        let service: CourseYearService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ThelearningzoneTestModule],
                declarations: [CourseYearUpdateComponent]
            })
                .overrideTemplate(CourseYearUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CourseYearUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CourseYearService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new CourseYear(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.courseYear = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new CourseYear();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.courseYear = entity;
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

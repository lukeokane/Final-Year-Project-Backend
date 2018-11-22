/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ThelearningzoneTestModule } from '../../../test.module';
import { CourseYearDetailComponent } from 'app/entities/course-year/course-year-detail.component';
import { CourseYear } from 'app/shared/model/course-year.model';

describe('Component Tests', () => {
    describe('CourseYear Management Detail Component', () => {
        let comp: CourseYearDetailComponent;
        let fixture: ComponentFixture<CourseYearDetailComponent>;
        const route = ({ data: of({ courseYear: new CourseYear(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ThelearningzoneTestModule],
                declarations: [CourseYearDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CourseYearDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CourseYearDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.courseYear).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});

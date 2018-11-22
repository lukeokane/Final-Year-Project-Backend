/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ThelearningzoneTestModule } from '../../../test.module';
import { SemesterDetailComponent } from 'app/entities/semester/semester-detail.component';
import { Semester } from 'app/shared/model/semester.model';

describe('Component Tests', () => {
    describe('Semester Management Detail Component', () => {
        let comp: SemesterDetailComponent;
        let fixture: ComponentFixture<SemesterDetailComponent>;
        const route = ({ data: of({ semester: new Semester(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ThelearningzoneTestModule],
                declarations: [SemesterDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SemesterDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SemesterDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.semester).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});

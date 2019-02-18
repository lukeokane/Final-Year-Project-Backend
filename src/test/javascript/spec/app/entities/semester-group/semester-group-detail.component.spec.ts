/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ThelearningzoneTestModule } from '../../../test.module';
import { SemesterGroupDetailComponent } from 'app/entities/semester-group/semester-group-detail.component';
import { SemesterGroup } from 'app/shared/model/semester-group.model';

describe('Component Tests', () => {
    describe('SemesterGroup Management Detail Component', () => {
        let comp: SemesterGroupDetailComponent;
        let fixture: ComponentFixture<SemesterGroupDetailComponent>;
        const route = ({ data: of({ semesterGroup: new SemesterGroup(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ThelearningzoneTestModule],
                declarations: [SemesterGroupDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SemesterGroupDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SemesterGroupDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.semesterGroup).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});

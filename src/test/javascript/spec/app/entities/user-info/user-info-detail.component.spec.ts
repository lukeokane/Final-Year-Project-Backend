/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ThelearningzoneTestModule } from '../../../test.module';
import { UserInfoDetailComponent } from 'app/entities/user-info/user-info-detail.component';
import { UserInfo } from 'app/shared/model/user-info.model';

describe('Component Tests', () => {
    describe('UserInfo Management Detail Component', () => {
        let comp: UserInfoDetailComponent;
        let fixture: ComponentFixture<UserInfoDetailComponent>;
        const route = ({ data: of({ userInfo: new UserInfo(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ThelearningzoneTestModule],
                declarations: [UserInfoDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(UserInfoDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(UserInfoDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.userInfo).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});

/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ThelearningzoneTestModule } from '../../../test.module';
import { BookingUserDetailsDetailComponent } from 'app/entities/booking-user-details/booking-user-details-detail.component';
import { BookingUserDetails } from 'app/shared/model/booking-user-details.model';

describe('Component Tests', () => {
    describe('BookingUserDetails Management Detail Component', () => {
        let comp: BookingUserDetailsDetailComponent;
        let fixture: ComponentFixture<BookingUserDetailsDetailComponent>;
        const route = ({ data: of({ bookingUserDetails: new BookingUserDetails(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ThelearningzoneTestModule],
                declarations: [BookingUserDetailsDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(BookingUserDetailsDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(BookingUserDetailsDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.bookingUserDetails).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});

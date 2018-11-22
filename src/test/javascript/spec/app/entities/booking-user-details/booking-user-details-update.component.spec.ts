/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ThelearningzoneTestModule } from '../../../test.module';
import { BookingUserDetailsUpdateComponent } from 'app/entities/booking-user-details/booking-user-details-update.component';
import { BookingUserDetailsService } from 'app/entities/booking-user-details/booking-user-details.service';
import { BookingUserDetails } from 'app/shared/model/booking-user-details.model';

describe('Component Tests', () => {
    describe('BookingUserDetails Management Update Component', () => {
        let comp: BookingUserDetailsUpdateComponent;
        let fixture: ComponentFixture<BookingUserDetailsUpdateComponent>;
        let service: BookingUserDetailsService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ThelearningzoneTestModule],
                declarations: [BookingUserDetailsUpdateComponent]
            })
                .overrideTemplate(BookingUserDetailsUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(BookingUserDetailsUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BookingUserDetailsService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new BookingUserDetails(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.bookingUserDetails = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new BookingUserDetails();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.bookingUserDetails = entity;
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

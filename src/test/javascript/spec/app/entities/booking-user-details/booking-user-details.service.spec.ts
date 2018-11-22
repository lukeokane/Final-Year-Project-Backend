/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { BookingUserDetailsService } from 'app/entities/booking-user-details/booking-user-details.service';
import { IBookingUserDetails, BookingUserDetails, OrdinalScale } from 'app/shared/model/booking-user-details.model';

describe('Service Tests', () => {
    describe('BookingUserDetails Service', () => {
        let injector: TestBed;
        let service: BookingUserDetailsService;
        let httpMock: HttpTestingController;
        let elemDefault: IBookingUserDetails;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(BookingUserDetailsService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new BookingUserDetails(0, 'AAAAAAA', OrdinalScale.NONE, currentDate, currentDate, false, false);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        usercheckInTime: currentDate.format(DATE_TIME_FORMAT),
                        usercheckOutTime: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a BookingUserDetails', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        usercheckInTime: currentDate.format(DATE_TIME_FORMAT),
                        usercheckOutTime: currentDate.format(DATE_TIME_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        usercheckInTime: currentDate,
                        usercheckOutTime: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new BookingUserDetails(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a BookingUserDetails', async () => {
                const returnedFromService = Object.assign(
                    {
                        userFeedback: 'BBBBBB',
                        userSatisfaction: 'BBBBBB',
                        usercheckInTime: currentDate.format(DATE_TIME_FORMAT),
                        usercheckOutTime: currentDate.format(DATE_TIME_FORMAT),
                        userCancelled: true,
                        tutorRejected: true
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        usercheckInTime: currentDate,
                        usercheckOutTime: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of BookingUserDetails', async () => {
                const returnedFromService = Object.assign(
                    {
                        userFeedback: 'BBBBBB',
                        userSatisfaction: 'BBBBBB',
                        usercheckInTime: currentDate.format(DATE_TIME_FORMAT),
                        usercheckOutTime: currentDate.format(DATE_TIME_FORMAT),
                        userCancelled: true,
                        tutorRejected: true
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        usercheckInTime: currentDate,
                        usercheckOutTime: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a BookingUserDetails', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});

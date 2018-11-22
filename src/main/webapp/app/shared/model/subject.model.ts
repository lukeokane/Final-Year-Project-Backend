import { ITopic } from 'app/shared/model//topic.model';
import { IBooking } from 'app/shared/model//booking.model';
import { ISemester } from 'app/shared/model//semester.model';

export interface ISubject {
    id?: number;
    title?: string;
    topics?: ITopic[];
    bookings?: IBooking[];
    semesters?: ISemester[];
}

export class Subject implements ISubject {
    constructor(
        public id?: number,
        public title?: string,
        public topics?: ITopic[],
        public bookings?: IBooking[],
        public semesters?: ISemester[]
    ) {}
}

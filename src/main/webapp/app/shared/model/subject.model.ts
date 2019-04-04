import { ITopic } from 'app/shared/model//topic.model';
import { IBooking } from 'app/shared/model//booking.model';
import { ICourseYear } from 'app/shared/model//course-year.model';

export interface ISubject {
    id?: number;
    title?: string;
    subjectCode?: string;
    topics?: ITopic[];
    bookings?: IBooking[];
    courseYears?: ICourseYear[];
}

export class Subject implements ISubject {
    constructor(
        public id?: number,
        public title?: string,
        public subjectCode?: string,
        public topics?: ITopic[],
        public bookings?: IBooking[],
        public courseYears?: ICourseYear[]
    ) {}
}

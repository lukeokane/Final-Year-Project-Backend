import { IResource } from 'app/shared/model//resource.model';
import { ISubject } from 'app/shared/model//subject.model';
import { IBooking } from 'app/shared/model//booking.model';

export interface ITopic {
    id?: number;
    title?: string;
    resources?: IResource[];
    subjects?: ISubject[];
    bookings?: IBooking[];
}

export class Topic implements ITopic {
    constructor(
        public id?: number,
        public title?: string,
        public resources?: IResource[],
        public subjects?: ISubject[],
        public bookings?: IBooking[]
    ) {}
}

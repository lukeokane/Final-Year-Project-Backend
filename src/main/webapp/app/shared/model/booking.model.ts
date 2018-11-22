import { Moment } from 'moment';
import { IBookingUserDetails } from 'app/shared/model//booking-user-details.model';
import { IUserInfo } from 'app/shared/model//user-info.model';
import { INotification } from 'app/shared/model//notification.model';

export const enum OrdinalScale {
    NONE = 'NONE',
    LOW = 'LOW',
    MEDIUM = 'MEDIUM',
    HIGH = 'HIGH'
}

export interface IBooking {
    id?: number;
    title?: string;
    requestedBy?: string;
    startTime?: Moment;
    endTime?: Moment;
    userComments?: string;
    importanceLevel?: OrdinalScale;
    tutorAccepted?: boolean;
    tutorAcceptedId?: number;
    tutorRejectedCount?: number;
    cancelled?: boolean;
    bookingUserDetails?: IBookingUserDetails[];
    subjectId?: number;
    userInfos?: IUserInfo[];
    notifications?: INotification[];
}

export class Booking implements IBooking {
    constructor(
        public id?: number,
        public title?: string,
        public requestedBy?: string,
        public startTime?: Moment,
        public endTime?: Moment,
        public userComments?: string,
        public importanceLevel?: OrdinalScale,
        public tutorAccepted?: boolean,
        public tutorAcceptedId?: number,
        public tutorRejectedCount?: number,
        public cancelled?: boolean,
        public bookingUserDetails?: IBookingUserDetails[],
        public subjectId?: number,
        public userInfos?: IUserInfo[],
        public notifications?: INotification[]
    ) {
        this.tutorAccepted = this.tutorAccepted || false;
        this.cancelled = this.cancelled || false;
    }
}

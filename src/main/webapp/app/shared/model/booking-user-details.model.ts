import { Moment } from 'moment';

export const enum OrdinalScale {
    NONE = 'NONE',
    LOW = 'LOW',
    MEDIUM = 'MEDIUM',
    HIGH = 'HIGH'
}

export interface IBookingUserDetails {
    id?: number;
    userFeedback?: string;
    userSatisfaction?: OrdinalScale;
    usercheckInTime?: Moment;
    usercheckOutTime?: Moment;
    userCancelled?: boolean;
    tutorRejected?: boolean;
    userInfoId?: number;
    bookingId?: number;
}

export class BookingUserDetails implements IBookingUserDetails {
    constructor(
        public id?: number,
        public userFeedback?: string,
        public userSatisfaction?: OrdinalScale,
        public usercheckInTime?: Moment,
        public usercheckOutTime?: Moment,
        public userCancelled?: boolean,
        public tutorRejected?: boolean,
        public userInfoId?: number,
        public bookingId?: number
    ) {
        this.userCancelled = this.userCancelled || false;
        this.tutorRejected = this.tutorRejected || false;
    }
}

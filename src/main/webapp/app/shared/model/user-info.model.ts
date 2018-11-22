import { IBookingUserDetails } from 'app/shared/model//booking-user-details.model';
import { INotification } from 'app/shared/model//notification.model';
import { IBooking } from 'app/shared/model//booking.model';
import { ISemester } from 'app/shared/model//semester.model';

export interface IUserInfo {
    id?: number;
    tutorSkills?: string;
    userId?: number;
    bookingUserDetails?: IBookingUserDetails[];
    senderUserInfos?: INotification[];
    receiverUserInfos?: INotification[];
    bookings?: IBooking[];
    semesters?: ISemester[];
}

export class UserInfo implements IUserInfo {
    constructor(
        public id?: number,
        public tutorSkills?: string,
        public userId?: number,
        public bookingUserDetails?: IBookingUserDetails[],
        public senderUserInfos?: INotification[],
        public receiverUserInfos?: INotification[],
        public bookings?: IBooking[],
        public semesters?: ISemester[]
    ) {}
}

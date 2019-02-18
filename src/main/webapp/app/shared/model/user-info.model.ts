import { IBookingUserDetails } from 'app/shared/model//booking-user-details.model';
import { INotification } from 'app/shared/model//notification.model';
import { IBooking } from 'app/shared/model//booking.model';

export interface IUserInfo {
    id?: number;
    tutorSkills?: string;
    profileImageURL?: string;
    userId?: number;
    semesterGroupId?: number;
    bookingUserDetails?: IBookingUserDetails[];
    sentNotifications?: INotification[];
    receivedNotifications?: INotification[];
    bookings?: IBooking[];
}

export class UserInfo implements IUserInfo {
    constructor(
        public id?: number,
        public tutorSkills?: string,
        public profileImageURL?: string,
        public userId?: number,
        public semesterGroupId?: number,
        public bookingUserDetails?: IBookingUserDetails[],
        public sentNotifications?: INotification[],
        public receivedNotifications?: INotification[],
        public bookings?: IBooking[]
    ) {}
}

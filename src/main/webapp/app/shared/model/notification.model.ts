import { Moment } from 'moment';

export interface INotification {
    id?: number;
    timestamp?: Moment;
    message?: string;
    senderImageURL?: string;
    read?: boolean;
    senderId?: number;
    receiverId?: number;
    bookingId?: number;
}

export class Notification implements INotification {
    constructor(
        public id?: number,
        public timestamp?: Moment,
        public message?: string,
        public senderImageURL?: string,
        public read?: boolean,
        public senderId?: number,
        public receiverId?: number,
        public bookingId?: number
    ) {
        this.read = this.read || false;
    }
}

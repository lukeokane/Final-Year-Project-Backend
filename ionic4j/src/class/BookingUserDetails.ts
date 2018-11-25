import { OrdinalScale } from "./OrdinalScale";

export class BookingUserDetails {

    constructor(
        public id?:number,
        public userFeedback?:string,
        public userSatisfaction?:OrdinalScale,
        public userCheckInTime?:Date,
        public userCheckOutTime?:Date,
        public userCancelled?:boolean,
        public tutorRejected?:boolean
    )
    { }

}
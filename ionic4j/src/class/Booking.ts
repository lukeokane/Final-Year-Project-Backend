import { OrdinalScale } from "./OrdinalScale";

export class Booking {

    constructor(
        public id?:number,
        public title?:string,
        public requestedBy?:string,
        public startTime?:Date,
        public endTime?:Date,
        public userComments?:String,
        public importanceLevel?:OrdinalScale,
        public tutorAccepted?:boolean,
        public tutorAcceptedId?:number,
        public tutorRejectedCount?:number,
        public cancelled?:boolean
    )
    { }

}
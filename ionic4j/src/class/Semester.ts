import { SemesterNumber } from "./SemesterNumber";

export class Semester {

    constructor(
        public id?:number,
        public semesterNumber?:SemesterNumber,
        public startDate?:Date,
        public endDate?:Date,
        public courseYearId?:number
    )
    { }

}
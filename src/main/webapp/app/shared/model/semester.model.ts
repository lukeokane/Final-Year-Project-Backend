import { Moment } from 'moment';
import { ISubject } from 'app/shared/model//subject.model';
import { IUserInfo } from 'app/shared/model//user-info.model';

export const enum SemesterNumber {
    NONE = 'NONE',
    ONE = 'ONE',
    TWO = 'TWO',
    THREE = 'THREE',
    FOUR = 'FOUR',
    FIVE = 'FIVE',
    SIX = 'SIX',
    SEVEN = 'SEVEN',
    EIGHT = 'EIGHT'
}

export interface ISemester {
    id?: number;
    semesterNumber?: SemesterNumber;
    semesterStartDate?: Moment;
    semesterEndDate?: Moment;
    subjects?: ISubject[];
    userInfos?: IUserInfo[];
    courseYearId?: number;
}

export class Semester implements ISemester {
    constructor(
        public id?: number,
        public semesterNumber?: SemesterNumber,
        public semesterStartDate?: Moment,
        public semesterEndDate?: Moment,
        public subjects?: ISubject[],
        public userInfos?: IUserInfo[],
        public courseYearId?: number
    ) {}
}

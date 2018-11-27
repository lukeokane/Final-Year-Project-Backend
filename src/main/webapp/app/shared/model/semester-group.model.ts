import { IUserInfo } from 'app/shared/model//user-info.model';
import { ISubject } from 'app/shared/model//subject.model';

export interface ISemesterGroup {
    id?: number;
    title?: string;
    userInfos?: IUserInfo[];
    subjects?: ISubject[];
    semesterId?: number;
}

export class SemesterGroup implements ISemesterGroup {
    constructor(
        public id?: number,
        public title?: string,
        public userInfos?: IUserInfo[],
        public subjects?: ISubject[],
        public semesterId?: number
    ) {}
}

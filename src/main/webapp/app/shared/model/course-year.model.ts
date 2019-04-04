import { IUserInfo } from 'app/shared/model//user-info.model';
import { ISubject } from 'app/shared/model//subject.model';

export interface ICourseYear {
    id?: number;
    courseYear?: number;
    userInfos?: IUserInfo[];
    subjects?: ISubject[];
    courseId?: number;
}

export class CourseYear implements ICourseYear {
    constructor(
        public id?: number,
        public courseYear?: number,
        public userInfos?: IUserInfo[],
        public subjects?: ISubject[],
        public courseId?: number
    ) {}
}

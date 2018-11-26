import { IUserInfo } from 'app/shared/model//user-info.model';

export interface ISemesterGroup {
    id?: number;
    title?: string;
    userInfos?: IUserInfo[];
    semesterId?: number;
}

export class SemesterGroup implements ISemesterGroup {
    constructor(public id?: number, public title?: string, public userInfos?: IUserInfo[], public semesterId?: number) {}
}

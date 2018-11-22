import { ICourseYear } from 'app/shared/model//course-year.model';

export interface ICourse {
    id?: number;
    title?: string;
    courseCode?: string;
    courseYears?: ICourseYear[];
}

export class Course implements ICourse {
    constructor(public id?: number, public title?: string, public courseCode?: string, public courseYears?: ICourseYear[]) {}
}

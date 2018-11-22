import { ISemester } from 'app/shared/model//semester.model';

export interface ICourseYear {
    id?: number;
    courseYear?: number;
    semesters?: ISemester[];
    courseId?: number;
}

export class CourseYear implements ICourseYear {
    constructor(public id?: number, public courseYear?: number, public semesters?: ISemester[], public courseId?: number) {}
}

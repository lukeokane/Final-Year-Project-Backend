export interface IResource {
    id?: number;
    title?: string;
    resourceURL?: string;
    topicId?: number;
}

export class Resource implements IResource {
    constructor(public id?: number, public title?: string, public resourceURL?: string, public topicId?: number) {}
}

export interface IMessage {
    id?: number;
    tag?: string;
    reason?: string;
    content?: string;
}

export class Message implements IMessage {
    constructor(public id?: number, public tag?: string, public reason?: string, public content?: string) {}
}

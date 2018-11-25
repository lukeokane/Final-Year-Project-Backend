export class Notification {

    constructor(
        public id?:number,
        public timestamp?:Date,
        public message?:string,
        public senderImageURL?:string,
        public read?:boolean
    )
    { }

}
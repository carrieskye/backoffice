export interface IPersonel {
    id?: number;
    name?: string;
    isIgnored?: boolean;
    photoContentType?: string;
    photo?: any;
}

export class Personel implements IPersonel {
    constructor(
        public id?: number,
        public name?: string,
        public isIgnored?: boolean,
        public photoContentType?: string,
        public photo?: any
    ) {
        this.isIgnored = this.isIgnored || false;
    }
}

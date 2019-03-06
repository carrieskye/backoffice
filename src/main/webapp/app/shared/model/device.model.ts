import { IHomePage } from 'app/shared/model/home-page.model';
import { ISlide } from 'app/shared/model/slide.model';

export interface IDevice {
    id?: number;
    name?: string;
    postalCode?: number;
    homepage?: IHomePage;
    slides?: ISlide[];
    lastIp?: string;
    lastConnection?: Date;
}

export class Device implements IDevice {
    constructor(
        public id?: number,
        public name?: string,
        public postalCode?: number,
        public homepage?: IHomePage,
        public slides?: ISlide[],
        public lastIp?: string,
        public lastConnection?: Date
    ) {}
}

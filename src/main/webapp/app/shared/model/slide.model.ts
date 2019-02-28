import { IDevice } from 'app/shared/model/device.model';

export const enum AgeCategory {
    YOUNG = 'YOUNG',
    ADULT = 'ADULT'
}

export const enum Gender {
    MALE = 'MALE',
    FEMALE = 'FEMALE'
}

export interface ISlide {
    id?: number;
    imageContentType?: string;
    image?: any;
    ageCategory?: AgeCategory;
    gender?: Gender;
    devices?: IDevice[];
}

export class Slide implements ISlide {
    constructor(
        public id?: number,
        public imageContentType?: string,
        public image?: any,
        public ageCategory?: AgeCategory,
        public gender?: Gender,
        public devices?: IDevice[]
    ) {}
}

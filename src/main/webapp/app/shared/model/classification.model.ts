import { Moment } from 'moment';
import { IDevice } from 'app/shared/model/device.model';

export const enum Gender {
    MALE = 'MALE',
    FEMALE = 'FEMALE'
}

export const enum Emotion {
    ANGRY = 'ANGRY',
    DISGUST = 'DISGUST',
    FEAR = 'FEAR',
    HAPPY = 'HAPPY',
    SAD = 'SAD',
    SURPRISE = 'SURPRISE',
    NEUTRAL = 'NEUTRAL'
}

export interface IClassification {
    id?: number;
    personId?: string;
    timestamp?: Moment;
    age?: number;
    gender?: Gender;
    emotion?: Emotion;
    device?: IDevice;
}

export class Classification implements IClassification {
    constructor(
        public id?: number,
        public personId?: string,
        public timestamp?: Moment,
        public age?: number,
        public gender?: Gender,
        public emotion?: Emotion,
        public device?: IDevice
    ) {}
}

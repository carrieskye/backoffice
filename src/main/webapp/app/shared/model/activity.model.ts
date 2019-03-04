export interface IActivity {
    f?: number;
    m?: number;
}

export class Activity implements IActivity {
    constructor(public f?: number, public m?: number) {}
}

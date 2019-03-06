export interface IActivity {
    m?: number;
    f?: number;
}

export class Activity implements IActivity {
    constructor(public m?: number, public f?: number) {}
}

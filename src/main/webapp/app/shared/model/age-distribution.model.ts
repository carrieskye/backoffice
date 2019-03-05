export interface IAgeDistribution {
    min?: number;
    count?: number;
}

export class AgeDistribution implements IAgeDistribution {
    constructor(public min?: number, public count?: number) {}
}

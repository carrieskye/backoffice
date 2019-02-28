export interface IHomePage {
    id?: number;
    name?: string;
    youngMaleUrl?: string;
    youngFemaleUrl?: string;
    adultMaleUrl?: string;
    adultFemaleUrl?: string;
}

export class HomePage implements IHomePage {
    constructor(
        public id?: number,
        public name?: string,
        public youngMaleUrl?: string,
        public youngFemaleUrl?: string,
        public adultMaleUrl?: string,
        public adultFemaleUrl?: string
    ) {}
}

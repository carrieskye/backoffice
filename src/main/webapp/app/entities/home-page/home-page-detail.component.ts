import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHomePage } from 'app/shared/model/home-page.model';

@Component({
    selector: 'jhi-home-page-detail',
    templateUrl: './home-page-detail.component.html'
})
export class HomePageDetailComponent implements OnInit {
    homePage: IHomePage;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ homePage }) => {
            this.homePage = homePage;
        });
    }

    previousState() {
        window.history.back();
    }
}

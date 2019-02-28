import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IHomePage } from 'app/shared/model/home-page.model';
import { HomePageService } from './home-page.service';

@Component({
    selector: 'jhi-home-page-update',
    templateUrl: './home-page-update.component.html'
})
export class HomePageUpdateComponent implements OnInit {
    homePage: IHomePage;
    isSaving: boolean;

    constructor(protected homePageService: HomePageService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ homePage }) => {
            this.homePage = homePage;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.homePage.id !== undefined) {
            this.subscribeToSaveResponse(this.homePageService.update(this.homePage));
        } else {
            this.subscribeToSaveResponse(this.homePageService.create(this.homePage));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IHomePage>>) {
        result.subscribe((res: HttpResponse<IHomePage>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}

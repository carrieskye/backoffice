import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { ISlide } from 'app/shared/model/slide.model';
import { AccountService } from 'app/core';
import { SlideService } from './slide.service';

@Component({
    selector: 'jhi-slide',
    templateUrl: './slide.component.html'
})
export class SlideComponent implements OnInit, OnDestroy {
    slides: ISlide[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected slideService: SlideService,
        protected jhiAlertService: JhiAlertService,
        protected dataUtils: JhiDataUtils,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.slideService
            .query()
            .pipe(
                filter((res: HttpResponse<ISlide[]>) => res.ok),
                map((res: HttpResponse<ISlide[]>) => res.body)
            )
            .subscribe(
                (res: ISlide[]) => {
                    this.slides = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInSlides();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ISlide) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInSlides() {
        this.eventSubscriber = this.eventManager.subscribe('slideListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}

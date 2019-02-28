import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IClassification } from 'app/shared/model/classification.model';
import { AccountService } from 'app/core';
import { ClassificationService } from './classification.service';

@Component({
    selector: 'jhi-classification',
    templateUrl: './classification.component.html'
})
export class ClassificationComponent implements OnInit, OnDestroy {
    classifications: IClassification[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        protected classificationService: ClassificationService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected accountService: AccountService
    ) {}

    loadAll() {
        this.classificationService
            .query()
            .pipe(
                filter((res: HttpResponse<IClassification[]>) => res.ok),
                map((res: HttpResponse<IClassification[]>) => res.body)
            )
            .subscribe(
                (res: IClassification[]) => {
                    this.classifications = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInClassifications();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IClassification) {
        return item.id;
    }

    registerChangeInClassifications() {
        this.eventSubscriber = this.eventManager.subscribe('classificationListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}

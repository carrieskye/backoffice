import { Component, OnInit } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';
import { ActivityService } from 'app/graphs/general/activity/activity.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IActivity } from 'app/shared/model/activity.model';
import { Moment } from 'moment';

@Component({
    selector: 'jhi-general-activity',
    templateUrl: './activity.component.html',
    styles: []
})
export class ActivityComponent implements OnInit {
    activities: any[] = [];
    test = 'TEST';

    constructor(protected activityService: ActivityService, protected jhiAlertService: JhiAlertService) {}

    loadAll() {
        this.activityService.query().subscribe(
            result => {
                this.convertCrap(result);

                // this.activities = result;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    protected convertCrap(crap) {
        console.log(crap);
    }
}

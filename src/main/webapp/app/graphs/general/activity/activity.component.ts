import { Component, OnInit } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';
import { ActivityService } from 'app/graphs/general/activity/activity.service';
import { HttpErrorResponse } from '@angular/common/http';

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
                const keys = Object.keys(result).sort();

                const values = keys.map(key => result[key]);
                console.log(keys);
                console.log(values);
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
}

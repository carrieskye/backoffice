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
    public activitiesLabels: string[] = [];
    public activitiesData: any[] = [{ data: [], label: 'Male' }, { data: [], label: 'Female' }];
    public activitiesChartType = 'bar';
    public activitiesLegend = true;
    public activitiesOptions: any = { scaleShowVerticalLines: false, responsive: true };

    constructor(protected activityService: ActivityService, protected jhiAlertService: JhiAlertService) {}

    loadAll() {
        this.activityService.query().subscribe(
            result => {
                const keys = Object.keys(result).sort();

                const maleData = [];
                const femaleData = [];

                keys.forEach(key => {
                    this.activitiesLabels.push(key);

                    maleData.push(result[key]['m']);
                    femaleData.push(result[key]['f']);
                });

                this.activitiesData = [{ data: maleData, label: 'Male' }, { data: femaleData, label: 'Female' }];
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

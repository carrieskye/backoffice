import { Component, OnInit } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';
import { ActivityService } from 'app/graphs/general/activity/activity.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-general-activity',
    templateUrl: './activity.component.html',
    styleUrls: ['../../graphs.scss']
})
export class ActivityComponent implements OnInit {
    loading = false;

    activitiesLabels: string[] = [];
    activitiesData: any[] = [{ data: [], label: 'F' }, { data: [], label: 'M' }];
    activitiesChartType = 'bar';
    activitiesLegend = true;
    activitiesOptions: any = { scaleShowVerticalLines: false, responsive: true };

    constructor(protected activityService: ActivityService, protected jhiAlertService: JhiAlertService) {}

    loadAll() {
        this.loading = true;
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

                this.activitiesData = [{ data: femaleData, label: 'F' }, { data: maleData, label: 'M' }];
                this.loading = false;
            },
            (res: HttpErrorResponse) => {
                this.onError(res.message);
                this.loading = false;
            }
        );
    }

    ngOnInit() {
        this.loadAll();
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}

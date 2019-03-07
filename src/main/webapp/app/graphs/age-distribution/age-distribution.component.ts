import { Component, OnInit } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Device, IDevice } from 'app/shared/model/device.model';
import { filter, map } from 'rxjs/operators';
import { DeviceService } from 'app/entities/device';
import { AgeDistributionService } from 'app/graphs/age-distribution/age-distribution.service';

@Component({
    selector: 'jhi-age-distribution',
    templateUrl: './age-distribution.component.html',
    styleUrls: ['../graphs.scss']
})
export class AgeDistributionComponent implements OnInit {
    noClassifications = false;

    devicesLoading = false;
    graphLoading = false;

    deviceOptions: IDevice[] = [];
    ageIntervalOptions = [5, 10, 15, 20];
    timeIntervalOptions = [1, 2, 3, 4, 5];
    startTimeOptions = ['09:00', '10:00', '11:00'];
    endTimeOptions = ['17:00', '18:00', '19:00'];

    selectedDevice = new Device(-1, 'All', 0);
    ageInterval = 20;
    timeInterval = 3;
    startTime = '09:00';
    endTime = '18:00';

    timeIntervalLabels: string[] = [];

    ageDistributionLabels: string[] = [];
    ageDistributionData: number[][] = [[]];
    ageDistributionChartType = 'pie';
    ageDistributionLegend = true;

    constructor(
        protected ageDistributionService: AgeDistributionService,
        protected deviceService: DeviceService,
        protected jhiAlertService: JhiAlertService
    ) {}

    loadAll() {
        this.devicesLoading = true;
        this.deviceService
            .query()
            .pipe(
                filter((res: HttpResponse<IDevice[]>) => res.ok),
                map((res: HttpResponse<IDevice[]>) => res.body)
            )
            .subscribe(
                (res: IDevice[]) => {
                    this.deviceOptions = res;

                    if (this.deviceOptions.length > 0) {
                        this.selectStore(this.selectedDevice);
                    }
                    this.devicesLoading = false;
                },
                (res: HttpErrorResponse) => {
                    this.onError(res.message);
                    this.devicesLoading = false;
                }
            );
    }

    updateTable() {
        this.graphLoading = true;
        this.ageDistributionLabels = [];

        const numberOfStores = this.selectedDevice.id === -1 ? this.deviceOptions.length : 1;

        this.ageDistributionService
            .query(this.selectedDevice.id, this.timeInterval * 60, this.ageInterval, this.startTime, this.endTime)
            .subscribe(
                result => {
                    this.noClassifications = true;

                    const keys = Object.keys(result).sort();
                    const labels = Object.keys(result[keys[0]]);

                    // Create age category labels
                    labels.forEach(label => {
                        this.ageDistributionLabels.push(label + ' - ' + (parseInt(label, 10) + this.ageInterval).toString());
                    });

                    const data = [];
                    this.timeIntervalLabels = [];

                    keys.forEach((key, timeIndex) => {
                        // Create time label
                        if (timeIndex < keys.length - 1) {
                            this.timeIntervalLabels.push(key.toString() + ' - ' + keys[timeIndex + 1]);
                        } else {
                            this.timeIntervalLabels.push(key.toString() + ' - ' + this.endTime);
                        }

                        // Add data to correct pie chart
                        data[timeIndex] = [];
                        labels.forEach(() => data[timeIndex].push(0));
                        Object.keys(result[key]).forEach((category, categoryIndex) => {
                            const count = Math.round(result[key][category] / numberOfStores);
                            if (count > 0) {
                                this.noClassifications = false;
                            }
                            data[timeIndex][categoryIndex] += count;
                        });
                    });

                    this.ageDistributionData = data;
                    this.graphLoading = false;
                },
                (res: HttpErrorResponse) => {
                    this.onError(res.message);
                    this.graphLoading = false;
                }
            );
    }

    ngOnInit() {
        this.loadAll();
    }

    selectStore(device: IDevice) {
        this.selectedDevice = device;
        this.updateTable();
    }

    selectAllStores() {
        this.selectedDevice = new Device(-1, 'All', 0);
        this.updateTable();
    }

    selectAgeInterval(interval: number) {
        this.ageInterval = interval;
        this.updateTable();
    }

    selectTimeInterval(interval: number) {
        this.timeInterval = interval;
        this.updateTable();
    }

    selectStartTime(startTime: string) {
        this.startTime = startTime;
        this.updateTable();
    }

    selectEndTime(endTime: string) {
        this.endTime = endTime;
        this.updateTable();
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}

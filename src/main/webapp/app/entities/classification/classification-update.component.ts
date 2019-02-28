import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IClassification } from 'app/shared/model/classification.model';
import { ClassificationService } from './classification.service';
import { IDevice } from 'app/shared/model/device.model';
import { DeviceService } from 'app/entities/device';

@Component({
    selector: 'jhi-classification-update',
    templateUrl: './classification-update.component.html'
})
export class ClassificationUpdateComponent implements OnInit {
    classification: IClassification;
    isSaving: boolean;

    devices: IDevice[];
    timestamp: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected classificationService: ClassificationService,
        protected deviceService: DeviceService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ classification }) => {
            this.classification = classification;
            this.timestamp = this.classification.timestamp != null ? this.classification.timestamp.format(DATE_TIME_FORMAT) : null;
        });
        this.deviceService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IDevice[]>) => mayBeOk.ok),
                map((response: HttpResponse<IDevice[]>) => response.body)
            )
            .subscribe((res: IDevice[]) => (this.devices = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.classification.timestamp = this.timestamp != null ? moment(this.timestamp, DATE_TIME_FORMAT) : null;
        if (this.classification.id !== undefined) {
            this.subscribeToSaveResponse(this.classificationService.update(this.classification));
        } else {
            this.subscribeToSaveResponse(this.classificationService.create(this.classification));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassification>>) {
        result.subscribe((res: HttpResponse<IClassification>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackDeviceById(index: number, item: IDevice) {
        return item.id;
    }
}

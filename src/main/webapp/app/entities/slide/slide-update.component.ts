import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { ISlide } from 'app/shared/model/slide.model';
import { SlideService } from './slide.service';
import { IDevice } from 'app/shared/model/device.model';
import { DeviceService } from 'app/entities/device';

@Component({
    selector: 'jhi-slide-update',
    templateUrl: './slide-update.component.html'
})
export class SlideUpdateComponent implements OnInit {
    slide: ISlide;
    isSaving: boolean;

    devices: IDevice[];

    constructor(
        protected dataUtils: JhiDataUtils,
        protected jhiAlertService: JhiAlertService,
        protected slideService: SlideService,
        protected deviceService: DeviceService,
        protected elementRef: ElementRef,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ slide }) => {
            this.slide = slide;
        });
        this.deviceService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IDevice[]>) => mayBeOk.ok),
                map((response: HttpResponse<IDevice[]>) => response.body)
            )
            .subscribe((res: IDevice[]) => (this.devices = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.slide, this.elementRef, field, fieldContentType, idInput);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.slide.id !== undefined) {
            this.subscribeToSaveResponse(this.slideService.update(this.slide));
        } else {
            this.subscribeToSaveResponse(this.slideService.create(this.slide));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ISlide>>) {
        result.subscribe((res: HttpResponse<ISlide>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

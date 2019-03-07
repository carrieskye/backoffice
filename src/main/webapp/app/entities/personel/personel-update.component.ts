import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiDataUtils } from 'ng-jhipster';
import { IPersonel } from 'app/shared/model/personel.model';
import { PersonelService } from './personel.service';

@Component({
    selector: 'jhi-personel-update',
    templateUrl: './personel-update.component.html'
})
export class PersonelUpdateComponent implements OnInit {
    personel: IPersonel;
    isSaving: boolean;

    constructor(
        protected dataUtils: JhiDataUtils,
        protected personelService: PersonelService,
        protected elementRef: ElementRef,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ personel }) => {
            this.personel = personel;
        });
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
        this.dataUtils.clearInputImage(this.personel, this.elementRef, field, fieldContentType, idInput);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.personel.id !== undefined) {
            this.subscribeToSaveResponse(this.personelService.update(this.personel));
        } else {
            this.subscribeToSaveResponse(this.personelService.create(this.personel));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonel>>) {
        result.subscribe((res: HttpResponse<IPersonel>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}

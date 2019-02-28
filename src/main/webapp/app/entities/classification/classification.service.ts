import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IClassification } from 'app/shared/model/classification.model';

type EntityResponseType = HttpResponse<IClassification>;
type EntityArrayResponseType = HttpResponse<IClassification[]>;

@Injectable({ providedIn: 'root' })
export class ClassificationService {
    public resourceUrl = SERVER_API_URL + 'api/classifications';

    constructor(protected http: HttpClient) {}

    create(classification: IClassification): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(classification);
        return this.http
            .post<IClassification>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(classification: IClassification): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(classification);
        return this.http
            .put<IClassification>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IClassification>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IClassification[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(classification: IClassification): IClassification {
        const copy: IClassification = Object.assign({}, classification, {
            timestamp: classification.timestamp != null && classification.timestamp.isValid() ? classification.timestamp.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.timestamp = res.body.timestamp != null ? moment(res.body.timestamp) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((classification: IClassification) => {
                classification.timestamp = classification.timestamp != null ? moment(classification.timestamp) : null;
            });
        }
        return res;
    }
}

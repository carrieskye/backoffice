import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPersonel } from 'app/shared/model/personel.model';

type EntityResponseType = HttpResponse<IPersonel>;
type EntityArrayResponseType = HttpResponse<IPersonel[]>;

@Injectable({ providedIn: 'root' })
export class PersonelService {
    public resourceUrl = SERVER_API_URL + 'api/personels';

    constructor(protected http: HttpClient) {}

    create(personel: IPersonel): Observable<EntityResponseType> {
        return this.http.post<IPersonel>(this.resourceUrl, personel, { observe: 'response' });
    }

    update(personel: IPersonel): Observable<EntityResponseType> {
        return this.http.put<IPersonel>(this.resourceUrl, personel, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IPersonel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPersonel[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}

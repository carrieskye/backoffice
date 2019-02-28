import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISlide } from 'app/shared/model/slide.model';

type EntityResponseType = HttpResponse<ISlide>;
type EntityArrayResponseType = HttpResponse<ISlide[]>;

@Injectable({ providedIn: 'root' })
export class SlideService {
    public resourceUrl = SERVER_API_URL + 'api/slides';

    constructor(protected http: HttpClient) {}

    create(slide: ISlide): Observable<EntityResponseType> {
        return this.http.post<ISlide>(this.resourceUrl, slide, { observe: 'response' });
    }

    update(slide: ISlide): Observable<EntityResponseType> {
        return this.http.put<ISlide>(this.resourceUrl, slide, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISlide>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISlide[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
